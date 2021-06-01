#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;
using System.Collections.Generic;
using System;

public class BarRunChart : MonoBehaviour
{

    class RunChartEntry
    {
        public RunChartEntry(string name,double amount)
        {
            Name = name;
            Amount = amount;
        }
        public string Name;
        public double Amount;
    }

    public float switchTime = 0.1f;
    float switchTimeCounter =0f;

    List<RunChartEntry> mEntries = new List<RunChartEntry>();
    public Material SourceMaterial;
    // Use this for initialization
    void Start()
    {
        switchTimeCounter = switchTime;

        
        var bar = GetComponent<BarChart>();
        bar.TransitionTimeBetaFeature = switchTime;
        bar.DataSource.ClearCategories();
        bar.DataSource.ClearGroups();
        bar.DataSource.AddGroup("Default");

        // generate a random run chart
        for (int i = 0; i < 10; i++)
        {
            string categoryName = "Item " + i;
            mEntries.Add(new RunChartEntry(categoryName, UnityEngine.Random.value * 10));
            Material mat = new Material(SourceMaterial);
            mat.color = new Color(
      UnityEngine.Random.Range(0f, 1f),
      UnityEngine.Random.Range(0f, 1f),
      UnityEngine.Random.Range(0f, 1f)
  );
            bar.DataSource.AddCategory(categoryName, mat);
        }


    }

    void AddValuesToCategories()
    {

        for (int i = 0; i < mEntries.Count; i++)
        {
            mEntries[i].Amount += UnityEngine.Random.Range(-0.3f, 0.3f);
        }
    }
    // Update is called once per frame
    void Update()
    {
        // changes are timed 
        switchTimeCounter -= Time.deltaTime;
        if (switchTimeCounter < 0f)
        {
            switchTimeCounter = switchTime;
            var bar = GetComponent<BarChart>();
            //position the categories according to the currently displayed values
            for (int i = 0; i < mEntries.Count; i++)
            {
                bar.DataSource.MoveCategory(mEntries[i].Name, i);
            }
            // add the changes
            AddValuesToCategories();
            // sort the changes
            mEntries.Sort((x, y) => (int)Math.Sign(x.Amount - y.Amount));
            // animate the transition to the next values
            for (int i = 0; i < mEntries.Count; i++)
            {

                bar.DataSource.SlideValue(mEntries[i].Name, "Default", mEntries[i].Amount, switchTime);
            }

        }
    }
}
