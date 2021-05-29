using System;
using System.Collections.Generic;
using UnityEngine;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;


public class WorldDataManager : Singleton<WorldDataManager>
{
    private List<WorldData> m_availableWorlds;
    public int objectCount;
    public WorldObject wb;

    public WorldData ActiveWorld { get; private set; }

    new private void Awake()
    {
        base.Awake();
        m_availableWorlds = new List<WorldData>();
        wb = GameObject.Find("WorldObject").GetComponent<WorldObject>();
    }



    public WorldData CreateNewWorld(string name)
    {
        WorldData world = new WorldData(name);
        Debug.Log("world_name:" + world.name);
        Debug.Log("newworldname:" + name);
        m_availableWorlds.Add(world);
        return world;
    }
    public void CreateNewWorld(WorldData world)
    {
        m_availableWorlds.Add(world);
    }
    public void ActivateWorld(string name)
    {
        if (ActiveWorld != null)
        {
            for (int i = objectCount-1; i >= 0; i--)
            {
                ActiveWorld.DeleteObject(i);
            }
            //ActiveWorld.UpdateAllObjects();
        }
        ActiveWorld = m_availableWorlds.Find(x => x.name == name);
        Debug.Log("activateworldname:" + name);
        Debug.Log("activeteworldsize:" + ActiveWorld.worldSize);
        if (wb.initiveScene == 0)
        {
            if (Application.dataPath != null)
            {
                var path = Application.dataPath + "/saveScene/" + name + ".save";
                if (System.IO.File.Exists(path) && wb.loadState != 1)
                {
                    BinaryFormatter bf = new BinaryFormatter();
                    var fs = File.Open(Application.dataPath + "/saveScene/" + name + ".save", FileMode.Open);
                    fs.Seek(0, SeekOrigin.Begin);
                    SaveData saveData = (SaveData)bf.Deserialize(fs);
                    fs.Close();
                    Debug.Log("Load " + name);
                    ActiveWorld.WorldInit(saveData.Objs, saveData.worldSize);
                    WorldDataManager.Instance.objectCount = WorldDataManager.Instance.ActiveWorld.ObjectList.Count;
                }
            }
        }
        ActiveWorld.UpdateAllObjects();
        wb.initiveScene = 0;
        wb.loadState = 0;
    }

    public void NextWorld()
    {
        // 只有一个world时无法切换
        if (m_availableWorlds.Count > 1)
        {
            if (ActiveWorld != null)
            {
                SaveData.SaveWorldData(ActiveWorld.name);
            }
            int counter = (Convert.ToInt32(ActiveWorld.name) + 1) % m_availableWorlds.Count;
            string name = counter.ToString();
            Debug.Log("nextworldname:" + name);

            objectCount = ActiveWorld.ObjectList.Count;
            ActivateWorld(name);
        }
        
    }

    public WorldData[] GetAvailableWorlds()
    {
        return m_availableWorlds.ToArray();
    }


}