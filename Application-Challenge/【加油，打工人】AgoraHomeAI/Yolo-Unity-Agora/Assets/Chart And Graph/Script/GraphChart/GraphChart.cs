#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.UI;

namespace ChartAndGraph
{
    public partial class GraphChart : GraphChartBase, ICanvas
    {
        HashSet<string> mOccupiedCateogies = new HashSet<string>();
        Dictionary<string, CategoryObject> mCategoryObjects = new Dictionary<string, CategoryObject>();
        List<DoubleVector3> mTmpData = new List<DoubleVector3>();
        List<DoubleVector4> mClipped = new List<DoubleVector4>();
        List<Vector4> mTransformed = new List<Vector4>();
        List<int> mTmpToRemove = new List<int>();
        private bool SupressRealtimeGeneration = false; 
        private StringBuilder mRealtimeStringBuilder = new StringBuilder();

        /// <summary>
        /// occures when a point is clicked
        /// </summary>
        public GraphEvent LineClicked = new GraphEvent();
        /// <summary>
        /// occurs when a point is hovered
        /// </summary>
        public GraphEvent LineHovered = new GraphEvent();

        protected void OnLineSelected(object userData)
        {
            GraphEventArgs args = userData as GraphEventArgs;
            if (LineClicked != null)
                LineClicked.Invoke(args);
            AddOccupiedCategory(args.Category, "line");
        }

        protected void OnLineHovered(object userData)
        {
            GraphEventArgs args = userData as GraphEventArgs;
            if (LineHovered != null)
                LineHovered.Invoke(args);
            AddOccupiedCategory(args.Category, "line");
        }


        [SerializeField]
        private bool fitToContainer = false;
        public bool FitToContainer
        {
            get { return fitToContainer; }
            set
            {
                fitToContainer = value;
                OnPropertyUpdated();
            }
        }

        [SerializeField]
        private ChartMagin fitMargin;
        public ChartMagin FitMargin
        {
            get { return fitMargin; }
            set
            {
                fitMargin = value;
                OnPropertyUpdated();
            }
        }

        // [SerializeField]
        private bool enableBetaOptimization = false; // this features is not ready yet

        private bool EnableBetaOptimization
        {
            get { return enableBetaOptimization; }
            set
            {
                enableBetaOptimization = value;
                OnPropertyUpdated();
            }
        }

        protected override ChartMagin MarginLink
        {
            get
            {
                return FitMargin;
            }
        }
        public override bool IsCanvas
        {
            get
            {
                return true;
            }
        }

        public override bool SupportRealtimeGeneration
        {
            get
            {
                return true;
            }
        }

        class CategoryObject
        {
            public CanvasChartMesh mItemLabels;
            public CanvasLines mLines;
            public CanvasLines mDots;
            public CanvasLines mFill;
            public Dictionary<int, string> mCahced = new Dictionary<int, string>();
        }
        
        private CanvasLines CreateDataObject(GraphData.CategoryData data, GameObject rectMask)
        {
            GameObject obj = new GameObject("Lines", typeof(RectTransform));
            ChartCommon.HideObject(obj, hideHierarchy);
            obj.AddComponent<ChartItem>();
            RectTransform t = obj.GetComponent<RectTransform>();
            obj.AddComponent<CanvasRenderer>();

            //  Canvas c = obj.AddComponent<Canvas>();

            //c.pixelPerfect = false;
            //obj.AddComponent<GraphicRaycaster>();

            CanvasLines lines = obj.AddComponent<CanvasLines>();
            lines.maskable = true;

            t.SetParent(rectMask.transform, false);
            t.localScale = new Vector3(1f, 1f, 1f);
            t.anchorMin = new Vector2(0f, 0f);
            t.anchorMax = new Vector2(1f, 1f);
            t.sizeDelta = new Vector2(0f, 0f);
            t.anchorMin = new Vector2(0f, 0f);
            t.anchorMax = new Vector2(0f, 0f);

            t.anchoredPosition = Vector3.zero;
            t.localRotation = Quaternion.identity;
            return lines;
        }

        protected override void Update()
        {
            base.Update();
        }

        protected override void ClearChart()
        {
            base.ClearChart();
            ClearBillboard();
            mActiveTexts.Clear();
            mCategoryObjects.Clear();
            ClearRealtimeIndexdata();
        }
        public override void ClearCache()
        {
            mCategoryObjects.Clear();
        }
        protected override double GetCategoryDepth(string category)
        {
            return 0.0;
        }

