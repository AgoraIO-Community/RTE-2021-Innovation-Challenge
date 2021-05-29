using UnityEngine;
using System.Collections.Generic;

public class ObjectComponent : MonoBehaviour
{
    public Vector3Int gridBasePoint;
    //Local Coordinates data
    public ObjectData voxelObjectData;

    public MeshFilter meshFilter;
    public MeshRenderer meshRenderer;
    public MeshCollider meshCollider;

    private void Update()
    {
        transform.position = (gridBasePoint - Vector3.zero) * WorldDataManager.Instance.ActiveWorld.worldSize;
    }

    /// <summary>
    /// 判断某个位置点是否与该Object相邻
    /// </summary>
    /// <param name="worldPosition">待判断的位置点，已恢复真实尺度</param>
    /// <returns></returns>
    public bool IsNearVoxel(Vector3 worldPosition)
    {
        var localPos = MathHelper.WorldPosToWorldIntPos(worldPosition)- gridBasePoint;
        foreach (var p in voxelObjectData.VoxelDataDict.Keys)
        {
            if ((localPos - p).magnitude <= 1.01f)
                return true;
        }
        return false;
    }

    public void UpdateObjectMesh()
    {
        Debug.Log("UpdateObjectMesh start");
        Mesh mesh;
        Material[] mats;
        voxelObjectData.GenerateMeshAndMats(out mesh, out mats);
        meshFilter.mesh = mesh;
        meshCollider.sharedMesh = mesh;
        meshRenderer.materials = mats;
    }
    public SerializableObject Serialize()
    {
        var obj = new SerializableObject();
        obj.GridPositions = new List<SerializableVector3Int>();
        obj.Voxels = new List<SerializableVoxel>();
        foreach (var e in voxelObjectData.VoxelDataDict)
        {
            obj.GridPositions.Add(new SerializableVector3Int(e.Key));
            obj.Voxels.Add(new SerializableVoxel(e.Value));
        }
        obj.isStatic = voxelObjectData.isStatic;
        obj.gridBasePos = new SerializableVector3Int(gridBasePoint);
        return obj;
    }
}

