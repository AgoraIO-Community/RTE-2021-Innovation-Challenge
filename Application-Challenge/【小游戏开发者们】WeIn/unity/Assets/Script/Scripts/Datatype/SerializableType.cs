using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public struct SerializableVector3Int
{
    public int x, y, z;
    public SerializableVector3Int(int X, int Y, int Z) { x = X; y = Y; z = Z; }
    public SerializableVector3Int(Vector3Int v) { x = v.x; y = v.y; z = v.z; }

    public Vector3Int Deserialize() { return new Vector3Int(x, y, z); }
}

[System.Serializable]
public struct SerializableObject
{
    public List<SerializableVector3Int> GridPositions;
    public List<SerializableVoxel> Voxels;
    public bool isStatic;
    public SerializableVector3Int gridBasePos;

}

[System.Serializable]
public struct SerializableVoxel
{
    public string voxelName;
    public SerializableColor color;

    public SerializableVoxel(Voxel v)
    {
        voxelName = v.voxel.name;
        color = new SerializableColor(v.color);
    }
    public Voxel Deserialize()
    {
        var v = new Voxel();
        v.voxel = VoxelInfoLibrary.GetVoxel(voxelName);
        v.color = color.Deserialize();
        return v;
    }
}

[System.Serializable]
public struct SerializableColor
{
    public float r, g, b;
    public SerializableColor(Color c) { r = c.r; g = c.g; b = c.b; }
    public Color Deserialize() { return new Color(r, g, b); }

}
