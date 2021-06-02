#define Graph_And_Chart_PRO
using ChartAndGraph;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

/// <summary>
/// posistions world space text on a special canvas used for billboarding.
/// </summary>
[RequireComponent(typeof(ChartItem))]
[ExecuteInEditMode]
public class TextController : MonoBehaviour
{
    public Camera Camera = null;
    Canvas mCanvas;
    RectTransform mRect;
    List<BillboardText> mText = new List<BillboardText>();
    public float PlaneDistance = 4;
    Vector3[] mPlaneCorners = new Vector3[4];
    bool mInvalidated = false;
    private AnyChart mPrivateParent;
    float mPrevScale = -1f;
    bool OwnsCanvas = false;
    //private bool mUnderCanvas = false;
    internal List<BillboardText> Text { get { return mText; } }
    GameObject mAddCanvasUnder = null;
    float mInnerScale = 1.0f;
    internal void SetInnerScale(float scale)
    {
        mInnerScale = scale;
    }

    internal AnyChart mParent
    {
        get { return mPrivateParent; }
        set
        {
            mPrivateParent = value;
            if (mPrivateParent != null)
            {
                Camera = ((IInternalUse)mPrivateParent).InternalTextCamera;
                PlaneDistance = ((IInternalUse)mPrivateParent).InternalTextIdleDistance;
                if (OwnsCanvas)
                    SafeCanvas.planeDistance = PlaneDistance;
            }
        }
    }

    void Start()
    {
        EnsureCanvas();
        Canvas.willRenderCanvases += Canvas_willRenderCanvases;
    }

    private void OnDestroy()
    {
        Canvas.willRenderCanvases -= Canvas_willRenderCanvases;
    }

    private void Canvas_willRenderCanvases()
    {
        
        if (this == null)
        {
            Canvas.willRenderCanvases -= Canvas_willRenderCanvases;
            return;
        }
        ApplyTextPosition();
    }

    void EnsureCanvas()
    {

        if (mCanvas == null)
        {
            mCanvas = GetComponentInParent<Canvas>();
            if (mCanvas == null)
            {
                OwnsCanvas = true;
                mAddCanvasUnder = new GameObject();
                mAddCanvasUnder.AddComponent<ChartItemNoDelete>();
                mAddCanvasUnder.AddComponent<RectTransform>();
                mAddCanvasUnder.transform.SetParent(gameObject.transform, false);
                mAddCanvasUnder.transform.localPosition = Vector3.zero;
                mAddCanvasUnder.transform.localScale = new Vector3(1f, 1f, 1f);
                mAddCanvasUnder.transform.localRotation = Quaternion.identity;

                mCanvas = mAddCanvasUnder.AddComponent<Canvas>();
                mAddCanvasUnder.AddComponent<CanvasScaler>();
                mAddCanvasUnder.AddComponent<GraphicRaycaster>();

                if (mParent != null && (mParent.VRSpaceText || mParent.PaperEffectText))
                {
                    mCanvas.renderMode = RenderMode.WorldSpace;
                }
                else
                {
                    mCanvas.renderMode = RenderMode.ScreenSpaceCamera;
                }
                mCanvas.planeDistance = PlaneDistance;
                Camera = EnsureCamera();
                mRect = mCanvas.GetComponent<RectTransform>();
                //  mCanvas.pixelPerfect = true;
                CanvasScaler scaler = mCanvas.GetComponent<CanvasScaler>();
                scaler.uiScaleMode = CanvasScaler.ScaleMode.ConstantPixelSize;
            }
            else
            {
                //mUnderCanvas = true;
            }
        }
    }

    Canvas SafeCanvas
    {
        get
        {
            EnsureCanvas();
            return mCanvas;
        }
    }

    void OnDestory()
    {
        DestroyAll();
    }

    public void DestroyAll()
    {
        for (int i = 0; i < mText.Count; i++)
        {
            if (mText[i] != null && mText[i].Recycled == false)
            {
                if (mText[i].UIText != null)
                    ChartCommon.SafeDestroy(mText[i].UIText.gameObject);
                if (mText[i].RectTransformOverride != null)
                    ChartCommon.SafeDestroy(mText[i].RectTransformOverride.gameObject);
                mText[i].UIText = null;
                mText[i].RectTransformOverride = null;
                ChartCommon.SafeDestroy(mText[i].gameObject);
            }
        }
        mText.Clear();
    }

