#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.EventSystems;
using UnityEngine.UI;

namespace ChartAndGraph
{
    /// <summary>
    /// controls the change of materials for a chart item based on the events that the item recives
    /// </summary>
    class ChartMaterialController : MonoBehaviour, IPointerEnterHandler, IPointerExitHandler, IPointerDownHandler, IPointerUpHandler
    {
        private static bool WarnedNull = false;

        public bool HandleEvents = true;
        [SerializeField]
        ChartDynamicMaterial materials;

        /// <summary>
        /// the materials that will be used by this controllers
        /// </summary>
        internal ChartDynamicMaterial Materials
        {
            get { return materials; }
            set
            {
                materials = value;
                if (materials != null)
                {
                    if (materials.Normal == null)
                        WarnNullItem();
                    else
                        SetMaterial(materials.Normal, null);
                }
            }
        }

        private void SetMaterial(Material m, Material fallback)
        {
            if (mRenderer == null)
                mRenderer = GetComponent<Renderer>();
            if (mRenderer != null)
                ChartCommon.SafeAssignMaterial(mRenderer, m, fallback);
            else
            {
                if (mCanvasRenderer == null)
                    mCanvasRenderer = GetComponent<Graphic>();
                if (mMat == null)
                {
                    mMat = new Material(materials.Normal);
                    mMat.hideFlags = HideFlags.DontSave;
                }
                if (mCanvasRenderer != null)
                    mCanvasRenderer.material = mMat;

            }
        }

        private void WarnNullItem()
        {
            if (WarnedNull == false)
            {
                Debug.LogWarning("null material are not allowed");
                WarnedNull = true;
            }
        }

        internal ChartItemMaterialLerpEffect mLerpEffect;
        private Color mLerpTo = Color.clear;
        Color mLerpFrom;
        private Renderer mRenderer;
        private Material mMat;
        private Graphic mCanvasRenderer;
        private bool mMouseOver = false;
        private bool mMouseDown = false;
        private int? CombineId;
        private int? BaseColorId;
        float mAccumilatedTime = 0f;
        bool mLerping = false;

        void Start()
        {
            mLerpEffect = GetComponent<ChartItemMaterialLerpEffect>();
            Refresh();
        }
        int BaseColor()
        {
            if (BaseColorId.HasValue == false)
                BaseColorId = Shader.PropertyToID("_BaseColor");
            return BaseColorId.Value;
        }
        int Combine()
        {
            if (CombineId.HasValue == false)
                CombineId = Shader.PropertyToID("_Combine");
            return CombineId.Value;
        }

        public void TriggerOn()
        {
            mMouseOver = true;
            Refresh();
        }

        public void TriggerOff()
        {
            mMouseOver = false;
            Refresh();
        }

        void OnMouseEnter()
        {
            if (HandleEvents == false)
                return;
            mMouseOver = true;
            Refresh();
        }

        void OnMouseExit()
        {
            if (HandleEvents == false)
                return;
            mMouseOver = false;
            Refresh();
        }

        void OnMouseDown()
        {
            if (HandleEvents == false)
                return;
            mMouseDown = true;
            Refresh();
        }

        void OnMouseUp()
        {
            if (HandleEvents == false)
                return;
            mMouseDown = false;
            Refresh();
        }

        void Update()
        {
            if (mLerpEffect != null && mLerping)
            {
                mAccumilatedTime += Time.deltaTime;
                if (mAccumilatedTime > mLerpEffect.LerpTime)
                {
                    SetRendererColor(mLerpTo);

                }
                else
                    SetRendererColor(Color.Lerp(mLerpFrom, mLerpTo, mAccumilatedTime / mLerpEffect.LerpTime));
            }
        }

        Color GetColorCombine(Material m)
        {
            if (m.HasProperty(Combine()))
                return m.GetColor(Combine());
            if (m.HasProperty(BaseColor()))
                return m.GetColor(BaseColor());
            return m.color;
        }

        void SetColorCombine(Material m, Color c)
        {
            if (m.HasProperty(Combine()))
                m.SetColor(Combine(), c);
            else if (m.HasProperty(BaseColor()))
                m.SetColor(BaseColor(), c);
            else
                m.color = c;
        }

        void SetRendererColor(Color c)
        {
            if (mRenderer == null)
                mRenderer = GetComponent<Renderer>();
            if (mRenderer != null)
            {
                if (c == GetColorCombine(materials.Normal) && mMouseOver == false && mMouseDown == false)
                {
                    ChartCommon.SafeDestroy(mRenderer.material);
                    mRenderer.material = null;
                    mRenderer.sharedMaterial = materials.Normal;
                    mLerping = false;
                }
                else
                    SetColorCombine(mRenderer.material, c);
            }
            else
            {

                if (mCanvasRenderer == null)
                    mCanvasRenderer = GetComponent<Graphic>();

                if (mCanvasRenderer != null)
                {

                    if (mMat == null)
                    {
                        mMat = new Material(materials.Normal);
                        mMat.hideFlags = HideFlags.DontSave;
                        mCanvasRenderer.material = mMat;
                    }

                    if (mCanvasRenderer.material != mMat)
                        mCanvasRenderer.material = mMat;

                    SetColorCombine(mMat, c);
                    if (c == GetColorCombine(materials.Normal) && mMouseOver == false && mMouseDown == false)
                    {
                        mLerping = false;
                        mCanvasRenderer.material = materials.Normal;
                    }
                }
            }
        }

        void SetColor(Color c)
        {
            if (ChartCommon.IsInEditMode)
                return;
            if (c == Color.clear)
                c = GetColorCombine(materials.Normal);
            if (mLerpEffect == null)
                SetRendererColor(c);
            else
            {
                if (mMat == null)
                {
                    mMat = new Material(materials.Normal);
                    mMat.hideFlags = HideFlags.DontSave;
                    if (mCanvasRenderer != null)
                        mCanvasRenderer.material = mMat;
                }

                if (mRenderer != null)
                    mLerpFrom = GetColorCombine(mRenderer.material);
                else if (mCanvasRenderer != null && mMat != null)
                    mLerpFrom = GetColorCombine(mMat);
                else
                    mLerpFrom = GetColorCombine(materials.Normal);

                mLerpTo = c;
                mLerping = true;
                mAccumilatedTime = 0f;
            }
        }

        void OnDestroy()
        {
            if (mRenderer != null)
                ChartCommon.SafeDestroy(mRenderer.material);
            ChartCommon.SafeDestroy(mMat);
        }

        /// <summary>
        /// Applies changes made to ChartMaterialController.Materials
        /// </summary>
        public void Refresh()
        {
            if (Materials == null || materials.Normal == null)
                return;
            if (mMouseOver == false)
                SetColor(Color.clear);
            else if (mMouseDown == false || Materials.Selected == Color.clear)
                SetColor(materials.Hover);
            else
                SetColor(materials.Selected);
        }

        public void OnPointerEnter(PointerEventData eventData)
        {

            OnMouseEnter();
        }

        public void OnPointerExit(PointerEventData eventData)
        {
            OnMouseExit();
        }

        public void OnPointerDown(PointerEventData eventData)
        {
            OnMouseDown();
        }

        public void OnPointerUp(PointerEventData eventData)
        {
            OnMouseUp();
        }
    }
}