        double AddRadius(double radius, double mag, double min, double max)
        {
            double size = max - min;
            double factor = size / mag;
            return factor * radius;
        }
        protected override void ViewPortionChanged()
        {
            InvalidateRealtime();
        }
        public override void GenerateRealtime()
        {
            if (SupressRealtimeGeneration)
                return;
            base.GenerateRealtime();

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

            Transform parentT = transform;

            if (mFixPosition != null)
                parentT = mFixPosition.transform;

            ClearBillboardCategories();

            foreach (GraphData.CategoryData data in ((IInternalGraphData)Data).Categories)
            {
                CategoryObject obj = null;

                if (mCategoryObjects.TryGetValue(data.Name, out obj) == false)
                    continue;

                int minUpdateIndex = 0;
                if (mRealtimeUpdateIndex == true)
                {
                    if (mMinimumUpdateIndex.TryGetValue(data.Name, out minUpdateIndex) == false)
                        minUpdateIndex = int.MaxValue;
                }
                mClipped.Clear();
                mTmpData.Clear();

                mTmpData.AddRange(data.getPoints());
                Rect uv;// = new Rect(0f, 0f, 1f, 1f);
                int refrenceIndex = ClipPoints(mTmpData, mClipped, out uv);
                //mClipped.AddRange(mTmpData);
                TransformPoints(mClipped, mTransformed, viewRect, min, max);
                mTmpToRemove.Clear();
                int range = refrenceIndex + mClipped.Count;
                foreach (int key in obj.mCahced.Keys)
                {
                    if (key < refrenceIndex || key > range)
                        mTmpToRemove.Add(key);
                }

                for (int i = 0; i < mTmpToRemove.Count; i++)
                    obj.mCahced.Remove(mTmpToRemove[i]);

                obj.mCahced.Remove(mTmpData.Count - 1); // never store the last point cache , it might be intepolating by the realtime feature

                if (mTmpData.Count == 0)
                    continue;
                if (mItemLabels != null && mItemLabels.isActiveAndEnabled && obj.mItemLabels != null)
                {
                    Rect textRect = viewRect;
                    textRect.xMin -= 1f;
                    textRect.yMin -= 1f;
                    textRect.xMax += 1f;
                    textRect.yMax += 1f;

                    CanvasChartMesh m = obj.mItemLabels;
                    m.Clear();

                    for (int i = 0; i < mTransformed.Count; i++)
                    {
                        if (mTransformed[i].w == 0.0)
                            continue;
                        Vector3 labelPos = ((Vector3)mTransformed[i]) + new Vector3(mItemLabels.Location.Breadth, mItemLabels.Seperation, mItemLabels.Location.Depth);
                        if (mItemLabels.Alignment == ChartLabelAlignment.Base)
                            labelPos.y -= (float)mTransformed[i].y;
                        if (textRect.Contains((Vector2)(mTransformed[i])) == false)
                            continue;
                        string toSet = null;
                        int pointIndex = i + refrenceIndex;
                        if (obj.mCahced.TryGetValue(pointIndex, out toSet) == false)
                        {
                            DoubleVector3 pointValue = mTmpData[i + refrenceIndex];
                            string xFormat = StringFromAxisFormat(pointValue, mHorizontalAxis, mItemLabels.FractionDigits,true);
                            string yFormat = StringFromAxisFormat(pointValue, mVerticalAxis, mItemLabels.FractionDigits,false);
                            FormatItem(mRealtimeStringBuilder, xFormat, yFormat);
                            string formatted = mRealtimeStringBuilder.ToString();
                            mItemLabels.TextFormat.Format(mRealtimeStringBuilder, formatted, data.Name, "");
                            toSet = mRealtimeStringBuilder.ToString();
                            obj.mCahced[pointIndex] = toSet;
                        }
                        labelPos -= new Vector3(CanvasFitOffset.x * TotalWidth, CanvasFitOffset.y * TotalHeight,0f);
                        BillboardText billboard = m.AddText(this, mItemLabels.TextPrefab, parentT, mItemLabels.FontSize, mItemLabels.FontSharpness, toSet, labelPos.x, labelPos.y, labelPos.z, 0f, null);
                        AddBillboardText(data.Name, i + refrenceIndex, billboard);
                    }

                    m.DestoryRecycled();
                    if (m.TextObjects != null)
                    {
                        foreach (BillboardText text in m.TextObjects)
                        {
                            ((IInternalUse)this).InternalTextController.AddText(text);
                        }
                    }
                }

                if (obj.mDots != null)
                {
                    Rect pickRect = viewRect;
                    float halfSize = (float)(data.PointSize * 0.5f);
                    pickRect.xMin -= halfSize;
                    pickRect.yMin -= halfSize;
                    pickRect.xMax += halfSize;
                    pickRect.yMax += halfSize;
                    obj.mDots.SetViewRect(pickRect, uv);
                    obj.mDots.ModifyLines(minUpdateIndex,mTransformed);
                    obj.mDots.SetRefrenceIndex(refrenceIndex);
                }

                if (obj.mLines != null)
                {
                    float tiling = 1f;
                    if (data.LineTiling.EnableTiling == true && data.LineTiling.TileFactor > 0f)
                    {
                        float length = 0f;
                        for (int i = 1; i < mTransformed.Count; i++)
                            length += (mTransformed[i - 1] - mTransformed[i]).magnitude;
                        tiling = length / data.LineTiling.TileFactor;
                    }
                    if (tiling <= 0.0001f)
                        tiling = 1f;
                    obj.mLines.Tiling = tiling;
                    obj.mLines.SetViewRect(viewRect, uv);
                    obj.mLines.ModifyLines(minUpdateIndex,mTransformed);
                    obj.mLines.SetRefrenceIndex(refrenceIndex);
                }

                if (obj.mFill != null)
                {
                    obj.mFill.SetViewRect(viewRect, uv);
                    obj.mFill.ModifyLines(minUpdateIndex,mTransformed);
                    obj.mFill.SetRefrenceIndex(refrenceIndex);
                }
            }
            ClearRealtimeIndexdata();
        }
        protected override bool ShouldFitCanvas { get { return true; } }
        protected override FitType FitAspectCanvas
        {
            get
            {
                return FitType.Aspect;
            }
        }

