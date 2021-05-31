#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;

public class ChangeOrder : MonoBehaviour {

    public float slideTime = 5f;
    public float switchTime = 7f;
    float timeCounter = 1f;
    float switchTimeCounter = 0.3f;
	// Use this for initialization
	void Start () {
        timeCounter = 0f;
        switchTimeCounter = switchTime;

    }
	
	// Update is called once per frame
	void Update ()
    {
        timeCounter -= Time.deltaTime;
        switchTimeCounter -= Time.deltaTime;
        if(switchTimeCounter < 0f)
        {
            switchTimeCounter = switchTime;
            var bar = GetComponent<BarChart>();
            bar.DataSource.SwitchCategoryPositions("Category 1", "Category 2");
        }
        if (timeCounter < 0f)
        {
            var bar = GetComponent<BarChart>();
            timeCounter = slideTime;
            for (int i = 1; i <= 3; i++)
            {
                string cat = "Category " + i;
                for (int j = 1; j <= 3; j++)
                {
                    string grp = "Group " + j;
                    bar.DataSource.SlideValue(cat, grp, Random.value * 10f, slideTime);
                }
            }
        }

	}
}
