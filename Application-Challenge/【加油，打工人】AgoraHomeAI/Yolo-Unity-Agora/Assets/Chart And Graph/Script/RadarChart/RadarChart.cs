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
    [ExecuteInEditMode]
    public abstract class RadarChart : AnyChart
    {
        protected Dictionary<string, List<BillboardText>> mTexts = new Dictionary<string, List<BillboardText>>();
        protected HashSet<BillboardText> mActiveTexts = new HashSet<BillboardText>();
        HashSet<string> mOccupiedCateogies = new HashSet<string>();
        Vector3[] mDirections;
        public class RadarEventArgs
        {
            public RadarEventArgs(string category,string group,double value,Vector3 position, int index)
            {
                Position = position;
                Category = category;
                Group = group;
                Value = value;
                Index = index;
            }

            public int Index { get; private set; }
            public string Category { get; private set; }
            public string Group { get; private set; }
            public double Value { get; private set; }
            public Vector3 Position { get; private set; }
        }

        [SerializeField]
        private float radius = 3f;

        public float Radius
        {
            get { return radius; }
            set
            {
                radius = value;
                GenerateChart();
            }
        }

        [SerializeField]
        private float angle = 0f;

        public float Angle
        {
            get { return angle; }
            set
            {
                angle = value;
                GenerateChart();
            }
        }
        [SerializeField]
        private Material axisPointMaterial;


        protected void AddBillboardText(string cat, BillboardText text)
        {
            List<BillboardText> addTo;
            if (mTexts.TryGetValue(cat, out addTo) == false)
            {
                addTo = new List<BillboardText>();
                mTexts.Add(cat, addTo);
            }
            addTo.Add(text);
        }

        public Material AxisPointMaterial
        {
            get
            {
                return axisPointMaterial;
            }
            set
            {
                axisPointMaterial = value;
                GenerateChart();
            }
        }


        [SerializeField]
        private Material axisLineMaterial;
        public Material AxisLineMaterial
        {
            get
            {
                return axisLineMaterial;
            }
            set
            {
                axisLineMaterial = value;
                GenerateChart();
            }
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

        [SerializeField]
        private float axisThickness = 0.1f;

        public float AxisThickness
        {
            get
            {
                return axisThickness;
            }
            set
            {
                axisThickness = value;
                GenerateChart();
            }
        }

        [SerializeField]
        private float axisPointSize = 1f;

        public float AxisPointSize
        {
            get
            {
                return axisPointSize;
            }
            set
            {
                axisPointSize = value;
                GenerateChart();
            }
        }

        [SerializeField]
        private float axisAdd = 0f;

        public float AxisAdd
        {
            get
            {
                return axisAdd;
            }
            set
            {
                axisAdd = value;
                GenerateChart();
            }
        }

        [SerializeField]
        private int totalAxisDevisions = 5;
         
        public int TotalAxisDevisions
        {
            get
            {
                return totalAxisDevisions;
            }
            set
            {
                totalAxisDevisions = value;
                GenerateChart();
            }
        }

        [Serializable]
        public class RadarEvent : UnityEvent<RadarEventArgs>
        {

        }

        /// <summary>
        /// occures when a point is clicked
        /// </summary>
        public RadarEvent PointClicked = new RadarEvent();
        /// <summary>
        /// occurs when a point is hovered
        /// </summary>
        public RadarEvent PointHovered = new RadarEvent();

        /// <summary>
        /// occurs when no point is hovered any longer
        /// </summary>
        public UnityEvent NonHovered =  new UnityEvent();

        List<GameObject> mAxisObjects = new List<GameObject>();
        /// <summary>
        /// the radar data
        /// </summary>
        [HideInInspector]
        [SerializeField]
        private RadarChartData Data = new RadarChartData();

        /// <summary>
        /// Holds the radar chart data. including values, categories and groups.
        /// </summary>
        public RadarChartData DataSource { get { return Data; } }

        void HookEvents()
        {
            Data.ProperyUpdated -= DataUpdated;
            Data.ProperyUpdated += DataUpdated;
            ((IInternalRadarData)Data).InternalDataSource.DataStructureChanged -= StructureUpdated;
            ((IInternalRadarData)Data).InternalDataSource.DataStructureChanged += StructureUpdated;
            ((IInternalRadarData)Data).InternalDataSource.DataValueChanged -= ValueChanged; ;
            ((IInternalRadarData)Data).InternalDataSource.DataValueChanged += ValueChanged;
        }

        private void ValueChanged(object sender, DataSource.ChartDataSourceBase.DataValueChangedEventArgs e)
        {
            Invalidate();
        }

        private void StructureUpdated(object sender, EventArgs e)
        {
            Invalidate();
        }

        private void DataUpdated()
        {
            Invalidate();
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
                foreach (var column in ((IInternalRadarData)Data).InternalDataSource.Columns)
                {
                    var item = new LegenedData.LegenedItem();
                    var catData = column.UserData as RadarChartData.CategoryData;
                    item.Name = column.Name;
                    if (catData.FillMaterial != null)
                        item.Material = catData.FillMaterial;
                    else
                    {
                        if (catData.LineMaterial != null)
                        {
                            item.Material = catData.LineMaterial;
                        }
                        else
                            item.Material = null;
                    }
                    legend.AddLegenedItem(item);
                }
                return legend;
            }
        }

        protected override IChartData DataLink
        {
            get
            {
                return Data;
            }
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
                return true;
            }
        }

        protected override bool SupportsItemLabels
        {
            get
            {
                return true;
            }
        }

        protected override float TotalDepthLink
        {
            get
            {
                return 0f;
            }
        }

        protected override float TotalHeightLink
        {
            get
            {
                return 0f;
            }
        }

        protected override float TotalWidthLink
        {
            get
            {
                return 0f;
            }
        }

        protected override void Update()
        {
            base.Update();
        }

        /// <summary>
        /// used internally , do not call this method
        /// </summary>
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
            mActiveTexts.Clear();
            mTexts.Clear();
        }

        protected override void LateUpdate()
        {
            base.LateUpdate();       
        }

        public bool ItemToWorldPosition(string group, double amount, out Vector3 worldposition)
        {
            worldposition = Vector3.zero;
            if (mDirections == null || mDirections.Length == 0)
                return false;
            int index = Data.GetGroupIndex(group);
            Vector3 dir = mDirections[index];
            double max = Data.GetMaxValue();
            worldposition = ((float)(amount / max) * Radius) * dir;
            worldposition = transform.TransformPoint(worldposition);
            return true;
        }

        public bool SnapWorldPointToPosition(Vector3 worldSpace,out string group,out double amount)
        {
            group = null;
            amount = 0f;
            if (mDirections == null || mDirections.Length == 0)
                return false;
            Vector3 pos = transform.InverseTransformPoint(worldSpace);
            pos.z = 0;

         //   Vector3 dir = mDirections[0];
            group = Data.GetGroupName(0);
            if (Math.Abs(pos.x) < 0.001f && Math.Abs(pos.y) < 0.001f)
            {
                //zero vector do nothing we are taking the first direction in the array
            }
            else
            {
                float dot = float.MinValue;
                for (int i = 0; i < mDirections.Length; i++)
                {
                    float newDot = Vector3.Dot(mDirections[i], pos);
                    if(newDot > dot)
                    {
                        dot = newDot;
                //        dir = mDirections[i];
                        group = Data.GetGroupName(i);
                    }
                }
            }
            
            float mag = pos.magnitude;
            double max = Data.GetMaxValue();
            amount = ((mag/Radius) * max);
            amount = Math.Max(0, Math.Min(max, amount));
            return true;
        }

        public override void InternalGenerateChart()
        {
            if (gameObject.activeInHierarchy == false)
                return;

            ClearChart();

            base.InternalGenerateChart();
            
            if (((IInternalRadarData)Data).InternalDataSource == null)
                return;

            double[,] data = ((IInternalRadarData)Data).InternalDataSource.getRawData();
            int rowCount = data.GetLength(0);
            int columnCount = data.GetLength(1);

            //restrict to 3 groups
            if (rowCount <3)
                return;

            mDirections = new Vector3[rowCount];
            float[] angles = new float[rowCount];

            for (int i = 0; i < rowCount; i++)
            {
                float angle = (float)((((float)i) / (float)rowCount) * Math.PI * 2f) + Angle * Mathf.Deg2Rad;
                angles[i] = angle;
                mDirections[i] = new Vector3(Mathf.Cos(angle), Mathf.Sin(angle), 0f);
            }

            Vector3[] path = new Vector3[rowCount];
            Vector3 zAdd = Vector3.zero;

            for (int i = 0; i < TotalAxisDevisions; i++)
            {
                float rad = Radius * ((float)(i + 1) / (float)TotalAxisDevisions);
                for (int j = 0; j < rowCount; j++)
                    path[j] = (mDirections[j] * rad) + zAdd;
              //  path[rowCount] = path[0];
                zAdd.z += AxisAdd;
                GameObject axisObject = CreateAxisObject(AxisThickness, path);
                mAxisObjects.Add(axisObject);
                axisObject.transform.SetParent(transform, false);
            }

            if (mGroupLabels != null && mGroupLabels.isActiveAndEnabled)
            {
                for (int i = 0; i < rowCount; i++)
                {
                    string group = Data.GetGroupName(i);
                    Vector3 basePosition = mDirections[i] * Radius;
                    Vector3 breadthAxis = Vector3.Cross(mDirections[i], Vector3.forward);
                    Vector3 position = basePosition + mDirections[i] * mGroupLabels.Seperation;
                    position += breadthAxis * mGroupLabels.Location.Breadth;
                    position += new Vector3(0f , 0f, mGroupLabels.Location.Depth);
                    string toSet = mGroupLabels.TextFormat.Format(group, "", "");
                    BillboardText billboard = ChartCommon.CreateBillboardText(null,mGroupLabels.TextPrefab, transform, toSet, position.x, position.y, position.z, angles[i],transform,hideHierarchy, mGroupLabels.FontSize, mGroupLabels.FontSharpness);
                    billboard.UserData = group;
                    TextController.AddText(billboard);
                }
            }

            double maxValue = Data.GetMaxValue();

            if (maxValue > 0.000001f)
            { 

                for (int i = 0; i < columnCount; i++)
                {
                    double finalMaxValue = DataSource.GetCategoryMaxValue(i);
                    if (finalMaxValue < 0f)
                        finalMaxValue = maxValue;
                    for (int j = 0; j < rowCount; j++)
                    {
                        float rad = ((float)(data[j, i] / finalMaxValue)) * Radius;
                        path[j] = mDirections[j] * rad;
                    }
                  //  path[rowCount] = path[0];
                    GameObject category = CreateCategoryObject(path, i);
                    category.transform.SetParent(transform, false);
                }

                if (mItemLabels != null && mItemLabels.isActiveAndEnabled)
                {
                    float angle = mItemLabels.Location.Breadth;
                    float blend = (angle / 360f);
                    blend -= Mathf.Floor(blend);
                    blend *= rowCount;
                    int index = (int)blend;
                    int nextIndex = (index + 1) % rowCount;
                    blend = blend - Mathf.Floor(blend);
                    for (int i = 0; i < TotalAxisDevisions; i++)
                    { 
                        float factor = ((float)(i + 1) / (float)TotalAxisDevisions);
                        float rad = Radius * factor + mItemLabels.Seperation;
                        string value = ChartAdancedSettings.Instance.FormatFractionDigits(mItemLabels.FractionDigits,(float)(maxValue * factor), CustomNumberFormat);
                        Vector3 position = Vector3.Lerp(mDirections[index] , mDirections[nextIndex] , blend) * rad;
                        position.z = mItemLabels.Location.Depth;
                        string toSet = mItemLabels.TextFormat.Format(value, "", "");
                        BillboardText billboard = ChartCommon.CreateBillboardText(null,mItemLabels.TextPrefab, transform, toSet, position.x, position.y, position.z,0f, transform,hideHierarchy, mItemLabels.FontSize, mItemLabels.FontSharpness);
                        billboard.UserData = (float)(maxValue * factor);
                        TextController.AddText(billboard);
                    }
                }
            }
        }

        protected override void OnItemSelected(object userData)
        {
            base.OnItemSelected(userData);
            RadarEventArgs args = userData as RadarEventArgs;
            if (args == null)
                return;
            mOccupiedCateogies.Add(args.Category);
            if(PointClicked != null)
                PointClicked.Invoke(args);
        }

        protected override void OnItemLeave(object userData,string type)
        {
            base.OnItemLeave(userData, type);
            RadarEventArgs args = userData as RadarEventArgs;
            foreach (BillboardText t in mActiveTexts)
            {
                foreach (ChartItemEffect effect in t.UIText.GetComponents<ChartItemEffect>())
                {
                    if (t.UIText == null)
                        continue;
                    effect.TriggerOut(false);
                }
            }
            mActiveTexts.Clear();

            string category = args.Category;
            mOccupiedCateogies.Remove(category);
            mOccupiedCateogies.RemoveWhere(x => !Data.HasCategory(x));
            if (mOccupiedCateogies.Count == 0)
            {
                if (NonHovered != null)
                    NonHovered.Invoke();
            }

        }

        protected override void OnItemHoverted(object userData)
        {
            base.OnItemHoverted(userData);
            List<BillboardText> catgoryTexts;
            RadarEventArgs args = userData as RadarEventArgs;
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


            if (mTexts.TryGetValue(args.Category, out catgoryTexts))
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
            mOccupiedCateogies.Add(args.Category);
            if(PointHovered != null)
                PointHovered.Invoke(args);
        }

        protected abstract GameObject CreateCategoryObject(Vector3[] path, int category);
        protected abstract GameObject CreateAxisObject(float thickness, Vector3[] path);

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
    }
}
