using UnityEngine;
public struct Matrix3x3
{
    public float M11;
    public float M12;
    public float M13;
    public float M21;
    public float M22;
    public float M23;
    public float M31;
    public float M32;
    public float M33;

    public static Matrix3x3 CreateRotation(Vector3 axis, float radiant)
    {
        axis = axis.normalized;
        float sin = Mathf.Sin(radiant);
        float cos = Mathf.Cos(radiant);
        return new Matrix3x3()
        {
            M11 = cos + (1 - cos) * axis.x * axis.x,
            M12 = (1 - cos) * axis.x * axis.y - sin * axis.z,
            M13 = (1 - cos) * axis.x * axis.z + sin * axis.y,
            M21 = (1 - cos) * axis.y * axis.x + sin * axis.z,
            M22 = cos + (1 - cos) * axis.y * axis.y,
            M23 = (1 - cos) * axis.y * axis.z - sin * axis.x,
            M31 = (1 - cos) * axis.z * axis.x - sin * axis.y,
            M32 = (1 - cos) * axis.z * axis.y - sin * axis.x,
            M33 = cos + (1 - cos) * axis.z * axis.z,
        };
    }

    public static Matrix3x3 DotProduct(Matrix3x3 A, Matrix3x3 B)
    {
        return new Matrix3x3()
        {
            M11 = A.M11 * B.M11 + A.M12 * B.M21 + A.M13 * B.M31,
            M12 = A.M11 * B.M12 + A.M12 * B.M22 + A.M13 * B.M32,
            M13 = A.M11 * B.M13 + A.M12 * B.M23 + A.M13 * B.M33,
            M21 = A.M21 * B.M11 + A.M22 * B.M21 + A.M23 * B.M31,
            M22 = A.M21 * B.M12 + A.M22 * B.M22 + A.M23 * B.M32,
            M23 = A.M21 * B.M13 + A.M22 * B.M23 + A.M23 * B.M33,
            M31 = A.M31 * B.M11 + A.M32 * B.M21 + A.M33 * B.M31,
            M32 = A.M31 * B.M12 + A.M32 * B.M22 + A.M33 * B.M32,
            M33 = A.M31 * B.M13 + A.M32 * B.M23 + A.M33 * B.M33,

        };
    }

    public static Vector3 DotProduct(Matrix3x3 A, Vector3 b)
    {
        return new Vector3()
        {
            x = A.M11 * b.x + A.M12 * b.y + A.M13 * b.z,
            y = A.M21 * b.x + A.M22 * b.y + A.M23 * b.z,
            z = A.M31 * b.x + A.M32 * b.y + A.M33 * b.z
        };
    }
}