#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    public enum GroupLabelAlignment
    {
        /// <summary>
        /// add the label at the center of each group
        /// </summary>
        Center,
        /// <summary>
        /// add the label at the end of each group
        /// </summary>
        EndOfGroup,
        /// <summary>
        /// add the label at the begining of each group
        /// </summary>
        BeginingOfGroup,
        /// <summary>
        /// alternate between both sides of the group
        /// </summary>
        AlternateSides,
        /// <summary>
        /// add a label at the top of each bar
        /// </summary>
        BarTop,
        /// <summary>
        /// add a label at the bottom of each bar
        /// </summary>
        BarBottom,
        /// <summary>
        /// add a label at the first bar of each group
        /// </summary>
        FirstBar
    }
}
