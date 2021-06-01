#define Graph_And_Chart_PRO
using ChartAndGraph;
using System;
using UnityEngine;
using UnityEngine.UI;

/// <summary>
/// repsents a chart item text that is billboarded in a unity scene
/// </summary>
public class BillboardText : MonoBehaviour
{
    private RectTransform mRect;
    public GameObject UIText { get; set; }
    internal TextDirection Direction;
    public RectTransform RectTransformOverride;
    public object UserData { get; set; }
    [NonSerialized]
    public float Scale = 1f;
    public bool parentSet = false;
    public RectTransform parent = null;
    public bool Recycled = false;
    public bool YMirror = false;
    CanvasRenderer[] mRenderers;
    public void SetVisible(bool visible)
    {
        bool cull = !visible;
        RectTransform t = Rect;
        if (t == null)
            return;
        if (mRenderers == null)
            mRenderers = t.GetComponentsInChildren<CanvasRenderer>();
        for(int i=0; i< mRenderers.Length; i++)
            mRenderers[i].cull = cull;
    }
    public RectTransform Rect
    {
        get
        {
            if (UIText == null)
                return null;
            if (RectTransformOverride != null)
                return RectTransformOverride;
            if (mRect == null)
                mRect = UIText.GetComponent<RectTransform>();
            return mRect;
        }
    }
}
