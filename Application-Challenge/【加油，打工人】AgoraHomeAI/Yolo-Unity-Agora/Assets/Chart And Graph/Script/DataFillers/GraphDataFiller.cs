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

public class GraphDataFiller : MonoBehaviour
{
    [Serializable]
    public enum DataType
    {
        VectorArray,
        ArrayForEachElement,
        ObjectArray,
    }

    public enum DocumentFormat
    {
        XML,
        JSON
    }

    public enum VectorFormat
    {
        X_Y,
        Y_X,
        X_Y_SIZE,
        Y_X_SIZE,
        SIZE_X_Y,
        SIZE_Y_X,
        X_Y_GAP_SIZE,
        Y_X_GAP_SIZE
    }

    class VectorFormatData
    {
        public int X, Y, Size, Length;
        public VectorFormatData(int x, int y, int size, int length)
        {
            X = x;
            Y = y;
            Size = size;
            Length = length;
        }
    }

    [Serializable]
    public class CategoryData
    {
        public bool Enabled = true;

        [ChartFillerEditor(DataType.ObjectArray)]
        [ChartFillerEditor(DataType.ArrayForEachElement)]
        [ChartFillerEditorAttribute(DataType.VectorArray)]
        public string Name;

        /// <summary>
        /// The way the data is stored in the object
        /// </summary>
        public DataType DataType;

        [ChartFillerEditorAttribute(DataType.VectorArray)]
        public VectorFormat DataFormat;

        /// <summary>
        /// the amount of items to skip after each dataformat instance
        /// </summary>
        [ChartFillerEditorAttribute(DataType.VectorArray)]
        public int Skip = 0;

        /// <summary>
        /// if this is empty then DataObjectName is not relative
        /// </summary>
        [ChartFillerEditor(DataType.ObjectArray)]
        [ChartFillerEditor(DataType.ArrayForEachElement)]
        public string ParentObjectName;

        [ChartFillerEditorAttribute(DataType.VectorArray)]
        public string DataObjectName;


        [ChartFillerEditor(DataType.ObjectArray)]
        [ChartFillerEditor(DataType.ArrayForEachElement)]
        public string XDataObjectName;

        [ChartFillerEditor(DataType.ObjectArray)]
        [ChartFillerEditor(DataType.ArrayForEachElement)]
        public string YDataObjectName;

        [ChartFillerEditor(DataType.ObjectArray)]
        [ChartFillerEditor(DataType.ArrayForEachElement)]
        public string SizeDataObjectName;

        /// <summary>
        /// set to empty null or "none" for numbers. Set to a date format for a date :  https://docs.microsoft.com/en-us/dotnet/standard/base-types/custom-date-and-time-format-strings
        /// </summary>
        [ChartFillerEditor(DataType.ObjectArray)]
        [ChartFillerEditor(DataType.ArrayForEachElement)]
        public string XDateFormat = "";

        [ChartFillerEditor(DataType.ObjectArray)]
        [ChartFillerEditor(DataType.ArrayForEachElement)]
        public string YDateFormat = "";

    }

    public GraphChartBase GraphObject;
    /// <summary>
    /// assign a graph chart prefab that will be used to copy category data
    /// </summary>
    public GraphChartBase CategoryPrefab;

    public DocumentFormat Format;
    public string RemoteUrl;
    public bool FillOnStart;
    public CategoryData[] Categories = new CategoryData[0];

    private object[] mCategoryVisualStyle;
    delegate void CategoryLoader(CategoryData data);
    private Dictionary<DataType, CategoryLoader> mLoaders;
    private static Dictionary<VectorFormat, VectorFormatData> mVectorFormats;
    private ChartParser mParser;

    static GraphDataFiller()
    {
        CreateVectorFormats();
    }

    void EnsureCreateDataTypes()
    {
        if (mLoaders != null)
            return;
        mLoaders = new Dictionary<DataType, CategoryLoader>();
        mLoaders[DataType.ArrayForEachElement] = LoadArrayForEachElement;
        mLoaders[DataType.ObjectArray] = LoadObjectArray;
        mLoaders[DataType.VectorArray] = LoadVectorArray;
    }

