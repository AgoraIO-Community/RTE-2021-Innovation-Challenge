using UnityEngine;

public static class VoxelGrid3DProcessor
{
    public static GridData3D<VoxelInfo> Zoom(GridData3D<VoxelInfo> source, Vector3 mutiplier)
    {
        int width = (int)(source.Width * mutiplier.x);
        int height = (int)(source.Height * mutiplier.y);
        int length = (int)(source.Length * mutiplier.z);

        GridData3D<VoxelInfo> result = new GridData3D<VoxelInfo>(width, height, length);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int z = 0; z < length; z++)
                {
                    VoxelInfo v = source.GetDataAt((int)(x / mutiplier.x), (int)(y / mutiplier.y), (int)(z / mutiplier.z));
                    result.SetDataAt(x, y, z, v);
                }
            }
        }

        return result;
    }

    public static GridData3D<VoxelInfo> Rotate(GridData3D<VoxelInfo> source, Vector3 axis, float radiant)
    {
        int width = 0;
        int height = 0;
        int length = 0;

        GridData3D<VoxelInfo> result = new GridData3D<VoxelInfo>(width, height, length);
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int z = 0; z < length; z++)
                {
                    Vector3 afterPos = new Vector3(x, y, z);
                    Vector3 beforePos = Matrix3x3.DotProduct(Matrix3x3.CreateRotation(axis, -radiant), afterPos);
                    VoxelInfo v = source.GetDataAt((int)beforePos.x, (int)beforePos.y, (int)beforePos.z);
                }
            }
        }
        return result;
    }
}
