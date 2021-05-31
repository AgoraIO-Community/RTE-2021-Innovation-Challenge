#define Graph_And_Chart_PRO
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PointHighlight : MonoBehaviour
{
    public Text TextPrefab;
    public ChartItemEffect PointHoverPrefab;

    public int FontSize = 5;
    public Vector3 TextOffset = new Vector3();
    int fractionDigits = 0;
    GraphChart mChart;

    class HighLight
    {
        public HighLight(Text t,ChartItemEffect p)
        {
            mText = t;
            mPoint = p;
        }
        public Text mText;
        public ChartItemEffect mPoint;
        public CharItemEffectController mControl;
    }
    List<HighLight> mItems = new List<HighLight>();
    List<HighLight> mRemoved = new List<HighLight>();

    void Start()
    {
        

        var labels = GetComponent<ItemLabels>();
        if (labels != null)
            fractionDigits = labels.FractionDigits;
        else
            fractionDigits = 2;

        var graph = GetComponent<GraphChart>();
        if (graph != null)
            mChart = graph;
    }



    void Update()
    {
        mRemoved.RemoveAll(x =>
        {
            if (!x.mControl.enabled)
            {
                ChartCommon.SafeDestroy(x.mText.gameObject);
                ChartCommon.SafeDestroy(x.mPoint.gameObject);
                return true;
            }
            return false;
        });
    }

    IEnumerator SelectText(Text text,GameObject point)
    {
        yield return new WaitForEndOfFrame();
        if (text != null)
        {
            ChartItemEvents e = text.GetComponent<ChartItemEvents>();
            if (e != null)
            {
                e.OnMouseHover.Invoke(e.gameObject);
            }
            e = point.GetComponent<ChartItemEvents>();
            if (e != null)
            {
                e.OnMouseHover.Invoke(e.gameObject);
            }
        }
    }
    void ClearHighLight()
    {
        for (int i = 0; i < mItems.Count; i++)
        {
            RemoveText(mItems[i]);
        }
        mItems.Clear();
    }
    void RemoveText(HighLight h)
    {
        if (h.mText != null)
        {
            ChartItemEvents e = h.mText.GetComponent<ChartItemEvents>();
            h.mControl= h.mText.GetComponent<CharItemEffectController>();
            if (e != null && h.mControl != null)
            {
                e.OnMouseLeave.Invoke(e.gameObject);
                mRemoved.Add(h);
            }
            else
            {
                ChartCommon.SafeDestroy(h.mText);
            }
            e = h.mPoint.GetComponent<ChartItemEvents>();
            if (e != null)
            {
                e.OnMouseLeave.Invoke(e.gameObject);
            }
            else
            {
                ChartCommon.SafeDestroy(h.mPoint);
            }

        }
    }

    void PopText(string data, Vector3 position, bool worldPositionStays)
    {
        var canvas = GetComponentInParent<Canvas>();
        if (canvas == null || TextPrefab == null)
            return;
        ClearHighLight();
        GameObject highlight = (GameObject)GameObject.Instantiate(PointHoverPrefab.gameObject, position, Quaternion.identity);
        GameObject obj = (GameObject)GameObject.Instantiate(TextPrefab.gameObject, position + TextOffset, Quaternion.identity);
        
        var text = obj.GetComponent<Text>();
        text.maskable = false;
        text.text = data;
        text.fontSize = FontSize;
        obj.transform.SetParent(transform, false);
        highlight.transform.SetParent(transform, false);
        if (worldPositionStays)
        {
            obj.transform.position = position + TextOffset;
            highlight.transform.position = position;
        }
        else
        {
            obj.transform.localPosition = position + TextOffset;
            highlight.transform.localPosition = position;
        }
        Vector3 local = obj.transform.localPosition;
        local.z = 0f;
        obj.transform.localPosition = local;

        local = highlight.transform.localPosition;
        local.z = 0f;
        highlight.transform.localPosition = local;
        mItems.Add(new HighLight(text, highlight.GetComponent<ChartItemEffect>()));
        StartCoroutine(SelectText(text, highlight));
    }

    public void HighlightPoint(string category,int index)
    {
        var point = mChart.DataSource.GetPoint(category, index);
        string text = mChart.FormatItem(point.x, point.y);

        Vector3 position;
        if (mChart.PointToWorldSpace(out position, point.x, point.y, category))
            PopText(text, position, true);
    }
}
