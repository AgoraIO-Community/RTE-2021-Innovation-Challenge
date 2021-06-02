#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    /// <summary>
    /// this inteface hides some functionallity so it is not public with scrollableAxisData. This funcationallity is meant to be used internally by the mixedSeriesChart
    /// </summary>
    interface IMixedSeriesProxy
    {
        /// <summary>
        /// this method allows The MixesSeriesChart to add a new category in a generic way
        /// </summary>
        /// <param name="category"></param>
        /// <param name="data"></param>
        /// <returns></returns>
        bool AddCategory(string category, BaseScrollableCategoryData data);
        /// <summary>
        /// checks if the specified category already exist in the chart
        /// </summary>
        /// <param name="catgeory"></param>
        /// <returns></returns>
        bool HasCategory(string catgeory);
        /// <summary>
        /// clears the specified category of any data
        /// </summary>
        /// <param name="category"></param>
        void ClearCategory(string category);
        /// <summary>
        /// this method allows The MixesSeriesChart to append a datum to the specified category in a generic way 
        /// </summary>
        /// <param name="category"></param>
        /// <param name="value"></param>
        void AppendDatum(string category, MixedSeriesGenericValue value);
        /// <summary>
        /// this method allows The MixesSeriesChart to append a series of datum objects to the specified category in a generic way 
        /// </summary>
        /// <param name="category"></param>
        /// <param name="value"></param>
        void AppendDatum(string category, IList<MixedSeriesGenericValue> value);
    }
}