    public void AddText(BillboardText billboard)
    {
        if (billboard == null)
            return;
        if (billboard.UIText == null)
            return;

        mInvalidated = false;
        mText.Add(billboard);
        var rect = billboard.Rect;
        EnsureCanvas();
        float yScale = (billboard.YMirror == true) ? -1f : 1f;
        if (rect == null)
        {
            var meshObj = billboard.UIText;
            TextDirection dir = meshObj.GetComponent<TextDirection>();
            if (dir != null)
                dir.SetTextController(this);

            billboard.parent = null;
            meshObj.transform.SetParent(transform, false);
            if (mParent != null)
                meshObj.layer = mParent.gameObject.layer;

          //  meshObj.transform.localRotation = Quaternion.identity;
            meshObj.transform.position = billboard.transform.position;
            //  meshObj.transform.localScale = new Vector3(1f, 1f, 1f);

            meshObj.transform.localScale = new Vector3(billboard.Scale * mInnerScale, yScale* billboard.Scale * mInnerScale, 1f);
        }
        else
        {
            TextDirection dir = rect.GetComponent<TextDirection>();
            if (dir != null)
                dir.SetTextController(this);

            GameObject obj = ChartCommon.CreateCanvasChartItem();
            RectTransform t = obj.GetComponent<RectTransform>();
            obj.AddComponent<Canvas>();
            var addUnder = mAddCanvasUnder;
            if (addUnder == null)
                addUnder = gameObject;

            obj.transform.SetParent(addUnder.transform, false);
            obj.transform.localPosition = Vector3.zero;
         //  obj.transform.localRotation = Quaternion.identity;
            obj.transform.localScale = new Vector3(1f,1f,1f);

            billboard.parent = t;
            billboard.Rect.SetParent(t, false);
            if (mParent != null)
            {
                obj.layer = mParent.gameObject.layer; 
                billboard.Rect.gameObject.layer = mParent.gameObject.layer;
            }
          //  billboard.Rect.localRotation = Quaternion.identity;
            billboard.Rect.localPosition = Vector3.zero;
            billboard.Rect.localScale = new Vector3(1f, 1f, 1f);

            //    rect.anchoredPosition3D = new Vector3();
            if (dir == null)
            {
                rect.anchorMin = Vector2.zero;
                rect.anchorMax = Vector2.zero;
            }

            billboard.parent.position = billboard.transform.position;

            billboard.UIText.transform.localScale = new Vector3(billboard.Scale * mInnerScale, yScale* billboard.Scale * mInnerScale, 1f);
        }








        //Vector3 scale = new Vector3(1f/transform.lossyScale.x, 1f/transform.lossyScale.y, 1f/transform.lossyScale.z);//SafeCanvas.transform.localScale;
        //Vector3 scale = SafeCanvas.transform.localScale;

        //if (mUnderCanvas)
        //  billboard.UIText.transform.localScale = scale;
        //else



        //        ContentSizeFitter fitter = billboard.UIText.gameObject.GetComponent<ContentSizeFitter>();
        //        if(fitter == null)
        //            fitter = billboard.UIText.gameObject.AddComponent<ContentSizeFitter>();
        //        fitter.verticalFit = ContentSizeFitter.FitMode.PreferredSize;
        //        fitter.horizontalFit = ContentSizeFitter.FitMode.PreferredSize;


    }

    Camera EnsureCamera()
    {
        if (Camera == null)
            return AssignCamera(Camera.main);
        else
            return AssignCamera(Camera);
    }

    Camera AssignCamera(Camera camera)
    {
        Canvas canvas = SafeCanvas;
        if (canvas.worldCamera != camera && OwnsCanvas)
            canvas.worldCamera = camera;
        return camera;
    }

    void Update()
    {
        if(Application.isEditor && Application.isPlaying == false)
        {
            ApplyTextPosition();
        }
    }

    Vector3 ProjectPointOnPlane(Vector3 planeNormal, Vector3 planePoint, Vector3 point)
    {
        planeNormal.Normalize();
        float distance = -Vector3.Dot(planeNormal.normalized, (point - planePoint));
        return point + planeNormal * distance;
    }

    void CalculatePlane(Camera cam, RectTransform transform, out Vector3 center, out Vector3 normal)
    {
        mRect.GetWorldCorners(mPlaneCorners);
        center = new Vector3();
        for (int i = 0; i < mPlaneCorners.Length; i++)
        {
            center += mPlaneCorners[i];
        }
        center *= 0.25f;
        Vector3 a = mPlaneCorners[1] - mPlaneCorners[0];
        Vector3 b = mPlaneCorners[2] - mPlaneCorners[0];
        normal = Vector3.Cross(a, b).normalized;
    }

