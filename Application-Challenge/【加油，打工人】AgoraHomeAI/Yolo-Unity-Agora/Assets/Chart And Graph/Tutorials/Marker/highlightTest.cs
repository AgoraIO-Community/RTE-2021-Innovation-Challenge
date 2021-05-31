#define Graph_And_Chart_PRO
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class highlightTest : MonoBehaviour
{
    public PointHighlight Highlight;
    public int count;
    float time = 1f;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        time -= Time.deltaTime;
        if(time <0f)
        {
            time = 2f;
            Highlight.HighlightPoint("Player 1", Random.Range(0, count));
        }
    }
}
