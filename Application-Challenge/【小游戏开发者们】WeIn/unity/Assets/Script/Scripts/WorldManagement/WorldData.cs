using System.Collections.Generic;
using UnityEditorInternal;
using UnityEngine;
using UnityEngine.PlayerLoop;
using UnityEngine.UI;
using Hi5_Interaction_Core;

public class WorldData
{
    public static Transform WorldTransform => GameObject.Find("WorldObject").GetComponent<Transform>();
    public string name;

    public float worldSize = 0.05f;

    public List<ObjectComponent> ObjectList;
    public WorldData(string name)
    {
        this.name = name;

        ObjectList = new List<ObjectComponent>();
    }

    public WorldData()
    {
    }

    public enum MergeType
    {
        Or,
        And,
        Not,
    }

    /// <summary>
    /// 根据合并种类参数，合并两个Object
    /// </summary>
    /// <param name="o1"></param>
    /// <param name="o2"></param>
    /// <param name="mergeType"></param>
    public void MergeTwoObjects(ObjectComponent o1, ObjectComponent o2, MergeType mergeType = MergeType.Or)
    {
        
        switch (mergeType)
        {
            case MergeType.Or:
                foreach (var pair in o2.voxelObjectData.VoxelDataDict)
                {
                    Vector3Int worldPos = pair.Key + o2.gridBasePoint;
                    Voxel v1 = GetVoxelAt(o1, worldPos);
                    Voxel v2 = pair.Value;//o2 must has
                    //If o1 dosen't have but o2 has
                    if (v1.voxel == null)
                    {
                        //Set o1 empty ones to o2's
                        SetVoxelAt(o1, worldPos, v2);
                    }
                }
                DeleteObject(o2);
                o1.UpdateObjectMesh();
                o1.gameObject.GetComponent<Outline>().enabled = false;
                o1.gameObject.GetComponent<Outline>().enabled = true;
                break;
            case MergeType.And:
            /*Dictionary<Vector3Int, Voxel> newDataDict = new Dictionary<Vector3Int, Voxel>();
            foreach (var pair in o1.voxelObjectData.VoxelDataDict)
            {
                Debug.Log("MergeType.And"); 

                Vector3Int worldPos = pair.Key + o1.gridBasePoint;
                Debug.Log("worldPos:" + worldPos);
                Voxel v2 = GetVoxelAt(o2, worldPos);
                //If o1 and 2o both have
                if (v2.voxel != null)
                {
                    Debug.Log("v2.voxel is not null");

                    //Set empty voxel
                    newDataDict.Add(pair.Key - o1.gridBasePoint, pair.Value);
                }
                //change to new data dict
                //o1.voxelObjectData.VoxelDataDict = newDataDict;
            }
            if(newDataDict.Count == 0)
            {
                Debug.Log("newDateDict is null");

                DeleteObject(o1);
                DeleteObject(o2);
            }
            else
            {
                Debug.Log("newDataDict.Count:"+newDataDict.Count);
                o1.voxelObjectData.VoxelDataDict = newDataDict;
                DeleteObject(o2);
                Debug.Log("o1.voxelObjectData.VoxelDataDict:"+ o1.voxelObjectData.VoxelDataDict.Count);
                o1.UpdateObjectMesh();
                Debug.Log("o1.UpdateObjectMesh end");

            }
            break;*/
            /*Dictionary<Vector3Int, Voxel> newDataDict = new Dictionary<Vector3Int, Voxel>();
            foreach (var pair in o1.voxelObjectData.VoxelDataDict)
            {

                Vector3Int worldPos = pair.Key + o1.gridBasePoint;
                Voxel v2 = GetVoxelAt(o2, worldPos);
                //If o1 and 2o both have
                if (v2.voxel != null)
                {
                    //Set empty voxel
                    newDataDict.Add(pair.Key - o1.gridBasePoint, pair.Value);
                }
                //change to new data dict
            }
            o1.voxelObjectData.VoxelDataDict = newDataDict;
            DeleteObject(o2);
            o1.UpdateObjectMesh();
            o1.gameObject.GetComponent<Outline>().enabled = false;
            o1.gameObject.GetComponent<Outline>().enabled = true;
            break;*/
            Dictionary<Vector3Int, Voxel> newDataDict = new Dictionary<Vector3Int, Voxel>();
            foreach (var pair in o1.voxelObjectData.VoxelDataDict)
            {

                Vector3Int worldPos = pair.Key + o1.gridBasePoint;
                Voxel v2 = GetVoxelAt(o2, worldPos);
                //If o1 and 2o both have
                if (v2.voxel == null)
                {
                    //Set empty voxel
                    SetVoxelAt(o1, worldPos, new Voxel());
                }
                //change to new data dict
            }
            DeleteObject(o2);
            o1.UpdateObjectMesh();
            o1.gameObject.GetComponent<Outline>().enabled = false;
            o1.gameObject.GetComponent<Outline>().enabled = true;
            break;
            case MergeType.Not:
                List<Vector3Int> samePosList = new List<Vector3Int>();
                foreach (var pair in o2.voxelObjectData.VoxelDataDict)
                {
                    Vector3Int worldPos = pair.Key + o2.gridBasePoint;
                    Voxel v1 = GetVoxelAt(o1, worldPos);
                    Voxel v2 = pair.Value;//o2 must has 
                    //If o1 and o2 both has
                    if (v1.voxel != null)
                    {
                        //Set empty voxel
                        SetVoxelAt(o1, worldPos, new Voxel());
                        samePosList.Add(worldPos);
                    }
                }
                foreach (var worldPos in samePosList)
                    SetVoxelAt(o2, worldPos, new Voxel());
                o1.UpdateObjectMesh();
                o1.gameObject.GetComponent<Outline>().enabled = false;
                o1.gameObject.GetComponent<Outline>().enabled = true;
                o2.UpdateObjectMesh();
                o2.gameObject.GetComponent<Outline>().enabled = false;
                o2.gameObject.GetComponent<Outline>().enabled = true;
                break;
            default:
                break;
        }
        
    }
    public Voxel GetVoxelAt(ObjectComponent obj, Vector3 worldPos)
    {
        Vector3Int p = MathHelper.WorldPosToWorldIntPos(worldPos);
        return obj.voxelObjectData.GetVoxelAt(p - obj.gridBasePoint);
    }
    public void SetVoxelAt(ObjectComponent obj, Vector3 worldPos, Voxel v)
    {
        Vector3Int p = MathHelper.WorldPosToWorldIntPos(worldPos);
        obj.voxelObjectData.SetVoxelAt(p - obj.gridBasePoint, v);
    }