        public override void InternalGenerateChart()
        {
            if (gameObject.activeInHierarchy == false)
                return;

            base.InternalGenerateChart();

            if (FitToContainer)
            {
                RectTransform trans = GetComponent<RectTransform>();
                widthRatio = trans.rect.width;
                heightRatio = trans.rect.height;
            }
            
            ClearChart();

            if (Data == null)
                return;

            GenerateAxis(true);
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
            bool edit = false;
            ClearBillboard();
            mActiveTexts.Clear();

            GameObject mask = CreateRectMask(viewRect);

            foreach (GraphData.CategoryData data in ((IInternalGraphData)Data).Categories)
            {
                mClipped.Clear();
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

                    DoubleVector3 pos1 = ChartCommon.interpolateInRect(viewRect, new DoubleVector3(0f, y1, -1f)).ToDoubleVector3();
                    DoubleVector3 pos2 = ChartCommon.interpolateInRect(viewRect, new DoubleVector3(0.5f, y2, -1f)).ToDoubleVector3();
                    DoubleVector3 pos3 = ChartCommon.interpolateInRect(viewRect, new DoubleVector3(1f, y1, -1f)).ToDoubleVector3();

                    points = new DoubleVector3[] { pos1, pos2, pos3 };
                    mTransformed.AddRange(points.Select(x => (Vector4)x.ToVector3()));
                    index++;
                }

                List<CanvasLines.LineSegement> list = new List<CanvasLines.LineSegement>();
                list.Add(new CanvasLines.LineSegement(mTransformed));
                CategoryObject categoryObj = new CategoryObject();

                if (data.FillMaterial != null)
                {
                    CanvasLines fill = CreateDataObject(data, mask);
                    fill.EnableOptimization = enableBetaOptimization;
                    fill.material = data.FillMaterial;
                    fill.SetRefrenceIndex(refrenceIndex);
                    fill.SetLines(list);
                    fill.SetViewRect(viewRect, uv);
                    fill.MakeFillRender(viewRect, data.StetchFill);
                    categoryObj.mFill = fill;
                }
                string catName = data.Name;
                if (data.LineMaterial != null)
                {
                    CanvasLines lines = CreateDataObject(data, mask);

                    float tiling = 1f;
                    if (data.LineTiling.EnableTiling == true && data.LineTiling.TileFactor > 0f)
                    {
                        float length = 0f;
                        for (int i = 1; i < mTransformed.Count; i++)
                            length += (mTransformed[i - 1] - mTransformed[i]).magnitude;
                        tiling = length / data.LineTiling.TileFactor;
                    }
                    if (tiling <= 0.0001f)
                        tiling = 1f;
                    lines.SetViewRect(viewRect, uv);
                    lines.EnableOptimization = enableBetaOptimization;
                    lines.Thickness = (float)data.LineThickness;
                    lines.Tiling = tiling;
                    lines.SetRefrenceIndex(refrenceIndex);
                    lines.material = data.LineMaterial;
                    lines.SetHoverPrefab(data.LineHoverPrefab);
                    lines.SetLines(list);
                    categoryObj.mLines = lines;
                    lines.Hover += (idx, t, d, pos) => { Lines_Hover(catName, idx, pos); };
                    lines.Click += (idx, t, d, pos) => { Lines_Clicked(catName, idx, pos); };
                    lines.Leave += () => { Lines_Leave(catName); };
                }

                //if (data.PointMaterial != null)
                //{
                CanvasLines dots = CreateDataObject(data, mask);
                categoryObj.mDots = dots;
                dots.material = data.PointMaterial;
                dots.EnableOptimization = enableBetaOptimization;
                dots.SetLines(list);
                Rect pickRect = viewRect;
                float halfSize = (float)data.PointSize * 0.5f;
                pickRect.xMin -= halfSize;
                pickRect.yMin -= halfSize;
                pickRect.xMax += halfSize;
                pickRect.yMax += halfSize;
                dots.SetViewRect(pickRect, uv);
                dots.SetRefrenceIndex(refrenceIndex);
                dots.SetHoverPrefab(data.PointHoverPrefab);

                if (data.PointMaterial != null)
                    dots.MakePointRender((float)data.PointSize);
                else
                    dots.MakePointRender(0f);

                if (mItemLabels != null && mItemLabels.isActiveAndEnabled)
                {
                    CanvasChartMesh m = new CanvasChartMesh(true);
                    m.RecycleText = true;
                    categoryObj.mItemLabels = m;
                    Rect textRect = viewRect;
                    textRect.xMin -= 1f;
                    textRect.yMin -= 1f;
                    textRect.xMax += 1f;
                    textRect.yMax += 1f;
                    for (int i = 0; i < mTransformed.Count; i++)
                    {
                        if (mTransformed[i].w == 0f)
                            continue;
                        DoubleVector2 pointValue = new DoubleVector2(mTransformed[i]);
                        if (textRect.Contains(pointValue.ToVector2()) == false)
                            continue;
                        if (edit == false)
                            pointValue = Data.GetPoint(data.Name, i + refrenceIndex).ToDoubleVector2();
                        string xFormat = StringFromAxisFormat(pointValue.ToDoubleVector3(), mHorizontalAxis, mItemLabels.FractionDigits,true);
                        string yFormat = StringFromAxisFormat(pointValue.ToDoubleVector3(), mVerticalAxis, mItemLabels.FractionDigits,false);
                        Vector3 labelPos = ((Vector3)mTransformed[i]) + new Vector3(mItemLabels.Location.Breadth, mItemLabels.Seperation, mItemLabels.Location.Depth);
                        if (mItemLabels.Alignment == ChartLabelAlignment.Base)
                            labelPos.y -= mTransformed[i].y;
                        labelPos -= new Vector3(CanvasFitOffset.x * TotalWidth, CanvasFitOffset.y * TotalHeight,0f);
                        FormatItem(mRealtimeStringBuilder, xFormat, yFormat);
                        string formatted = mRealtimeStringBuilder.ToString();
                        string toSet = mItemLabels.TextFormat.Format(formatted, data.Name, "");
                        BillboardText billboard = m.AddText(this, mItemLabels.TextPrefab, transform, mItemLabels.FontSize, mItemLabels.FontSharpness, toSet, labelPos.x, labelPos.y, labelPos.z, 0f, null);
                        //                          BillboardText billboard = ChartCommon.CreateBillboardText(null,mItemLabels.TextPrefab, transform, toSet, labelPos.x, labelPos.y, labelPos.z, 0f, null, hideHierarchy, mItemLabels.FontSize, mItemLabels.FontSharpness);
                        TextController.AddText(billboard);
                        AddBillboardText(data.Name, i + refrenceIndex, billboard);
                    }
                }

                dots.Hover += (idx,t,d, pos) => { Dots_Hover(catName, idx, pos); };
                dots.Click += (idx,t,d, pos) => { Dots_Click(catName, idx, pos); };
                dots.Leave += () => { Dots_Leave(catName); };
                mCategoryObjects[catName] = categoryObj;
            }
        }

