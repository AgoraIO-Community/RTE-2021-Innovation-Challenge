#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ChartAndGraph.Axis;
using UnityEngine;

namespace ChartAndGraph
{
    public class WorldSpaceBarChart : BarChart
    {
        /// <summary>
        /// If this value is set all the text in the chart will be rendered to this specific camera. otherwise text is rendered to the main camera
        /// </summary>
        [SerializeField]
        [Tooltip("If this value is set all the text in the chart will be rendered to this specific camera. otherwise text is rendered to the main camera")]
        private Camera textCamera;

        /// <summary>
        /// If this value is set all the text in the chart will be rendered to this specific camera. otherwise text is rendered to the main camera
        /// </summary>
        public Camera TextCamera
        {
            get { return textCamera; }
            set
            {
                textCamera = value;
                OnPropertyUpdated();
            }
        }

        public override bool IsCanvas
        {
            get
            {
                return false;
            }
        }
        /// <summary>
        /// The distance from the camera at which the text is at it's original size.
        /// </summary>
        [SerializeField]
        [Tooltip("The distance from the camera at which the text is at it's original size")]
        private float textIdleDistance = 20f;

        /// <summary>
        /// The distance from the camera at which the text is at it's original size.
        /// </summary>
        public float TextIdleDistance
        {
            get { return textIdleDistance; }
            set
            {
                textIdleDistance = value;
                OnPropertyUpdated();
            }
        }
        /// <summary>
        /// prefab for all the bar elements of the chart. must be one unit size and with a bottom middle pivot
        /// </summary>
        [SerializeField]
        [Tooltip("prefab for all the bar elements of the chart. must be one unit size and with a bottom middle pivot")]
        private GameObject barPrefab;

        /// <summary>
        /// prefab for all the bar elements of the chart. must be one unit size and with a bottom middle pivot
        /// </summary>
        public GameObject BarPrefab
        {
            get
            {
                return barPrefab;
            }
            set
            {
                barPrefab = value;
                OnPropertyUpdated();
            }
        }

        /// <summary>
        /// The seperation between the axis and the chart bars.
        /// </summary>
        [SerializeField]
        [Tooltip("The seperation between the axis and the chart bars")]
        private ChartOrientedSize AxisSeperation = new ChartOrientedSize();

        /// <summary>
        /// The seperation between the axis and the chart bars
        /// </summary>
        public ChartOrientedSize axisSeperation
        {
            get { return AxisSeperation; }
            set
            {
                AxisSeperation = value;
                OnPropertyUpdated();
            }
        }

        /// <summary>
        /// The seperation between bars of the same group.
        /// Use cases:
        /// 1. set the depth to 0 to make each group look more 2d.
        /// 2. set the breadth to 0 to make align the bars of each group along the z axis
        /// </summary>
        [SerializeField]
        [Tooltip("The seperation between bars of the same group")]
        private ChartOrientedSize barSeperation = new ChartOrientedSize();

        /// <summary>
        /// The seperation between bars of the same group.
        /// Use cases:
        /// 1. set the depth to 0 to make each group look more 2d.
        /// 2. set the breadth to 0 to make align the bars of each group along the z axis
        /// </summary>
        public ChartOrientedSize BarSeperation
        {
            get { return barSeperation; }
            set
            {
                barSeperation = value;
                OnPropertyUpdated();
            }
        }

        /// <summary>
        /// The seperation between bar groups.
        /// Use cases:
        /// 1.set the depth to 0 for a more 2d look.
        /// 2.set the breadth to 0 to align the groups on the z axis
        /// </summary>
        [SerializeField]
        [Tooltip("seperation between bar groups")]
        private ChartOrientedSize groupSeperation = new ChartOrientedSize();

        /// <summary>
        /// The seperation between bar groups.
        /// Use cases:
        /// 1.set the depth to 0 for a more 2d look.
        /// 2.set the breadth to 0 to align the groups on the z axis
        /// <summary>
        public ChartOrientedSize GroupSeperation
        {
            get { return groupSeperation; }
            set
            {
                groupSeperation = value;
                OnPropertyUpdated();
            }
        }
        /// <summary>
        /// the size of each bar in the chart
        /// </summary>
        [SerializeField]
        [Tooltip("the size of each bar in the chart")]
        private ChartOrientedSize barSize = new ChartOrientedSize(1f, 1f);
        /// <summary>
        /// the size of each bar in the chart
        /// </summary>
        public ChartOrientedSize BarSize
        {
            get { return barSize; }
            set
            {
                barSize = value;
                OnPropertyUpdated();
            }
        }

        protected override ChartOrientedSize AxisSeperationLink
        {
            get
            {
                return AxisSeperation;
            }
        }

        protected override ChartOrientedSize BarSeperationLink
        {
            get
            {
                return BarSeperation;
            }
        }
        protected override ChartOrientedSize GroupSeperationLink
        {
            get
            {
                return GroupSeperation;
            }
        }
        protected override ChartOrientedSize BarSizeLink
        {
            get
            {
                return BarSize;
            }
        }
        protected override Camera TextCameraLink
        {
            get
            {
                return TextCamera;
            }
        }

        protected override float TextIdleDistanceLink
        {
            get
            {
                return TextIdleDistance;
            }
        }

        protected override GameObject BarPrefabLink
        {
            get
            {
                return BarPrefab;
            }
        }
        protected override void ValidateProperties()
        {
            base.ValidateProperties();
            if (barSize.Breadth < 0f)
                barSize.Breadth = 0f;
            if (barSize.Depth < 0f)
                barSize.Depth = 0f;
        }

    }
}
