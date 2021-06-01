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

public class BarDataFiller : MonoBehaviour
{
    [Serializable]
    public enum DataType
    {
        /// <summary>
        /// each category is an array of values. each value matches a group in the bar chart
        /// </summary>
        ValueArray,
        /// <summary>
        /// each category is an object containing a named object for each group
        /// </summary>
        ObjectForEachElement,
    }

    public enum DocumentFormat
    {
        XML,
        JSON
    }

    [Serializable]
    public class CategoryData
    {
        public bool Enabled = true;

        [ChartFillerEditor(DataType.ValueArray)]
        [ChartFillerEditor(DataType.ObjectForEachElement)]
        public string Name;

        /// <summary>
        /// The way the data is stored in the object
        /// </summary>
        public DataType DataType;

        [ChartFillerEditorAttribute(DataType.ValueArray)]
        public string DataObjectName;
    }

    public BarChart BarObject;
    /// <summary>
    /// assign a graph chart prefab that will be used to copy category data
    /// </summary>
    public BarChart CategoryPrefab;

    public DocumentFormat Format;
    public string RemoteUrl;
    public bool FillOnStart;
    public CategoryData[] Categories = new CategoryData[0];

    private object[] mCategoryVisualStyle;
    delegate void CategoryLoader(CategoryData data);
    private Dictionary<DataType, CategoryLoader> mLoaders;
    private ChartParser mParser;

    static BarDataFiller()
    {
    }

    void EnsureCreateDataTypes()
    {
        if (mLoaders != null)
            return;
        mLoaders = new Dictionary<DataType, CategoryLoader>();
        mLoaders[DataType.ValueArray] = LoadValueArray;
        mLoaders[DataType.ObjectForEachElement] = LoadObjectForEachElement;
    }


    private double ParseItem(string item, string format)
    {
        if (String.IsNullOrEmpty(format) || format.Equals("none", StringComparison.OrdinalIgnoreCase))
        {
            return double.Parse(item);
        }
        return ChartDateUtility.DateToValue(DateTime.ParseExact(item, format, CultureInfo.InvariantCulture));
    }

    void LoadValueArray(CategoryData data)
    {
        BarChart bar = BarObject.GetComponent<BarChart>();
        var obj = mParser.GetObject(data.DataObjectName);
        int size = mParser.GetArraySize(obj);
        if (size < 0) // this is not an array , show warning
        {
            Debug.LogWarning("DataType " + data.DataType + " does not match category " + data.Name);
            return;
        }
        try
        {
            for (int i = 0; i < size; i++)
            {
                double val = ParseItem(mParser.GetItem(obj, i),null);
                string group = bar.DataSource.GetGroupName(i);
                bar.DataSource.SetValue(data.Name, group, val);
            }
        }
        catch (Exception e)
        {
            Debug.LogWarning("Data for category " + data.Name + " does not match the specified format. Ended with exception : " + e.ToString());
        }
    }

    void LoadObjectForEachElement(CategoryData data)
    {
        BarChart bar = BarObject.GetComponent<BarChart>();
        var obj = mParser.GetObject(data.DataObjectName);
        int size = bar.DataSource.TotalGroups;
        if (size < 0) // this is not an array , show warning
        {
            Debug.LogWarning("DataType " + data.DataType + " does not match category " + data.Name);
            return;
        }
        try
        {
            for (int i = 0; i < size; i++)
            {

                string group = bar.DataSource.GetGroupName(i);
                var groupObj = mParser.GetChildObject(obj, group);

                double val = ParseItem(mParser.ObjectValue(groupObj), null);
                
                bar.DataSource.SetValue(data.Name, group, val);
            }
        }
        catch (Exception e)
        {
            Debug.LogWarning("Data for category " + data.Name + " does not match the specified format. Ended with exception : " + e.ToString());
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

    void LoadCategoryVisualStyle(BarChart bar)
    {
        var prefab = CategoryPrefab;
        if (prefab == null)
        {
            if (bar is CanvasBarChart)
                prefab = ((GameObject)Resources.Load("Chart And Graph/DefualtBarCategoryStyle2D")).GetComponent<BarChart>();
            else
                prefab = ((GameObject)Resources.Load("Chart And Graph/DefualtBarCategoryStyle3D")).GetComponent<BarChart>(); // load default
        }
        if (prefab == null)
            Debug.LogError("missing resources for bar and chart, please reimport the package");
        else
            mCategoryVisualStyle = prefab.DataSource.StoreAllCategoriesinOrder();
    }

    public void ApplyData(string text)
    {
        BarChart bar = BarObject.GetComponent<BarChart>();

        if (Format == DocumentFormat.JSON)
            mParser = new JsonParser(text);
        else
            mParser = new XMLParser(text);

        LoadCategoryVisualStyle(bar);
        EnsureCreateDataTypes();
        if (mCategoryVisualStyle.Length == 0)
        {
            Debug.LogWarning("no visual styles defeind for BarDataFiller, aborting");
            return;
        }

        if (mCategoryVisualStyle.Length < Categories.Length)
            Debug.LogWarning("not enough visual styles in BarDataFiller");


        for (int i = 0; i < Categories.Length; i++)
        {
            var cat = Categories[i];
            if (cat.Enabled == false)
                continue;
            int visualIndex = Math.Min(i, mCategoryVisualStyle.Length - 1);
            object visualStyle = mCategoryVisualStyle[visualIndex];

            if (bar.DataSource.HasCategory(cat.Name))
                bar.DataSource.RemoveCategory(cat.Name);
            bar.DataSource.AddCategory(cat.Name, (Material)null);
            bar.DataSource.RestoreCategory(cat.Name, visualStyle);    // set the visual style of the category to the one in the prefab
            var loader = mLoaders[cat.DataType];    // find the loader based on the data type
            loader(cat); // load the category data
        }
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
                Debug.LogError("Bar Data Filler : Invalid document format, please check your settings , with exception " + e.ToString());
            }
        }
        else
        {
            Debug.LogError("Bar Data Filler : URL request failed ," + request.error);
        }
    }
#endif
    void Update()
    {

    }
}