        private void Dots_Leave(string category)
        {
            TriggerActiveTextsOut();
            OnItemLeave(new GraphEventArgs(0,Vector3.zero,new DoubleVector2(0.0,0.0),-1f,category,"",""),"point");
        }

        private void Lines_Leave(string category)
        {
            OnItemLeave(new GraphEventArgs(0, Vector3.zero, new DoubleVector2(0.0, 0.0), -1f, category, "", ""),"line");
        }

        private void Dots_Click(string category,int idx, Vector2 pos)
        {
            DoubleVector3 point = Data.GetPoint(category, idx);
            Dictionary<int, BillboardText> catgoryTexts;
            BillboardText b;

            if (mTexts.TryGetValue(category, out catgoryTexts))
            {
                if (catgoryTexts.TryGetValue(idx, out b))
                    SelectActiveText(b);
            }
            string xString = StringFromAxisFormat(point, mHorizontalAxis,true);
            string yString = StringFromAxisFormat(point, mVerticalAxis,false);
            OnItemSelected(new GraphEventArgs(idx,pos, point.ToDoubleVector2(),(float)point.z, category,xString,yString));
        }

        private void Lines_Clicked(string category,int idx,Vector2 pos)
        {
            DoubleVector3 point = Data.GetPoint(category, idx);
            string xString = StringFromAxisFormat(point, mHorizontalAxis, true);
            string yString = StringFromAxisFormat(point, mVerticalAxis, false);
            OnLineSelected(new GraphEventArgs(idx, pos, point.ToDoubleVector2(), (float)point.z, category, xString, yString));
        }

