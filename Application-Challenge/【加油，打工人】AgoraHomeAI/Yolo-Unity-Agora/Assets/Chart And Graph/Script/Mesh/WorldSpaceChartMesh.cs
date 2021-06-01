#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    /// <summary>
    /// chart mesh class for world space charts
    /// </summary>
    internal class WorldSpaceChartMesh : ChartMeshBase
    {
        List<Vector3> mVertices;
        List<Vector2> mUv;
        List<int>[] mTringles;
        bool mIsCanvas = false;


        public WorldSpaceChartMesh(bool isCanvas)
            :this(1)
        {
            ValidateMesh();
            mIsCanvas = isCanvas;
        }

        public WorldSpaceChartMesh(int groups)
        {
            ValidateMesh();
            mTringles = new List<int>[groups];
        }

        protected void ValidateMesh()
        {
            if (mVertices == null)
                mVertices = new List<Vector3>();
            if (mUv == null)
                mUv = new List<Vector2>();
        }
        public int AddVertex(UIVertex v)
        {
            return AddVertex(v.position, v.uv0);
        }
        public override BillboardText AddText(AnyChart chart, MonoBehaviour prefab, Transform parentTransform, int fontSize,float fontScale, string text, float x, float y, float z,float angle, object userData)
        {
            if(mIsCanvas == false)
                return base.AddText(chart, prefab, parentTransform, fontSize,fontScale, text, x, y, z,angle, userData);
            return null;
        }

        public int AddVertex(Vector3 pos,Vector2 uv)
        {
            if (mVertices.Count != mUv.Count)
                throw new InvalidOperationException();
            int index = mVertices.Count;
            mVertices.Add(pos);
            mUv.Add(uv);
            return index;            
        }

        public void AddTringle(int x, int y, int z)
        {
            List<int> tringleList = GetTringlesForGroup(0);
            AddTringle(tringleList, x, y, z);
        }

        protected void AddTringle(List<int> tringleList,int x,int y,int z)
        {
            tringleList.Add(x);
            tringleList.Add(y);
            tringleList.Add(z);
        }

        public override void Clear()
        {
            base.Clear();
            for (int i = 0; i < mTringles.Length; i++)
            {
                if (mTringles[i] != null)
                    mTringles[i].Clear();
            }
            mVertices.Clear();
            mUv.Clear();

        }


        protected List<int> GetTringlesForGroup(int subMeshGroup)
        {
            List<int> res;
            if (mTringles[subMeshGroup] == null)
            {
                res = new List<int>();
                mTringles[subMeshGroup] = res;
            }
            else
                res = mTringles[subMeshGroup];
            return res;
        }

        public override void AddYZRect(Rect rect, int subMeshGroup, float xPosition)
        {
            if (mIsCanvas == true)
                return;
            ValidateMesh();
            Vector2[] uvs = GetUvs(rect,(Orientation == ChartOrientation.Vertical) ? ChartOrientation.Horizontal : ChartOrientation.Vertical);            
            int leftTop = AddVertex(new Vector3(xPosition,rect.xMin,rect.yMin), uvs[1]);
            int rightTop = AddVertex(new Vector3(xPosition,rect.xMax,rect.yMin), uvs[3]);
            int leftBottom = AddVertex(new Vector3(xPosition,rect.xMin,rect.yMax), uvs[0]);
            int rightBottom = AddVertex(new Vector3(xPosition,rect.xMax,rect.yMax), uvs[2]);

            List<int> tringles = GetTringlesForGroup(subMeshGroup);
            AddTringle(tringles, leftTop, rightTop, rightBottom);
            AddTringle(tringles, rightBottom, leftBottom, leftTop);

            leftTop = AddVertex(new Vector3(xPosition, rect.xMin, rect.yMin), uvs[1]);
            rightTop = AddVertex(new Vector3(xPosition, rect.xMax, rect.yMin), uvs[3]);
            leftBottom = AddVertex(new Vector3(xPosition, rect.xMin, rect.yMax), uvs[0]);
            rightBottom = AddVertex(new Vector3(xPosition, rect.xMax, rect.yMax), uvs[2]);

            AddTringle(tringles, rightBottom, rightTop, leftTop);
            AddTringle(tringles, leftTop, leftBottom, rightBottom);

        }

        public override void AddXZRect(Rect rect,int subMeshGroup,float yPosition)
        {
            if (mIsCanvas == true)
                return;
            ValidateMesh();
            Vector2[] uvs = GetUvs(rect);
            int leftTop = AddVertex(new Vector3(rect.xMin, yPosition,rect.yMin), uvs[0]);
            int rightTop = AddVertex(new Vector3(rect.xMax,yPosition, rect.yMin), uvs[1]);
            int leftBottom = AddVertex(new Vector3(rect.xMin,yPosition, rect.yMax), uvs[2]);
            int rightBottom = AddVertex(new Vector3(rect.xMax,yPosition, rect.yMax), uvs[3]);
            List<int> tringles = GetTringlesForGroup(subMeshGroup);
            AddTringle(tringles, leftTop, rightTop, rightBottom);
            AddTringle(tringles, rightBottom, leftBottom, leftTop);

            leftTop = AddVertex(new Vector3(rect.xMin, yPosition, rect.yMin), uvs[0]);
            rightTop = AddVertex(new Vector3(rect.xMax, yPosition, rect.yMin), uvs[1]);
            leftBottom = AddVertex(new Vector3(rect.xMin, yPosition, rect.yMax), uvs[2]);
            rightBottom = AddVertex(new Vector3(rect.xMax, yPosition, rect.yMax), uvs[3]);

            AddTringle(tringles, rightBottom, rightTop, leftTop);
            AddTringle(tringles, leftTop, leftBottom, rightBottom);
        }

        public override void AddXYRect(Rect rect,int subMeshGroup,float depth)
        {
            ValidateMesh();
            Vector2[] uvs = GetUvs(rect);
            int leftTop = AddVertex(new Vector3(rect.xMin, rect.yMin, depth), uvs[0]);
            int rightTop = AddVertex(new Vector3(rect.xMax, rect.yMin, depth), uvs[1]);
            int leftBottom = AddVertex(new Vector3(rect.xMin, rect.yMax, depth), uvs[2]);
            int rightBottom = AddVertex(new Vector3(rect.xMax, rect.yMax, depth), uvs[3]);
            List<int> tringles = GetTringlesForGroup(subMeshGroup);
            AddTringle(tringles,leftTop, rightTop, rightBottom);
            AddTringle(tringles, rightBottom, leftBottom, leftTop);

            leftTop = AddVertex(new Vector3(rect.xMin, rect.yMin, depth), uvs[0]);
            rightTop = AddVertex(new Vector3(rect.xMax, rect.yMin, depth), uvs[1]);
            leftBottom = AddVertex(new Vector3(rect.xMin, rect.yMax, depth), uvs[2]);
            rightBottom = AddVertex(new Vector3(rect.xMax, rect.yMax, depth), uvs[3]);

            AddTringle(tringles, rightBottom,rightTop , leftTop);
            AddTringle(tringles, leftTop, leftBottom, rightBottom);
        }

        public override void AddQuad(UIVertex vLeftTop,UIVertex vRightTop, UIVertex vLeftBottom, UIVertex vRightBottom)
        {
            ValidateMesh();
            int leftTop = AddVertex(vLeftTop);
            int rightTop = AddVertex(vRightTop);
            int leftBottom = AddVertex(vLeftBottom);
            int rightBottom = AddVertex(vRightBottom);
            List<int> tringles = GetTringlesForGroup(0);
            AddTringle(tringles, leftTop, rightTop, rightBottom);
            AddTringle(tringles, rightBottom, leftBottom, leftTop);
        }

        Color[] GetColors()
        {
            Color[] res = new Color[mVertices.Count];
            for (int i = 0; i < res.Length; ++i)
                res[i] = Color.white;
            return res;
        }

        public void ApplyToMesh(Mesh m)
        {
            ValidateMesh();
            m.Clear();
            m.subMeshCount = mTringles.Length;
            m.vertices = mVertices.ToArray();
            m.colors = GetColors();
            m.uv = mUv.ToArray();

            for (int i = 0; i < mTringles.Length; i++)
            {
                if (mTringles[i] == null)
                    m.SetTriangles(new int[0], i); // set to empt
                else
                    m.SetTriangles(mTringles[i].ToArray(), i);
            }
            m.RecalculateNormals();
            m.RecalculateBounds();
        }

        public Mesh Generate(Mesh m)
        {
            ValidateMesh();
            if (m == null)
                m = new Mesh();
            m.subMeshCount = mTringles.Length;
            m.SetVertices(mVertices);
            m.SetUVs(0,mUv);
            m.colors = GetColors();
            for (int i = 0; i < mTringles.Length; i++)
            {
                if (mTringles[i] == null)
                    m.SetTriangles(new int[0], i); // set to empt
                else
                    m.SetTriangles(mTringles[i], i);
            }
            m.RecalculateNormals();
            m.RecalculateBounds();
            return m;
        }
        public Mesh Generate()
        {
            ValidateMesh();
            Mesh m = new Mesh();
            m.subMeshCount = mTringles.Length;
            m.vertices = mVertices.ToArray();
            m.uv = mUv.ToArray();
            m.colors = GetColors();
            for (int i=0; i<mTringles.Length; i++)
            {
                if(mTringles[i] == null)
                    m.SetTriangles(new int[0],i); // set to empt
                else
                    m.SetTriangles(mTringles[i].ToArray(),i);
            }
            m.RecalculateNormals();
            return m;
        }
    }
}
