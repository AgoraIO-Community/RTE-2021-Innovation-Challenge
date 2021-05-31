using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using System.Collections;


[System.Serializable]
public class SaveData
{
	public float worldSize;
	public List<SerializableObject> Objs = new List<SerializableObject>();

	static public SaveData SaveWorldData(string SaveFileName)
	{
		var saveData = new SaveData();
		saveData.worldSize = WorldDataManager.Instance.ActiveWorld.worldSize;
		Debug.Log("SaveData_worldname:" + SaveFileName);
		Debug.Log("SaveData_objectcount:" + WorldDataManager.Instance.ActiveWorld.ObjectList.Count);
		foreach(var e in WorldDataManager.Instance.ActiveWorld.ObjectList)
		{
			saveData.Objs.Add(e.Serialize());
		}
		BinaryFormatter bf = new BinaryFormatter();
		
		var fs = File.Create(Application.dataPath + "/saveScene/"+ SaveFileName + ".save");
		bf.Serialize(fs, saveData);
		fs.Close();
		return saveData;
	}
	static public void LoadWorldData(string SaveFileName)
	{
		Debug.Log("LoadWorldData");
		BinaryFormatter bf = new BinaryFormatter();
		var path = Application.dataPath + "/saveScene/" + "load" + ".save";
		Debug.Log("path:"+path);
		if (System.IO.File.Exists(path))
		{
			Debug.Log("System.IO.Directory.Exists");

			var fs = File.Open(Application.dataPath + "/saveScene/" + "load" + ".save", FileMode.Open);
			fs.Seek(0, SeekOrigin.Begin);
			SaveData saveData = (SaveData)bf.Deserialize(fs);
			fs.Close();
			Debug.Log("Load " + SaveFileName);
			var world = WorldDataManager.Instance.CreateNewWorld(SaveFileName);
			world.WorldInit(saveData.Objs, saveData.worldSize);
            WorldDataManager.Instance.objectCount = WorldDataManager.Instance.ActiveWorld.ObjectList.Count;
			WorldDataManager.Instance.ActivateWorld(SaveFileName);
		}
	}
}
