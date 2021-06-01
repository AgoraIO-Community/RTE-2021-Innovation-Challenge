#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    public class WorldSpaceGraphChart : GraphChartBase
    {
        List<DoubleVector4> mClipped = new List<DoubleVector4>();
        List<Vector4> mTransformed = new List<Vector4>();
        private StringBuilder mRealtimeStringBuilder = new StringBuilder();
        protected Dictionary<string, List<BillboardText>> m3DTexts = new Dictionary<string, List<BillboardText>>();
        private float totalDepth = 0f;
        GameObject mEmptyPointPrefab = null;
        /// <summary>
        /// If this value is set all the text in the chart will be rendered to this specific camera. otherwise text is rendered to the main camera
        /// </summary>
        [SerializeField]
        [Tooltip("If this value is set all the text in the chart will be rendered to this specific camera. otherwise text is rendered to the main camera")]
        private Camera textCamera;

        /// <summary>
        /// If this value is set all the text in the chart will be rendered to this specific camera. otherwise text is rendered to the main camera
        /// </summary>
        public Camera TextCamera
        {
            get { return textCamera; }
            set
            {
                textCamera = value;
                OnPropertyUpdated();
            }
        }
        /// <summary>
        /// The distance from the camera at which the text is at it's original size.
        /// </summary>
        [SerializeField]
        [Tooltip("The distance from the camera at which the text is at it's original size")]
        private float textIdleDistance = 20f;

        /// <summary>
        /// The distance from the camera at which the text is at it's original size.
        /// </summary>
        public float TextIdleDistance
        {
            get { return textIdleDistance; }
            set
            {
                textIdleDistance = value;
                OnPropertyUpdated();
            }
        }

        protected override Camera TextCameraLink
        {
            get
            {
                return TextCamera;
            }
        }

        protected override float TextIdleDistanceLink
        {
            get
            {
                return TextIdleDistance;
            }
        }
       
        protected override float TotalDepthLink
        {
            get
            {
                return totalDepth;
            }
        }

        public override bool IsCanvas
        {
            get
            {
                return false;
            }
        }

        public override bool SupportRealtimeGeneration
        {
            get
            {
                return false;
            }
        }

        protected override void OnPropertyUpdated()
        {
            base.OnPropertyUpdated();
            Invalidate();
        }

        private GameObject CreatePointObject(GraphData.CategoryData data)
        {
            GameObject prefab = data.DotPrefab;
            if(prefab == null)
            {
                if (mEmptyPointPrefab == null)
                    mEmptyPointPrefab = (GameObject)Resources.Load("Chart And Graph/SelectHandle");
                prefab = mEmptyPointPrefab;
            }
            GameObject obj = GameObject.Instantiate(prefab);
            ChartCommon.HideObject(obj, hideHierarchy);
            if (obj.GetComponent<ChartItem>() == null)
                obj.AddComponent<ChartItem>();
            obj.transform.SetParent(transform);
            obj.transform.localScale = new Vector3(1f, 1f, 1f);
            obj.transform.localPosition = Vector3.zero;
            obj.transform.localRotation = Quaternion.identity;

            return obj;
        }

        private FillPathGenerator CreateFillObject(GraphData.CategoryData data)
        {
            GameObject obj = GameObject.Instantiate(data.FillPrefab.gameObject);
            ChartCommon.HideObject(obj, hideHierarchy);
            FillPathGenerator fill = obj.GetComponent<FillPathGenerator>();
            if (obj.GetComponent<ChartItem>() == null)
                obj.AddComponent<ChartItem>();
            obj.transform.SetParent(transform);
            obj.transform.localScale = new Vector3(1f, 1f, 1f);
            obj.transform.localPosition = Vector3.zero;
            obj.transform.localRotation = Quaternion.identity;
            return fill;
        }

        private PathGenerator CreateLineObject(GraphData.CategoryData data)
        {
            GameObject obj = GameObject.Instantiate(data.LinePrefab.gameObject);
            ChartCommon.HideObject(obj, hideHierarchy);
            PathGenerator lines = obj.GetComponent<PathGenerator>();
            if (obj.GetComponent<ChartItem>() == null)
                obj.AddComponent<ChartItem>();
            obj.transform.SetParent(transform);
            obj.transform.localScale = new Vector3(1f, 1f, 1f);
            obj.transform.localPosition = Vector3.zero;
            obj.transform.localRotation = Quaternion.identity;
            return lines;
        }

        protected override void OnNonHoverted()
        {
            base.OnNonHoverted();
            foreach (BillboardText t in mActiveTexts)
            {
                if (t.UIText == null)
                    continue;
                foreach (ChartItemEffect effect in t.UIText.GetComponents<ChartItemEffect>())
                    effect.TriggerOut(false);
            }
            mActiveTexts.Clear();
            if (NonHovered != null)
                NonHovered.Invoke();
        }

        protected override double GetCategoryDepth(string category)
        {
            var cat = ((IInternalGraphData)Data).Categories.Where(x => x.Name == category).FirstOrDefault();
            if (cat == null)
                return 0.0;
            return cat.Depth;
        }

        protected override void OnItemHoverted(object userData)
        {
            base.OnItemHoverted(userData);
            GraphEventArgs args = userData as GraphEventArgs;
            if (args == null)
                return;
            foreach (BillboardText t in mActiveTexts)
            {
                if (t.UIText == null)
                    continue;
                foreach (ChartItemEffect effect in t.UIText.GetComponents<ChartItemEffect>())
                    effect.TriggerOut(false);
            }
            mActiveTexts.Clear();
            List<BillboardText> catgoryTexts;
            if (m3DTexts.TryGetValue(args.Category, out catgoryTexts))
            {
                if (args.Index < catgoryTexts.Count)
                {
                    BillboardText b = catgoryTexts[args.Index];
                    mActiveTexts.Add(b);
                    GameObject t = b.UIText;
                    if (t != null)
                    {
                        foreach (ChartItemEffect effect in t.GetComponents<ChartItemEffect>())
                            effect.TriggerIn(false);
                    }
                }
            }
        }

        void AddBillboardText(string cat, BillboardText text)
        {
            List<BillboardText> addTo;
            if (m3DTexts.TryGetValue(cat, out addTo) == false)
            {
                addTo = new List<BillboardText>();
                m3DTexts.Add(cat, addTo);
            }
            addTo.Add(text);
        }

        protected override void ClearChart()
        {
            base.ClearChart();
            m3DTexts.Clear();
            mActiveTexts.Clear();
        }

        public override void ClearCache()
        {
            
        }

        public override void GenerateRealtime()
        {
            base.GenerateRealtime();
            Debug.Log("realtime graph updates are not yet supported for 3d graphs");
        }

        protected override void ViewPortionChanged()
        {
            Invalidate();
        }

        public override void InternalGenerateChart()
        {
            if (gameObject.activeInHierarchy == false)
                return;
            base.InternalGenerateChart();
            ClearChart();

            if (Data == null)
                return;

            double minX = ((IInternalGraphData)Data).GetMinValue(0, false);
            double minY = ((IInternalGraphData)Data).GetMinValue(1, false);
            double maxX = ((IInternalGraphData)Data).GetMaxValue(0, false);
            double maxY = ((IInternalGraphData)Data).GetMaxValue(1, false);

            double xScroll = GetScrollOffset(0);
            double yScroll = GetScrollOffset(1);
            double xSize = maxX - minX;
            double ySize = maxY - minY;
            double xOut = minX + xScroll + xSize;
            double yOut = minY + yScroll + ySize;

            DoubleVector3 min = new DoubleVector3(xScroll + minX, yScroll + minY);
            DoubleVector3 max = new DoubleVector3(xOut, yOut);

            Rect viewRect = new Rect(0f, 0f, widthRatio, heightRatio);

            int index = 0;
            int total = ((IInternalGraphData)Data).TotalCategories + 1;
            double positiveDepth = 0f;
            double maxThickness = 0f;
            bool edit = false;
            m3DTexts.Clear();
            mActiveTexts.Clear();
            foreach (GraphData.CategoryData data in ((IInternalGraphData)Data).Categories)
            {
                mClipped.Clear();
                maxThickness = Math.Max(data.LineThickness, maxThickness);
                DoubleVector3[] points = data.getPoints().ToArray();
                Rect uv;
                int refrenceIndex = ClipPoints(points, mClipped, out uv);
                TransformPoints(mClipped, mTransformed, viewRect, min, max);

                if (points.Length == 0 && ChartCommon.IsInEditMode)
                {
                    edit = true;
                    int tmpIndex = total - 1 - index;
                    float y1 = (((float)tmpIndex) / (float)total);
                    float y2 = (((float)tmpIndex + 1) / (float)total);
                    DoubleVector3 pos1 = ChartCommon.interpolateInRect(viewRect, new DoubleVector3(0f, y1,-1f)).ToDoubleVector3();
                    DoubleVector3 pos2 = ChartCommon.interpolateInRect(viewRect, new DoubleVector3(0.5f, y2, -1f)).ToDoubleVector3();
                    DoubleVector3 pos3 = ChartCommon.interpolateInRect(viewRect, new DoubleVector3(1f, y1, -1f)).ToDoubleVector3();
                    points = new DoubleVector3[] { pos1, pos2, pos3 };
                    mTransformed.AddRange(points.Select(x => (Vector4)x.ToVector3()));
                    index++;
                }

                /*if (data.FillMaterial != null)
                {
                    CanvasLines fill = CreateDataObject(data);
                    fill.material = data.FillMaterial;
                    fill.SetLines(list);
                    fill.MakeFillRender(viewRect, data.StetchFill);
                }*/

                if (data.Depth > 0)
                    positiveDepth = Math.Max(positiveDepth, data.Depth);
                // if (data.DotPrefab != null)
                //{
                float minViewX = Math.Min(viewRect.xMin, viewRect.xMax);
                float maxViewX = Math.Max(viewRect.xMin, viewRect.xMax);
                for (int i = 0; i < mTransformed.Count; i++)
                {
                    float transX = mTransformed[i].x;
                    if (minViewX > transX || maxViewX < transX)
                        continue;
                    DoubleVector3 pointValue = points[i];
                    if (edit == false)
                        pointValue = Data.GetPoint(data.Name, i +refrenceIndex);

                    string xFormat = StringFromAxisFormat(pointValue, mHorizontalAxis,true);
                    string yFormat = StringFromAxisFormat(pointValue, mVerticalAxis, false);

                    GraphEventArgs args = new GraphEventArgs(i, (mTransformed[i] + new Vector4(0f, 0f, (float)data.Depth)), pointValue.ToDoubleVector2(), (float)pointValue.z, data.Name, xFormat, yFormat);
                    GameObject point = CreatePointObject(data);
                    ChartItemEvents[] events = point.GetComponentsInChildren<ChartItemEvents>();

                    for (int j = 0; j < events.Length; ++j)
                    {
                        if (events[j] == null)
                            continue;
                        InternalItemEvents comp = (InternalItemEvents)events[j];
                        comp.Parent = this;
                        comp.UserData = args;
                    }

                    double pointSize = mTransformed[i].w * data.PointSize;
                    if (pointSize < 0f)
                        pointSize = data.PointSize;



                    point.transform.localScale = new DoubleVector3(pointSize, pointSize, pointSize).ToVector3();

                    if (data.PointMaterial != null)
                    {
                        Renderer rend = point.GetComponent<Renderer>();
                        if (rend != null)
                            rend.material = data.PointMaterial;
                        ChartMaterialController controller = point.GetComponent<ChartMaterialController>();
                        if (controller != null && controller.Materials != null)
                        {
                            Color hover = controller.Materials.Hover;
                            Color selected = controller.Materials.Selected;
                            controller.Materials = new ChartDynamicMaterial(data.PointMaterial, hover, selected);
                        }
                    }

                    DoubleVector3 position = new DoubleVector3(mTransformed[i]);
                    position.z = data.Depth;
                    point.transform.localPosition = position.ToVector3();
                    if (mItemLabels != null && mItemLabels.isActiveAndEnabled)
                    {
                        Vector3 labelPos = (new DoubleVector3(mTransformed[i]) + new DoubleVector3(mItemLabels.Location.Breadth, mItemLabels.Seperation, mItemLabels.Location.Depth + data.Depth)).ToVector3();
                        if (mItemLabels.Alignment == ChartLabelAlignment.Base)
                            labelPos.y -= (float)mTransformed[i].y;
                        FormatItem(mRealtimeStringBuilder, xFormat, yFormat);
                        string formatted = mRealtimeStringBuilder.ToString();
                        string toSet = mItemLabels.TextFormat.Format(formatted, data.Name, "");
                        BillboardText billboard = ChartCommon.CreateBillboardText(null,mItemLabels.TextPrefab, transform, toSet, labelPos.x, labelPos.y, labelPos.z, 0f, null, hideHierarchy, mItemLabels.FontSize, mItemLabels.FontSharpness);
                        TextController.AddText(billboard);
                        AddBillboardText(data.Name, billboard);
                    }
                }
                //}
                for (int i = 0; i < mTransformed.Count; i++)
                {
                    var t = mTransformed[i];
                    t.z = 0f;
                    t.w = 0f;
                    mTransformed[i]= t;
                }
                Vector3[] floatPoints = mTransformed.Select(x => (Vector3)x).ToArray();
                if (floatPoints.Length >= 2)
                {
                    Vector2 res;
                    float maxF = Math.Max(floatPoints[0].y, floatPoints[1].y);
                    float minF = Math.Min(floatPoints[0].y, floatPoints[1].y);
                    float firstX = viewRect.x;
                    float secondX = viewRect.x + viewRect.width;
                    if(min.x >max.x)
                    {
                        float tmp = firstX;
                        firstX = secondX;
                        secondX = tmp;
                    }
                    if (ChartCommon.SegmentIntersection(floatPoints[0],floatPoints[1], new Vector3(firstX, maxF, 0f), new Vector3(firstX, minF, 0f),out res))
                    {
                        floatPoints[0] = res;
                    }
                    Vector3 last = floatPoints[floatPoints.Length - 1];
                    Vector3 secondLast = floatPoints[floatPoints.Length - 2];
                    maxF = Math.Max(last.y, secondLast.y);
                    minF = Math.Min(last.y, secondLast.y);

                    if (ChartCommon.SegmentIntersection(last,secondLast, new Vector3(secondX, maxF, 0f), new Vector3(secondX, minF, 0f), out res))
                    {
                        floatPoints[floatPoints.Length - 1] = res;
                    }
                }
                if (data.LinePrefab != null)
                {
                    PathGenerator lines = CreateLineObject(data);
                //    float tiling = 1f;

                    if (data.LineTiling.EnableTiling == true && data.LineTiling.TileFactor > 0f)
                    {
                        float length = 0f;
                        for (int i = 1; i < mTransformed.Count; i++)
                            length += (float)(((Vector3)mTransformed[i - 1]) - (Vector3)mTransformed[i]).magnitude;
                      //  tiling = length / data.LineTiling.TileFactor;
                    }

                    lines.Generator(floatPoints, (float) data.LineThickness, false);
                    Vector3 tmp = lines.transform.localPosition;
                    tmp.z = (float)data.Depth;
                    lines.transform.localPosition = tmp;
                    if (data.LineMaterial != null)
                    {
                        Renderer rend = lines.GetComponent<Renderer>();
                        if (rend != null)
                            rend.material = data.LineMaterial;
                        ChartMaterialController controller = lines.GetComponent<ChartMaterialController>();
                        if (controller != null && controller.Materials != null)
                        {
                            Color hover = controller.Materials.Hover;
                            Color selected = controller.Materials.Selected;
                            controller.Materials = new ChartDynamicMaterial(data.LineMaterial, hover, selected);
                        }
                    }
                }
                totalDepth = (float)(positiveDepth + maxThickness*2f);

                
                if (data.FillPrefab != null)
                {
                    FillPathGenerator fill = CreateFillObject(data);
                    Vector3 tmp = fill.transform.localPosition;
                    tmp.z = (float)data.Depth;
                    fill.transform.localPosition = tmp;

                    if (data.LinePrefab == null || !(data.LinePrefab is SmoothPathGenerator))
                    {
                        fill.SetLineSmoothing(false, 0, 0f);
                    }
                    else
                    {
                        SmoothPathGenerator smooth = ((SmoothPathGenerator)data.LinePrefab);
                        fill.SetLineSmoothing(true, smooth.JointSmoothing, smooth.JointSize);
                    }

                    fill.SetGraphBounds(viewRect.yMin, viewRect.yMax);
                    fill.SetStrechFill(data.StetchFill);
                    fill.Generator(floatPoints, (float)data.LineThickness * 1.01f, false);

                    if (data.FillMaterial != null)
                    {
                        Renderer rend = fill.GetComponent<Renderer>();
                        if (rend != null)
                            rend.material = data.FillMaterial;
                        ChartMaterialController controller = fill.GetComponent<ChartMaterialController>();

                        if (controller != null && controller.Materials != null)
                        {
                            Color hover = controller.Materials.Hover;
                            Color selected = controller.Materials.Selected;
                            controller.Materials = new ChartDynamicMaterial(data.FillMaterial, hover, selected);
                        }

                    }
                }

                
            }
            GenerateAxis(true);
        }

        internal override void SetAsMixedSeries()
        {
            throw new NotImplementedException();
        }
    }
}
