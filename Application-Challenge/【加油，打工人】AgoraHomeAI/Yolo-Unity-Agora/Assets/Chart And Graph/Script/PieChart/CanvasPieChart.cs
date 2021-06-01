#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    /// <summary>
    /// canvas pie chart component
    /// </summary>
    [RequireComponent(typeof(RectTransform))]
    [Serializable]
    public class CanvasPieChart : PieChart, ICanvas
    {
        [SerializeField]
        [Tooltip("prefab for the pie item. must contain a PieCanvasGenerator component")]
        private PieCanvasGenerator prefab;

        [SerializeField]
        [Tooltip("the thickness of the guideline between each slice and it's label")]
        private float lineThickness = 1f;

        [SerializeField]
        [Tooltip("The line spacing for each category label line")]
        private float lineSpacing = 20f;

        [SerializeField]
        [Tooltip("The line material for each category label line")]
        private Material lineMaterial;

        public RectTransform Container;
        public Text Title;
        public Text Info;
        public Image Image;
        public CanvasPieChart()
        {
            radius = 40f;
        }

        /// <summary>
        /// prefab for the pie item. must contain a PieCanvasGenerator component
        /// </summary>
        public PieCanvasGenerator Prefab
        {
            get { return prefab; }
            set
            {
                prefab = value;
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
        protected override float InnerDepthLink
        {
            get
            {
                return 0f;
            }
        }
        protected override float OuterDepthLink
        {
            get
            {
                return 0f;
            }
        }
        protected override Material LineMaterialLink
        {
            get
            {
                return lineMaterial;
            }
        }
        protected override float LineThicknessLink
        {
            get
            {
                return lineThickness;
            }
        }
        protected override float LineSpacingLink
        {
            get
            {
                return lineSpacing;
            }
        }
        /// <summary>
        /// The line spacing for eacg category label line
        /// </summary>
        public float LineSpacing
        {
            get { return lineSpacing; }
            set
            {
                lineSpacing = value;
                OnPropertyUpdated();
            }
        }
        protected override void ValidateProperties()
        {
            base.ValidateProperties();
            if (lineSpacing < 0f)
                lineSpacing = 0f;
            if (lineThickness < 1f)
                lineThickness = 1f;
        }
        /// <summary>
        /// the thickness of the guideline between each slice and it's label
        /// </summary>
        public float LineThickness
        {
            get { return lineThickness; }
            set
            {
                lineThickness = value;
                OnPropertyUpdated();
            }
        }
        /// <summary>
        /// The line material for each category label line
        /// </summary>
        public Material LineMaterial
        {
            get { return lineMaterial; }
            set
            {
                lineMaterial = value;
                OnPropertyUpdated();
            }
        }

        public override void InternalGenerateChart()
        {
            base.InternalGenerateChart();
            if (TextController != null && TextController.gameObject)
                TextController.gameObject.transform.SetAsLastSibling();
        }
        protected override IPieGenerator PreparePieObject(out GameObject pieObject)
        {
            if(Prefab == null)
                pieObject = new GameObject();
            else
                pieObject = GameObject.Instantiate(Prefab.gameObject);
            ChartCommon.EnsureComponent<RectTransform>(pieObject);
            ChartCommon.EnsureComponent <CanvasRenderer>(pieObject);
            return ChartCommon.EnsureComponent<PieCanvasGenerator>(pieObject);

        }
    }
}
