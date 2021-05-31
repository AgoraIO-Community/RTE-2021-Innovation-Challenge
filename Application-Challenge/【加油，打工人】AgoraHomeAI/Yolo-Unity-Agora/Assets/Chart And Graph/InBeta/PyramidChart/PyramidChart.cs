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
    public class PyramidChart : AnyChart
    {
        public class PyramidEventArgs
        {
            public PyramidEventArgs(string category, string title,string text)
            {
                Title = title;
                Text = text;
                Category = category;
            }
            public string Category { get; private set; }
            public string Title { get; private set; }
            public string Text { get; private set; }
        }

        public enum JustificationType
        {
            LeftAligned,
            RightAligned,
            CenterAligned
        }

        public enum SlopeType
        {
            Center,
            Left,
            Right,
            Custom
        }

        bool mQuick = false;
        [Serializable]
        public class PyramidEvent : UnityEvent<PyramidEventArgs>
        {

        }

        /// <summary>
        /// occures when a pie item is clicked
        /// </summary>
        public PyramidEvent ItemClicked = new PyramidEvent();

        /// <summary>
        /// occures when a pie item is hovered
        /// </summary>
        public PyramidEvent ItemHovered = new PyramidEvent();

        /// <summary>
        /// occurs when no pie is hovered any longer
        /// </summary>
        public UnityEvent NonHovered = new UnityEvent();

        [SerializeField]
        [Tooltip("The material of the back of the pyramid")]
        public Material backMaterial;


        /// <summary>
        /// The inset of each pyramid component
        /// </summary>
        public Material BackMaterial
        {
            get { return backMaterial; }
            set
            {
                backMaterial = value;
                OnPropertyUpdated();
            }
        }


        [SerializeField]
        [Tooltip("The inset of each pyramid component")]
        public float inset;

        /// <summary>
        /// The inset of each pyramid component
        /// </summary>
        public float Inset
        {
            get { return inset; }
            set
            {
                inset = value;
                OnPropertyUpdated();
            }
        }


        [SerializeField]
        [Tooltip("the text justification of the pyramid chart")]
        private JustificationType justification;

        /// <summary>
        /// the text justification of the pyramid chart
        /// </summary>
        public JustificationType Justification
        {
            get { return justification; }
            set
            {
                justification = value;
                OnPropertyUpdated();
            }
        }


        [SerializeField]
        [Tooltip("the slope type of the pyramid")]
        public SlopeType slope;

        /// <summary>
        /// prefab for the pie item. must contain a PieCanvasGenerator component
        /// </summary>
        public SlopeType Slope
        {
            get { return slope; }
            set
            {
                slope = value;
                OnPropertyUpdated();
            }
        }


        [SerializeField]
        [Tooltip("prefab for the pyramid item. must contain a PyramidCanvasGenerator component")]
        private PyramidCanvasGenerator prefab;


        /// <summary>
        /// prefab for the pie item. must contain a PieCanvasGenerator component
        /// </summary>
        public PyramidCanvasGenerator Prefab
        {
            get { return prefab; }
            set
            {
                prefab = value;
                OnPropertyUpdated();
            }
        }


        [HideInInspector]
        [SerializeField]
        private PyramidData Data = new PyramidData();


        float totalWidth, totalHeight;
        protected override IChartData DataLink
        {
            get
            {
                return Data;
            }
        }

        public PyramidData DataSource
        {
            get { return Data; }
        }

        public override bool SupportRealtimeGeneration
        {
            get
            {
                return false;
            }
        }

        protected override LegenedData LegendInfo
        {
            get
            {
                LegenedData legend = new LegenedData();
                if (Data == null)
                    return legend;
                foreach (var column in ((IInternalPyramidData)Data).InternalDataSource.Columns)
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

        public PyramidChart()
        {

        }

        public class PyramidObject
        {
            public string category;
            public GameObject pyramidObject;
            public GameObject backObject; 
            public IPyramidGenerator Generator;
            public IPyramidGenerator BackGenerator;
            public string Title, Text;
            public BillboardText ItemLabel;
        }
        Dictionary<string, PyramidObject> mPyramids = new Dictionary<string, PyramidObject>();
        void HookEvents()
        {
            Data.ProperyUpdated -= Data_ProperyUpdated;
            Data.ProperyUpdated += Data_ProperyUpdated;
            Data.RealtimeProperyUpdated -= Data_RealtimeProperyUpdated;
            Data.RealtimeProperyUpdated += Data_RealtimeProperyUpdated;

            ((IInternalPyramidData)Data).InternalDataSource.DataStructureChanged -= MDataSource_DataStructureChanged;
            ((IInternalPyramidData)Data).InternalDataSource.DataStructureChanged += MDataSource_DataStructureChanged;
            ((IInternalPyramidData)Data).InternalDataSource.DataValueChanged -= MDataSource_DataValueChanged;
            ((IInternalPyramidData)Data).InternalDataSource.DataValueChanged += MDataSource_DataValueChanged;
        }

        private void Data_RealtimeProperyUpdated()
        {
            QuickInvalidate();
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
        }

        private void MDataSource_DataStructureChanged(object sender, EventArgs e)
        {
            Invalidate();
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
            mPyramids.Clear();
        }

        Vector3 AlignTextPosition(AlignedItemLabels labels, PyramidObject obj, IPyramidGenerator generator,float height)
        {
            Vector3 position = new Vector3(labels.Location.Breadth, labels.Seperation, labels.Location.Depth);
            position.y += height;
            position = generator.GetTextPosition(justification, labels.Alignment == ChartLabelAlignment.Base);
            return position;
        }

        protected IPyramidGenerator PreparePyramidObject(out GameObject pyramidObject)
        {
            if (Prefab == null)
                pyramidObject = new GameObject();
            else
                pyramidObject = GameObject.Instantiate(Prefab.gameObject);
            ChartCommon.EnsureComponent<RectTransform>(pyramidObject);
            ChartCommon.EnsureComponent<CanvasRenderer>(pyramidObject);
            return ChartCommon.EnsureComponent<PyramidCanvasGenerator>(pyramidObject);
        }

        private void GeneratePyramid(bool update)
        {
            if (update == false)
                ClearChart();
            else
                EnsureTextController();
            if (((IInternalPyramidData)Data).InternalDataSource == null)
                return;

            double[,] data = ((IInternalPyramidData)Data).InternalDataSource.getRawData();
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

            var rectTrans = GetComponent<RectTransform>();
            totalHeight = rectTrans.rect.height;
            totalWidth = rectTrans.rect.width;

            float baseX1 = 0;
            float baseX2 = totalWidth;
            float accumilatedHeight = 0;
            float? firstCenterHeight = null;
            float acummilatedWeight = 0;
            for (int i = 0; i < columnCount; ++i)
            {
                object userData = ((IInternalPyramidData)Data).InternalDataSource.Columns[i].UserData;
                var categoryData = ((PyramidData.CategoryData)userData);

                string name = ((IInternalPyramidData)Data).InternalDataSource.Columns[i].Name;
                double amount = Math.Max(data[0, i], 0);
                if (amount == 0f)
                    continue;

                float weight = (float)(amount / total);
                float actualHeight = totalHeight * weight;

                float slopeRight = categoryData.RightSlope;
                float slopeLeft = categoryData.LeftSlope;
                float atan;
                switch(slope)
                {
                    case SlopeType.Center:
                        atan = -Mathf.Atan2(totalHeight, totalWidth * 0.5f) * Mathf.Rad2Deg +90;
                        slopeRight = atan;
                        slopeLeft = atan;
                        break;
                    case SlopeType.Left:
                        atan = -Mathf.Atan2(totalHeight, totalWidth) * Mathf.Rad2Deg + 90;
                        slopeLeft = 0;
                        slopeRight = atan;
                        break;
                    case SlopeType.Right:
                        atan = -Mathf.Atan2(totalHeight, totalWidth) * Mathf.Rad2Deg + 90;
                        slopeLeft = atan;
                        slopeRight = 0;
                        break;
                    default:
                        break;

                }
                GameObject pyramidObject = null;
                GameObject pyramidBackObject = null;
                IPyramidGenerator generator = null;
                IPyramidGenerator backgenerator = null;
                PyramidObject dataObject;
                float centerHeight = actualHeight * 0.5f + accumilatedHeight;
                float unblendedHeight = centerHeight;
                if (firstCenterHeight.HasValue == false)
                    firstCenterHeight = centerHeight;
                centerHeight = Mathf.Lerp(firstCenterHeight.Value, centerHeight, categoryData.PositionBlend);

                if (mPyramids.TryGetValue(name, out dataObject))
                {
                    pyramidBackObject = dataObject.backObject;
                    pyramidObject = dataObject.pyramidObject;
                    backgenerator = dataObject.BackGenerator;
                    generator = dataObject.Generator;
                    generator.SetParams(baseX1, baseX2, totalWidth, slopeLeft, slopeRight, actualHeight,inset,0f,1f);
                    if (backgenerator !=null)
                        backgenerator.SetParams(baseX1, baseX2, totalWidth, slopeLeft, slopeRight, actualHeight, 0f, acummilatedWeight, acummilatedWeight + weight);
                    if (dataObject.ItemLabel)
                    {
                        Vector3 labelPos = AlignTextPosition(mItemLabels, dataObject,generator, centerHeight);
                        dataObject.ItemLabel.transform.localPosition = labelPos;
                        ChartCommon.UpdateTextParams(dataObject.ItemLabel.UIText, categoryData.Title);
                    }
                }
                else
                {
                    dataObject = new PyramidObject();

                    if (backMaterial != null)
                    {
                        var backGenerator = PreparePyramidObject(out pyramidBackObject);
                        backGenerator.SetParams(baseX1, baseX2, totalWidth, slopeLeft, slopeRight, actualHeight, 0f, acummilatedWeight, acummilatedWeight +weight);
                        dataObject.backObject = pyramidBackObject;
                        dataObject.BackGenerator = backGenerator;
                        ChartCommon.HideObject(pyramidBackObject, hideHierarchy);
                        pyramidBackObject.transform.SetParent(transform,false);
                        ChartCommon.EnsureComponent<ChartItem>(pyramidBackObject);
                        ChartMaterialController backcontrol = ChartCommon.EnsureComponent<ChartMaterialController>(pyramidBackObject);
                        backcontrol.Materials = new ChartDynamicMaterial(backMaterial);
                        foreach(var itemEffect  in pyramidBackObject.GetComponents<ChartItemEffect>())
                            ChartCommon.SafeDestroy(itemEffect);
                        ChartCommon.SafeDestroy(backGenerator.ContainerObject);
                    }

                    generator = PreparePyramidObject(out pyramidObject);
                    generator.SetParams(baseX1, baseX2, totalWidth, slopeLeft, slopeRight, actualHeight, inset,0f,1f);
                    ChartCommon.HideObject(pyramidObject, hideHierarchy);
                    pyramidObject.transform.SetParent(transform,false);
                    ChartCommon.EnsureComponent<ChartItem>(pyramidObject);



                    ChartMaterialController control = ChartCommon.EnsureComponent<ChartMaterialController>(pyramidObject);
                    control.Materials = Data.GetMaterial(name);
                    control.Refresh();


                    dataObject.Generator = generator;
                    dataObject.category = name;
                    dataObject.pyramidObject = pyramidObject;
                    mPyramids.Add(name, dataObject);

                    CharItemEffectController effect = ChartCommon.EnsureComponent<CharItemEffectController>(pyramidObject);
                    effect.WorkOnParent = false;
                    effect.InitialScale = false;

                    ChartItemEvents[] events = pyramidObject.GetComponentsInChildren<ChartItemEvents>();
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

                        Vector3 labelPos = AlignTextPosition(mItemLabels, dataObject, generator, 0f);
                        float angle = justification == JustificationType.LeftAligned ? -180f : 180f;
                        BillboardText billboard = ChartCommon.CreateBillboardText(null, mItemLabels.TextPrefab, dataObject.pyramidObject.transform, categoryData.Title, labelPos.x, labelPos.y, labelPos.z, angle, null, hideHierarchy, mItemLabels.FontSize, mItemLabels.FontSharpness);
                        dataObject.ItemLabel = billboard;
                        dataObject.ItemLabel.transform.localPosition = labelPos;
                        TextController.AddText(billboard);
                    }
                }

                dataObject.Text = categoryData.Text;
                dataObject.Title = categoryData.Title;
                
                if (IsCanvas)
                {
                    if (pyramidObject != null)
                    {
                        Vector2 actualPosition = new Vector2(0.5f, centerHeight) + categoryData.Shift;
                        actualPosition = new Vector2(actualPosition.x , actualPosition.y / TotalHeight);
                        var objectRect = pyramidObject.GetComponent<RectTransform>();
                        objectRect.pivot = new Vector2(0.5f, 0.5f);
                        objectRect.anchorMin = actualPosition;
                        objectRect.anchorMax = actualPosition;
                        objectRect.anchoredPosition = new Vector2();
                        objectRect.sizeDelta = new Vector2(totalWidth, actualHeight);
                    }
                    if(pyramidBackObject != null)
                    {
                        Vector2 actualPosition = new Vector2(0.5f, unblendedHeight);
                        actualPosition = new Vector2(actualPosition.x, actualPosition.y / TotalHeight);
                        var objectRect = pyramidBackObject.GetComponent<RectTransform>();
                        objectRect.pivot = new Vector2(0f, 0f);
                        objectRect.anchorMin = actualPosition;
                        objectRect.anchorMax = actualPosition;
                        objectRect.anchoredPosition = new Vector2();
                    }
                }
                accumilatedHeight += actualHeight;
                acummilatedWeight += weight;
                if(backgenerator!= null)
                    backgenerator.Generate();
                generator.Generate();
                generator.GetUpperBase(out baseX1, out baseX2);
                generator.ApplyInfo(categoryData.Title, categoryData.Text, categoryData.Image,categoryData.Scale);
                generator.SetAlpha(categoryData.Alpha);
            }
        }

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
            GeneratePyramid(mQuick);
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
            if (inset < 0)
                inset = 0;
        }

        PyramidEventArgs userDataToEventArgs(object userData)
        {
            PyramidObject pyramid = (PyramidObject)userData;
            return new PyramidEventArgs(pyramid.category, pyramid.Title,pyramid.Text);
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
            if (ItemHovered != null)
                ItemHovered.Invoke(userDataToEventArgs(userData));
        }

        protected override void OnItemSelected(object userData)
        {
            base.OnItemSelected(userData);
            var args = userDataToEventArgs(userData);
            if (ItemClicked != null)
                ItemClicked.Invoke(args);
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
                return false;
            }
        }

        protected override bool ShouldFitCanvas { get { return false; } }

        protected override float TotalDepthLink { get { return 0f; } }

        protected override float TotalHeightLink { get { return totalHeight; } }

        protected override float TotalWidthLink { get { return totalWidth; } }

        public override bool IsCanvas { get { return true; } }
    }
}
