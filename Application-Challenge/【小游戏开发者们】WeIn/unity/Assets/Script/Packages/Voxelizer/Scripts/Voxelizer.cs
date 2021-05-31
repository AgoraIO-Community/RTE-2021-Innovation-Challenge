using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using VoxelSystem;

public class Voxelizer : MonoBehaviour
{
    public ComputeShader voxelizer;
    public List<Vector3Int> gridData;
    // Start is called before the first frame update
   public void Voxelize()
    {
        
        var mesh = GetComponent<MeshFilter>().mesh;
        GPUVoxelData data = GPUVoxelizer.Voxelize(
    voxelizer,  // ComputeShader (Voxelizer.compute)
    mesh,       // a target mesh
    transform.worldToLocalMatrix,         
    1.0f        
);

        gridData = data.VoxeltoGridVoxel(transform.localToWorldMatrix);
        WorldDataManager.Instance.ActiveWorld.CreateNewObjectFromGridData(gridData,new Voxel());

        // need to release a voxel buffer
        data.Dispose();
        Destroy(gameObject);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
