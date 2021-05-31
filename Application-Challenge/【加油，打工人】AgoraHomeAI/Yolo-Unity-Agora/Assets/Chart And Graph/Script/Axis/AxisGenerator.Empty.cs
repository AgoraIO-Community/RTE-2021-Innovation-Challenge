#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
namespace ChartAndGraph.Axis
{
    public partial class AxisGenerator : MonoBehaviour,IAxisGenerator
    {
        partial void InnerFixLabels(AnyChart parent);
        partial void InnerSetAxis(double scrollOffset, AnyChart parent, AxisBase axis, ChartOrientation axisOrientation, bool isSubDivisions);

        public void FixLabels(AnyChart parent)
        {
            InnerFixLabels(parent);
        }

        public void SetAxis(double scrollOffset, AnyChart parent, AxisBase axis, ChartOrientation axisOrientation, bool isSubDivisions)
        {
            InnerSetAxis(scrollOffset, parent, axis, axisOrientation, isSubDivisions);
        }

        public UnityEngine.Object This()
        {
            return this;
        }

        public GameObject GetGameObject()
        {
            return gameObject;
        }

    }
}
 