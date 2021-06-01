#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

namespace ChartAndGraph
{
    /// <summary>
    /// this class is used internally in order to draw lines, line fill and line points into a mesh
    /// </summary>
    public partial class CanvasLines : EventHandlingGraphic
    {
        /// <summary>
        /// thickness of the lines being drawn
        /// </summary>
        public float Thickness = 2f;

        float innerTile = 1f;
        int mMinModifyIndex = 0;
        /// <summary>
        /// Tiling value for the graphic. The tiliing is the total length of the lines being drawn. It is set by the parent graph
        /// </summary>
        public float Tiling
        {
            get { return innerTile; }
            set
            {
                innerTile = value;
            }
        }

        /// <summary>
        /// true if this graphic is in fill render mode (drawing the inner fill of a line graph)
        /// </summary>
        bool mFillRender = false;
        /// <summary>
        /// true if this graph is in point drawing render mode
        /// </summary>
        bool mPointRender = false;
        /// <summary>
        /// for point render mode , sets the point size
        /// </summary>
        float mPointSize = 5f;
        /// <summary>
        /// for fill render mode , this sets the boundries of the fill. (so the bottom of the fill matches the bottom of the graph)
        /// </summary>
        Rect mFillRect;
        /// <summary>
        /// if true , the Y of the fill is stretched
        /// </summary>
        bool mStretchY;
        /// <summary>
        /// The material for the graphic object
        /// </summary>
        Material mCachedMaterial;
        /// <summary>
        /// list of lines for the line object , this will be used to render either dots , fill or lines depending on the render mode
        /// </summary>
        List<LineSegement> mLines;
        /// <summary>
        /// bounding box for the value of mLines, used for event handling
        /// </summary>
        float mMinX, mMinY, mMaxX, mMaxY;

        /// <summary>
        /// Sets point render mode
        /// </summary>
        /// <param name="pointSize"></param>
        public void MakePointRender(float pointSize)
        {
            mPointSize = pointSize;
            mPointRender = true;
        }

        /// <summary>
        /// sets inner fill render mode
        /// </summary>
        /// <param name="fillRect"></param>
        /// <param name="stretchY"></param>
        public void MakeFillRender(Rect fillRect, bool stretchY)
        {
            mFillRect = fillRect;
            mFillRender = true;
            mStretchY = stretchY;
        }

        UIVertex[] mTmpVerts = new UIVertex[4];
        /// <summary>
        /// holds line data and pre cacultates normal and speration
        /// </summary>
        internal struct Line
        {
            public Line(Vector3 from, Vector3 to, float halfThickness, bool hasNext, bool hasPrev) : this()
            {

                Vector3 diff = (to - from);
                float magDec = 0;
                if (hasNext)
                    magDec += halfThickness;
                if (hasPrev)
                    magDec += halfThickness;
                Mag = diff.magnitude - magDec * 2;
                Degenerated = false;
                if (Mag <= 0)
                    Degenerated = true;
                Dir = diff.normalized;
                Vector3 add = halfThickness * 2 * Dir;
                if (hasPrev)
                    from += add;
                if (hasNext)
                    to -= add;
                From = from;
                To = to;
                Normal = new Vector3(Dir.y, -Dir.x, Dir.z); // this part calculates the line inset and points based on thichkness
                P1 = From + Normal * halfThickness; 
                P2 = from - Normal * halfThickness;
                P3 = to + Normal * halfThickness;
                P4 = to - Normal * halfThickness;
            }

            public bool Degenerated { get; private set; }
            public Vector3 P1 { get; private set; }
            public Vector3 P2 { get; private set; }
            public Vector3 P3 { get; private set; }
            public Vector3 P4 { get; private set; }

            public Vector3 From { get; private set; }
            public Vector3 To { get; private set; }
            public Vector3 Dir { get; private set; }
            public float Mag { get; private set; }
            public Vector3 Normal { get; private set; }
        }

        /// <summary>
        /// represents one line segemenet.
        /// </summary>
        internal class LineSegement
        {
            List<Vector4> mLines = new List<Vector4>();

