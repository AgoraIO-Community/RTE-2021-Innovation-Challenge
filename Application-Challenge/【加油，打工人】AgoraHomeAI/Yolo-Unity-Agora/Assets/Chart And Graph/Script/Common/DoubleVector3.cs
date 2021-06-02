#define Graph_And_Chart_PRO
using System;
using System.Runtime.CompilerServices;
using UnityEngine;
using UnityEngine.Internal;
using UnityEngine.Scripting;

namespace ChartAndGraph
{
    public struct DoubleVector3
    { 
        public double x;

        public double y;

        public double z;

        public double this[int index]
        {
            get
            {
                double result;
                switch (index)
                {
                    case 0:
                        result = this.x;
                        break;
                    case 1:
                        result = this.y;
                        break;
                    case 2:
                        result = this.z;
                        break;
                    default:
                        throw new IndexOutOfRangeException("Invalid DoubleVector3 index!");
                }
                return result;
            }
            set
            {
                switch (index)
                {
                    case 0:
                        this.x = value;
                        break;
                    case 1:
                        this.y = value;
                        break;
                    case 2:
                        this.z = value;
                        break;
                    default:
                        throw new IndexOutOfRangeException("Invalid DoubleVector3 index!");
                }
            }
        }
        public Vector2 ToVector2()
        {
            return new Vector2((float)x, (float)y);
        }

        public Vector3 ToVector3()
        {
            return new Vector3((float)x, (float)y, (float)z);
        }
        public Vector3 ToVector4()
        {
            return new Vector4((float)x, (float)y, (float)z,0f);
        }
        public DoubleVector4 ToDoubleVector4()
        {
            return new DoubleVector4(x, y, z, 0.0);
        }
        public DoubleVector2 ToDoubleVector2()
        {
            return new DoubleVector2(x, y);
        }
        public DoubleVector3 normalized
        {
            get
            {
                return DoubleVector3.Normalize(this);
            }
        }

        public double magnitude
        {
            get
            {
                return Math.Sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
            }
        }

        public double sqrMagnitude
        {
            get
            {
                return this.x * this.x + this.y * this.y + this.z * this.z;
            }
        }

        public static DoubleVector3 zero
        {
            get
            {
                return new DoubleVector3(0f, 0f, 0f);
            }
        }

        public static DoubleVector3 one
        {
            get
            {
                return new DoubleVector3(1f, 1f, 1f);
            }
        }

        public static DoubleVector3 forward
        {
            get
            {
                return new DoubleVector3(0f, 0f, 1f);
            }
        }

        public static DoubleVector3 back
        {
            get
            {
                return new DoubleVector3(0f, 0f, -1f);
            }
        }

        public static DoubleVector3 up
        {
            get
            {
                return new DoubleVector3(0f, 1f, 0f);
            }
        }

        public static DoubleVector3 down
        {
            get
            {
                return new DoubleVector3(0f, -1f, 0f);
            }
        }

        public static DoubleVector3 left
        {
            get
            {
                return new DoubleVector3(-1f, 0f, 0f);
            }
        }

        public static DoubleVector3 right
        {
            get
            {
                return new DoubleVector3(1f, 0f, 0f);
            }
        }

        [Obsolete("Use DoubleVector3.forward instead.")]
        public static DoubleVector3 fwd
        {
            get
            {
                return new DoubleVector3(0f, 0f, 1f);
            }
        }

