#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// This class contains functionallity for generating 3d and 2d bar meshes
    /// </summary
    class BarMesh
    {
        private static Mesh m2DStrechMesh;
       

        /// <summary>
        /// A lazy loaded 2d stretch mesh. This mesh is always the same
        /// </summary>
        public static Mesh StrechMesh2D
        {
            get
            {
                if (m2DStrechMesh == null)
                    m2DStrechMesh = Generate2DMesh(1.0f);
                return m2DStrechMesh;
            }
        }
        /// <summary>
        /// updates the uv of a mesh generated with Generate2DMesh
        /// </summary>
        /// <param name="m"></param>
        /// <param name="maxV"></param>
        public static void Update2DMeshUv(Mesh m,float maxV)
        {
            Vector2[] uv = m.uv;
            uv[4] = uv[0] = new Vector2(0f, 0f);
            uv[5] = uv[1] = new Vector2(1f, 0f);
            uv[6] = uv[2] = new Vector2(1f, maxV);
            uv[7] = uv[3] = new Vector2(0f, maxV);
            m.uv = uv;
        }
        /// <summary>
        /// generates a 2d bar mesh with the specified maximum V coord. (used with trim material fit)
        /// </summary>
        /// <param name="maxV"></param>
        /// <returns></returns>
        public static Mesh Generate2DMesh(float maxV)
        {
            Vector3[] vertices = new Vector3[8];
            Vector2[] uv = new Vector2[8];
            int[] tringles = new int[12];

            vertices[4] = vertices[0] = new Vector3(-0.5f, 0f, 0f);
            vertices[5] = vertices[1] = new Vector3(0.5f, 0f, 0f);
            vertices[6] = vertices[2] = new Vector3(0.5f, 1f, 0f);
            vertices[7] = vertices[3] = new Vector3(-0.5f, 1f, 0f);

            uv[4] = uv[0] = new Vector2(0f, 0f);
            uv[5] = uv[1] = new Vector2(1f, 0f);
            uv[6] = uv[2] = new Vector2(1f, maxV);
            uv[7] = uv[3] = new Vector2(0f, maxV);

            WriteRect(tringles, 0, 1, 2, 0, 3, false);
            WriteRect(tringles, 2, 5, 6, 4, 7, true);

            Mesh mesh = new Mesh();
            mesh.hideFlags = HideFlags.DontSave;
            mesh.vertices = vertices;
            mesh.uv = uv;
            mesh.triangles = tringles;
            mesh.RecalculateNormals();
            return mesh;
        }

        /// <summary>
        /// writes a single rectangle to the tringles array
        /// </summary>
        /// <param name="tringles"></param>
        /// <param name="tringleIndex">this value is multiplied by 3 in order to find the real array index</param>
        /// <param name="a"> index of the first vertex</param>
        /// <param name="b"> index of the second vertex</param>
        /// <param name="c"> index of the third vertex</param>
        /// <param name="d"> index of the forth vertex</param>
        static void WriteRect(int[] tringles,int tringleIndex,int a,int b,int c,int d,bool flip)
        {
            if (flip)
            {
                WriteTringle(tringles, tringleIndex++, a, b, c);
                WriteTringle(tringles, tringleIndex, d, c, b);
            }
            else
            {
                WriteTringle(tringles, tringleIndex++, c, b, a);
                WriteTringle(tringles, tringleIndex, b, c, d);
            }
        }

        /// <summary>
        /// writes a single tringle to the tringle array
        /// </summary>
        /// <param name="tringles"></param>
        /// <param name="tringleIndex">this value is multiplied by 3 in order to find the real array index</param>
        /// <param name="a">index of the first vertex</param>
        /// <param name="b">index of the second vertex</param>
        /// <param name="c">index of the third vertex</param>
        static void WriteTringle(int[] tringles,int tringleIndex,int a,int b,int c)
        {
            int index = tringleIndex*3;
            tringles[index++] = a;
            tringles[index++] = b;
            tringles[index++] = c;
        }
        private static Mesh m3DStretchMesh;

        /// <summary>
        /// A lazy loaded 3d stretch mesh. This mesh is always the same
        /// </summary>
        public static Mesh StrechMesh3D
        {
            get
            {
                if (m3DStretchMesh == null)
                    m3DStretchMesh = Generate3DMesh(1.0f);
                return m3DStretchMesh;
            }
        }



        /// <summary>
        /// updates the uv of a mesh created with Generate3DMesh
        /// </summary>
        /// <param name="m"></param>
        /// <param name="maxV"></param>
        public static void Update3DMeshUv(Mesh m, float maxV)
        {
            Vector2[] uv = m.uv;
            uv[0] = new Vector2(0f, 0f);
            uv[1] = new Vector2(1f, 0f);
            uv[2] = new Vector2(1f, maxV);
            uv[3] = new Vector2(0f, maxV);

            uv[4] = new Vector2(1f, 0f);
            uv[5] = new Vector2(0f, 0f);
            uv[6] = new Vector2(0f, maxV);
            uv[7] = new Vector2(1f, maxV);

            uv[8] = new Vector2(0f, 0f);
            uv[9] = new Vector2(1f, 0f);
            uv[10] = new Vector2(1f, 1f);
            uv[11] = new Vector2(0f, 1f);

            uv[12] = new Vector2(1f, 0f);
            uv[13] = new Vector2(0f, 0f);
            uv[14] = new Vector2(0f, 1f);
            uv[15] = new Vector2(1f, 1f); m.uv = uv;
        }

        /// <summary>
        /// generates a 3d bar mesh with the specified maximum V coord. (used with trim material fit)
        /// </summary>
        /// <param name="maxV"></param>
        /// <returns></returns>
        public static Mesh Generate3DMesh(float maxV)
        {
            Vector3[] vertices = new Vector3[16];
            Vector2[] uv = new Vector2[16];
            int[] tringles = new int[36];

            vertices[0] = new Vector3(-0.5f, 0f, -0.5f);
            vertices[1] = new Vector3(0.5f, 0f, -0.5f);
            vertices[2] = new Vector3(0.5f, 1f, -0.5f);
            vertices[3] = new Vector3(-0.5f, 1f, -0.5f);

            vertices[4] = new Vector3(-0.5f, 0f, 0.5f);
            vertices[5] = new Vector3(0.5f, 0f, 0.5f);
            vertices[6] = new Vector3(0.5f, 1f, 0.5f);
            vertices[7] = new Vector3(-0.5f, 1f, 0.5f);

            vertices[8] = new Vector3(-0.5f, 0f, -0.5f);
            vertices[9] = new Vector3(0.5f, 0f, -0.5f);
            vertices[10] = new Vector3(0.5f, 1f, -0.5f);
            vertices[11] = new Vector3(-0.5f, 1f, -0.5f);

            vertices[12] = new Vector3(-0.5f, 0f, 0.5f);
            vertices[13] = new Vector3(0.5f, 0f, 0.5f);
            vertices[14] = new Vector3(0.5f, 1f, 0.5f);
            vertices[15] = new Vector3(-0.5f, 1f, 0.5f);

            uv[0] = new Vector2(0f, 0f);
            uv[1] = new Vector2(1f, 0f);
            uv[2] = new Vector2(1f, maxV);
            uv[3] = new Vector2(0f, maxV);

            uv[4] = new Vector2(1f, 0f);
            uv[5] = new Vector2(0f, 0f);
            uv[6] = new Vector2(0f, maxV);
            uv[7] = new Vector2(1f, maxV);

            uv[8] = new Vector2(0f, 0f);
            uv[9] = new Vector2(1f, 0f);
            uv[10] = new Vector2(1f, 1f);
            uv[11] = new Vector2(0f, 1f);

            uv[12] = new Vector2(1f, 0f);
            uv[13] = new Vector2(0f, 0f);
            uv[14] = new Vector2(0f, 1f);
            uv[15] = new Vector2(1f, 1f);

            WriteRect(tringles, 0, 1, 2, 0, 3, false);
            WriteRect(tringles, 2, 5, 6, 4, 7, true);

            WriteRect(tringles, 4, 8, 9, 12, 13, true);
            WriteRect(tringles, 6, 10, 11, 14, 15, true);

            WriteRect(tringles, 8, 6, 5, 2, 1, true);
            WriteRect(tringles, 10, 7, 4, 3, 0, false);

            Mesh mesh = new Mesh();
            mesh.hideFlags = HideFlags.DontSave;
            mesh.vertices = vertices;
            mesh.uv = uv;
            mesh.triangles = tringles;
            mesh.RecalculateNormals();

            return mesh;
        }

    }
}
