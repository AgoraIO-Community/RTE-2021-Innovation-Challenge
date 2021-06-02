#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.Exceptions
{
    class ChartDuplicateItemException : ChartException
    {
        public ChartDuplicateItemException(String message)
            : base(message)
        {

        }
    }
}
