#define Graph_And_Chart_PRO

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    class WorldSpaceRadarChart : RadarChart
    {
        GameObject mEmptyPointPrefab;
        [SerializeField]
        private PathGenerator axisPrefab;
        public PathGenerator AxisPrefab
        {
            get
            {
                return axisPrefab;
            }
            set
            {
                axisPrefab = value;
                Invalidate();
            }
        }

        [SerializeField]
        private GameObject axisPointPrefab;
        public GameObject AxisPointPrefab
        {
            get
            {
                return axisPointPrefab;
            }
            set
            {
                axisPointPrefab = value;
                Invalidate();
            }
        }

        protected override GameObject CreateAxisObject(float thickness, Vector3[] path)
        {
            GameObject obj = ChartCommon.CreateChartItem();
            ChartCommon.HideObject(obj, true);
            if (AxisPrefab != null && AxisThickness > 0.0001f)
            {
                GameObject axis = GameObject.Instantiate(AxisPrefab.gameObject);
                axis.AddComponent<ChartItem>();
                ChartCommon.HideObject(axis, hideHierarchy);
                axis.transform.SetParent(obj.transform, true);
                axis.transform.localScale = new Vector3(1f, 1f, 1f);
                axis.transform.localPosition = Vector3.zero; 
                axis.transform.rotation = Quaternion.identity;
                Renderer rend = axis.GetComponent<Renderer>();
                if (rend != null && AxisLineMaterial != null)
                    rend.material = AxisLineMaterial;
                PathGenerator gen = axis.GetComponent<PathGenerator>();
                gen.Generator(path, thickness, true);
            }

            if (AxisPointPrefab != null && AxisPointSize > 0.0001f)
            {
                for(int i=0; i< path.Length; i++)
                {
                    GameObject point = GameObject.Instantiate(AxisPointPrefab.gameObject);
                    point.transform.SetParent(obj.transform, true);
                    point.transform.localScale = new Vector3(AxisPointSize, AxisPointSize, AxisPointSize);
                    point.transform.localPosition = path[i];
                    point.transform.rotation = Quaternion.identity;
                    Renderer rend = point.GetComponent<Renderer>();
                    if(rend != null && AxisPointMaterial != null)
                        rend.material = AxisPointMaterial;
                }
            }
            return obj;            
        }

        protected GameObject CreatePrefab(GameObject container,GameObject prefab)
        {
            GameObject obj = GameObject.Instantiate(prefab);
            ChartCommon.HideObject(obj, hideHierarchy);
            ChartCommon.EnsureComponent<ChartItem>(obj);
            obj.transform.SetParent(container.transform, false);
            obj.transform.localScale = new Vector3(1f, 1f, 1f);
            obj.transform.localPosition = Vector3.zero;
            obj.transform.localRotation = Quaternion.identity;
            return obj;
        }

        protected RadarFillGenerator CreateFillObject(GameObject conatiner)
        {
            GameObject obj = ChartCommon.CreateChartItem();
            ChartCommon.HideObject(obj, hideHierarchy);
            obj.AddComponent<ChartItem>();
            RadarFillGenerator fill = obj.AddComponent<RadarFillGenerator>();
            obj.transform.SetParent(conatiner.transform, false);
            obj.transform.localScale = new Vector3(1f, 1f, 1f);
            obj.transform.localPosition = Vector3.zero;
            obj.transform.localRotation = Quaternion.identity;
            return fill;
        }
        public override bool IsCanvas
        {
            get
            {
                return false;
            }
        }
        protected override GameObject CreateCategoryObject(Vector3[] path, int category)
        {
            RadarChartData.CategoryData cat = ((IInternalRadarData)DataSource).getCategoryData(category);
            GameObject container = ChartCommon.CreateChartItem();
            ChartCommon.HideObject(container, hideHierarchy);
            container.transform.SetParent(transform, false);
            container.transform.localScale = new Vector3(1f, 1f, 1f);
            container.transform.localPosition = new Vector3(0f,0f,cat.Seperation);
            container.transform.localRotation = Quaternion.identity;

            if (cat.FillMaterial != null)
            {
                
                RadarFillGenerator fill = CreateFillObject(container);
                fill.Smoothing = cat.FillSmoothing;
                Renderer rend = fill.GetComponent<Renderer>();
                if(rend != null)
                    rend.material = cat.FillMaterial;
                fill.Generate(path, Radius, cat.Curve);
            }

            if (cat.LinePrefab != null && cat.LineMaterial != null && cat.LineThickness > 0)
            {
                GameObject line = CreatePrefab(container,cat.LinePrefab.gameObject);
                PathGenerator pathGen = line.GetComponent<PathGenerator>();
                Renderer rend = line.GetComponent<Renderer>();
                if (rend != null)
                    rend.material = cat.LineMaterial;

                pathGen.Generator(path, cat.LineThickness, true);
            }
            GameObject prefab = cat.PointPrefab;
            if(prefab == null)
            {
                if (mEmptyPointPrefab == null)
                    mEmptyPointPrefab = (GameObject)Resources.Load("Chart And Graph/SelectHandle");
                prefab = mEmptyPointPrefab;
            }
            if (prefab != null)
            {
                for (int i=0; i<path.Length; i++)
                {
                    GameObject point = CreatePrefab(container, prefab);
                    string group = DataSource.GetGroupName(i);
                    double value = DataSource.GetValue(cat.Name, group);
                    RadarEventArgs args = new RadarEventArgs(cat.Name, group, value, path[i], i);
                    point.transform.localPosition = path[i];
                    point.transform.localScale = new Vector3(cat.PointSize, cat.PointSize, cat.PointSize);
                    
                    Renderer rend = point.GetComponent<Renderer>();
                    if (rend != null)
                        rend.material = cat.PointMaterial;
                    ChartMaterialController controller = point.GetComponent<ChartMaterialController>();
                    if (controller != null && controller.Materials != null)
                    {
                        Color hover = controller.Materials.Hover;
                        Color selected = controller.Materials.Selected;
                        controller.Materials = new ChartDynamicMaterial(cat.PointMaterial, hover, selected);
                    }
                    ChartItemEvents[] events = point.GetComponentsInChildren<ChartItemEvents>();
                    for (int j = 0; j < events.Length; ++j)
                    {
                        if (events[j] == null)
                            continue;
                        InternalItemEvents comp = (InternalItemEvents)events[j];
                        comp.Parent = this;
                        comp.UserData = args;
                    }
                }
            }

            if (mCategoryLabels != null && mCategoryLabels.isActiveAndEnabled)
            {
                for (int i = 0; i < path.Length ; i++)
                {
                    string group = DataSource.GetGroupName(i);
                    double val = DataSource.GetValue(cat.Name, group);
                    Vector3 labelPos = path[i];
                    Vector3 dir = labelPos.normalized;
                    labelPos += dir * mCategoryLabels.Seperation;
                    labelPos += new Vector3(mCategoryLabels.Location.Breadth, 0f, mCategoryLabels.Location.Depth);
                    int fractionDigits = 2;
                    if (mItemLabels != null)
                        fractionDigits = mItemLabels.FractionDigits;
                    string toSet = mCategoryLabels.TextFormat.Format(ChartAdancedSettings.Instance.FormatFractionDigits(fractionDigits, val, CustomNumberFormat), cat.Name, group);
                    BillboardText billboard = ChartCommon.CreateBillboardText(null,mCategoryLabels.TextPrefab, transform, toSet, labelPos.x, labelPos.y, labelPos.z, 0f, null, hideHierarchy, mCategoryLabels.FontSize, mCategoryLabels.FontSharpness);
                    TextController.AddText(billboard);
                    AddBillboardText(cat.Name, billboard);
                }
            }
            return container;
        }
    }
}
