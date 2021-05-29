using System.Collections.Generic;
using UnityEngine;
public static class MathHelper
{
    /// <summary>
    /// 将一个浮点3D坐标转换为整数
    /// </summary>
    /// <param name="worldPos"></param>
    /// <returns></returns>
    static public Vector3Int WorldPosToWorldIntPos(Vector3 worldPos)
    {
        Vector3Int value = new Vector3Int(
            Mathf.FloorToInt(worldPos.x),
            Mathf.FloorToInt(worldPos.y),
            Mathf.FloorToInt(worldPos.z));
        return value;
    }

    static public Vector3 IntPosScaleByFloat(Vector3Int IntPos, float scale)
    {
        Vector3 value = new Vector3(
            IntPos.x * scale,
            IntPos.y * scale,
            IntPos.z * scale);
        return value;
    }

    static public Vector3Int WorldOriToMainAxis(Vector3 worldOri)
    {
        if ((Mathf.Abs(worldOri.x) > Mathf.Abs(worldOri.y)) && (Mathf.Abs(worldOri.x) > Mathf.Abs(worldOri.y)))
        {
            if (worldOri.x >= 0)
            {
                return new Vector3Int(1, 0, 0);
            }
            else
            {
                return new Vector3Int(-1, 0, 0);
            }
            
        }
        else if ((Mathf.Abs(worldOri.y) > Mathf.Abs(worldOri.x)) && (Mathf.Abs(worldOri.y) > Mathf.Abs(worldOri.z)))
        {
            if (worldOri.y >= 0)
            {
                return new Vector3Int(0, 1, 0);
            }
            else
            {
                return new Vector3Int(0, -1, 0);
            }
        }
        else
        {
            if (worldOri.z >= 0)
            {
                return new Vector3Int(0, 0, 1);
            }
            else
            {
                return new Vector3Int(0, 0, -1);
            }
        }
    }

    /// <summary>
    /// 根据对角线的两个端点，生成一个立方体整型网格
    /// </summary>
    /// <param name="min"></param>
    /// <param name="max"></param>
    /// <returns></returns>
    static public List<Vector3Int> GenerateGridFromDiagnal(Vector3Int min, Vector3Int max)
    {
        List<Vector3Int> result = new List<Vector3Int>();
        for (int x = min.x; x <= max.x; x++)
        {
            for (int y = min.y; y <= max.y; y++)
            {
                for (int z = min.z; z <= max.z; z++)
                {
                    result.Add(new Vector3Int(x, y, z));
                }
            }
        }
        return result;
    }

    /// <summary>
    /// 返回两个点构成的，缩放坐标下的最大、最小边界点
    /// </summary>
    /// <param name="p1">世界坐标下的1个点</param>
    /// <param name="p2">世界坐标下的另1个点</param>
    /// <param name="min">缩放后的最小点</param>
    /// <param name="max">缩放后的最大点</param>
    static public void GetMinMaxPoint(Vector3 p1, Vector3 p2, out Vector3Int min, out Vector3Int max)
    {
        Vector3 pp1 = p1 / WorldDataManager.Instance.ActiveWorld.worldSize;
        Vector3 pp2 = p2 / WorldDataManager.Instance.ActiveWorld.worldSize;
        min = MathHelper.WorldPosToWorldIntPos(
            new Vector3(
                Mathf.Min(pp1.x, pp2.x),
                Mathf.Min(pp1.y, pp2.y),
                Mathf.Min(pp1.z, pp2.z)
                )
            );
        max = MathHelper.WorldPosToWorldIntPos(
            new Vector3(
                Mathf.Max(pp1.x, pp2.x),
                Mathf.Max(pp1.y, pp2.y),
                Mathf.Max(pp1.z, pp2.z)
                )
            );
    }
}