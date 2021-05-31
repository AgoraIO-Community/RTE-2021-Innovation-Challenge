#define Graph_And_Chart_PRO
#if PLAYMAKER
using UnityEngine;
using UnityEditor;
using HutongGames.PlayMaker.Actions;
using HutongGames.PlayMakerEditor;

[CustomActionEditor(typeof(AppendGraphPointAction))]
public class CustomActionEditorTest : CustomActionEditor
{
    enum DateOrNumeric
    {
        Numeric,
        Date
    }
    public override void OnEnable()
    {
        // Do any expensive initialization stuff here.
        // This is called when the editor is created.
    }

    public override bool OnGUI()
    {
        var action = target as AppendGraphPointAction;

        EditField("ChartObject");
        EditField("CategoryName");
        EditField("AnimationTime");
        EditField("DateTimeKind");
        EditField("PointSize");

        EditorGUILayout.LabelField("X value:");
        DateOrNumeric type = DateOrNumeric.Numeric;
        if (action.XValueIsDate)
            type = DateOrNumeric.Date;
        type = (DateOrNumeric)EditorGUILayout.EnumPopup("type", type);
        action.XValueIsDate = type == DateOrNumeric.Date;
        if (action.XValueIsDate)
        {
            EditField("XDateYear");
            EditField("XDateMonth");
            EditField("XDateDay");
            EditField("XDateHour");
            EditField("XDateMinute");
            EditField("XDateSecond");
        }
        else
            EditField("XValueFloat");

        EditorGUILayout.LabelField("Y value:");
        type = DateOrNumeric.Numeric;
        if (action.YValueIsDate)
            type = DateOrNumeric.Date;
        type = (DateOrNumeric)EditorGUILayout.EnumPopup("type", type);
        action.YValueIsDate = type == DateOrNumeric.Date;
        if (action.YValueIsDate)
        {
            EditField("YDateYear");
            EditField("YDateMonth");
            EditField("YDateDay");
            EditField("YDateHour");
            EditField("YDateMinute");
            EditField("YDateSecond");
        }
        else
            EditField("YValueFloat");

        return GUI.changed;
    }
}
#endif