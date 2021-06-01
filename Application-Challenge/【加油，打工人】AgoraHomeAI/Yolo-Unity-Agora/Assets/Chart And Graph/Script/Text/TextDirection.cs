#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    [ExecuteInEditMode]
    class TextDirection : MonoBehaviour
    {
        public Material PointMaterial = null;
        public Material LineMaterial = null;

        public float Length = 20f;
        public float Gap = 5f;
        public float Thickness = 2f;
        public float PointSize = 10f;

        public CanvasLines Lines = null;
        public CanvasLines Point = null;
        public MonoBehaviour Text = null;

        private Transform relativeTo;
        private Transform relativeFrom;
        private TextController controller;
        public void Start()
        {

        }

        public void SetTextController (TextController control)
        {
            controller = control;
        }

        public void SetRelativeTo(Transform from,Transform to)
        {
            relativeTo = to;
            relativeFrom = from;
            
        }

        public void LateUpdate()
        {
            if (relativeFrom == null || relativeTo == null || controller == null || controller.Camera == null)
                return;
            Vector3 dir = (relativeTo.position - relativeFrom.position).normalized * Length;
            Quaternion inverse = Quaternion.Inverse(controller.Camera.transform.rotation);
            dir = inverse * dir;
            SetDirection(dir);
        }

        public void SetDirection(float angle)
        {
            SetDirection(ChartCommon.FromPolar(angle, Length));
        }

        private void SetDirection(Vector3 dir)
        {
            //Vector3 dir = ChartCommon.FromPolar(angle, Length);
            float sign = Mathf.Sign(dir.x);
            Vector3 dirAdd = new Vector3(1f, 0f, 0f) * sign * Length;
            Vector3 gapAdd = new Vector3(1f, 0f, 0f) * sign * Gap;
            if (LineMaterial != null)
            {
                List<CanvasLines.LineSegement> segments = new List<CanvasLines.LineSegement>();
                segments.Add(new CanvasLines.LineSegement(new Vector3[] { Vector3.zero, dir, dir + dirAdd }));
                Lines.Thickness = Thickness;
                Lines.Tiling = 1f;
                Lines.material = LineMaterial;
                Lines.SetLines(segments);        
            }

            if(PointMaterial != null)
            {
                List<CanvasLines.LineSegement> segments = new List<CanvasLines.LineSegement>();
                segments.Add(new CanvasLines.LineSegement(new Vector3[] { Vector3.zero}));
                Point.MakePointRender(PointSize);
                Point.material = PointMaterial;
                Point.SetLines(segments);
            }

            Vector2 anchor = new Vector2( 0.5f , 0.5f);
            Vector2 pivot = new Vector2((sign > 0f) ? 0f : 1f, 0.5f);

            var rect = Text.GetComponent<RectTransform>();
            if (rect != null)
            {
                rect.anchorMin = anchor;
                rect.anchorMax = anchor;
                rect.pivot = pivot;
                var t = Text.GetComponent<Text>();
                if (t != null)
                    t.alignment = (sign > 0f) ? TextAnchor.MiddleLeft : TextAnchor.MiddleRight;
                else
                {
                    ChartCommon.DoTextSign(Text, sign);

                }
                rect.anchoredPosition = dir + dirAdd + gapAdd;
            }
            else
            {
                Debug.LogWarning("Direction text must contain a rect transform");
            }
        }
    }
}