        private void Lines_Hover(string category,int idx,Vector2 pos)
        {
            DoubleVector3 point = Data.GetPoint(category, idx);
            string xString = StringFromAxisFormat(point, mHorizontalAxis, true);
            string yString = StringFromAxisFormat(point, mVerticalAxis, false);
            OnLineHovered(new GraphEventArgs(idx, pos, point.ToDoubleVector2(), (float)point.z, category, xString, yString));
        }

        private void Dots_Hover(string category, int idx, Vector2 pos)
        {
            DoubleVector3 point = Data.GetPoint(category, idx);
            Dictionary<int, BillboardText> catgoryTexts;
            BillboardText b;

            if (mTexts.TryGetValue(category, out catgoryTexts))
            {
                if (catgoryTexts.TryGetValue(idx, out b))
                    SelectActiveText(b);
            }

            string xString = StringFromAxisFormat(point, mHorizontalAxis,true);
            string yString = StringFromAxisFormat(point, mVerticalAxis,false);
            OnItemHoverted(new GraphEventArgs(idx,pos, point.ToDoubleVector2(),(float)point.z, category, xString, yString));
        }

        protected override void OnItemHoverted(object userData)
        {
            base.OnItemHoverted(userData);
            var args = userData as GraphEventArgs;
            AddOccupiedCategory(args.Category, "point");
        }

        protected override void OnItemSelected(object userData)
        {
            base.OnItemSelected(userData);
            var args = userData as GraphEventArgs;
            AddOccupiedCategory(args.Category, "point");
        }
        void AddOccupiedCategory(string cat,string type)
        {
            mOccupiedCateogies.Add(cat + "|"+ type);
        }
        protected override void OnItemLeave(object userData,string type)
        {
            GraphEventArgs args = userData as GraphEventArgs;
            if (args == null)
                return;

            string item = args.Category + "|" + type;
            mOccupiedCateogies.Remove(item);
            mOccupiedCateogies.RemoveWhere(x => !Data.HasCategory(x.Split('|')[0]));

            if (mOccupiedCateogies.Count == 0)
            {
                if (NonHovered != null)
                    NonHovered.Invoke();
            }
        }

        internal override void SetAsMixedSeries()
        {
            throw new NotImplementedException();
        }
    }
}