    static void CreateVectorFormats()
    {
        mVectorFormats = new Dictionary<VectorFormat, VectorFormatData>();
        mVectorFormats[VectorFormat.X_Y] = new VectorFormatData(0, 1, -1, 2);
        mVectorFormats[VectorFormat.Y_X] = new VectorFormatData(1, 0, -1, 2);
        mVectorFormats[VectorFormat.X_Y_SIZE] = new VectorFormatData(0, 1, 2, 3);
        mVectorFormats[VectorFormat.Y_X_SIZE] = new VectorFormatData(1, 0, 2, 3);
        mVectorFormats[VectorFormat.SIZE_X_Y] = new VectorFormatData(1, 2, 0, 3);
        mVectorFormats[VectorFormat.SIZE_Y_X] = new VectorFormatData(2, 1, 0, 3);
        mVectorFormats[VectorFormat.X_Y_GAP_SIZE] = new VectorFormatData(0, 1, 3, 4);
        mVectorFormats[VectorFormat.Y_X_GAP_SIZE] = new VectorFormatData(1, 0, 3, 4);
    }

    private double ParseItem(string item, string format)
    {
        if (String.IsNullOrEmpty(format) || format.Equals("none", StringComparison.OrdinalIgnoreCase))
        {
            return double.Parse(item);
        }
        return ChartDateUtility.DateToValue(DateTime.ParseExact(item, format, CultureInfo.InvariantCulture));
    }

    void LoadArrayForEachElement(CategoryData data)
    {
        GraphChartBase graph = GraphObject.GetComponent<GraphChartBase>();
        if (mParser.SetPathRelativeTo(data.ParentObjectName) == false)
        {
            Debug.LogWarning("Object " + data.ParentObjectName + " does not exist in the document");
            return;
        }
        var xObj = mParser.GetObject(data.XDataObjectName);
        var yObj = mParser.GetObject(data.YDataObjectName);
        object sizeObj = null;
        if (String.IsNullOrEmpty(data.SizeDataObjectName) == false)
            sizeObj = mParser.GetObject(data.SizeDataObjectName);
        int length = Math.Min(mParser.GetArraySize(xObj), mParser.GetArraySize(yObj));
        if (sizeObj != null)
            length = Math.Min(length, mParser.GetArraySize(sizeObj));
        try
        {
            for (int i = 0; i < length; i++)
            {
                double x = ParseItem(mParser.GetItem(xObj, i), data.XDateFormat);
                double y = ParseItem(mParser.GetItem(yObj, i), data.YDateFormat);
                double pointSize = -1;
                if (sizeObj != null)
                    pointSize = double.Parse(mParser.GetItem(sizeObj, i));
                graph.DataSource.AddPointToCategory(data.Name, x, y, pointSize);
            }
        }
        catch (Exception e)
        {
            Debug.LogWarning("Data for category " + data.Name + " does not match the specified format. Ended with exception : " + e.ToString());
        }
    }

    void LoadObjectArray(CategoryData data)
    {
        GraphChartBase graph = GraphObject.GetComponent<GraphChartBase>();
        var parent = mParser.GetObject(data.ParentObjectName);
        if (parent == null)
        {
            Debug.LogWarning("Object " + data.ParentObjectName + " does not exist in the document");
            return;
        }

        int length = mParser.GetArraySize(parent);
        try
        {
            for (int i = 0; i < length; i++)
            {
                object item = mParser.GetItemObject(parent, i);
                double x = ParseItem(mParser.GetChildObjectValue(item, data.XDataObjectName), data.XDateFormat);
                double y = ParseItem(mParser.GetChildObjectValue(item, data.YDataObjectName), data.YDateFormat);
                double pointSize = -1;
                if (String.IsNullOrEmpty(data.SizeDataObjectName) == false)
                    pointSize = double.Parse(mParser.GetChildObjectValue(item, data.SizeDataObjectName));
                graph.DataSource.AddPointToCategory(data.Name, x, y, pointSize);
            }
        }
        catch (Exception e)
        {
            Debug.LogWarning("Data for category " + data.Name + " does not match the specified format. Ended with exception : " + e.ToString());
        }
    }

