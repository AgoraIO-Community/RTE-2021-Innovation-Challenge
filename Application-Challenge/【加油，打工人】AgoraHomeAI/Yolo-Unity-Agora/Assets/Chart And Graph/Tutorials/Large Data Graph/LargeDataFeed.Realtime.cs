#define Graph_And_Chart_PRO
using UnityEngine;
using ChartAndGraph;
using System.Collections.Generic;
using System;

public partial class LargeDataFeed
{
    public void AppendPointRealtime(double x, double y, double slideTime = 0f)
    {
        if (graph == null)
            return;
        bool show = false;
        if (mData.Count == 0)
            show = true;
        else
        {
            double viewX = mData[mData.Count - 1].x;
            double pageStartThreshold = currentPagePosition - mCurrentPageSizeFactor;
            double pageEndThreshold = currentPagePosition + mCurrentPageSizeFactor - graph.DataSource.HorizontalViewSize;
            if (viewX >= pageStartThreshold && viewX <= pageEndThreshold)
                show = true;
        }
        mData.Add(new DoubleVector2(x, y));
        if (show)
            graph.DataSource.AddPointToCategoryRealtime("Player 1", x, y, slideTime);
    }

}

