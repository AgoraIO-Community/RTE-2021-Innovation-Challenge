#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;
using UnityEngine.UI;

public class MarkerText : MonoBehaviour {

    public GraphChartBase Chart;
    public Text TextObject;
    /// <summary>
    /// the text position in graph coordinates
    /// </summary>
    public DoubleVector2 point;

    /// <summary>
    /// offsets the text on the x axis away from the selected point
    /// </summary>
    public float SeperationOffset = 0f;
    /// <summary>
    /// offsets the text on the y axus away from the selected point
    /// </summary>
    public float ElevationOffset = 0f;
    // Use this for initialization
    void Start()
    {
    }

    // Update is called once per frame
    void Update()
    {
        if (Chart == null)
            return;
        Vector3 res;

        if (Chart.PointToWorldSpace(out res, point.x, point.y))
            TextObject.transform.position = res + new Vector3(SeperationOffset, ElevationOffset, 0f);
    }
}
