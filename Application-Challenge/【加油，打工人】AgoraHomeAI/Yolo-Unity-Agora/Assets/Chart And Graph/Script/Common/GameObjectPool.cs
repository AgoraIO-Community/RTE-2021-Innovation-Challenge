#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// pools gameobjects for repeated use
    /// </summary>
    /// <typeparam name="T"></typeparam>
    class GameObjectPool<T> where T : MonoBehaviour
    {
        List<T> mPool = new List<T>();
        public void RecycleObject(T obj)
        {
            mPool.Add(obj);
        }

        public T TakeObject()
        {
            if (mPool.Count == 0)
                return default(T);
            int last = mPool.Count - 1;
            T res = mPool[last];
            mPool.RemoveAt(last);
            if (res.gameObject.activeInHierarchy == false)
                res.gameObject.SetActive(true);
            return res;
        }

        public void DestoryAll()
        {
            for (int i = 0; i < mPool.Count; i++)
            {
                T t = mPool[i];
                if (t != null && t.gameObject != null)
                {
                    ChartCommon.SafeDestroy(t.gameObject);
                }
            }
            mPool.Clear();
        }

        public void DeactivateObjects()
        {
            for (int i = 0; i < mPool.Count; i++)
            {
                T t = mPool[i];
                if (t != null && t.gameObject != null)
                {
                    if (t.gameObject.activeInHierarchy)
                        t.gameObject.SetActive(false);
                }
            }
        }

    }
}