            public LineSegement(IList<Vector3> lines)
            {
                mLines.AddRange(lines.Select(x=>new Vector4(x.x,x.y,x.z,-1f)));
            }
            
            public LineSegement(IList<Vector4> lines)
            {
                mLines.AddRange(lines);
            }

            /// <summary>
            /// reset the value of the linesSegment to the specified list
            /// </summary>
            /// <param name="v"></param>
            public void ModifiyLines(List<Vector4> v)
            {
                mLines.Clear();
                mLines.AddRange(v);
            }

            /// <summary>
            /// The total amount of points in this line segmenet
            /// </summary>
            public int PointCount
            {
                get
                {
                    if (mLines == null)
                        return 0;
                    return mLines.Count;
                }
            }

            /// <summary>
            /// Line count is point count minus one. Since all dots have to be connected
            /// </summary>
            public int LineCount
            {
                get
                {
                    if (mLines == null)
                        return 0;
                    if (mLines.Count < 2)
                        return 0;
                    return mLines.Count - 1;
                }
            }

            /// <summary>
            /// Gets a point
            /// </summary>
            /// <param name="index"></param>
            /// <returns></returns>
            public Vector4 getPoint(int index)
            {
                Vector4 p = mLines[index];
                return p;
            }

            /// <summary>
            /// gets the line at the specified index.index Must be below LineCount
            /// </summary>
            /// <param name="index"></param>
            /// <param name="from"></param>
            /// <param name="to"></param>
            public void GetLine(int index, out Vector3 from, out Vector3 to)
            {
                from = mLines[index];
                to = mLines[index + 1];
            }

            /// <summary>
            /// gets the line at the specified index.index Must be below LineCount. if the line has a previous or a next line an inset is calculated for better visual aperance
            /// </summary>
            /// <param name="index"></param>
            /// <param name="from"></param>
            /// <param name="to"></param>
            public Line GetLine(int index, float halfThickness, bool hasPrev, bool hasNext)
            {
                Vector3 from = mLines[index];
                Vector3 to = mLines[index + 1];
                return new Line(from, to, halfThickness, false, false);
            }
            public double GetLineMag(int index)
            {
                return (mLines[index] - mLines[index + 1]).magnitude;
            }

        }

        public CanvasLines()
        {

        }

        /// <summary>
        /// finds the minimum and maximum values of the currently set data
        /// </summary>
        void FindBoundingValues()
        {
            mMinX = float.PositiveInfinity;
            mMinY = float.PositiveInfinity;
            mMaxX = float.NegativeInfinity;
            mMaxY = float.NegativeInfinity;
            // this part finds the bounding box of the lines
            if (mLines != null)
            {
                for (int i = 0; i < mLines.Count; i++)
                {
                    LineSegement seg = mLines[i];
                    int totalPoints = seg.PointCount;
                    for (int j = 0; j < totalPoints; j++)
                    {
                        Vector3 point = seg.getPoint(j);
                        mMinX = Mathf.Min(mMinX, point.x);
                        mMinY = Mathf.Min(mMinY, point.y);
                        mMaxX = Mathf.Max(mMaxX, point.x);
                        mMaxY = Mathf.Max(mMaxY, point.y);
                    }
                }
            }
        }

        internal void ModifyLines(int minModifyIndex,List<Vector4> lines)
        {
            if (mLines.Count == 0)
            {
                mLines.Add(new LineSegement(lines.ToArray()));
                return;
            }

            mMinModifyIndex = minModifyIndex;
            FindBoundingValues();
            mLines[0].ModifiyLines(lines);
            SetVerticesDirty(); // clear previous animations
            Rebuild(CanvasUpdate.PostLayout);
            RefreshInputs();
        }
        public bool EnableOptimization { get; set; }

        /// <summary>
        /// sets the lines for this renderer
        /// </summary>
        /// <param name="lines"></param>
        internal void SetLines(List<LineSegement> lines)
        {
            mLines = lines;
            FindBoundingValues(); 
            mMinModifyIndex = 0;
            SetAllDirty();
            ClearEvents(); // clear previous animations
            if(EnableOptimization)
                Rebuild(CanvasUpdate.PostLayout);
            else
                Rebuild(CanvasUpdate.PreRender);
        }

