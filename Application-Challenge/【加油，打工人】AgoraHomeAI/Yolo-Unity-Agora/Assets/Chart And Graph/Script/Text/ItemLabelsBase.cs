#define Graph_And_Chart_PRO
using ChartAndGraph;
using System;
using UnityEngine;
using UnityEngine.UI;

[Serializable]
public abstract class ItemLabelsBase : ChartSettingItemBase,ISerializationCallbackReceiver
{
    /// <summary>
    /// This prefab will be used to create all the text associated with the chart. If the prefab is null no labels will be shown
    /// </summary>
    [SerializeField]
    [Tooltip("This prefab will be used to create all the text associated with the chart. If the prefab is null no labels will be shown")]
    private MonoBehaviour textPrefab;

    /// <summary>
    /// This prefab will be used to create all the text associated with the chart. If the prefab is null no labels will be shown
    /// </summary>
    public MonoBehaviour TextPrefab
    {
        get { return textPrefab; }
        set { textPrefab = value; RaiseOnChanged(); }
    }


    public ItemLabelsBase()
    {
        AddChildObjects();
    }

    void AddChildObjects()
    {
        if (textFormat != null)
            AddInnerItem(textFormat);
    }

    /// <summary>
    /// determine the formatting of the label data. when the values are available , you can use the predefined macros : "\n" for newline , '<?category>' for the current category and '<?group>' for the current group
    /// </summary>
    [SerializeField]
    [Tooltip(@" determine the formatting of the label data. when the values are available , you can use the predefined macros : '\n' for newline , '<?category>' for the current category and '<?group>' for the current group")]
    private TextFormatting textFormat = new TextFormatting();

    /// <summary>
    /// determine the formatting of the label data. when the values are available , you can use the predefined macros : "\n" for newline , '<?category>' for the current category and '<?group>' for the current group
    /// </summary>
    public TextFormatting TextFormat
    {
        get { return textFormat; }
        set
        {
            textFormat = value;
            RaiseOnUpdate();
        }
    }

    /// <summary>
    /// The size of the text.
    /// </summary>
    [SerializeField]
    [Tooltip("the font size for the labels")]
    private int fontSize = 14;

    /// <summary>
    /// The size of the text.
    /// </summary>
    public int FontSize
    {
        get { return fontSize; }
        set { fontSize = value; RaiseOnUpdate(); }
    }

    /// <summary>
    /// adjusts the sharpness of the font
    /// </summary>
    [SerializeField]
    [Range(1f,3f)]
    [Tooltip("adjusts the sharpness of the font")]
    private float fontSharpness = 1f;

    /// <summary>
    /// adjusts the sharpness of the font
    /// </summary>
    public float FontSharpness
    {
        get { return fontSharpness; }
        set { fontSharpness = value; RaiseOnUpdate(); }
    }

    /// <summary>
    /// the seperation of each label from it's origin
    /// </summary>
    [SerializeField]
    [Tooltip("the seperation of each label from it's origin")]
    private float seperation = 1f;

    /// <summary>
    /// the seperation of each label from it's origin
    /// </summary>
    public float Seperation
    {
        get { return seperation; }
        set { seperation = value; RaiseOnUpdate(); }
    }
    /// <summary>
    /// validates all properties
    /// </summary>
    public virtual void ValidateProperties()
    {
        fontSize = Mathf.Max(fontSize, 0);
        fontSharpness = Mathf.Clamp(fontSharpness, 1f, 3f);

    }
    /// <summary>
    /// the location of the label relative to the item
    /// </summary>
    [SerializeField]
    [Tooltip("the location of the label relative to the item")]
    private ChartOrientedSize location = new ChartOrientedSize(0f,0f);

    /// <summary>
    /// the location of the label relative to the item
    /// </summary>
    public ChartOrientedSize Location
    {
        get { return location; }
        set { location = value; RaiseOnUpdate(); }
    }

    void ISerializationCallbackReceiver.OnBeforeSerialize()
    {
        
    }

    void ISerializationCallbackReceiver.OnAfterDeserialize()
    {
        AddChildObjects();
    }
}
