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
    /// Base class for canvas series graphic object. Contains functionallity that enable creating a hovering prefab where the mouse is located , and event handling
    /// The main idea behind this implementation is that it allows to create a mesh once. Then in order to create the hover effect , an object is placed on top of that mesh , those leaving the mesh unmodified.
    /// </summary>
    public abstract class EventHandlingGraphic : MaskableGraphic
    {
        /// <summary>
        /// The prefab for the hover effect
        /// </summary>
        ChartItemEffect mHoverPrefab;
        /// <summary>
        /// The currently hovered item
        /// </summary>
        ChartItemEffect mCurrentHover = null;
        /// <summary>
        /// All the objects that have a hover animation displayed. Only one object can be hovered. But the hover animation has a fade effect so multiple objects can display the animation at the same time
        /// </summary>
        List<ChartItemEffect> mHoverObjectes = new List<ChartItemEffect>();
        /// <summary>
        /// when hover prefabs end their life time. They are pooled in this list
        /// </summary>
        List<ChartItemEffect> mHoverFreeObjects = new List<ChartItemEffect>();

        protected Rect? ViewRect = null;
        protected Rect? mUvRect;
        
        /// <summary>
        /// if true , mouse events will be proccedd even if the mouse hasn't  moved
        /// </summary>
        bool mForceMouseMove = false;
        bool mIsMouseIn = false;

        Transform mHoverTransform;

        int mPickedIndex = -1, mPickedType = -1;
        object mPickedData = null;
        Vector2 mLastMousePosition = new Vector2();
        GraphicRaycaster mCaster;

        /// <summary>
        /// When the graphic data is clliped , this representes the first index being displayed in the graph. Thus the [Data Index] = refrenceIndex + [Graphic Index]
        /// </summary>
        protected int refrenceIndex { get; private set; }

        public void SetRefrenceIndex(int index)
        {
            refrenceIndex = index;
        }

        public float Sensitivity
        {
            get
            {
                float sensitivity = 10f;
                if(mControl == null)
                    mControl = GetComponent<SensitivityControl>();
                if (mControl != null)
                    sensitivity = mControl.Sensitivity;
                return sensitivity;
            }
        }

        /// <summary>
        /// call this to free all hover objects and reset event input. This is useful mostly when the entire data of the graphic has been replaced and old animations should be discarded
        /// </summary>
        public void ClearEvents()
        {
            mIsMouseIn = false;
            for (int i = 0; i < mHoverObjectes.Count; i++)
            {
                mHoverObjectes[i].gameObject.SetActive(false);
                mHoverFreeObjects.Add(mHoverObjectes[i]);
            }
            mHoverObjectes.Clear();

            if (mCurrentHover != null)
            {
                mCurrentHover.gameObject.SetActive(false);
                mHoverFreeObjects.Add(mCurrentHover);
                mCurrentHover = null;
            }
            mPickedIndex = mPickedType = -1;
            mPickedData = null;
        }
        /// <summary>
        /// The minimum for the bounding rect of the graphic data
        /// </summary>
        protected abstract Vector2 Min { get; }

        /// <summary>
        /// The maximum for the bounding rect of the graphic data
        /// </summary>
        protected abstract Vector2 Max { get; }

        /// <summary>
        /// Contains properties to define mouse sensitivy for event handling
        /// </summary>
        SensitivityControl mControl;

        /// <summary>
        /// 
        /// </summary>
        /// <param name="index"></param>
        /// <param name="type"></param>
        /// <param name="data"></param>
        /// <param name="position"></param>
        public delegate void GraphicEvent(int index, int type, object data, Vector2 position);

        /// <summary>
        /// The selected index is hovered about the specified point
        /// </summary>
        public event GraphicEvent Hover;
        /// <summary>
        /// The selected index is clicked about the specified point
        /// </summary>
        public event GraphicEvent Click;
        /// <summary>
        /// The currently hovered and selected objects are no longer selected or hovered.
        /// </summary>
        public event Action Leave;


        /// <summary>
        /// Sets the viewing data for the graphic.
        /// </summary>
        /// <param name="r">the canvas rect bounds</param>
        /// <param name="uvRect">the uv bounds of the currently cliped data</param>
        public void SetViewRect(Rect r, Rect uvRect)
        {
            ViewRect = r;
            mUvRect = uvRect;
        }

        public void HoverTransform(Transform t)
        {
            mHoverTransform = t;
        }

        public void SetHoverPrefab(ChartItemEffect prefab)
        {
            mHoverPrefab = prefab;
        }

        /// <summary>
        /// call this when the graphic view has changed , this will recheck the mouse position and input for new events.
        /// This is useful for features such as realtime graph. Where new objects can be picked even if the mouse is in one place only
        /// </summary>
        public void RefreshInputs()
        {
            mForceMouseMove = true;
        }

        protected virtual void Update()
        {
            if(HandleMouseMove(mForceMouseMove)) // handle mouse move
                mForceMouseMove = false; // mouse events are already handled in the above line, so we set this to false
        }


        /// <summary>
        /// Set up the position of the hover prefab
        /// </summary>
        /// <param name="hover">the hovered gameobject passesd using it's ChartItemEffect component</param>
        /// <param name="index">index of the data item which is hovered</param>
        protected abstract void SetUpHoverObject(ChartItemEffect hover, int index,int type,object selectionData);
        /// <summary>
        /// returns the threshold at which the mouse is considered in the area of the graphic. This threshold is realtive to the graphic rectangle.
        /// </summary>
        protected abstract float MouseInThreshold { get; }

        /// <summary>
        /// override this in a derived class to pick a data item based on the mouse position.
        /// </summary>
        /// <param name="mouse">mouse position</param>
        /// <param name="pickedIndex">first index of the selected item, or -1 for no selection</param>
        /// <param name="pickedType">type of the selected item, or -1 for no selection</param>
        protected abstract void Pick(Vector3 mouse, out int pickedIndex, out int pickedType,out object SelectionData);

        /// <summary>
        /// This method is called during update if RefreshInputs has been called in the preivous frame. When the canvas graph mesh updates , all active hover effects are repositioned to match their new position.
        /// </summary>
        protected void SetUpAllHoverObjects()
        {
            if (mHoverObjectes == null)
                return;
            for (int i = 0; i < mHoverObjectes.Count; i++)
                SetUpHoverObject(mHoverObjectes[i]);
        }

        /// <summary>
        /// utillity method that can be used in derived classes in order to 
        /// </summary>
        /// <param name="hover"></param>
        /// <param name="index"></param>
        /// <param name="type"></param>
        /// <param name="rect"></param>
        protected void SetupHoverObjectToRect(ChartItemEffect hover, int index, int type,Rect rect)
        {
            if (hover == null)
                return;
            RectTransform transform = hover.GetComponent<RectTransform>();
            transform.localScale = new Vector3(1f, 1f, 1f);
            transform.sizeDelta = new Vector2(rect.width, rect.height);
            transform.anchoredPosition3D = new Vector3(rect.center.x, rect.center.y, 0f);
        }

        /// <summary>
        /// Shuts down a hover prefab effect
        /// </summary>
        /// <param name="hover"></param>
        void TriggerOut(ChartItemEffect hover)
        {
            hover.TriggerOut(true);
            ChartMaterialController control = hover.GetComponent<ChartMaterialController>();
            if (control)
                control.TriggerOff();
        }

        /// <summary>
        /// Shuts down a hover prefab effect
        /// </summary>
        /// <param name="hover"></param>
        void TriggerIn(ChartItemEffect hover)
        {
            hover.TriggerIn(false);
            ChartMaterialController control = hover.GetComponent<ChartMaterialController>();
            if (control)
                control.TriggerOn();
        }

        /// <summary>
        /// Takes a hover object out of the pool , or creates a new one if the pool is empty
        /// </summary>
        /// <param name="index">the index of the data item , this index will be assigned to the returned object</param>
        /// <returns></returns>
        public ChartItemEffect LockHoverObject(int index,int type,object selectionData)
        {
            int count = mHoverFreeObjects.Count;
            ChartItemEffect effect = null;

            if (count > 0)
            {
                effect = mHoverFreeObjects[count - 1];
                mHoverFreeObjects.RemoveAt(count - 1); 
            }
            else
            {
                if (mHoverPrefab == null)
                    return null;
                GameObject obj = GameObject.Instantiate(mHoverPrefab.gameObject);
                MaskableGraphic g = obj.GetComponent<MaskableGraphic>();
                if (g != null)
                    g.maskable = false;
                ChartCommon.EnsureComponent<ChartItem>(obj);
                Transform parent = transform;
                if (mHoverTransform != null)
                    parent = mHoverTransform;
                obj.transform.SetParent(parent);
                effect = obj.GetComponent<ChartItemEffect>();
                effect.Deactivate += Effect_Deactivate;
            }

            effect.ItemType = type;
            effect.ItemIndex = index;
            effect.ItemData = selectionData;
            mHoverObjectes.Add(effect);
            return effect;
        }

        /// <summary>
        /// called when an effect is done animating. Reclaims the object into the hover object pool
        /// </summary>
        /// <param name="obj"></param>
        private void Effect_Deactivate(ChartItemEffect obj)
        {
            obj.gameObject.SetActive(false);
            mHoverObjectes.Remove(obj);
            mHoverFreeObjects.Add(obj);
        }

        /// <summary>
        /// helped method that extract the index from the hover object and then call SetUpHoverObject overload with index
        /// </summary>
        /// <param name="hover"></param>
        void SetUpHoverObject(ChartItemEffect hover)
        {
            if (hover == null)
                return;
            int pickedIndex = hover.ItemIndex - refrenceIndex;
            if (pickedIndex < 0)
                return;
            SetUpHoverObject(hover, pickedIndex,hover.ItemType,hover.ItemData);
        }

       /// <summary>
       /// Handle mouse events
       /// </summary>
       /// <param name="mouse"></param>
       /// <param name="leave"></param>
       /// <param name="force"></param>
        void DoMouse(Vector3 mouse, bool leave, bool force)
        {
            int prevI = mPickedIndex;
            int prevJ = mPickedType;
            if (leave)
            {
                mPickedIndex = -1;
                mPickedType = -1;
                mPickedData = null;
            }
            else
                Pick(mouse, out mPickedIndex, out mPickedType,out mPickedData);

            if (prevI != mPickedIndex || prevJ != mPickedType)
            {

                if (mCurrentHover != null)
                {
                    TriggerOut(mCurrentHover);
                    if (mPickedIndex == -1 && mPickedType == -1)
                    {
                        if (Leave != null)
                            Leave();
                    }
                    mCurrentHover = null;
                }

                if (mPickedIndex != -1 && mPickedType != -1)
                {
                    mCurrentHover = LockHoverObject(mPickedIndex,mPickedType, mPickedData);
                    if (mCurrentHover == null)
                        return;
                    if (Hover != null)
                        Hover(mPickedIndex,mPickedType, mPickedData, mouse);
                    mCurrentHover.gameObject.SetActive(true);
                    SetUpHoverObject(mCurrentHover);
                    TriggerIn(mCurrentHover);
                }
            }

        }
        
        /// <summary>
        /// Handles a mouse move event.
        /// </summary>
        /// <param name="force"></param>
        public bool HandleMouseMove(bool force)
        {
            mCaster = GetComponentInParent<GraphicRaycaster>();
            if (mCaster == null)
                return false;
            Vector2 mousePos;


            // on some machines and settings , it seems that Input.mousePosition returns invalid coordinates, the following checks make sure that the mouse position is valid
            Vector3 checkMousePos = Input.mousePosition;

            var pointer = GetComponentInParent<CustomChartPointer>();
            if (pointer != null)
                checkMousePos = pointer.ScreenPosition;
            Camera checkCamera = mCaster.eventCamera;
            if (checkCamera == null)
                checkCamera = Camera.main;
            if (checkCamera == null)
                return false;
            if (checkCamera.pixelRect.Contains(checkMousePos) == false)
                return false;

            RectTransformUtility.ScreenPointToLocalPointInRectangle(transform as RectTransform, checkMousePos, mCaster.eventCamera, out mousePos);
            //DoMouse(mousePos, false);
            float thresh = MouseInThreshold;
            if (force)
            {
                SetUpAllHoverObjects();
            }

            Vector2 min = Min;
            Vector2 max = Max;
            if (mousePos.x < min.x - thresh || mousePos.y < min.y - thresh || mousePos.x > max.x + thresh || mousePos.y > max.y + thresh)
            {
                if (mIsMouseIn)
                {
                    mIsMouseIn = false;
                    DoMouse(mousePos, true, force);
                }
                return true;
            }
            else
            {
                if (mIsMouseIn == false)
                {
                    mIsMouseIn = true;
                    mLastMousePosition = mousePos;
                    DoMouse(mousePos, false, force);
                }
                else
                {
                    if (((mLastMousePosition - mousePos).sqrMagnitude > 1) || force)
                    {
                        mLastMousePosition = mousePos;
                        DoMouse(mousePos, false, force);
                    }
                }
            }

            if ((pointer== null && Input.GetMouseButtonDown(0)) || (pointer !=null && pointer.Click))
            {
                HandleMouseDown();
            }
            return true;
        }

        /// <summary>
        /// Handles mouse down. This method will fire the mouse click event if an item is clicked
        /// </summary>
        private void HandleMouseDown()
        {
            if (mPickedIndex != -1 && mPickedType != -1)
            {
                if (Click != null)
                {
                    mCaster = GetComponentInParent<GraphicRaycaster>();
                    if (mCaster == null)
                        return;
                    Vector3 checkMousePos = Input.mousePosition;

                    var pointer = GetComponentInParent<CustomChartPointer>();
                    if (pointer != null)
                        checkMousePos = pointer.ScreenPosition;
                    Vector2 mousePos;
                    RectTransformUtility.ScreenPointToLocalPointInRectangle(transform as RectTransform, checkMousePos, mCaster.eventCamera, out mousePos);
                    Vector3 pos = transform.InverseTransformPoint(mousePos);
                    Click(mPickedIndex,mPickedType, mPickedData, pos);
                }
            }
        }

    }
}
