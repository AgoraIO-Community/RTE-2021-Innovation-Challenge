#define Graph_And_Chart_PRO
using ChartAndGraph;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    [Serializable]
    public class ChartMainDivisionInfo : ChartDivisionInfo
    {
        public DivisionMessure Messure
        {
            get { return messure; }
            set
            {
                messure = value;
                RaiseOnChanged();
            }
        }

        public float UnitsPerDivision
        {
            get { return unitsPerDivision; }
            set
            {
                unitsPerDivision = value;
                RaiseOnChanged();
            }
        }

    }
}
