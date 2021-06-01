#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public class CanvasRadarChart : RadarChart
    {
        protected RadarFill CreateFillObject(GameObject conatiner)
        {
            GameObject obj = ChartCommon.CreateCanvasChartItem();
            ChartCommon.HideObject(obj, hideHierarchy);
            obj.AddComponent<ChartItem>();
            obj.AddComponent<CanvasRenderer>();
            RadarFill fill = obj.AddComponent<RadarFill>();
            obj.transform.SetParent(conatiner.transform, false);
            obj.transform.localScale = new Vector3(1f, 1f, 1f);
            obj.transform.localPosition = Vector3.zero;
            obj.transform.localRotation = Quaternion.identity;
            return fill;
        }

        protected CanvasLines CreateLinesObject(GameObject conatiner)
        {
            GameObject obj = ChartCommon.CreateCanvasChartItem();
            ChartCommon.HideObject(obj, hideHierarchy);
            obj.AddComponent<ChartItem>();
            obj.AddComponent<CanvasRenderer>();
            CanvasLines lines = obj.AddComponent<CanvasLines>();
            obj.transform.SetParent(conatiner.transform,false);
            obj.transform.localScale = new Vector3(1f, 1f, 1f);
            obj.transform.localPosition = Vector3.zero;
            obj.transform.localRotation = Quaternion.identity;
            return lines;
        }

        protected override GameObject CreateAxisObject(float thickness, Vector3[] path)
        {
            Vector3[] newPath = new Vector3[path.Length + 1];
            path.CopyTo(newPath, 0);
            newPath[path.Length] = path[0];
            path = newPath;
            List<CanvasLines.LineSegement> seg = new List<CanvasLines.LineSegement>();
            seg.Add(new CanvasLines.LineSegement(path));
            GameObject conatiner = ChartCommon.CreateChartItem();
            ChartCommon.HideObject(conatiner, hideHierarchy);
            conatiner.transform.SetParent(transform,false);
            conatiner.transform.localScale = new Vector3(1f, 1f, 1f);
            conatiner.transform.localPosition = Vector3.zero;
            conatiner.transform.localRotation = Quaternion.identity;

            if (AxisLineMaterial != null && AxisThickness > 0f)
            {
                CanvasLines lines = CreateLinesObject(conatiner);
                lines.material = AxisLineMaterial;
                lines.Thickness = thickness;
                lines.SetLines(seg);
            }

            if (AxisPointMaterial != null && AxisPointSize > 0f)
            {
                CanvasLines points = CreateLinesObject(conatiner);
                points.material = AxisPointMaterial;
                points.MakePointRender(AxisPointSize);
                points.SetLines(seg);
            }
                       
            return conatiner;
        }

        public override void InternalGenerateChart()
        {
            base.InternalGenerateChart();
            if (TextController != null && TextController.gameObject)
                TextController.gameObject.transform.SetAsLastSibling();
        }

        protected override GameObject CreateCategoryObject(Vector3[] path, int category)
        {
            Vector3[] newPath = new Vector3[path.Length + 1];
            path.CopyTo(newPath, 0);
            newPath[path.Length] = path[0];
            path = newPath;
            List<CanvasLines.LineSegement> seg = new List<CanvasLines.LineSegement>();
            seg.Add(new CanvasLines.LineSegement(path));
            RadarChartData.CategoryData cat = ((IInternalRadarData)DataSource).getCategoryData(category);
            GameObject container = ChartCommon.CreateChartItem();
            ChartCommon.HideObject(container, hideHierarchy);
            container.transform.SetParent(transform, false);
            container.transform.localScale = new Vector3(1f, 1f, 1f);
            container.transform.localPosition = Vector3.zero;
            container.transform.localRotation = Quaternion.identity;

            if (cat.FillMaterial != null)
            {
                RadarFill fill = CreateFillObject(container);
                fill.material = cat.FillMaterial;
                fill.SetPath(path, Radius);
            }

            if (cat.LineMaterial != null && cat.LineThickness > 0)
            {
                CanvasLines lines = CreateLinesObject(container);
                lines.material = cat.LineMaterial;
                lines.Thickness = cat.LineThickness;
                lines.SetHoverPrefab(cat.LineHover);
                lines.SetLines(seg);
            }

            if (cat.PointMaterial != null && cat.PointSize > 0f)
            {
                CanvasLines points = CreateLinesObject(container);
                points.material = cat.PointMaterial;
                points.MakePointRender(cat.PointSize);
                points.SetHoverPrefab(cat.PointHover);
                points.SetLines(seg);
                string name = cat.Name;
                points.Hover += (int arg1,int t,object d, Vector2 arg2) =>  Points_Hover(name, arg1, arg2);
                points.Leave += () => Points_Leave(name);
                points.Click += (int arg1, int t, object d, Vector2 arg2) => Points_Click(name, arg1, arg2);
            }

            if(mCategoryLabels != null && mCategoryLabels.isActiveAndEnabled)
            {
                for(int i=0; i<path.Length-1; i++)
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

        protected override void OnEnable()
        {
            base.OnEnable();
            Invalidate();
        }

        protected override void OnItemHoverted(object userData)
        {
            base.OnItemHoverted(userData);
        }

        protected override void OnItemLeave(object userData,string type)
        {
            base.OnItemLeave(userData, type);
        }
        public override bool IsCanvas
        {
            get
            {
                return true;
            }
        }
        protected override void OnItemSelected(object userData)
        {
            base.OnItemSelected(userData);
        }

        private void Points_Click(string category,int index, Vector2 position)
        {
            index = index % DataSource.TotalGroups;
            string group = DataSource.GetGroupName(index);
            double amount = DataSource.GetValue(category, group);
            RadarEventArgs args = new RadarEventArgs(category, group, amount, position, index);
            OnItemSelected(args);
        }

        private void Points_Leave(string category)
        {
            RadarEventArgs args = new RadarEventArgs(category, "",0.0,Vector3.zero,0);
            OnItemLeave(args,"none");
        }

        private void Points_Hover(string category, int index, Vector2 position)
        {
            index = index % DataSource.TotalGroups;
            string group = DataSource.GetGroupName(index);
            double amount = DataSource.GetValue(category, group);
            RadarEventArgs args = new RadarEventArgs(category, group, amount, position, index);
            OnItemHoverted(args);

        }
    }
}
