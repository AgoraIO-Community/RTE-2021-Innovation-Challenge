#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// holds settings for an axis division.
    /// </summary>
    [Serializable]
    class ChartSubDivisionInfo : ChartDivisionInfo
    {
        protected override float ValidateTotal(float total)
        {
            return Mathf.Round(total);
        }
    }
}
