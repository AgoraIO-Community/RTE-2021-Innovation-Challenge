#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// advanced settings for charts (currently includes fraction digits)
    /// </summary>
    [Serializable]
    class ChartAdancedSettings
    {
        private static ChartAdancedSettings mInstance;
        public static ChartAdancedSettings Instance
        {
            get
            {
                if (mInstance == null)
                    mInstance = new ChartAdancedSettings();
                return mInstance;
            }
        }

        private static string[] FractionDigits = new string[]
        {
            "{0:0}",
            "{0:0.#}",
            "{0:0.##}",
            "{0:0.###}",
            "{0:0.####}",
            "{0:0.#####}",
            "{0:0.######}",
            "{0:0.#######}",
        };

        [Range(0, 7)]
        public int ValueFractionDigits = 2;
        [Range(0, 7)]
        public int AxisFractionDigits = 2;

        private string InnerFormat(string format,double val)
        {
            try
            {
                return string.Format(format, val);
            }
            catch
            {

            }
            return " ";
        }

        string getFormat(int value)
        {
            value = Mathf.Clamp(value, 0, 7);
            return FractionDigits[value];
        }

        public string FormatFractionDigits(int digits,double val,string format = null)
        {
            if(String.IsNullOrEmpty(format))
                return InnerFormat(getFormat(digits), val);
            return InnerFormat(format, val);
        }

    }
}
