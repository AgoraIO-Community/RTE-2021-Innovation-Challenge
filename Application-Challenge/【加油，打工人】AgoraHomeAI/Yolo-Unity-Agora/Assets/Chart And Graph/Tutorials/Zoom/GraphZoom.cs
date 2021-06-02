#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;
using UnityEngine.UI;
using System;

/// <summary>
/// this is an example of zoom using mouse for the graph chart
/// </summary>
[RequireComponent(typeof(GraphChart))]
public class GraphZoom : MonoBehaviour
{
    GraphChart graph;
    Vector3 mZoomBasePosition;
    DoubleVector3 mZoomBaseChartSpace;
    DoubleVector3 InitalScrolling;
    DoubleVector3 InitalViewSize;
    DoubleVector3 InitalViewDirection;
    DoubleVector3 InitialOrigin;
    public float errorMargin = 5f;
    public float ZoomSpeed = 20f;
    public float MaxViewSize = 10f;
    public float MinViewSize = 0.1f;
    float totalZoom = 0;
    
	// Use this for initialization
	void Start ()
    {

    }

    void OnEnable()
    {
        graph = GetComponent<GraphChart>();
    }

    void ResetZoomAnchor()
    {
        totalZoom = 0;
        InitalScrolling = new DoubleVector3(graph.HorizontalScrolling, graph.VerticalScrolling);
        InitalViewSize = new DoubleVector3(graph.DataSource.HorizontalViewSize, graph.DataSource.VerticalViewSize);
        InitalViewDirection = new DoubleVector3(Math.Sign(InitalViewSize.x), Math.Sign(InitalViewSize.y));
        InitialOrigin = new DoubleVector3(graph.DataSource.HorizontalViewOrigin, graph.DataSource.VerticalViewOrigin);

    }
    bool CompareWithError(Vector3 a,Vector3 b)
    {
        if (Mathf.Abs(a.x - b.x) > errorMargin)
            return false;
        if (Mathf.Abs(a.y - b.y) > errorMargin)
            return false;
        return true;
    }

	// Update is called once per frame
	void Update ()
    {
        if (graph == null) // no graph attached to this script for some reason
            return;
        Vector2 mousePos = Input.mousePosition;
        double mouseX, mouseY;
        graph.PointToClient(mousePos, out mouseX, out mouseY);
        if (CompareWithError(mousePos, mZoomBasePosition) == false) // the mouse has moved beyond the erroo
        {
            mZoomBasePosition = mousePos;      
            graph.PointToClient(mousePos, out mouseX, out mouseY);
            mZoomBaseChartSpace = new DoubleVector3(mouseX, mouseY);
            ResetZoomAnchor();
        }
        else
        {
            mousePos = mZoomBasePosition;
        }

        float delta = Input.mouseScrollDelta.y;
        totalZoom += delta;    //accumilate the delta change for the currnet positions

        if (delta!=0 && graph.PointToClient(mousePos, out mouseX, out mouseY))
        {
            DoubleVector3 ViewCenter = InitialOrigin + InitalScrolling;
            DoubleVector3 trans = new DoubleVector3((mZoomBaseChartSpace.x - ViewCenter.x), (mZoomBaseChartSpace.y - ViewCenter.y));
            float growFactor = Mathf.Pow(2,totalZoom/ZoomSpeed);
            double hSize = InitalViewSize.x * growFactor;
            double vSize = InitalViewSize.y * growFactor;
            if (hSize * InitalViewDirection.x < MaxViewSize && hSize * InitalViewDirection.x > MinViewSize  && vSize * InitalViewDirection.y  < MaxViewSize  && vSize * InitalViewDirection.y > MinViewSize )
            {
                graph.HorizontalScrolling = InitalScrolling.x + trans.x - (trans.x * growFactor);
                graph.VerticalScrolling = InitalScrolling.y + trans.y - (trans.y * growFactor);
                graph.DataSource.HorizontalViewSize = hSize;
                graph.DataSource.VerticalViewSize = vSize;
            }
        }

	}
}
