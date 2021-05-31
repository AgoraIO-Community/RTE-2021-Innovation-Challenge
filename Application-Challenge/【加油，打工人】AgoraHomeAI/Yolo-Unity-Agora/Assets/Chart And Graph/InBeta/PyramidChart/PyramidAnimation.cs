#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;

public class PyramidAnimation : MonoBehaviour {


    public AnimationCurve Curve = AnimationCurve.Linear(0f, 0f, 1f, 1f);
    public bool AnimateOnStart = true;
    public float AnimationTime = 3f;
    float animationStart = -1f;
    PyramidChart chart;
    // Use this for initialization
    void Start () {
        chart = GetComponent<PyramidChart>();
        if (AnimateOnStart)
            Animate();
    }
    public void Animate()
    {
        animationStart = Time.time;
    }
    // Update is called once per frame
    void Update ()
    {
        if (chart == null)
            return;
        if (animationStart < 0f)
            return;
        float elasped = (Time.time - animationStart) / AnimationTime;
        elasped = Mathf.Clamp(elasped, 0f, 1f);
        float blend = Curve.Evaluate(elasped);
        for(int i=0; i<chart.DataSource.TotalCategories; i++)
        {
            string name = chart.DataSource.GetCategoryName(i);
            chart.DataSource.SetCategoryAlpha(name, blend);
            chart.DataSource.SetCategoryOrientation(name, blend, 0f, 0f);
        }

	}
}
