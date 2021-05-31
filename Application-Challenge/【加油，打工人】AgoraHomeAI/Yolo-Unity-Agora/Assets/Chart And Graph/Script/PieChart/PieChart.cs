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
    /// <summary>
    /// Pie chart class
    /// </summary>
    [ExecuteInEditMode]
    [Serializable]
    public abstract class PieChart : AnyChart
    {
        public class PieEventArgs
        {
            public PieEventArgs(string category,double value)
            {
                Value = value;
                Category = category;
            }
            public double Value { get; private set; }
            public string Category { get; private set; }
        }

        bool mQuick = false;
        [Serializable]
        public class PieEvent : UnityEvent<PieEventArgs>
        {

        }

        /// <summary>
        /// occures when a pie item is clicked
        /// </summary>
        public PieEvent PieClicked = new PieEvent();
        
        /// <summary>
        /// occures when a pie item is hovered
        /// </summary>
        public PieEvent PieHovered = new PieEvent();

        /// <summary>
        /// occurs when no pie is hovered any longer
        /// </summary>
        public UnityEvent NonHovered = new UnityEvent();

        [SerializeField]
        [Tooltip("The number of mesh segements in each pie slice")]
        private int meshSegements = 20;

        [SerializeField]
        [Tooltip("The start angle of the pie chart")]
        private float startAngle =0;

        [SerializeField]
        [Range(0f,360f)]
        [Tooltip("The angle span of the pie chart")]
        private float angleSpan = 360;

        [SerializeField]
        [Range(0f, 360f)]
        [Tooltip("The spacing angle of the pie chart")]
        private float spacingAngle;

        [SerializeField]
        [Tooltip("The outer radius of the pie chart")]
        protected float radius;

        [SerializeField]
        [Tooltip("The inner radius of the pie chart")]
        private float torusRadius;

        [SerializeField]
        [Tooltip("The extrusion of each pie slice")]
        private float extrusion;


        [HideInInspector]
        [SerializeField]
        private PieData Data = new PieData();

        [SerializeField]
        [Tooltip("draw the pie in a clockwise order ")]
        private bool clockWise = false;


        GameObject mFixPositionPie = null;
        public bool ClockWise
        {
            get { return clockWise; }
            set
            {
                clockWise = value;
                Invalidate();
            }
        }

        protected override IChartData DataLink
        {
            get
            {
                return Data;
            }
        }
        public PieData DataSource
        {
            get { return Data; }
        }

        protected abstract float LineSpacingLink
        {
            get;
        }

        protected abstract float LineThicknessLink
        {
            get;
        }

        protected abstract Material LineMaterialLink
        {
            get;
        }
        protected override float TotalDepthLink
        {
            get
            {
                return 0.0f;
            }
        }
        protected override float TotalHeightLink
        {
            get
            {
                return (radius+extrusion) * 2f;
            }
        }

        protected override float TotalWidthLink
        {
            get
            {
                return (radius + extrusion) * 2f;
            }
        }

        /// <summary>
        /// The number of mesh segements in each pie slice
        /// </summary>
        public int MeshSegements
        {
            get { return meshSegements; }
            set
            {
                meshSegements = value;
                OnPropertyUpdated();
            }
        }

        /// <summary>
        /// The angle span of the pie chart
        /// </summary>
        public float AngleSpan
        {
            get { return angleSpan; }
            set
            {
                angleSpan = value;
                OnPropertyChanged();
            }
        }
        /// <summary>
        /// The spacing angle of the pie chart
        /// </summary>
        public float SpacingAngle
        {
            get { return spacingAngle; }
            set
            {
                spacingAngle = value;
                OnPropertyUpdated();
            }
        }
        public override bool SupportRealtimeGeneration
        {
            get
            {
                return false;
            }
        }
        /// <summary>
        /// The outer radius of the pie chart
        /// </summary>
        public float Radius
        {
            get { return radius; }
            set
            {
                radius = value;
                OnPropertyUpdated();
            }
        }

        /// <summary>
        /// The inner radius of the pie chart
        /// </summary>
        public float TorusRadius
        {
            get { return torusRadius; }
            set
            {
                torusRadius = value;
                OnPropertyUpdated();
            }
        }
        /// <summary>
        /// The start angle of the pie chart
        /// </summary>
        public float StartAngle
        {
            get { return startAngle; }
            set
            {
                startAngle = value;
                OnPropertyChanged();
            }
        }

        /// <summary>
        /// The extrusion of each pie slice
        /// </summary>
        public float Extrusion
        {
            get { return extrusion; }
            set
            {
                extrusion = value;
                OnPropertyUpdated();
            }
        }

        protected abstract float InnerDepthLink
        {
            get;
        }

        protected abstract float OuterDepthLink
        {
            get;
        }

        protected override LegenedData LegendInfo
        {
            get
            {
                LegenedData legend = new LegenedData();
                if (Data == null)
                    return legend;
                foreach (var column in ((IInternalPieData)Data).InternalDataSource.Columns)
                {
                    var item = new LegenedData.LegenedItem();
                    item.Name = column.Name;
                    if (column.Material != null)
                        item.Material = column.Material.Normal;
                    else
                        item.Material = null;
                    legend.AddLegenedItem(item);
                }
                return legend;
            }
        }

        public PieChart()
        {
            
        }

        public class PieObject
        {
            public string category;
            public float StartAngle;
            public float AngleSpan;
            public float Value;
            public GameObject TopObject;
            public IPieGenerator Generator;
            public BillboardText ItemLabel;
            public BillboardText CategoryLabel;
            public CanvasLines ItemLine = null;
            public CanvasLines CategoryLine = null;
        }

        /// <summary>
        /// the bars generated for the chart
        /// </summary>
        [NonSerialized]
        Dictionary<string, PieObject> mPies = new Dictionary<string, PieObject>();

        void HookEvents()
        {
            Data.ProperyUpdated -= Data_ProperyUpdated;
            Data.ProperyUpdated += Data_ProperyUpdated;
            ((IInternalPieData)Data).InternalDataSource.DataStructureChanged -= MDataSource_DataStructureChanged;
            ((IInternalPieData)Data).InternalDataSource.DataStructureChanged += MDataSource_DataStructureChanged;
            ((IInternalPieData)Data).InternalDataSource.DataValueChanged -= MDataSource_DataValueChanged;
            ((IInternalPieData)Data).InternalDataSource.DataValueChanged += MDataSource_DataValueChanged;
        }

        private void Data_ProperyUpdated()
        {
            Invalidate();
        }

        protected void QuickInvalidate()
        {
            if (Invalidating)
                return;
            Invalidate();
            mQuick = true;
        }

        public override void Invalidate()
        {
            base.Invalidate();
            mQuick = false;
        }

        private void MDataSource_DataValueChanged(object sender, DataSource.ChartDataSourceBase.DataValueChangedEventArgs e)
        {
            QuickInvalidate();
//            GeneratePie(true);
        }

        private void MDataSource_DataStructureChanged(object sender, EventArgs e)
        {
            Invalidate();
//            GenerateChart();
        }

        protected override void Start()
        {
            base.Start();
            if (ChartCommon.IsInEditMode == false)
            {
                HookEvents();
            }
            Invalidate();
        }

        protected override void OnValidate()
        {
            base.OnValidate();
            if (Application.isPlaying)
            {
                HookEvents();
            }
            Invalidate();
        }

        protected override void ClearChart()
        {
            base.ClearChart();
            mPies.Clear();
            mFixPositionPie = null;
        }

        Vector3 AlignTextPosition(AlignedItemLabels labels,PieObject obj,out CanvasLines.LineSegement line,float modifiedRaidus)
        {
            line = null;
            float angle = obj.StartAngle + obj.AngleSpan * 0.5f;
            Vector3 position = new Vector3(labels.Seperation,labels.Location.Breadth,labels.Location.Depth);
            position = Quaternion.AngleAxis(angle, Vector3.forward) * position;
            float alignRadius = (modifiedRaidus + TorusRadius) * 0.5f;
            Vector3 atAngle = (Vector3)ChartCommon.FromPolar(angle, 1f);
            if (labels.Alignment == ChartLabelAlignment.Top)
            {
                alignRadius = Mathf.Max(modifiedRaidus, TorusRadius);
                Vector3 basePosition = atAngle * alignRadius;
                Vector3 end = basePosition + position;
                end -= (position.normalized * LineSpacingLink);
                Vector4[] arr = new Vector4[] { basePosition, end };
                arr[0].w = -1f;
                arr[1].w = -1f;
                line = new CanvasLines.LineSegement(arr);
            }
            
            position += atAngle * alignRadius;
            return position;
        }

        private CanvasLines AddLineRenderer(GameObject topObject, CanvasLines.LineSegement line)
        {
            GameObject obj = ChartCommon.CreateCanvasChartItem();
            obj.transform.SetParent(topObject.transform);
            obj.transform.localScale = new Vector3(1f, 1f, 1f);
            obj.transform.localPosition = new Vector3(0f, 0f, 0f);
            obj.transform.localRotation = Quaternion.identity;
            CanvasLines lines = obj.AddComponent<CanvasLines>();
            lines.raycastTarget = false;
            var lst = new List<CanvasLines.LineSegement>();
            lst.Add(line);
            lines.SetLines(lst);
            lines.Thickness = LineThicknessLink;
            lines.material = LineMaterialLink;
            return lines;
        }

        private void GeneratePie(bool update)
        {
            if (mFixPositionPie == null)
                update = false;
            if (update == false)
                ClearChart();
            else
                EnsureTextController();
            if (((IInternalPieData)Data).InternalDataSource == null)
                return;

            double[,] data = ((IInternalPieData)Data).InternalDataSource.getRawData();
            int rowCount = data.GetLength(0);
            int columnCount = data.GetLength(1);

            if (rowCount != 1) // row count for pie must be 1
                return;

            double total = 0.0;

            for (int i = 0; i < columnCount; ++i)
            {
                double val = Math.Max(data[0, i], 0);
                total += val;
            }

            float start = startAngle;
            if (clockWise)
                start -= angleSpan;
            float totalGaps = columnCount * spacingAngle;
            float spanWithoutGaps = angleSpan - totalGaps;

            if (spanWithoutGaps < 0f)
                spanWithoutGaps = 0f;

            if (mFixPositionPie == null)
            {
                mFixPositionPie = new GameObject("FixPositionPie", typeof(ChartItem));
                ChartCommon.HideObject(mFixPositionPie, hideHierarchy);
                mFixPositionPie.transform.SetParent(transform, false);
                if (IsCanvas)
                {
                    var rectTrans = mFixPositionPie.AddComponent<RectTransform>();
                    rectTrans.anchorMax = new Vector2(0.5f, 0.5f);
                    rectTrans.anchorMin = new Vector2(0.5f, 0.5f);
                    rectTrans.pivot = new Vector2(0.5f, 0.5f);
                    rectTrans.anchoredPosition = new Vector2(0.5f, 0.5f);
                }
            }
            
            for (int i = 0; i < columnCount; ++i)
            {
                object userData = ((IInternalPieData)Data).InternalDataSource.Columns[i].UserData;
                float radiusScale = 1f;
                float depthScale = 1f;
                float depthOffset = 0f;
                if (userData != null && userData is PieData.CategoryData)
                {
                    radiusScale = ((PieData.CategoryData)userData).RadiusScale;
                    depthScale = ((PieData.CategoryData)userData).DepthScale;
                    depthOffset = ((PieData.CategoryData)userData).DepthOffset;
                }
                if (radiusScale <= 0.001f)
                    radiusScale = 1f;
                if (depthScale <= 0.001f)
                    depthScale = 1f;
                                string name = ((IInternalPieData)Data).InternalDataSource.Columns[i].Name;
                double amount = Math.Max(data[0, i], 0);
                if (amount == 0f)
                    continue;
                float weight = (float)(amount / total);
                float currentSpan = spanWithoutGaps * weight;
                GameObject pieObject = null;
                IPieGenerator generator = null;
                PieObject dataObject;
                CanvasLines.LineSegement line;
                float modifiedRadius = Mathf.Max(radius * radiusScale, torusRadius);
              //  float modifiedDepth = d
                float lineAngle = start + currentSpan * 0.5f;
                if (mPies.TryGetValue(name, out dataObject))
                {
                    dataObject.StartAngle = start;
                    dataObject.AngleSpan = currentSpan;
                    generator = dataObject.Generator;
                    if (dataObject.ItemLabel)
                    {
                        Vector3 labelPos = AlignTextPosition(mItemLabels, dataObject, out line, modifiedRadius);
                        dataObject.ItemLabel.transform.localPosition = labelPos;
                        string toSet = ChartAdancedSettings.Instance.FormatFractionDigits(mItemLabels.FractionDigits, amount, CustomNumberFormat);
                        toSet = mItemLabels.TextFormat.Format(toSet, name, "");
                        ChartCommon.UpdateTextParams(dataObject.ItemLabel.UIText, toSet);
                        if (dataObject.ItemLine != null)
                        {
                            var lst = new List<CanvasLines.LineSegement>();
                            lst.Add(line);
                            dataObject.ItemLine.SetLines(lst);
                        }
                    }
                    if (dataObject.CategoryLabel != null)
                    {
                        Vector3 labelPos = AlignTextPosition(mCategoryLabels, dataObject, out line, modifiedRadius);
                        dataObject.CategoryLabel.transform.localPosition = labelPos;
                        if (dataObject.CategoryLine != null)
                        {
                            var lst = new List<CanvasLines.LineSegement>();
                            lst.Add(line);
                            dataObject.CategoryLine.SetLines(lst);
                        }
                    }
                    Vector2 add = ChartCommon.FromPolar(start + currentSpan * 0.5f, Extrusion);
                    dataObject.TopObject.transform.localPosition = new Vector3(add.x, add.y, 0f);
                }
                else
                {
                    GameObject topObject = new GameObject();
                    if (IsCanvas)
                        topObject.AddComponent<RectTransform>();
                    ChartCommon.HideObject(topObject, hideHierarchy);
                    topObject.AddComponent<ChartItem>();
                    topObject.transform.SetParent(mFixPositionPie.transform);
                    topObject.transform.localPosition = new Vector3();
                    topObject.transform.localRotation = Quaternion.identity;
                    topObject.transform.localScale = new Vector3(1f, 1f, 1f);

                    generator = PreparePieObject(out pieObject);

                    ChartCommon.EnsureComponent<ChartItem>(pieObject);
                    ChartMaterialController control = ChartCommon.EnsureComponent<ChartMaterialController>(pieObject);
                    control.Materials = Data.GetMaterial(name);
                    control.Refresh();
                    dataObject = new PieObject();
                    dataObject.StartAngle = start;
                    dataObject.AngleSpan = currentSpan;
                    dataObject.TopObject = topObject;
                    dataObject.Generator = generator;
                    dataObject.category = name;
                    var pieInfo = pieObject.AddComponent<PieInfo>();
                    pieInfo.pieObject = dataObject;
                    pieObject.transform.SetParent(topObject.transform);
                    Vector2 add = ChartCommon.FromPolar(start + currentSpan * 0.5f, Extrusion);
                    pieObject.transform.localPosition = new Vector3(0f, 0f, 0f);
                    pieObject.transform.localScale = new Vector3(1f, 1f, 1f);
                    pieObject.transform.localRotation = Quaternion.identity;
                    mPies.Add(name,dataObject);

                    topObject.transform.localPosition = new Vector3(add.x, add.y, 0f);
                    CharItemEffectController effect = ChartCommon.EnsureComponent<CharItemEffectController>(pieObject);
                    effect.WorkOnParent = true;
                    effect.InitialScale = false;

                    ChartItemEvents[] events = pieObject.GetComponentsInChildren<ChartItemEvents>();

                    for (int j = 0; j < events.Length; ++j)
                    {
                        if (events[j] == null)
                            continue;
                        InternalItemEvents comp = (InternalItemEvents)events[j];
                        comp.Parent = this;
                        comp.UserData = dataObject;
                    }

                    if (mItemLabels != null)
                    {
                        Vector3 labelPos = AlignTextPosition(mItemLabels, dataObject, out line, modifiedRadius);
                        if (line != null && IsUnderCanvas)
                            dataObject.ItemLine = AddLineRenderer(topObject, line);
                        string toSet = ChartAdancedSettings.Instance.FormatFractionDigits(mItemLabels.FractionDigits, amount, CustomNumberFormat);
                        toSet = mItemLabels.TextFormat.Format(toSet, name, "");
                        BillboardText billboard = ChartCommon.CreateBillboardText(null,mItemLabels.TextPrefab, topObject.transform, toSet, labelPos.x, labelPos.y, labelPos.z,lineAngle, topObject.transform, hideHierarchy, mItemLabels.FontSize, mItemLabels.FontSharpness);
                        dataObject.ItemLabel = billboard;
                        TextController.AddText(billboard);
                    }

                    if (mCategoryLabels != null)
                    {
                        Vector3 labelPos = AlignTextPosition(mCategoryLabels, dataObject, out line, modifiedRadius);
                        if (line != null && IsUnderCanvas)
                            dataObject.CategoryLine = AddLineRenderer(topObject, line);
                        string toSet = name;
                        toSet = mCategoryLabels.TextFormat.Format(toSet, "", "");
                        BillboardText billboard = ChartCommon.CreateBillboardText(null,mCategoryLabels.TextPrefab, topObject.transform, toSet, labelPos.x, labelPos.y, labelPos.z, lineAngle, topObject.transform, hideHierarchy, mCategoryLabels.FontSize, mCategoryLabels.FontSharpness);
                        dataObject.CategoryLabel = billboard;
                        TextController.AddText(billboard);
                    }

                }
                float maxDepth = Mathf.Max(OuterDepthLink, InnerDepthLink);
                float depthSize = maxDepth * depthScale;
                if (pieObject != null)
                {
                    float depthStart = (maxDepth - depthSize) * 0.5f;
                    pieObject.transform.localPosition = new Vector3(0f, 0f, depthStart - depthSize * depthOffset);
                }
                dataObject.Value =(float) data[0, i];
                generator.Generate(Mathf.Deg2Rad * start, Mathf.Deg2Rad * currentSpan, modifiedRadius, torusRadius, meshSegements, OuterDepthLink * depthScale,InnerDepthLink * depthScale);
                start += spacingAngle + currentSpan;
            }

        }

        protected abstract IPieGenerator PreparePieObject(out GameObject pieObject);


        protected override void OnLabelSettingChanged()
        {
            base.OnLabelSettingChanged();
            Invalidate();
        }
        protected override void OnLabelSettingsSet()
        {
            base.OnLabelSettingsSet();
            Invalidate();
        }
        public override void InternalGenerateChart()
        {
            if (gameObject.activeInHierarchy == false)
                return;
            base.InternalGenerateChart();
            GeneratePie(mQuick);
            mQuick = false;
        }

        protected override bool HasValues(AxisBase axis)
        {
            return false;
        }

        protected override double MaxValue(AxisBase axis)
        {
            return 0.0;
        }

        protected override double MinValue(AxisBase axis)
        {
            return 0.0;
        }

        protected virtual void OnPropertyChanged()
        {
            QuickInvalidate();
        }

        protected override void OnPropertyUpdated()
        {
            base.OnPropertyUpdated();
            Invalidate();
        }

        protected override void ValidateProperties()
        {
            base.ValidateProperties();
            if (extrusion < 0)
                extrusion = 0f;
            if (radius < 0f)
                radius = 0;
            if (torusRadius < 0f)
                torusRadius = 0f;
            if (torusRadius > radius)
                torusRadius = radius;
            if (angleSpan < 10f)
                angleSpan = 10f;
            if (spacingAngle < 0f)
                spacingAngle = 0f;
        }

        PieEventArgs userDataToEventArgs(object userData)
        {
            PieObject pie = (PieObject)userData;
            return new PieEventArgs(pie.category, pie.Value);
        }

        protected override void OnNonHoverted()
        {
            base.OnNonHoverted();
            if (NonHovered != null)
                NonHovered.Invoke();
        }

        protected override void OnItemHoverted(object userData)
        {
            base.OnItemHoverted(userData);
            if (PieHovered != null)
                PieHovered.Invoke(userDataToEventArgs(userData));
        }

        protected override void OnItemSelected(object userData)
        {
            base.OnItemSelected(userData);
            var args = userDataToEventArgs(userData);
            if (PieClicked != null)
                PieClicked.Invoke(args);
        }

        protected override void Update()
        {
            base.Update();
        }

        protected override bool SupportsCategoryLabels
        {
            get
            {
                return true;
            }
        }
        protected override bool SupportsGroupLables
        {
            get
            {
                return false;
            }
        }
        protected override bool SupportsItemLabels
        {
            get
            {
                return true;
            }
        }
    }
}
