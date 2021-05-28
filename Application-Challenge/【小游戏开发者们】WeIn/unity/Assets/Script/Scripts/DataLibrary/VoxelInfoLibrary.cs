using System.Collections.Generic;
using UnityEngine;


public static class VoxelInfoLibrary
{
    private static Dictionary<string, VoxelInfo> NameLibrary;
    static VoxelInfoLibrary()
    {
        NameLibrary = new Dictionary<string, VoxelInfo>();
        VoxelInfo[] assets = Resources.LoadAll<VoxelInfo>("VoxelAssets/");
        foreach (var t in assets)
        {
            NameLibrary.Add(t.name, t);
        }

    }
    public static VoxelInfo GetVoxel(string name)
    {
        if (NameLibrary.ContainsKey(name))
            return NameLibrary[name];
        else
        {
            Debug.LogWarning(name + " Voxel was not found in the library!");
            return null;
        }

    }
}
