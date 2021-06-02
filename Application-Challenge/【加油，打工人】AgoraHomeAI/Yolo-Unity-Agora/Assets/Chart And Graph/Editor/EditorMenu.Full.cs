#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;
using UnityEngine;

namespace ChartAndGraph
{

    partial class EditorMenu
    {
        private static void InstanciateWorldSpace(string path)
        {
            GameObject obj = Resources.Load<GameObject>(path);
            //  GameObject obj = AssetDatabase.LoadAssetAtPath<GameObject>(path);
            GameObject newObj = (GameObject)GameObject.Instantiate(obj);
            newObj.name = newObj.name.Replace("(Clone)", "");
            Undo.RegisterCreatedObjectUndo(newObj, "Create Object");
        }

        [MenuItem("Tools/Charts/Radar/3D")]
        public static void AddRadarChartWorldSpace()
        {
            InstanciateWorldSpace("MenuPrefabs/3DRadar");
        }

        [MenuItem("Tools/Charts/Bar/3D/Simple")]
        public static void AddBarChartSimple3D()
        {
            InstanciateWorldSpace("MenuPrefabs/Bar3DSimple");
        }

        [MenuItem("Tools/Charts/Bar/3D/Multiple Groups")]
        public static void AddBarChartMultiple3D()
        {
            InstanciateWorldSpace("MenuPrefabs/Bar3DMultiple");
        }

        [MenuItem("Tools/Charts/Torus/3D")]
        public static void AddTorusChart3D()
        {
            InstanciateWorldSpace("MenuPrefabs/Torus3D");
        }

        [MenuItem("Tools/Charts/Pie/3D")]
        public static void AddPieChart3D()
        {
            InstanciateWorldSpace("MenuPrefabs/Pie3D");
        }

        [MenuItem("Tools/Charts/Graph/3D")]
        public static void AddGraph3D()
        {
            InstanciateWorldSpace("MenuPrefabs/3DGraph");
        }

    }
}
