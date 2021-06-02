#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// Contains diffrent mateirals for different gui events 
    /// </summary>
    [Serializable]
    public class ChartDynamicMaterial
    {
        public ChartDynamicMaterial()
            :this(null,Color.clear,Color.clear)
        {

        }
        /// <summary>
        /// creates a new dynamic material
        /// </summary>
        /// <param name="normal">the default material</param>
        public ChartDynamicMaterial(Material normal)
            :this(normal, Color.clear, Color.clear)
        {
            
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="normal">the default material</param>
        /// <param name="hover">the material of the mouse hover event</param>
        /// <param name="selected">the material of the mouse click event </param>
        public ChartDynamicMaterial(Material normal,Color hover, Color selected)
        {
            Normal = normal;
            Hover = hover;
            Selected = selected;
        }

        public Material Normal;
        public Color Hover;
        public Color Selected;
    }
}
