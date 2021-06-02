#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public abstract class AlignedItemLabels : ItemLabelsBase
    {
        /// <summary>
        /// Select the alignment of the label relative to the item
        /// </summary>
        [SerializeField]
        [Tooltip("Select the alignment of the label relative to the item")]
        private ChartLabelAlignment alignment;

        /// <summary>
        /// Select the alignment of the label relative to the item
        /// </summary>
        public ChartLabelAlignment Alignment
        {
            get { return alignment; }
            set { alignment = value; RaiseOnUpdate(); }
        }

    }
}