        public DoubleVector3(double x, double y, double z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public DoubleVector3(double x, double y)
        {
            this.x = x;
            this.y = y;
            this.z = 0f;
        }
        public DoubleVector3(Vector3 v)
            :this(v.x,v.y,v.z)
        {

        }

        public static DoubleVector3 Lerp(DoubleVector3 a, DoubleVector3 b, double t)
        {
            t = Math.Max(0.0, Math.Min(1.0, t));
            return new DoubleVector3(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t, a.z + (b.z - a.z) * t);
        }

        public static DoubleVector3 LerpUnclamped(DoubleVector3 a, DoubleVector3 b, double t)
        {
            return new DoubleVector3(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t, a.z + (b.z - a.z) * t);
        }

        public static DoubleVector3 MoveTowards(DoubleVector3 current, DoubleVector3 target, double maxDistanceDelta)
        {
            DoubleVector3 a = target - current;
            double magnitude = a.magnitude;
            DoubleVector3 result;
            if (magnitude <= maxDistanceDelta || magnitude == 0f)
            {
                result = target;
            }
            else
            {
                result = current + a / magnitude * maxDistanceDelta;
            }
            return result;
        }

        public void Set(double new_x, double new_y, double new_z)
        {
            this.x = new_x;
            this.y = new_y;
            this.z = new_z;
        }

        public static DoubleVector3 Scale(DoubleVector3 a, DoubleVector3 b)
        {
            return new DoubleVector3(a.x * b.x, a.y * b.y, a.z * b.z);
        }

        public void Scale(DoubleVector3 scale)
        {
            this.x *= scale.x;
            this.y *= scale.y;
            this.z *= scale.z;
        }

        public static DoubleVector3 Cross(DoubleVector3 lhs, DoubleVector3 rhs)
        {
            return new DoubleVector3(lhs.y * rhs.z - lhs.z * rhs.y, lhs.z * rhs.x - lhs.x * rhs.z, lhs.x * rhs.y - lhs.y * rhs.x);
        }

        public override int GetHashCode()
        {
            return this.x.GetHashCode() ^ this.y.GetHashCode() << 2 ^ this.z.GetHashCode() >> 2;
        }

        public override bool Equals(object other)
        {
            bool result;
            if (!(other is DoubleVector3))
            {
                result = false;
            }
            else
            {
                DoubleVector3 vector = (DoubleVector3)other;
                result = (this.x.Equals(vector.x) && this.y.Equals(vector.y) && this.z.Equals(vector.z));
            }
            return result;
        }

        public static DoubleVector3 Reflect(DoubleVector3 inDirection, DoubleVector3 inNormal)
        {
            return -2f * DoubleVector3.Dot(inNormal, inDirection) * inNormal + inDirection;
        }

        public static DoubleVector3 Normalize(DoubleVector3 value)
        {
            double num = DoubleVector3.Magnitude(value);
            DoubleVector3 result;
            if (num > 1E-05f)
            {
                result = value / num;
            }
            else
            {
                result = DoubleVector3.zero;
            }
            return result;
        }

        public void Normalize()
        {
            double num = DoubleVector3.Magnitude(this);
            if (num > 1E-05f)
            {
                this /= num;
            }
            else
            {
                this = DoubleVector3.zero;
            }
        }

        public static double Dot(DoubleVector3 lhs, DoubleVector3 rhs)
        {
            return lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z;
        }




        public static double Distance(DoubleVector3 a, DoubleVector3 b)
        {
            DoubleVector3 vector = new DoubleVector3(a.x - b.x, a.y - b.y, a.z - b.z);
            return Math.Sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
        }

        public static DoubleVector3 ClampMagnitude(DoubleVector3 vector, double maxLength)
        {
            DoubleVector3 result;
            if (vector.sqrMagnitude > maxLength * maxLength)
            {
                result = vector.normalized * maxLength;
            }
            else
            {
                result = vector;
            }
            return result;
        }

        public static double Magnitude(DoubleVector3 a)
        {
            return Math.Sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
        }

        public static double SqrMagnitude(DoubleVector3 a)
        {
            return a.x * a.x + a.y * a.y + a.z * a.z;
        }

        public static DoubleVector3 Min(DoubleVector3 lhs, DoubleVector3 rhs)
        {
            return new DoubleVector3(Math.Min(lhs.x, rhs.x), Math.Min(lhs.y, rhs.y), Math.Min(lhs.z, rhs.z));
        }

        public static DoubleVector3 Max(DoubleVector3 lhs, DoubleVector3 rhs)
        {
            return new DoubleVector3(Math.Max(lhs.x, rhs.x), Math.Max(lhs.y, rhs.y), Math.Max(lhs.z, rhs.z));
        }

        public static DoubleVector3 operator +(DoubleVector3 a, DoubleVector3 b)
        {
            return new DoubleVector3(a.x + b.x, a.y + b.y, a.z + b.z);
        }

        public static DoubleVector3 operator -(DoubleVector3 a, DoubleVector3 b)
        {
            return new DoubleVector3(a.x - b.x, a.y - b.y, a.z - b.z);
        }

        public static DoubleVector3 operator -(DoubleVector3 a)
        {
            return new DoubleVector3(-a.x, -a.y, -a.z);
        }

        public static DoubleVector3 operator *(DoubleVector3 a, double d)
        {
            return new DoubleVector3(a.x * d, a.y * d, a.z * d);
        }

        public static DoubleVector3 operator *(double d, DoubleVector3 a)
        {
            return new DoubleVector3(a.x * d, a.y * d, a.z * d);
        }

        public static DoubleVector3 operator /(DoubleVector3 a, double d)
        {
            return new DoubleVector3(a.x / d, a.y / d, a.z / d);
        }

        public static bool operator ==(DoubleVector3 lhs, DoubleVector3 rhs)
        {
            return DoubleVector3.SqrMagnitude(lhs - rhs) < 9.99999944E-11f;
        }

        public static bool operator !=(DoubleVector3 lhs, DoubleVector3 rhs)
        {
            return !(lhs == rhs);
        }

        public override string ToString()
        {
            return String.Format("({0}, {1}, {2})", new object[]
            {
                this.x,
                this.y,
                this.z
            });
        }

        public string ToString(string format)
        {
            return String.Format("({0}, {1}, {2})", new object[]
            {
                this.x.ToString(format),
                this.y.ToString(format),
                this.z.ToString(format)
            });
        }
    }
}