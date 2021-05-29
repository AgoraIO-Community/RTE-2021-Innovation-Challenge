using System;
using UnityEngine;
using Hi5_Interaction_Core;
using System.IO;

public class WorldOptions : MonoBehaviour
{
    public ObjectManipulator om;
    public int worldCounter;
    public WorldObject wb;



    public void Start() 
    {
        this.worldCounter = Convert.ToInt32(WorldDataManager.Instance.ActiveWorld.name);
        wb = GameObject.Find("WorldObject").GetComponent<WorldObject>();

    }

    public void OnPressForCreate()
    {
        string name = this.worldCounter + "";
        SaveData.SaveWorldData(name);
        /*var ifnotexist = false;
        while(ifnotexist)
        {
            this.worldCounter++;
            name = this.worldCounter + "";
            if (!System.IO.File.Exists(Application.dataPath + "/saveScene/" + name + ".save"))
            {
                ifnotexist = true;
            }
        }*/
        this.worldCounter++;
        name = this.worldCounter + "";
        WorldData newWorld = WorldDataManager.Instance.CreateNewWorld(name);
        Debug.Log("OnPressForCreate! "+this.worldCounter);

        // 切换到新world中去

        WorldDataManager.Instance.NextWorld();
        this.gameObject.SetActive(false);
    }

    public void OnPressForSwitch()
    {
        WorldDataManager.Instance.NextWorld();
        Debug.Log("OnPressForSwitch!");
        this.gameObject.SetActive(false);
    }

    public void OnPressForSave()
    {
        string name = this.worldCounter + "";
        SaveData.SaveWorldData(name);
        Debug.Log("OnPressForSave!:"+name);
        this.gameObject.SetActive(false);
    }

    public void OnPressForLoad()
    {
        string name = this.worldCounter + "";
        SaveData.SaveWorldData(name);
        Debug.Log("saveworldname:" + name);
        if (WorldDataManager.Instance.ActiveWorld != null)
        {
            for (int i = WorldDataManager.Instance.ActiveWorld.ObjectList.Count - 1; i >= 0; i--)
            {
                WorldDataManager.Instance.ActiveWorld.DeleteObject(i);
            }
        }
        wb.loadState = 1;
        this.worldCounter++;
        name = this.worldCounter + "";
        SaveData.LoadWorldData(name);
        Debug.Log("OnPressForLoad!:"+name);
        this.gameObject.SetActive(false);
    }
}