        protected override void UpdateMaterial()
        {
            base.UpdateMaterial();
            canvasRenderer.SetTexture(material.mainTexture);
        }

        /// <summary>
        /// this method inflate one point in a line with the specified distantce. This enable the graphic to control the thickness of lines
        /// </summary>
        /// <param name="point"></param>
        /// <param name="dir"></param>
        /// <param name="normal"></param>
        /// <param name="dist"></param>
        /// <param name="size"></param>
        /// <param name="z"></param>
        /// <param name="p1"></param>
        /// <param name="p2"></param>
        void GetSide(Vector3 point, Vector3 dir,Vector3 normal,float dist,float size,float z,out Vector3 p1,out Vector3 p2)
        {
            point.z = z; 
            point += dir * dist;
            normal *= size;
            p1 = point + normal;
            p2 = point - normal;
        }

        protected override void OnDestroy()
        {
            base.OnDestroy();
            ChartCommon.SafeDestroy(mCachedMaterial);
        }

        protected override void OnDisable()
        {
            base.OnDisable();

        }

        /// <summary>
        /// overriding the default implementation to support materials with _ChartTiling property. This is used to tile texture along the graphic lines
        /// </summary>
        public override Material material
        {
            get
            {  
                return base.material;
            }
             
            set
            {
                ChartCommon.SafeDestroy(mCachedMaterial);
                if (value == null)
                { 
                    mCachedMaterial = null;
                    base.material = null;
                    return;
                }
                mCachedMaterial = new Material(value);
                mCachedMaterial.hideFlags = HideFlags.DontSave;
                if (mCachedMaterial.HasProperty("_ChartTiling"))
                    mCachedMaterial.SetFloat("_ChartTiling", Tiling);
                base.material = mCachedMaterial;
            }
        }

        protected override Vector2 Min
        {
            get
            {
                return new Vector2(mMinX, mMinY);
            }
        }

        protected override Vector2 Max
        {
            get
            {
                return new Vector2(mMaxX, mMaxY);
            }
        }

        protected override float MouseInThreshold
        {
            get
            {
                return Mathf.Max(Thickness, mPointSize) + Sensitivity;
            }
        }

        /// <summary>
        /// sets the transform of the hover object based on the data in this graphic
        /// </summary>
        /// <param name="hover"></param>
        /// <param name="index"></param>
        protected override void SetUpHoverObject(ChartItemEffect hover, int index, int type,object data)
        {
            if (hover == null)
                return;
            if (mLines == null || mLines.Count == 0)
                return;
            if (index < 0)
                return;

            if (mPointRender)
            {
                if (index >= mLines[0].PointCount)
                    return;
                Vector4 point = mLines[0].getPoint(index);
                RectTransform transform = hover.GetComponent<RectTransform>();
                transform.localScale = new Vector3(1f, 1f, 1f);
                float size = mPointSize;
                if (point.w >= 0f)
                    size = point.w;
                transform.sizeDelta = new Vector2(size, size);
                transform.anchoredPosition3D = new Vector3(point.x, point.y, 0f);
            }
            else
            {
                if (index >= mLines[0].LineCount)
                    return;
                Vector3 from;
                Vector3 to;
                mLines[0].GetLine(index, out from, out to);

                if (ViewRect.HasValue)
                {
                    Vector2 vFrom = from;
                    Vector2 vTo = to;
                    TrimLine(ViewRect.Value, ref vFrom, ref vTo);
                    from = new Vector3(vFrom.x, vFrom.y, from.z);
                    to = new Vector3(vTo.x, vTo.y, to.z);
                }

                Vector3 dir = (to - from);
                float angle = Mathf.Atan2(dir.y, dir.x) * Mathf.Rad2Deg;
                RectTransform transform = hover.GetComponent<RectTransform>();
                transform.sizeDelta = new Vector2(dir.magnitude, Thickness);
                transform.localScale = new Vector3(1f, 1f, 1f);
                transform.localRotation = Quaternion.Euler(0f, 0f, angle);
                Vector3 point = (from + to) * 0.5f;
                transform.anchoredPosition3D = new Vector3(point.x, point.y, 0f);
            }
        }

