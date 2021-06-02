#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.Exceptions
{
    class ChartItemNotExistException : ChartException
    {
        public ChartItemNotExistException(string message)
            :base(message)
        {

        }
    }
}
