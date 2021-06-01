#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    interface InternalItemEvents
    {
        IInternalUse Parent { get; set; }
        object UserData { get; set; }
    }
}