        protected override void Pick(Vector3 mouse, out int pickedIndex, out int pickedType, out object selectionData)
        {
            if (mPointRender)
                PickDot(mouse, out pickedType, out pickedIndex,out selectionData);
            else
                PickLine(mouse, out pickedType, out pickedIndex, out selectionData);
            if (pickedIndex >= 0)
                pickedIndex += refrenceIndex;
        }

        protected override void Update()
        {
            base.Update();
            Material mat = material;
            if (mCachedMaterial != null && mat != null && mCachedMaterial.HasProperty("_ChartTiling"))
            {
                if (mCachedMaterial != mat)
                    mCachedMaterial.CopyPropertiesFromMaterial(mat);
                mCachedMaterial.SetFloat("_ChartTiling", Tiling);
            }
        }
        partial void ProcesssPoint(ref Vector4 point, ref float halfSize);
        IEnumerable<UIVertex> getDotVeritces()
        {
            if (mLines == null)
                yield break;
            float z = 0f;
            float halfSize = mPointSize * 0.5f;
            for (int i = 0; i < mLines.Count; ++i)
            {
                LineSegement seg = mLines[i];
                int total = seg.PointCount;
                for (int j = mMinModifyIndex; j < total; ++j)
                {
                    Vector4 magPoint = seg.getPoint(j);
                    if (magPoint.w == 0f)
                        continue;

                    Vector3 point = (Vector3)magPoint;                    
                    halfSize = mPointSize * 0.5f;
                    ProcesssPoint(ref magPoint, ref halfSize);
                    Vector3 p1 = point + new Vector3(-halfSize, -halfSize, 0f);
                    Vector3 p2 = point + new Vector3(halfSize, -halfSize, 0f);
                    Vector3 p3 = point + new Vector3(-halfSize, halfSize, 0f);
                    Vector3 p4 = point + new Vector3(halfSize, halfSize, 0f);
                    Vector2 uv1 = new Vector2(0f, 0f);
                    Vector2 uv2 = new Vector2(1f, 0f);
                    Vector2 uv3 = new Vector2(0f, 1f);
                    Vector2 uv4 = new Vector2(1f, 1f);

                    UIVertex v1 = ChartCommon.CreateVertex(p1, uv1, z);
                    UIVertex v2 = ChartCommon.CreateVertex(p2, uv2, z);
                    UIVertex v3 = ChartCommon.CreateVertex(p3, uv3, z);
                    UIVertex v4 = ChartCommon.CreateVertex(p4, uv4, z);

                    yield return v1;
                    yield return v2;
                    yield return v3;
                    yield return v4;
                }
            }
        }

        Vector2 TransformUv(Vector2 uv)
        {
            return uv;
            //if (mUvRect.HasValue == false)
            //    return uv;
            //Rect r = mUvRect.Value;
            //float x = r.x + uv.x * r.width;
            //float y = r.y + uv.y * r.height;
            //return new Vector2(x, y);
        }

