#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.EventSystems;

namespace ChartAndGraph
{
    public class CanvasBarChart : BarChart, ICanvas
    {

        /// <summary>
        /// The seperation between the axis and the chart bars.
        /// </summary>
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
        protected override ChartMagin MarginLink
        {
            get
            {
                return fitMargin;
            }
        }

        [SerializeField]
        /// <summary>
        /// prefab for the bar elements of the chart. must be the size of one unit with a pivot at the middle bottom
        /// </summary>
        [Tooltip("Prefab for the bar elements of the chart. must be the size of one unit with a pivot at the middle bottom")]
        private CanvasRenderer barPrefab;

        /// <summary>
        /// prefab for the bar elements of the chart. must be the size of one unit with a pivot at the middle bottom
        /// </summary>
        public CanvasRenderer BarPrefab
        {
            get { return barPrefab; }
            set
            {
                barPrefab = value;
                OnPropertyUpdated();
            }
        }

        public override bool IsCanvas
        {
            get
            {
                return true;
            }
        }
        /// <summary>
        /// The seperation between the axis and the chart bars.
        /// </summary>
        [SerializeField]
        [Tooltip("The seperation between the axis and the chart bars")]
        private float axisSeperation = 20f;
        protected override float TotalDepthLink
        {
            get
            {
                return 0.0f;
            }
        }
        /// <summary>
        /// The seperation between the axis and the chart bars.
        /// </summary>
        public float AxisSeperation
        {
            get { return axisSeperation; }
            set
            {
                axisSeperation = value;
                OnPropertyUpdated();
            }
        }

        /// <summary>
        /// seperation between bar of the same group
        /// </summary>
        [SerializeField]
        [Tooltip("seperation between bar of the same group")]
        private float barSeperation = 45f;

        /// <summary>
        /// seperation between bars of the same group.
        /// </summary>
        public float BarSeperation
        {
            get { return barSeperation; }
            set
            {
                barSeperation = value;
                OnPropertyUpdated();
            }
        }

        /// <summary>
        /// seperation between bar groups
        /// </summary>
        [SerializeField]
        [Tooltip("seperation between bar groups")]
        private float groupSeperation = 220f;

        /// <summary>
        /// The seperation between bar groups.
        /// <summary>
        public float GroupSeperation
        {
            get { return groupSeperation; }
            set
            {
                groupSeperation = value;
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
        /// the width of each bar in the chart
        /// </summary>
        [SerializeField]
        [Tooltip("the width of each bar in the chart")]
        private float barSize = 20f;

        /// <summary>
        /// the width of each bar in the chart
        /// </summary>
        public float BarSize
        {
            get { return barSize; }
            set
            {
                barSize = value;
                OnPropertyUpdated();
            }
        }

        protected override ChartOrientedSize AxisSeperationLink
        {
            get
            {
                return new ChartOrientedSize(AxisSeperation);
            }
        }

        protected override ChartOrientedSize BarSeperationLink
        {
            get
            {
                return new ChartOrientedSize(BarSeperation);
            }
        }

        protected override ChartOrientedSize GroupSeperationLink
        {
            get
            {
                return new ChartOrientedSize(GroupSeperation);
            }
        }

        protected override ChartOrientedSize BarSizeLink
        {
            get
            {
                return new ChartOrientedSize(BarSize);
            }
        }

        protected override void SetBarSize(GameObject bar, Vector3 size,float elevation)
        {
            RectTransform rect = bar.GetComponent<RectTransform>();

            if (rect != null)
            {
                float ySize = size.y;
                float yAnchor = 0f;
                if (ySize < 0)
                {
                    ySize = -ySize;
                    yAnchor = 1;
                }
                rect.pivot = new Vector2(0.5f, yAnchor);
                rect.sizeDelta = new Vector2(size.x, ySize);
                Vector2 v = rect.localPosition;
                v.y = elevation;
                rect.localPosition = v;
            }
            else
                base.SetBarSize(bar, size,elevation);
        }

        protected override void Update()
        {
            base.Update();
        }
        //protected override Vector3 CanvasFitOffset
        //{
        //    get
        //    {
        //        return new Vector3();
        //    }
        //}

        /// <summary>
        /// 
        /// </summary>
        [SerializeField]
        [Tooltip("")]
        private FitType barFitType = FitType.Aspect;

        /// <summary>
        /// the width of each bar in the chart
        /// </summary
        public FitType BarFitType
        {
            get { return barFitType; }
            set
            {
                barFitType = value;
                OnPropertyUpdated();
            }
        }

        /// <summary>
        /// 
        /// </summary>
        [SerializeField]
        [Tooltip("")]
        private FitAlign barFitAlign = FitAlign.CenterXCenterY;

        /// <summary>
        /// the width of each bar in the chart
        /// </summary
        public FitAlign BarFitAlign
        {
            get { return barFitAlign; }
            set
            {
                barFitAlign = value;
                OnPropertyUpdated();
            }
        }


        protected override FitType FitAspectCanvas { get { return BarFitType; } }
        protected override FitAlign FitAlignCanvas { get { return BarFitAlign; } }
        [ContextMenu("Refresh chart")]
        public override void InternalGenerateChart()
        {
            RectTransform trans = GetComponent<RectTransform>();

            if (FitToContainer)
            {
                float width = MessureWidth();
                heightRatio = width * (trans.rect.size.y / trans.rect.size.x);
            }

            base.InternalGenerateChart();
            //if (TextController != null && TextController.gameObject)
            //    TextController.gameObject.transform.SetAsLastSibling();
            //float widthScale = trans.rect.size.x / TotalWidth;
            //float heightScale = trans.rect.size.y / HeightRatio;
            //GameObject fixPosition = new GameObject();
            //ChartCommon.HideObject(fixPosition, hideHierarchy);
            //fixPosition.AddComponent<ChartItem>();
            //fixPosition.transform.position = transform.position;
            //while (gameObject.transform.childCount > 0)
            //    transform.GetChild(0).SetParent(fixPosition.transform, false);
            //fixPosition.transform.SetParent(transform, false);
            //fixPosition.transform.localScale = new Vector3(1f, 1f, 1f);
            //float uniformScale = Math.Min(widthScale, heightScale);
            //fixPosition.transform.localScale = new Vector3(uniformScale, uniformScale, uniformScale);
            //fixPosition.transform.localPosition = new Vector3(-TotalWidth * uniformScale * 0.5f, -HeightRatio * uniformScale * 0.5f, 0f);
        }

        protected override GameObject BarPrefabLink
        {
            get
            {
                if (BarPrefab == null)
                    return null;
                return BarPrefab.gameObject;
            }
        }

    }
}
