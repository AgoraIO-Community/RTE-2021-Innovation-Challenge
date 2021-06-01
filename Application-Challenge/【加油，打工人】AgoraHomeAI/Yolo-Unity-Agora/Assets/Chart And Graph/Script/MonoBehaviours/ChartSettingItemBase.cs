#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// base class for all chart setting items
    /// </summary>
    [RequireComponent(typeof(AnyChart))]
    [ExecuteInEditMode]
    public abstract class ChartSettingItemBase : MonoBehaviour,IInternalSettings
    {

        private event EventHandler OnDataUpdate;
        private event EventHandler OnDataChanged;

        protected abstract Action<IInternalUse, bool> Assign { get; }
        AnyChart mChart;

        private AnyChart SafeChart
        {
            get
            {
                if (mChart == null)
                    mChart = GetComponent<AnyChart>();
                return mChart;
            }
        }

        protected void AddInnerItem(IInternalSettings item)
        {
            item.InternalOnDataChanged += Item_InternalOnDataChanged;
            item.InternalOnDataUpdate += Item_InternalOnDataUpdate;
        }

        private void Item_InternalOnDataUpdate(object sender, EventArgs e)
        {
            RaiseOnUpdate();
        }

        private void Item_InternalOnDataChanged(object sender, EventArgs e)
        {
            RaiseOnChanged();
        }

        protected virtual void RaiseOnChanged()
        {
            if (OnDataChanged != null)
                OnDataChanged(this, EventArgs.Empty);
        }

        protected virtual void RaiseOnUpdate()
        {
            if (OnDataUpdate != null)
                OnDataUpdate(this, EventArgs.Empty);
        }

        private void SafeAssign(bool clear)
        {
            AnyChart chart = SafeChart;
            if (chart != null)
                Assign(chart, clear);
        }

        protected virtual void OnEnable()
        {
            SafeAssign(false);
        }

        protected virtual void OnDisable()
        {
            SafeAssign(true);
        }

        protected virtual void OnDestroy()
        {
            SafeAssign(true);
        }

        protected virtual void OnValidate()
        {
            AnyChart chart = SafeChart;
            if (chart != null)
                ((IInternalUse)chart).CallOnValidate();
        }

        protected virtual void Start()
        {
            SafeAssign(false);
        }

        #region Intenal Use
        event EventHandler IInternalSettings.InternalOnDataUpdate
        {
            add
            {
                OnDataUpdate += value;
            }

            remove
            {
                OnDataUpdate -= value;
            }
        }

        event EventHandler IInternalSettings.InternalOnDataChanged
        {
            add
            {
                OnDataChanged += value;
            }
            remove
            {
                OnDataChanged -= value;
            }
        }
        #endregion
    }
}
