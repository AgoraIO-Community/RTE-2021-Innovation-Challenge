#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph.Legened
{
    /// <summary>
    /// use this in legened item prefabs to set an image and text for the legend item
    /// </summary>
    class CanvasLegendItem : MonoBehaviour
    {
        public Image Image = null;
        public Text Text = null;
    }
}