        IEnumerable<UIVertex> getFillVeritces()
        {
            if (mLines == null)
                yield break;
            float z = 0f;
            for (int i = 0; i < mLines.Count; ++i)
            {
                LineSegement seg = mLines[i];
                int totalLines = seg.LineCount;
                for (int j = mMinModifyIndex; j < totalLines; ++j)
                {
                    Vector3 from;
                    Vector3 to;
                    seg.GetLine(j,out from, out to);

                    Vector2 toTrim = to;
                    Vector2 fromTrim = from;
                    TrimItem(mFillRect.xMin, mFillRect.yMin, mFillRect.xMax, mFillRect.yMin, true, false, ref fromTrim, ref toTrim);
                    to = new Vector3(toTrim.x, toTrim.y, to.z);
                    from = new Vector3(fromTrim.x, fromTrim.y, from.z);
                    Vector3 fromBottom = from;
                    Vector3 toBottom = to;

                    fromBottom.y = mFillRect.yMin;
                    toBottom.y = mFillRect.yMin;

                    float fromV = 1f;
                    float toV = 1f;

                    if (mStretchY == false)
                    {
                        fromV = Mathf.Abs((from.y - mFillRect.yMin) / mFillRect.height);
                        toV = Mathf.Abs((to.y - mFillRect.yMin) / mFillRect.height);
                    }

                    float fromU = ((from.x - mFillRect.xMin) / mFillRect.width);
                    float toU = ((to.x - mFillRect.xMin) / mFillRect.width);
                    Vector2 uv1 = TransformUv(new Vector2(fromU, fromV));
                    Vector2 uv2 = TransformUv(new Vector2(toU, toV));
                    Vector2 uv3 = TransformUv(new Vector2(fromU, 0f));
                    Vector2 uv4 = TransformUv(new Vector2(toU, 0f));

                    UIVertex v1 = ChartCommon.CreateVertex(from, uv1, z);
                    UIVertex v2 = ChartCommon.CreateVertex(to, uv2, z);
                    UIVertex v3 = ChartCommon.CreateVertex(fromBottom, uv3, z);
                    UIVertex v4 = ChartCommon.CreateVertex(toBottom, uv4, z);

                    yield return v1;
                    yield return v2;
                    yield return v3;
                    yield return v4;
                }
            }
        }

        IEnumerable<UIVertex> getLineVertices()
        {
            if (mLines == null)
                yield break;
            float halfThickness = Thickness * 0.5f;
            float z = 0f;

            for (int i = 0; i < mLines.Count; ++i)
            {
                LineSegement seg = mLines[i];
                int totalLines = seg.LineCount;
                Line? peek = null;
                Line? prev = null;
                float tileUv = 0f;
                float totalUv = 0f;
                for (int j = mMinModifyIndex; j < totalLines; ++j)
                    totalUv += (float)seg.GetLineMag(j);
                for (int j = mMinModifyIndex; j < totalLines; ++j)
                {
                    Line line;
                    bool hasNext = j + 1 < totalLines;
                    if (peek.HasValue)
                        line = peek.Value;
                    else
                        line = seg.GetLine(j, halfThickness, prev.HasValue, hasNext);
                    peek = null;
                    if (j + 1 < totalLines)
                        peek = seg.GetLine(j + 1, halfThickness, true, j + 2 < totalLines);

                    Vector3 p1 = line.P1;
                    Vector3 p2 = line.P2;
                    Vector3 p3 = line.P3;
                    Vector3 p4 = line.P4;

                    Vector2 uv1 = new Vector2(tileUv * Tiling, 0f);
                    Vector2 uv2 = new Vector2(tileUv * Tiling, 1f);
                    tileUv += line.Mag / totalUv;

                    Vector2 uv3 = new Vector2(tileUv * Tiling, 0f);
                    Vector2 uv4 = new Vector2(tileUv * Tiling, 1f);

                    UIVertex v1 = ChartCommon.CreateVertex(p1, uv1, z);
                    UIVertex v2 = ChartCommon.CreateVertex(p2, uv2, z);
                    UIVertex v3 = ChartCommon.CreateVertex(p3, uv3, z);
                    UIVertex v4 = ChartCommon.CreateVertex(p4, uv4, z);

                    yield return v1;
                    yield return v2;
                    yield return v3;
                    yield return v4;

                    if (peek.HasValue)
                    {
                        float myZ = z + 0.2f;
                        Vector3 a1, a2;
                        GetSide(line.To, line.Dir, line.Normal, halfThickness * 0.5f, halfThickness * 0.6f, v3.position.z, out a1, out a2);
                        yield return v3;
                        yield return v4;
                        yield return ChartCommon.CreateVertex(a1, v3.uv0, myZ);
                        yield return ChartCommon.CreateVertex(a2, v4.uv0, myZ);
                    }
                    if (prev.HasValue)
                    {
                        float myZ = z + 0.2f;
                        Vector3 a1, a2;
                        GetSide(line.From, -line.Dir, line.Normal, halfThickness * 0.5f, halfThickness * 0.6f, v1.position.z, out a1, out a2);
                        yield return ChartCommon.CreateVertex(a1, v1.uv0, myZ);
                        yield return ChartCommon.CreateVertex(a2, v2.uv0, myZ);
                        yield return v1;
                        yield return v2;
                    }
                    //z -= 0.05f;
                    prev = line;
                }
            }
        }

