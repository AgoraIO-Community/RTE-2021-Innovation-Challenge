#define Graph_And_Chart_PRO
using ChartAndGraph;
using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public partial class HoverText : MonoBehaviour
{
    public Text TextPrefab;
    public int FontSize = 5;
    public Vector3 TextOffset = new Vector3();
    int fractionDigits = 0;
    AnyChart mChart;

    List<Text> mItems = new List<Text>();
    List<CharItemEffectController> mRemoved = new List<CharItemEffectController>();

    partial void HoverStart();
	void Start ()
    {
        var graph = GetComponent<GraphChart>();

        var labels = GetComponent<ItemLabels>();
        if (labels != null)
            fractionDigits = labels.FractionDigits;
        else
            fractionDigits = 2;

        if (graph != null)
        {
            mChart = graph;
            graph.PointHovered.AddListener(GraphHover);
            graph.NonHovered.AddListener(NonHover);
            return;
        }

        var bar = GetComponent<CanvasBarChart>();
        if (bar != null)
        {
            mChart = bar;
            bar.BarHovered.AddListener(BarHover);
            bar.NonHovered.AddListener(NonHover);
            return;
        }

        HoverStart();
    }

    void NonHover()
    {
        for (int i = 0; i < mItems.Count; i++)
        {
            RemoveText(mItems[i]);
        }
        mItems.Clear();
    }

    void Update()
    {
        mRemoved.RemoveAll(x =>
        {
            if(!x.enabled)
            {
                ChartCommon.SafeDestroy(x.gameObject);
                return true;
            }
            return false;
        });
    }

    IEnumerator SelectText(Text text)
    {
        yield return new WaitForEndOfFrame();
        if (text != null)
        {
            ChartItemEvents e = text.GetComponent<ChartItemEvents>();
            if (e != null)
            {
                e.OnMouseHover.Invoke(e.gameObject);
            }
        }
    }

    void RemoveText(Text text)
    {
        if (text != null)
        {
            ChartItemEvents e = text.GetComponent<ChartItemEvents>();
            CharItemEffectController control = text.GetComponent<CharItemEffectController>();
            if (e != null && control != null)
            {
                e.OnMouseLeave.Invoke(e.gameObject);
                mRemoved.Add(control);
            }
            else
            {
                ChartCommon.SafeDestroy(text);
            }
        }
    }

    void PopText(string data,Vector3 position,bool worldPositionStays)
    {
        var canvas = GetComponentInParent<Canvas>();
        if (canvas == null || TextPrefab == null)
            return;
        NonHover();
        GameObject obj =(GameObject) GameObject.Instantiate(TextPrefab.gameObject, position + TextOffset, Quaternion.identity);
        var text = obj.GetComponent<Text>();
        text.maskable = false;
        text.text = data;
        text.fontSize = FontSize;
        obj.transform.SetParent(transform, false);
        if (worldPositionStays)
            obj.transform.position = position + TextOffset;
        else
            obj.transform.localPosition = position + TextOffset;
        Vector3 local = obj.transform.localPosition;
        local.z = 0f;
        obj.transform.localPosition = local;
        mItems.Add(text);
        StartCoroutine(SelectText(text));
    }

    void BarHover(BarChart.BarEventArgs args)
    {
        String data = ChartAdancedSettings.Instance.FormatFractionDigits(fractionDigits, args.Value);
        PopText(data, args.TopPosition,true);
    }
    void GraphHover(GraphChartBase.GraphEventArgs args)
    {
        var graph = mChart as GraphChart;
        var point = graph.DataSource.GetPoint(args.Category, args.Index);
        Vector3 position;
        if(graph.PointToWorldSpace(out position, point.x, point.y, args.Category))
            PopText(args.XString + ":" + args.YString, position, true);
        else
            PopText(args.XString + ":" + args.YString, args.Position,false);
    }

}
