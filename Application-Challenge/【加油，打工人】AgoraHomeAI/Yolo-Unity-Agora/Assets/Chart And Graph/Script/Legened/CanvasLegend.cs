#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph.Legened
{
    /// <summary>
    /// class for canvas legned. this class basiically creates the legned prefab for each category in the chart
    /// </summary>
    [ExecuteInEditMode]
    class CanvasLegend : MonoBehaviour
    {
        [SerializeField]
        private int fontSize;


        [Serializable]
        public class ImageOverride
        {
            public Texture2D Image = null;
            public String category = "";
        }
        [SerializeField]
        public ImageOverride[] CategoryImages = null;

        private Dictionary<string,Texture2D> CreateimageDictionary()
        {
            Dictionary<string, Texture2D> dic = new Dictionary<string, Texture2D>();
            if (CategoryImages == null)
                return dic;
            foreach(ImageOverride ent in CategoryImages)
            {
                dic[ent.category] = ent.Image;
            }
            return dic;
        }

        public int FontSize
        {
            get { return fontSize; }
            set
            {
                fontSize = value;
                PropertyChanged();
            }
        }

        [SerializeField]
        private CanvasLegendItem legendItemPrefab;

        public CanvasLegendItem LegenedItemPrefab
        {
            get { return legendItemPrefab; }
            set
            {
                legendItemPrefab = value;
                PropertyChanged();
            }
        }

        [SerializeField]
        private AnyChart chart;

        List<UnityEngine.Object> mToDispose = new List<UnityEngine.Object>();
        bool mGenerateNext = false;
        public AnyChart Chart
        {
            get { return chart; }
            set
            {
                if (chart != null)
                    ((IInternalUse)chart).Generated -= CanvasLegend_Generated;
                chart = value;
                if(chart != null)
                    ((IInternalUse)chart).Generated += CanvasLegend_Generated;
                PropertyChanged();
            }
        }
        void Start()
        {
            if (chart != null)
                ((IInternalUse)chart).Generated += CanvasLegend_Generated;
            InnerGenerate();
        }
        void OnEnable()
        {
            if (chart != null)
                ((IInternalUse)chart).Generated += CanvasLegend_Generated;
            InnerGenerate();
        }
        void OnDisable()
        {
            if (chart != null)
                ((IInternalUse)chart).Generated -= CanvasLegend_Generated;
        //    Clear();
        }
        void OnDestory()
        {
            if (chart != null)
                ((IInternalUse)chart).Generated -= CanvasLegend_Generated;
            Clear();
        }
        private void CanvasLegend_Generated()
        {
           InnerGenerate();
        }
        protected void OnValidate()
        {
            if (chart != null)
                ((IInternalUse)chart).Generated += CanvasLegend_Generated;
            Generate();
        }
        protected void PropertyChanged()
        {
            Generate();
        }

        public void Clear()
        {
            CanvasLegendItem[] items = gameObject.GetComponentsInChildren<CanvasLegendItem>();
            for(int i=0; i<items.Length; i++)
            {
                if (items[i] == null || items[i].gameObject == null)
                    continue;
                ChartCommon.SafeDestroy(items[i].gameObject);
            }
            for(int i=0; i<mToDispose.Count; i++)
            {
                UnityEngine.Object obj = mToDispose[i];
                if (obj != null)
                    ChartCommon.SafeDestroy(obj);
            }
            mToDispose.Clear();
        }

        bool isGradientShader(Material mat)
        {
            if (mat.HasProperty("_ColorFrom") && mat.HasProperty("_ColorTo"))
                return true;
            return false;
        }

        Sprite CreateSpriteFromTexture(Texture2D t)
        {
            Sprite sp = Sprite.Create(t, new Rect(0f, 0f, (float)t.width, (float)t.height), new Vector2(0.5f, 0.5f));
            sp.hideFlags = HideFlags.DontSave;
            mToDispose.Add(sp);
            return sp;
        }

        Material CreateCanvasGradient(Material mat)
        {
            Material grad = new Material((Material)Resources.Load("Chart And Graph/Legend/CanvasGradient"));
            grad.hideFlags = HideFlags.DontSave;
            Color from = mat.GetColor("_ColorFrom");
            Color to = mat.GetColor("_ColorTo");
            grad.SetColor("_ColorFrom", from);
            grad.SetColor("_ColorTo", to);
            mToDispose.Add(grad);
            return grad;
        }

        public void Generate()
        {
            mGenerateNext = true;
        }

        void Update()
        {

            if (mGenerateNext == true)
                InnerGenerate();
        }

        private void InnerGenerate()
        {
            if (enabled == false || gameObject.activeInHierarchy == false)
                return;
            mGenerateNext = false;
            Clear();
            if (chart == null || legendItemPrefab == null)
                return;
            LegenedData inf = ((IInternalUse)chart).InternalLegendInfo;
            if (inf == null)
                return;
            var dic = CreateimageDictionary();
            foreach (LegenedData.LegenedItem item in inf.Items)
            {
                GameObject prefab = (GameObject)GameObject.Instantiate(legendItemPrefab.gameObject);
                prefab.transform.SetParent(transform, false);
                prefab.hideFlags = HideFlags.HideAndDontSave;
                foreach (Transform child in prefab.transform)
                    child.gameObject.hideFlags = HideFlags.HideAndDontSave;

                CanvasLegendItem legendItemData = prefab.GetComponent<CanvasLegendItem>();
                Texture2D overrideImage = null;

                if (dic.TryGetValue(item.Name, out overrideImage) == false)
                    overrideImage = null;

                if(overrideImage != null)
                {
                    legendItemData.Image.material = null;
                    Texture2D tex = overrideImage;
                    legendItemData.Image.sprite = CreateSpriteFromTexture(tex);
                }
                else
                    if (legendItemData.Image != null)
                    {
                        if (item.Material == null)
                            legendItemData.Image.material = null;
                        else
                        {
                            if (isGradientShader(item.Material))
                            {
                                legendItemData.Image.material = CreateCanvasGradient(item.Material);
                            }
                            else
                            {
                                legendItemData.Image.material = null;
                                Texture2D tex = item.Material.mainTexture as Texture2D;
                                if (tex != null)
                                    legendItemData.Image.sprite = CreateSpriteFromTexture(tex);
                                legendItemData.Image.color = item.Material.color;
                            }
                        }
                    }
                if (legendItemData.Text != null)
                {
                    legendItemData.Text.text = item.Name;
                    legendItemData.Text.fontSize = fontSize;
                }
            }
        }
    }
}
