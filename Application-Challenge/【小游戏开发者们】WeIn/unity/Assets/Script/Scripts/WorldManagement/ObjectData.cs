using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Rendering;
using UnityEngine.SocialPlatforms;

[Serializable]
public struct Voxel : IEquatable<Voxel>
{
    public VoxelInfo voxel;
    public Color color;

    public bool Equals(Voxel other)
    {
        return EqualityComparer<VoxelInfo>.Default.Equals(voxel, other.voxel) &&
               color.Equals(other.color);
    }

    public override int GetHashCode()
    {
        int hashCode = 1806356647;
        hashCode = hashCode * -1521134295 + EqualityComparer<VoxelInfo>.Default.GetHashCode(voxel);
        hashCode = hashCode * -1521134295 + color.GetHashCode();
        return hashCode;
    }
}

public class ObjectData
{
    public static readonly Vector3[][] QUAD_VERTS = new Vector3[][] { 
        //y-1
        new Vector3[] {
            new Vector3(0,0,1),
            new Vector3(0,0,0),
            new Vector3(1,0,0),
            new Vector3(1,0,1)
        },
        //y+1
        new Vector3[] {
            new Vector3(0,1,0),
            new Vector3(0,1,1),
            new Vector3(1,1,1),
            new Vector3(1,1,0)
        },

        //z-1
        new Vector3[] {
            new Vector3(1,1,0),
            new Vector3(1,0,0),
            new Vector3(0,0,0),
            new Vector3(0,1,0)
        },
        //z+1
        new Vector3[] {
            new Vector3(0,1,1),
            new Vector3(0,0,1),
            new Vector3(1,0,1),
            new Vector3(1,1,1)
        },
        //x-1
        new Vector3[] {
            new Vector3(0,1,0),
            new Vector3(0,0,0),
            new Vector3(0,0,1),
            new Vector3(0,1,1)
        },
        //x+1
        new Vector3[] {
            new Vector3(1,1,1),
            new Vector3(1,0,1),
            new Vector3(1,0,0),
            new Vector3(1,1,0)
        }
    };

    public static readonly Vector3Int[] NORMALS =
    {
        new Vector3Int(0,-1,0),//y-1
		new Vector3Int(0,1,0),//y+1
		new Vector3Int(0,0,-1),//z-1
		new Vector3Int(0,0,1),//z+1
		new Vector3Int(-1,0,0),//x-1
		new Vector3Int(1,0,0)//x+1
    };

    public Dictionary<Vector3Int, Voxel> VoxelDataDict; // 通过坐标索引Voxel
    public bool isStatic = false;

    public ObjectData()
    {
        VoxelDataDict = new Dictionary<Vector3Int, Voxel>();
    }

    public Voxel GetVoxelAt(Vector3Int localPosition)
    {
        if (VoxelDataDict.ContainsKey(localPosition))
        {
            return VoxelDataDict[localPosition];
        }
        else
        //Not Found
        return new Voxel();
    }

    public void SetVoxelAt(Vector3Int localPosition, Voxel v)
    {
        if (v.voxel == null)
        {
            DeleteVoxelAt(localPosition);
        }
        else
        {
            if (!VoxelDataDict.ContainsKey(localPosition))
            {
                VoxelDataDict.Add(localPosition, v);
            }
        }
        
    }
    public void DeleteVoxelAt(Vector3Int localPosition)
    {
        if (VoxelDataDict.ContainsKey(localPosition))
        {
            VoxelDataDict.Remove(localPosition);
        }
    }
    
    public void GenerateMeshAndMats(out Mesh mesh, out Material[] mats)
    {
        Dictionary<VoxelInfo, List<Vector3>> voxelVertListDict = new Dictionary<VoxelInfo, List<Vector3>>();
        List<Material> voxelMatsList = new List<Material>();

        foreach (var pair in VoxelDataDict)
        {
            if (pair.Value.voxel != null)
            {
                for (int i = 0; i < 6; i++)
                {
                    if (GetVoxelAt(pair.Key + NORMALS[i]).voxel == null)
                    {
                        if (!voxelVertListDict.ContainsKey(pair.Value.voxel))
                        {
                            voxelVertListDict.Add(pair.Value.voxel, new List<Vector3>());
                            voxelMatsList.Add(pair.Value.voxel.material);
                        }
                        var verts = voxelVertListDict[pair.Value.voxel];
                        foreach (var p in QUAD_VERTS[i])
                        {
                            verts.Add((p + pair.Key) * WorldDataManager.Instance.ActiveWorld.worldSize);
                        }
                    }

                }
            }
        }

        List<Vector3> totalVertices = new List<Vector3>();
        List<int> totalIndices = new List<int>();
        List<SubMeshDescriptor> subMeshDescList = new List<SubMeshDescriptor>();
        foreach (var verts in voxelVertListDict.Values)
        {
            //Add a descriptor for each vert list
            subMeshDescList.Add(new SubMeshDescriptor(totalIndices.Count, verts.Count, MeshTopology.Quads));
            //Append to total lists
            foreach (var vert in verts)
            {
                totalIndices.Add(totalIndices.Count);
                totalVertices.Add(vert);
            }
        }

        //Output voxelmats
        mats = voxelMatsList.ToArray();

        //Output voxelmesh
        mesh = new Mesh();
        mesh.SetVertices(totalVertices);//Set vertex buffer
        mesh.SetIndexBufferParams(totalIndices.Count, IndexFormat.UInt32);//Set index buffer format and data,since the max count is 65565*4*6 so must be int32
        mesh.SetIndexBufferData(totalIndices, 0, 0, totalIndices.Count);

        //Set submesh description
        int count = 0;
        foreach (var desc in subMeshDescList)
        {
            mesh.SetSubMesh(count, desc);
            count++;
        }
        //optimization
        mesh.Optimize();
        mesh.RecalculateNormals();
        mesh.RecalculateTangents();



    }

}