#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;

[RequireComponent(typeof(RectTransform))]
[RequireComponent(typeof(CanvasBarChart))]
public class BarContentFitter : MonoBehaviour
{
    public float RatioAxisMarign = 1f;
    public float FixedBarSize = 30f;
    public float RatioGroupSeperation = 5f;
    public float RatioBarSeperation = 2f;

    // Use this for initialization
    void Start()
    {
    }

    void OnValidate()
    {
        var bar = GetComponent<BarChart>();
        bar.Invalidate();   
    }

    public virtual void Match()
    {
        var rect = GetComponent<RectTransform>();
        var totalWidth = rect.rect.width;
        var bar = GetComponent<CanvasBarChart>();
        int columnCount = bar.DataSource.TotalCategories;
        int rowCount = bar.DataSource.TotalGroups;

        int rowLimit = rowCount - 1;
        double barGroupSeprationSize = RatioBarSeperation * (columnCount - 1);
        double barGroupSize = barGroupSeprationSize;// + RatioBarSize;
        double totalSize = RatioGroupSeperation * rowLimit;
        double baseSize = totalSize + 2* RatioAxisMarign + barGroupSize;

        double factor = totalWidth / baseSize;

        bar.HeightRatio = rect.rect.height;
        bar.AxisSeperation = (float)(RatioAxisMarign * factor);
        bar.BarSeperation = (float)(RatioBarSeperation * factor);
        bar.GroupSeperation = (float)(RatioGroupSeperation * factor);
        bar.BarSize = FixedBarSize;// (float)(RatioBarSize * factor);

    }

    // Update is called once per frame
    void Update()
    {

    }
}
