
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class HUD : MonoBehaviour
{
    public TextMeshProUGUI toolText;
    public TextMeshProUGUI objectText;
    // Update is called once per frame
    void Update()
    {
        toolText.text = ToolManager.Instance.Tmode.ToString();
        string t = "";
        foreach (var o in ToolManager.Instance.objectManipulator.objectSelector.selectedObjects)
        {
            t += WorldDataManager.Instance.ActiveWorld.GetVoxelObjectIndex(o);
            t += " ;";
        }
        objectText.text = t;
    }
}
