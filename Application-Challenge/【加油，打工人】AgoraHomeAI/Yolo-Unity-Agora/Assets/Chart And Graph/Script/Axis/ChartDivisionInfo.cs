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
    /// holds settings for an axis division.
    /// </summary>
    [Serializable]
    public class ChartDivisionInfo : IInternalSettings
    {
        public enum DivisionMessure
        {
            TotalDivisions,
            DataUnits            
        }

        /// <summary>
        /// validates all properties of this instance. (check if they have correct values)
        /// </summary>
        public void ValidateProperites()
        {
            if (total < 0)
                total = 0;
            fontSharpness = Mathf.Clamp(fontSharpness, 1f, 3f);
            fontSize = Mathf.Max(fontSize, 0);
            fractionDigits = Mathf.Clamp(fractionDigits, 0, 7);
            markThickness = Mathf.Max(markThickness, 0f);
            materialTiling.TileFactor = Mathf.Max(materialTiling.TileFactor, 0f);
            textPrefix = (textPrefix == null) ? "" : textPrefix;
            textSuffix = (textSuffix == null) ? "" : textSuffix;
        }

        protected virtual float ValidateTotal(float total)
        {
            return total;
        }


        /// <summary>
        /// total division lines
        /// </summary>
        [SerializeField]
        [Canvas, NonCanvas]
        [Tooltip("messure used to create divisions")]
        protected DivisionMessure messure = DivisionMessure.TotalDivisions;

        /// <summary>
        /// data units per each division in the chart
        /// </summary>
        [SerializeField]
        [Canvas, NonCanvas]
        [Tooltip("data units per division")]
        protected float unitsPerDivision;

        /// <summary>
        /// total division lines
        /// </summary>
        [SerializeField]
        [Canvas, NonCanvas]
        [Tooltip("total division lines")]
        private int total = 3;

        /// <summary>
        /// total division lines
        /// </summary>
        public int Total
        {
            get { return total; }
            set
            {
                total = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// Material for the line of the division
        /// </summary>
        [SerializeField]
        [Canvas, NonCanvas]
        [Tooltip("Material for the line of the division")]
        private Material material;

        /// <summary>
        /// Material for the division lines
        /// </summary>
        public Material Material
        {
            get { return material; }
            set
            {
                material = value;
                RaiseOnChanged();
            }
        }


        /// <summary>
        /// Material tiling for the division lines. Use this to strech or tile the material along the line
        /// </summary>
        [SerializeField]
        [Canvas, NonCanvas]
        [Tooltip("Material tiling for the division lines. Use this to strech or tile the material along the line")]
        private MaterialTiling materialTiling = new MaterialTiling(false, 100f);
        
        /// <summary>
        /// Material tiling for the division lines. Use this to strech or tile the material along the line
        /// </summary>
        public MaterialTiling MaterialTiling
        {
            get {return materialTiling;}
            set
            {
                materialTiling = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// The length of the far side of the division lines. This is used only by 3d chart when MarkDepth >0
        /// </summary>
        [SerializeField]
        [Simple, NonCanvas]
        [Tooltip("The length of the far side of the division lines. This is used only by 3d chart when MarkDepth >0")]
        private AutoFloat markBackLength = new AutoFloat(true, 0.5f);
        /// <summary>
        /// The length of the far side of the division lines. This is used only by 3d charts when MarkDepth >0
        /// </summary>
        public AutoFloat MarkBackLength
        {
            get { return markBackLength; }
            set
            {
                markBackLength = value;
                RaiseOnChanged();
            }
        }
        /// <summary>
        /// The length of the the division lines.
        /// </summary>
        [SerializeField]
        [Simple, Canvas, NonCanvas]
        [Tooltip("The length of the the division lines.")]
        private AutoFloat markLength = new AutoFloat(true,0.5f);
        /// <summary>
        /// The length of the the division lines.
        /// </summary>
        public AutoFloat MarkLength
        {
            get { return markLength; }
            set
            {
                markLength = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// the depth of the division line. This is used by 3d charts only 
        /// </summary>
        [SerializeField]
        [Simple, NonCanvas]
        private AutoFloat markDepth = new AutoFloat(true,0.5f);
        public AutoFloat MarkDepth
        {
            get { return markDepth; }
            set
            {
                markDepth = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// the thickness of the division lines
        /// </summary>
        [SerializeField]
        [Simple, Canvas, NonCanvas]
        [Tooltip("the thickness of the division lines")]
        private float markThickness = 0.1f;

        /// <summary>
        /// the thickness of the division lines
        /// </summary>
        public float MarkThickness
        {
            get { return markThickness; }
            set
            {
                markThickness = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// A prefab for the division labels
        /// </summary>
        [SerializeField]
        [Simple,Canvas, NonCanvas]
        [Tooltip("A prefab for the division labels")]
        private MonoBehaviour textPrefab;
        /// <summary>
        /// A prefab for the division labels
        /// </summary>
        public MonoBehaviour TextPrefab
        {
            get { return textPrefab; }
            set
            {
                textPrefab = value;
                RaiseOnChanged();
            }
        }
        /// <summary>
        /// prefix for the axis labels
        /// </summary>
        [SerializeField]
        [Simple,Canvas,NonCanvas]
        [Tooltip("prefix for the axis labels")]
        private string textPrefix;

        /// <summary>
        /// prefix for the axis labels
        /// </summary>
        public string TextPrefix
        {
            get { return textPrefix; }
            set
            {
                textPrefix = value;
                RaiseOnChanged();
            }
        }
        /// <summary>
        /// suffix for the axis labels
        /// </summary>
        [SerializeField]
        [Simple, Canvas, NonCanvas]
        [Tooltip("suffix for the axis labels")]
        private string textSuffix;
        /// <summary>
        /// suffix for the axis labels
        /// </summary>
        public string TextSuffix
        {
            get { return textSuffix; }
            set
            {
                textSuffix = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// the number of fraction digits in the division labels.
        /// </summary>
        [Range(0,7)]
        [Simple, Canvas, NonCanvas]
        [SerializeField]
        [Tooltip("the number of fraction digits in text labels")]
        private int fractionDigits = 2;
        /// <summary>
        /// the number of fraction digits in text labels.
        /// </summary>
        public int FractionDigits
        {
            get { return fractionDigits; }
            set
            {
                fractionDigits = value;
                RaiseOnChanged();
            }
        }
        /// <summary>
        /// Label font size
        /// </summary>
        [Simple,Canvas, NonCanvas]
        [SerializeField]
        [Tooltip("Label font size")]
        private int fontSize = 12;
        /// <summary>
        /// Label font size
        /// </summary>
        public int FontSize
        {
            get { return fontSize; }
            set
            {
                fontSize = value;
                RaiseOnChanged(); 
            }
        }

        /// <summary>
        /// makes the labels sharper if they are blurry
        /// </summary>
        [Range(1f,3f)]
        [Simple, Canvas, NonCanvas]
        [Tooltip("makes the labels sharper if they are blurry")]
        [SerializeField]
        private float fontSharpness = 1f;

        /// <summary>
        /// makes the labels sharper if they are blurry
        /// </summary>
        public float FontSharpness
        {
            get { return fontSharpness; }
            set
            {
                fontSharpness = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// depth seperation the division lables. used by 3d charts only
        /// </summary>
        [Simple, NonCanvas]
        [SerializeField]
        [Tooltip("depth seperation the division lables")]
        private float textDepth;

        /// <summary>
        /// depth seperation the division lables.used by 3d charts only
        /// </summary>
        public float TextDepth
        {
            get { return textDepth; }
            set
            {
                textDepth = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// breadth seperation for the division labels
        /// </summary>
        [Simple, Canvas,NonCanvas]
        [SerializeField]
        [Tooltip("breadth seperation for the division labels")]
        private float textSeperation;

        /// <summary>
        /// breadth seperation for the division labels
        /// </summary>
        public float TextSeperation
        {
            get { return textSeperation; }
            set
            {
                textSeperation = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// Alignment of the division lables
        /// </summary>
        [Simple, Canvas, NonCanvas]
        [SerializeField]
        [Tooltip("Alignment of the division lables")]
        private ChartDivisionAligment alignment = ChartDivisionAligment.Standard;

        /// <summary>
        /// Alignment of the division lables
        /// </summary>
        public ChartDivisionAligment Alignment
        {
            get { return alignment; }
            set
            {
                alignment = value;
                RaiseOnChanged();
            }
        }

#pragma warning disable 0067
        private event EventHandler OnDataUpdate;
#pragma warning restore 0067
        private event EventHandler OnDataChanged;

        protected virtual void RaiseOnChanged()
        {
            if (OnDataChanged != null)
                OnDataChanged(this, EventArgs.Empty);
        }

        #region Intenal Use
        event EventHandler IInternalSettings.InternalOnDataUpdate
        {
            add
            {
                OnDataUpdate += value;
            }

            remove
            {
                OnDataUpdate -= value;
            }
        }

        event EventHandler IInternalSettings.InternalOnDataChanged
        {
            add
            {
                OnDataChanged += value;
            }
            remove
            {
                OnDataChanged -= value;
            }
        }
        #endregion
    }
}