    public void DeleteVoxelAt(ObjectComponent obj, Vector3 worldPos)
    {
        Vector3Int p = MathHelper.WorldPosToWorldIntPos(worldPos);
        obj.voxelObjectData.DeleteVoxelAt(p - obj.gridBasePoint);
    }

    public void UpdateAllObjects()
    {
        foreach (var o in ObjectList)
        {
            o.UpdateObjectMesh();
        }
    }

    public ObjectComponent CreateNewObject(Vector3Int basePoint,bool isStatic = false)
    {

        var c = GameObject.Instantiate(
            Resources.Load<GameObject>("Prefabs/VoxelObject"),
            basePoint,
            Quaternion.Euler(new Vector3(0, 0, 0)),
            WorldTransform).GetComponent<ObjectComponent>();

        //Setup object
        c.gridBasePoint = basePoint;
        c.voxelObjectData = new ObjectData();
        c.voxelObjectData.isStatic = isStatic;
        
        ObjectList.Add(c);

        return c;
    }
    public void CreateNewObjectFromGridData(List<Vector3Int> GridData, Voxel v, bool isStatic = false)
    {
        if (v.voxel == null)
        {
            v.voxel = VoxelInfoLibrary.GetVoxel("Stone");
            v.color = Color.white;
        }
        var c = GameObject.Instantiate(
           Resources.Load<GameObject>("Prefabs/VoxelObject"),
           GridData[0],
           Quaternion.Euler(new Vector3(0, 0, 0)),
           WorldTransform).GetComponent<ObjectComponent>();

        //Setup object
        c.gridBasePoint = GridData[0];
        c.voxelObjectData = new ObjectData();
        c.voxelObjectData.isStatic = isStatic;
        foreach (var worldPos in GridData)
        {
            SetVoxelAt(c, worldPos, v);
        }

        ObjectList.Add(c);
    }
    public void CreateNewObjectFromGridDataWithColor(List<Vector3Int> GridData, List<Color> colors, bool isStatic = false)
    {
        var c = GameObject.Instantiate(
           Resources.Load<GameObject>("Prefabs/VoxelObject"),
           GridData[0],
           Quaternion.Euler(new Vector3(0, 0, 0)),
           WorldTransform).GetComponent<ObjectComponent>();

        //Setup object
        c.gridBasePoint = GridData[0];
        c.voxelObjectData = new ObjectData();
        c.voxelObjectData.isStatic = isStatic;
        for (int i = 0;i < GridData.Count;i++)
        {
            Voxel v = new Voxel();
            v.voxel = VoxelInfoLibrary.GetVoxel("Stone");
            v.color = colors[i];
            SetVoxelAt(c, GridData[i], v);
        }

        ObjectList.Add(c);
    }