    void LoadVectorArray(CategoryData data)
    {
        GraphChartBase graph = GraphObject.GetComponent<GraphChartBase>();
        var obj = mParser.GetObject(data.DataObjectName);
        int size = mParser.GetArraySize(obj);
        VectorFormatData formatData = mVectorFormats[data.DataFormat];
        if (size < 0) // this is not an array , show warning
        {
            Debug.LogWarning("DataType " + data.DataType + " does not match category " + data.Name);
            return;
        }
        int itemLength = data.Skip + formatData.Length;
        try
        {
            for (int i = 0; i < size; i += itemLength)
            {
                double x = ParseItem(mParser.GetItem(obj, i + formatData.X), data.XDateFormat);
                double y = ParseItem(mParser.GetItem(obj, i + formatData.Y), data.YDateFormat);
                double pointSize = -1;
                if (formatData.Size >= 0)
                    pointSize = double.Parse(mParser.GetItem(obj, i + formatData.Size));
                graph.DataSource.AddPointToCategory(data.Name, x, y, pointSize);
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

    void LoadCategoryVisualStyle(GraphChartBase graph)
    {
        var prefab = CategoryPrefab;
        if (prefab == null)
        {
            if (graph is GraphChart)
                prefab = ((GameObject)Resources.Load("Chart And Graph/DefualtGraphCategoryStyle2D")).GetComponent<GraphChartBase>();
            else
                prefab = ((GameObject)Resources.Load("Chart And Graph/DefualtGraphCategoryStyle3D")).GetComponent<GraphChartBase>(); // load default
        }
        if (prefab == null)
            Debug.LogError("missing resources for graph and chart, please reimport the package");
        else
            mCategoryVisualStyle = prefab.DataSource.StoreAllCategoriesinOrder();
    }

    public void ApplyData(string text)
    {
        GraphChartBase graph = GraphObject.GetComponent<GraphChartBase>();

        if (Format == DocumentFormat.JSON)
            mParser = new JsonParser(text);
        else
            mParser = new XMLParser(text);

        LoadCategoryVisualStyle(graph);
        EnsureCreateDataTypes();
        if (mCategoryVisualStyle.Length == 0)
        {
            Debug.LogWarning("no visual styles defeind for GraphDataFiller, aborting");
            return;
        }

        if (mCategoryVisualStyle.Length < Categories.Length)
            Debug.LogWarning("not enough visual styles in GraphDataFiller");


        graph.DataSource.StartBatch();
        for (int i = 0; i < Categories.Length; i++)
        {
            var cat = Categories[i];
            if (cat.Enabled == false)
                continue;
            int visualIndex = Math.Min(i, mCategoryVisualStyle.Length - 1);
            object visualStyle = mCategoryVisualStyle[visualIndex];

            if (graph.DataSource.HasCategory(cat.Name))
                graph.DataSource.RemoveCategory(cat.Name);
            graph.DataSource.AddCategory(cat.Name, null, 0, new MaterialTiling(), null, false, null, 0);
            graph.DataSource.RestoreCategory(cat.Name, visualStyle);    // set the visual style of the category to the one in the prefab
            var loader = mLoaders[cat.DataType];    // find the loader based on the data type
            loader(cat); // load the category data
        }
        graph.DataSource.EndBatch();
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
                Debug.LogError("Graph Data Filler : URL request failed ," + webRequest.error);
            else
            {
                try
                {
                    string text = webRequest.downloadHandler.text;
                    ApplyData(text);
                }
                catch (Exception e)
                {
                    Debug.LogError("Graph Data Filler : Invalid document format, please check your settings , with exception " + e.ToString());
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
                Debug.LogError("Graph Data Filler : Invalid document format, please check your settings , with exception " + e.ToString());
            }
        }
        else
        {
            Debug.LogError("Graph Data Filler : URL request failed ," + request.error);
        }
    }
#endif
    void Update()
    {

    }
}
