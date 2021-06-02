#define Graph_And_Chart_PRO
using ChartAndGraph;
using System;
using UnityEngine;

[Serializable]
public class CategoryLabels : AlignedItemLabels
{
    public enum ChartCategoryLabelOptions
    {
        All,
        FirstOnly
    }
    /// <summary>
    /// Determines which labels are visible
    /// </summary>
    [SerializeField]
    [Tooltip("Determines which labels are visible")]
    private ChartCategoryLabelOptions visibleLabels;
    /// <summary>
    /// Determines which labels are visible
    /// </summary>
    public ChartCategoryLabelOptions VisibleLabels
    {
        get { return visibleLabels; }
        set { visibleLabels = value; RaiseOnChanged(); }
    }

    protected override Action<IInternalUse, bool> Assign
    {
        get
        {
            return (x, clear) =>
            {
                if (clear)
                {
                    if (x.CategoryLabels == this)
                        x.CategoryLabels = null;
                }
                else
                {
                    if (x.CategoryLabels != this)
                        x.CategoryLabels = this;
                }
            };
        }
    }


}