    public ObjectComponent CopyObject(ObjectComponent o)
    {
        var newObject = CreateNewObject(o.gridBasePoint);
        foreach (var pair in o.voxelObjectData.VoxelDataDict)
        {
            newObject.voxelObjectData.VoxelDataDict.Add(pair.Key, pair.Value);
        }

        newObject.UpdateObjectMesh();
        return newObject;
        
    }

    public ObjectComponent GetVoxelObject(int index)
    {
        return ObjectList[index];
    }

    public int GetVoxelObjectIndex(ObjectComponent o)
    {
        return ObjectList.IndexOf(o);
    }

    /// <summary>
    /// 返回wordlPos位置处有voxel的Object集合
    /// </summary>
    /// <param name="worldPos"></param>
    /// <returns>Object数组</returns>
    public ObjectComponent[] GetVoxelObjectsAt(Vector3 worldPos)
    {
        List<ObjectComponent> result = new List<ObjectComponent>();
        bool ex = false;
        foreach (var o in ObjectList)
        {
            Vector3Int intPos = MathHelper.WorldPosToWorldIntPos(worldPos / WorldDataManager.Instance.ActiveWorld.worldSize);
            //local position
            if (o.voxelObjectData.GetVoxelAt(intPos-o.gridBasePoint).voxel != null)
            {
                foreach(ObjectComponent ob in result)
                {
                    if (ob.gridBasePoint == o.gridBasePoint)
                    {
                        ex = true;
                        break;
                    }
                }
                if(ex == false)
                {
                    result.Add(o);
                }
                ex = false;
            }
        }
        return result.ToArray();
    }

    public ObjectComponent GetOneObjectAt(Vector3 worldPos)
    { 
        ObjectComponent result = new ObjectComponent();
        foreach (var o in ObjectList)
        {
            Vector3Int intPos = MathHelper.WorldPosToWorldIntPos(worldPos / WorldDataManager.Instance.ActiveWorld.worldSize);
            //local position
            Debug.Log("text_intPos:" + intPos);
            if (o.voxelObjectData.GetVoxelAt(intPos - o.gridBasePoint).voxel != null)
            {
                result = o;
                Debug.Log("text_get_one_object_at:" + result.gridBasePoint);
            }
        }
        return result;
    }
    //Get the next object in the list, back to start if ended
    public ObjectComponent GetNextObject(ObjectComponent o)
    {
        int index = ObjectList.IndexOf(o);
        var result = GetVoxelObject(index + 1 >= ObjectList.Count ? 0 : index + 1);
        return result;
    }
    public void DeleteObject(int index)
    {
        var obj = ObjectList[index];
        var selectedList = ToolManager.Instance.objectManipulator.objectSelector.selectedObjects;
        if (selectedList.Contains(obj))
        {
            selectedList.Remove(obj);
        }
        GameObject.Destroy(obj.gameObject);
        ObjectList.RemoveAt(index);
    }
    public void DeleteObject(ObjectComponent o)
    {
        var selectedList = ToolManager.Instance.objectManipulator.objectSelector.selectedObjects;
        if (selectedList.Contains(o))
        {
            selectedList.Remove(o);
        }
        GameObject.Destroy(o.gameObject);
        ObjectList.Remove(o);
    }

    public void WorldInit(List<SerializableObject> Objs, float WorldSize)
    {
        for (int i = 0; i < Objs.Count; i++)
        {
            var c = GameObject.Instantiate(
            Resources.Load<GameObject>("Prefabs/VoxelObject"),
            Vector3.zero,
            Quaternion.Euler(new Vector3(0, 0, 0)),
            WorldTransform).GetComponent<ObjectComponent>();
            c.voxelObjectData = new ObjectData();
            for (int j = 0; j < Objs[i].GridPositions.Count; j++)
            {
                c.voxelObjectData.VoxelDataDict.Add(Objs[i].GridPositions[j].Deserialize(), Objs[i].Voxels[j].Deserialize());

            }
            c.voxelObjectData.isStatic = Objs[i].isStatic;
            c.gridBasePoint = Objs[i].gridBasePos.Deserialize();
            ObjectList.Add(c);
        }
        worldSize = WorldSize;

    }
}