        IEnumerable<UIVertex> getVerices()
        {
            IEnumerable<UIVertex> vertices; 
            if (mPointRender)
                vertices = getDotVeritces();
            else
            {
                if (mFillRender)
                    vertices = getFillVeritces();
                else
                    vertices = getLineVertices();
            }
            return vertices;
        }

        Mesh mVHMesh;
        List<Vector3> mPositions = new List<Vector3>();
        List<Vector2> mUvs = new List<Vector2>();
        List<int> mTringles = new List<int>();

        void WriteTo<T> (List<T> list,int index,T val)
        {
            if (list.Count == index)
                list.Add(val);
            else
                list[index] = val;
        }

        protected override void UpdateGeometry()
        {

            if (EnableOptimization == false)
            {
                mMinModifyIndex = 0;
                base.UpdateGeometry();
                return;
            }

            if (mVHMesh == null)
                mVHMesh = new Mesh();
            if (mMinModifyIndex == int.MaxValue)
                mMinModifyIndex = mPositions.Count;
            if (mMinModifyIndex <= 0)
            {
                mMinModifyIndex = 0;
                mPositions.Clear();
                mUvs.Clear();
                mTringles.Clear();
                foreach (UIVertex v in getVerices())
                {
                    mPositions.Add(v.position);
                    mUvs.Add(v.uv0);
                }
                int quads = (mPositions.Count / 4) ;
                int baseIndex = 0;
                for (int i = 0; i < quads; i++)
                {

                    mTringles.Add(baseIndex);
                    mTringles.Add(baseIndex + 1);
                    mTringles.Add(baseIndex + 3);

                    mTringles.Add(baseIndex + 3);
                    mTringles.Add(baseIndex + 2);
                    mTringles.Add(baseIndex);
                    baseIndex += 4;
                }
            }
            else
            {

               // Debug.Log("adding");
                int total = 0;
                int index = mMinModifyIndex;
                foreach (UIVertex v in getVerices())
                {
                 //   Debug.Log("extra");
                    WriteTo<Vector3>(mPositions, index, v.position);
                    WriteTo<Vector2>(mUvs, index, v.uv0);
                    total++;
                }
                int quads = total / 4;
                for (int i = 0; i < quads; i++)
                {
                //    Debug.Log("extra tring");
                    mTringles.Add(0);
                    mTringles.Add(1);
                    mTringles.Add(3);

                    mTringles.Add(3);
                    mTringles.Add(2);
                    mTringles.Add(0);
                }
            }
            mVHMesh.Clear();
            mVHMesh.SetVertices(mPositions);
            mVHMesh.SetUVs(0, mUvs);
            mVHMesh.SetTriangles(mTringles, 0);
            //at the end 
            mMinModifyIndex = int.MaxValue;
            GetComponent<CanvasRenderer>().SetMesh(mVHMesh);
        }

#if (!UNITY_5_2_0) && (!UNITY_5_2_1)
    
