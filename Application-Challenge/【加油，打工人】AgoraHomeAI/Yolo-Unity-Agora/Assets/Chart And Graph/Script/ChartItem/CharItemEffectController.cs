#define Graph_And_Chart_PRO

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// manages all the chart effect for a chart item. This includes scale translation and rotation effects
    /// </summary>
    public class CharItemEffectController : MonoBehaviour
    {
        List<ChartItemEffect> mEffects = new List<ChartItemEffect>();
        Transform mParent;
        internal bool WorkOnParent { get; set; }
        internal bool InitialScale { get; set; }
        Vector3 mInitialScale;
        protected Transform Parent
        {
            get
            {
                if (mParent == null)
                    mParent = transform.parent;
                return mParent;
            }
        }
        public CharItemEffectController()
        {
            InitialScale = true;
        }
        void Start()
        {
            mInitialScale = transform.localScale;
        }
        void OnTransformParentChanged()
        {
            mInitialScale = transform.localScale;
        }
        void Update()
        {
            Transform trans = transform;
            if (WorkOnParent)
            {
                trans = Parent;
                if (trans == null)
                    return;
            }
            Vector3 scale = new Vector3(1f,1f,1f);
            if (InitialScale)
                scale = mInitialScale;
            Vector3 translation = Vector3.zero;
            Quaternion rotation = Quaternion.identity;

            for (int i=0; i<mEffects.Count; i++)
            {
                ChartItemEffect effect = mEffects[i];
                if (effect == null || effect.gameObject == null)
                {
                    mEffects.RemoveAt(i);
                    --i;
                    continue;
                }
                scale.x *= effect.ScaleMultiplier.x;
                scale.y *= effect.ScaleMultiplier.y;
                scale.z *= effect.ScaleMultiplier.z;

                translation += effect.Translation;
                rotation *= effect.Rotation;
            }
            trans.localScale = scale;
        }

        public void Unregister(ChartItemEffect effect)
        {
            mEffects.Remove(effect);
            if(mEffects.Count == 0)
                enabled = false;
        }
        public void Register(ChartItemEffect effect)
        {
            if (mEffects.Contains(effect))
                return;
            if (enabled == false)
                enabled = true;
            mEffects.Add(effect);
        }
    }
}
