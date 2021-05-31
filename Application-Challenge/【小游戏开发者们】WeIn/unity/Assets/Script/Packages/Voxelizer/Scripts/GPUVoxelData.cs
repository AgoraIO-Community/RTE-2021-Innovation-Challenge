using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;

using UnityEngine;

namespace VoxelSystem {

	public class GPUVoxelData : System.IDisposable {

		public ComputeBuffer Buffer { get { return buffer; } }

		public int Width { get { return width; } }
		public int Height { get { return height; } }
		public int Depth { get { return depth; } }
		public Vector3 Unit { get { return unit; } }

		int width, height, depth;
		Vector3 unit;

		ComputeBuffer buffer;
        Voxel_t[] voxels;

		public GPUVoxelData(ComputeBuffer buf, int w, int h, int d, Vector3 u) {
			buffer = buf;
			width = w;
			height = h;
			depth = d;
			unit = u;
		}

		public Voxel_t[] GetData() {
            // cache
            if(voxels == null) {
			    voxels = new Voxel_t[Buffer.count];
			    Buffer.GetData(voxels);
            }
			return voxels;
		}
		public List<Vector3Int> VoxeltoGridVoxel(Matrix4x4 objToWorldMat)
		{
			if (voxels == null)
			{
				voxels = new Voxel_t[Buffer.count];
				Buffer.GetData(voxels);
			}
			var gridVoxels = new List<Vector3Int>();
			for (int i = 0; i < voxels.Length; i++)
			{
				if (!voxels[i].IsEmpty())
				{
					var temp = objToWorldMat.MultiplyPoint(voxels[i].position);
					var gridPos = new Vector3Int(Mathf.RoundToInt(temp.x), Mathf.RoundToInt(temp.y), Mathf.RoundToInt(temp.z));
					gridVoxels.Add(gridPos);
				}
			}
			return gridVoxels;
		}
		public void Dispose() {
			buffer.Release();
		}

	}

}