        protected override void OnPopulateMesh(VertexHelper vh)
        {
            base.OnPopulateMesh(vh);
            vh.Clear();
            int vPos = 0;
            foreach (UIVertex v in getVerices())
            {
                mTmpVerts[vPos++] = v;
                if (vPos == 4)
                {
                    UIVertex tmp = mTmpVerts[2];
                    mTmpVerts[2] = mTmpVerts[3];
                    mTmpVerts[3] = tmp;
                    vPos = 0;
                    vh.AddUIVertexQuad(mTmpVerts);
                }
            }
        }
#endif
#pragma warning disable 0672
#if !UNITY_2017_1_OR_NEWER
        /// <summary>
        /// A chart mesh used for populating the 
        /// </summary>
        WorldSpaceChartMesh mMesh = null;
        protected override void OnPopulateMesh(Mesh m)
        {
            if (mMesh == null)
                mMesh = new WorldSpaceChartMesh(1);
            else
                mMesh.Clear();
            int vPos = 0;
            mMinModifyIndex = 0; // not supported here
            foreach (UIVertex v in getVerices())
            {
                mTmpVerts[vPos++] = v;
                if (vPos == 4)
                {
                    vPos = 0;
                    mMesh.AddQuad(mTmpVerts[0], mTmpVerts[1], mTmpVerts[2], mTmpVerts[3]);
                }
            }

            mMesh.ApplyToMesh(m);
        }
#endif
        void PickLine(Vector3 mouse, out int segment, out int line, out object selectionData)
        {
            float minDist = Mathf.Infinity;
            segment = -1;
            line = -1;
            selectionData = null;
            if (mLines == null)
            {
                return;
            }
            for (int i = 0; i < mLines.Count; ++i)
            {
                LineSegement seg = mLines[i];
                int total = seg.LineCount;
                for (int j = 0; j < total; ++j)
                {
                    Vector3 from;
                    Vector3 to;
                    seg.GetLine(j, out from, out to);
                    float dist = ChartCommon.SegmentPointSqrDistance(from,to,mouse);
                    if (dist < minDist)
                    {
                        minDist = dist;
                        segment = i;
                        line = j;
                    }
                }
            }
            float thresh = (Thickness + Sensitivity);

            if ((ViewRect.HasValue && !ViewRect.Value.Contains(mouse)) ||  minDist > thresh * thresh)
            {
                segment = -1;
                line = -1;
            }
        }

        void PickDot(Vector3 mouse, out int segment , out int point, out object selectionData)
        {
            float minDist = Mathf.Infinity;
            segment = -1;
            point = -1;
            selectionData = null;
            if (mLines == null)
                return;
            float mag = mPointSize;
            for (int i = 0; i < mLines.Count; ++i)
            {
                LineSegement seg = mLines[i];
                int total = seg.PointCount;
                for (int j = 0; j < total; ++j)
                {
                    Vector4 p = seg.getPoint(j);
                    if (p.w == 0f)
                        continue;
                    float dist = (mouse - ((Vector3)p)).sqrMagnitude;
                    if(dist < minDist)
                    {
                        mag = p.w;
                        if (mag < 0f)
                            mag = mPointSize;
                        minDist = dist;
                        segment = i;
                        point = j;
                    }
                    
                }
            }
            float thresh = mag + Sensitivity;
            if ((ViewRect.HasValue && !ViewRect.Value.Contains(mouse)) || minDist > thresh * thresh)
            {
                segment = -1;
                point = -1;
            }
        }

        void TrimItem(float x1, float y1 ,float x2,float y2,bool xAxis,bool oposite, ref Vector2 from, ref Vector2 to)
        {
            Vector2 seg1 = new Vector2(x1, y1);
            Vector2 seg2 = new Vector2(x2, y2);
            Vector2 point;
            if (ChartCommon.SegmentIntersection(seg1, seg2, from, to, out point) == false)
                return;
            if(xAxis)
            {
                if ((to.y > from.y) ^ oposite)
                    from = point;
                else
                    to = point;
                return;
            }
            if ((to.x > from.x) ^ oposite)
                from = point;
            else
                to = point;
        }

        void TrimLine(Rect r,ref Vector2 from, ref Vector2 to)
        {
            TrimItem(r.xMin, r.yMin, r.xMax, r.yMin, true, false, ref from, ref to);
            TrimItem(r.xMin, r.yMax, r.xMax, r.yMax, true, true, ref from, ref to);
            TrimItem(r.xMin, r.yMin, r.xMin, r.yMax, false, false, ref from, ref to);
            TrimItem(r.xMax, r.yMin, r.xMax, r.yMax, false, true, ref from, ref to);
        }

#pragma warning restore 0672

    }
}
