#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using System;
using ChartAndGraph;
using System.Collections.Generic;
using System.Globalization;
#if UNITY_2018_1_OR_NEWER
using UnityEngine.Networking;
#endif

public class PieDataFiller : MonoBehaviour
{
    [Serializable]
    public enum DataType
    {
        /// <summary>
        /// Parent object is an array where each value matches a category that is already in the pie chart
        /// </summary>
        ValueArray,
        /// <summary>
        /// each category is an object containing a named object for each group. the pie chart is cleared and all categories are added
        /// </summary>
        ObjectForEachElement,
    }

    public enum DocumentFormat
    {
        XML,
        JSON
    }

    public PieChart PieObject;
    /// <summary>
    /// assign a graph chart prefab that will be used to copy category data
    /// </summary>
    public PieChart CategoryPrefab;

    public DocumentFormat Format;
    public string RemoteUrl;
    public bool FillOnStart;

    public string ParentObject;
    public DataType DataStructure;

    private object[] mCategoryVisualStyle;

    private ChartParser mParser;

    static PieDataFiller()
    {
    }


    private double ParseItem(string item, string format)
    {
        if (String.IsNullOrEmpty(format) || format.Equals("none", StringComparison.OrdinalIgnoreCase))
        {
            return double.Parse(item);
        }
        return ChartDateUtility.DateToValue(DateTime.ParseExact(item, format, CultureInfo.InvariantCulture));
    }


    void LoadObjectforEachElement()
    {
        PieChart pie = PieObject.GetComponent<PieChart>();
        var obj = mParser.GetObject(ParentObject);

        pie.DataSource.Clear();
        int i = 0;
        foreach(var pair in mParser.GetAllChildObjects(obj))
        {
            int visualIndex = Math.Min(i, mCategoryVisualStyle.Length - 1);
            object visualStyle = mCategoryVisualStyle[visualIndex];
            pie.DataSource.AddCategory(pair.Key, null);
            pie.DataSource.RestoreCategory(pair.Key, visualStyle);
            double val = ParseItem(mParser.ObjectValue(pair.Value), null);
            pie.DataSource.SetValue(pair.Key, val);
            i++;
        }

    }

    void LoadValueArray()
    {
        PieChart pie = PieObject.GetComponent<PieChart>();
        var obj = mParser.GetObject(ParentObject);
        int size = mParser.GetArraySize(obj);
;        for (int i = 0; i < size; i++)
        {
 
            double val = ParseItem(mParser.GetItem(obj, i), null);
            string category = pie.DataSource.GetCategoryName(i);
            pie.DataSource.SetValue(category, val);
        }
    }

    void Start()
    {
        if (FillOnStart)
            Fill();
    }

    public void Fill()
    {
        Fill(null);
    }

    public void Fill(WWWForm postData)
    {
        StartCoroutine(GetData(postData));
    }

    void LoadCategoryVisualStyle(PieChart bar)
    {
        var prefab = CategoryPrefab;
        if (prefab == null)
        {
            if (bar is CanvasPieChart)
                prefab = ((GameObject)Resources.Load("Chart And Graph/DefualtPieCategoryStyle2D")).GetComponent<PieChart>();
            else
                prefab = ((GameObject)Resources.Load("Chart And Graph/DefualtPieCategoryStyle3D")).GetComponent<PieChart>(); // load default
        }
        if (prefab == null)
            Debug.LogError("missing resources for graph and chart, please reimport the package");
        else
            mCategoryVisualStyle = prefab.DataSource.StoreAllCategoriesinOrder();
    }

    public void ApplyData(string text)
    {

        PieChart pie = PieObject.GetComponent<PieChart>();

        if (Format == DocumentFormat.JSON)
            mParser = new JsonParser(text);
        else
            mParser = new XMLParser(text);

        LoadCategoryVisualStyle(pie);
        if (mCategoryVisualStyle.Length == 0)
        {
            Debug.LogWarning("no visual styles defeind for BarDataFiller, aborting");
            return;
        }

        if (DataStructure == DataType.ValueArray)
            LoadValueArray();
        else
            LoadObjectforEachElement();

    }

#if UNITY_2018_1_OR_NEWER
    UnityWebRequest CreateRequest(WWWForm postData)
    {
        if (postData == null)
            return UnityWebRequest.Get(RemoteUrl);
        return UnityWebRequest.Post(RemoteUrl, postData);
    }
    IEnumerator GetData(WWWForm postData)
    {
        using (UnityWebRequest webRequest = CreateRequest(postData))
        {
            yield return webRequest.SendWebRequest();
            if (webRequest.isNetworkError)
                Debug.LogError("Bar Data Filler : URL request failed ," + webRequest.error);
            else
            {
                try
                {
                    string text = webRequest.downloadHandler.text;
                    ApplyData(text);
                }
                catch (Exception e)
                {
                    Debug.LogError("Bar Data Filler : Invalid document format, please check your settings , with exception " + e.ToString());
                }
            }
        }
    }
#else
    IEnumerator GetData(WWWForm postData)
    {
        WWW request;
        if (postData != null)
        {
            request = new WWW(RemoteUrl, postData);
        }
        else
            request = new WWW(RemoteUrl);
        yield return request;
        if (String.IsNullOrEmpty(request.error))
        {
            try
            {
                string text = request.text;
                ApplyData(text);
            }
            catch (Exception e)
            {
                Debug.LogError("Pie Data Filler : Invalid document format, please check your settings , with exception " + e.ToString());
            }
        }
        else
        {
            Debug.LogError("Pie Data Filler : URL request failed ," + request.error);
        }
    }
#endif
    void Update()
    {

    }
}
