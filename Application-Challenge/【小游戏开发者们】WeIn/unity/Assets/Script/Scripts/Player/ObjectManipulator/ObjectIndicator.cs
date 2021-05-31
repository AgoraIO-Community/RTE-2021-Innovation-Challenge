using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ObjectIndicator : MonoBehaviour
{
    public ObjectManipulator om;

    public List<Vector3Int> data;

    private MeshFilter m_meshFilter;

    public void Awake()
    {
        data = new List<Vector3Int>();
        m_meshFilter = GetComponent<MeshFilter>();
    }

    private void Update()
    {
        data.Clear();

        foreach (var v in om.creatingObjectBuffer)
        {
            //Do not need repeated points
            if (!data.Contains(v))
            {
                data.Add(v);
            }
        }

        m_meshFilter.sharedMesh = GenerateMesh();
    }

    private Mesh GenerateMesh()
    {

        List<Vector3> totalVertices = new List<Vector3>();
        List<int> totalIndices = new List<int>();


        foreach (var v in data)
        {
            for (int i = 0; i < 6; i++)
            {
                Vector3Int p = v + ObjectData.NORMALS[i];
                if (!data.Contains(p))
                {
                    foreach (var dp in ObjectData.QUAD_VERTS[i])
                    {
                        totalIndices.Add(totalIndices.Count);
                        totalVertices.Add((v + dp) * WorldDataManager.Instance.ActiveWorld.worldSize);
                    }
                }

            }
        }

        Mesh mesh = new Mesh();
        mesh.SetVertices(totalVertices);
        mesh.SetIndices(totalIndices, MeshTopology.Quads, 0);
        mesh.Optimize();
        mesh.RecalculateNormals();
        mesh.RecalculateTangents();

        return mesh;
    }
}
