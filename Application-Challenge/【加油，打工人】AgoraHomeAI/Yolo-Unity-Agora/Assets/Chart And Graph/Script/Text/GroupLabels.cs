#define Graph_And_Chart_PRO
using ChartAndGraph;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

[Serializable]
public class GroupLabels : ItemLabelsBase
{
    /// <summary>
    /// Detemines the alignment of the group labels
    /// </summary>
    [SerializeField]
    [Tooltip("Detemines the alignment of the group labels")]
    private GroupLabelAlignment alignment;

    /// <summary>
    /// Detemines the alignment of the group labels
    /// </summary>
    public GroupLabelAlignment Alignment
    {
        get { return alignment; }
        set { alignment = value; RaiseOnUpdate(); }
    }

    protected override Action<IInternalUse, bool> Assign
    {
        get
        {
            return (x, clear) =>
            {
                if (clear)
                {
                    if (x.GroupLabels == this)
                        x.GroupLabels = null;
                }
                else
                {
                    if (x.GroupLabels != this)
                        x.GroupLabels = this;
                }
            };
        }
    }
}