    public void ApplyTextPosition()
    {
        Camera = EnsureCamera();
        if (mCanvas == null)
            return;
        float scale = 1f;
        if (Camera != null)
        { 
            if (mParent != null && (mParent.VRSpaceText || mParent.PaperEffectText) && OwnsCanvas)
            {
                Vector3 difVector = new Vector3(transform.position.x, 0, transform.position.z) - new Vector3(Camera.transform.position.x, 0, Camera.transform.position.z);
                if (difVector != Vector3.zero && mParent.PaperEffectText == false)
                {
                    mCanvas.transform.rotation = Quaternion.LookRotation(difVector, Vector3.up);
                }

                //            mCanvas.transform.rotation = Camera.transform.rotation;
                mCanvas.transform.localScale = new Vector3(mParent.VRSpaceScale, mParent.VRSpaceScale, mParent.VRSpaceScale);
            }


            if (mPrivateParent != null && mPrivateParent.KeepOrthoSize)
            {

                if (Camera != null && Camera.orthographic && Camera.orthographicSize > 0.1f)
                {
                    scale = 5f / Camera.orthographicSize;
                }
            }
            scale *= mInnerScale;
            if (Mathf.Abs(mPrevScale - scale) > 0.001f)
            {
                mInvalidated = false;
            }
            mPrevScale = scale;
        }
        else
        {
            if (mParent == null || mParent.IsCanvas == false)
                return;
        }
        //if (mInvalidated == false) 
        // {
        mText.RemoveAll(x =>
        {
            if (x == null)
                return true;
            BillboardText billboard = x;
            if (mInvalidated == false || billboard.transform.hasChanged || mCanvas.transform.hasChanged)
            {
                var rect = billboard.Rect;
                if (rect != null)
                    rect.transform.position = billboard.transform.position;
                else
                    billboard.UIText.transform.position = billboard.transform.position;
                float yScale = (billboard.YMirror == true) ? -1f : 1f;
                billboard.UIText.transform.localScale = new Vector3(billboard.Scale * scale, yScale* billboard.Scale * scale, 1f);
                billboard.transform.hasChanged = false;
            }
            return false;
        });

        mInvalidated = true;

        // }
        return;
        //}
        /*        Canvas canvas = SafeCanvas;
                if (mParent != null)
                {
                    Camera = mParent.TextCamera;
                    PlaneDistance = mParent.TextIdleDistance;
                    canvas.planeDistance = PlaneDistance;
                }

                Camera cam = EnsureCamera();
                if (cam == null)
                {
                    if (mWarn == false)
                        Debug.LogWarning("Chart Warning: No main camera set , please set the Chart's camera using \"TextCamera\" in the inspector");
                    mWarn = true;
                    return;
                }
                mWarn = false;
                Vector3 planeCenter;
                Vector3 planeNormal;
                CalculatePlane(cam, mRect, out planeCenter, out planeNormal);
                //Debug.DrawLine(planeCenter, planeCenter+ planeNormal,Color.red);
                foreach (BillboardText text in mText)
                {
                    //  RectTransformUtility.
                    Vector3 point = text.transform.position;
                    Vector3 pointOnPlane = ProjectPointOnPlane(planeNormal, planeCenter, text.transform.position);
                    //Debug.DrawLine(point, pointOnPlane);
                    Vector3 worldVec = (pointOnPlane - point);
                    Vector3 vec = mRect.worldToLocalMatrix * worldVec;
                    float dist = vec.magnitude;
                    if (Vector3.Dot(worldVec, cam.transform.forward) > 0)
                        dist = -dist;

                    Vector2 viewport = cam.WorldToViewportPoint(pointOnPlane);
                  //  Debug.DrawLine(viewport, pointOnPlane);
        //            Vector3 anchored = new Vector3(
          //              (mRect.sizeDelta.x * viewport.x) - (mRect.sizeDelta.x * mRect.anchorMin.x),
            //            (mRect.sizeDelta.y * viewport.y) - (mRect.sizeDelta.y * mRect.anchorMin.y),
              //          dist);
                    Vector2 screen = RectTransformUtility.WorldToScreenPoint(cam,pointOnPlane);
                    Vector2 pos;
                    RectTransformUtility.ScreenPointToLocalPointInRectangle(mRect, screen, cam, out pos);
                    //            float scaleDist = mPlaneDistance + worldVec.magnitude;
                    //          if (scaleDist <= 0)
                    //            scaleDist = 0.1f;
                    // float scale = scaleDist/ mPlaneDistance;
                    //text.Rect.localScale = new Vector3(scale, scale, 1f);
                    text.Rect.localPosition = new Vector3(pos.x, pos.y , dist);
                }*/
    }

    void LateUpdate()
    {
        //ApplyTextPosition();
    }
}