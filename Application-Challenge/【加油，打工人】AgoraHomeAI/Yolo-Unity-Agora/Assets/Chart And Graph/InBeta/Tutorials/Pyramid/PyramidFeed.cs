#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;

public class PyramidFeed : MonoBehaviour {

    public Material newCategory;
	// Use this for initialization
	void Start () {
        var chart = GetComponent<PyramidChart>();
        chart.DataSource.AddCategory("New category", new ChartDynamicMaterial( newCategory,Color.blue,Color.white), "New", "and Fresh", null);
        chart.DataSource.SetCategoryHeightRatio("New category", 3f);
    }
	
	// Update is called once per frame
	void Update () {
	
	}
}
