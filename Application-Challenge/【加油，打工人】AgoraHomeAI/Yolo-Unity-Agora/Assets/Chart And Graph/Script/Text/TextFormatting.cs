#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// text formatting for an item label
    /// </summary>
    [Serializable]
    public class TextFormatting : IInternalSettings
    {

        [SerializeField]
        private string prefix = "";

        [SerializeField]
        private string suffix = "";

        [SerializeField]
        private string customFormat = "";

        public string Suffix
        {
            get { return suffix; }
            set
            {
                suffix = value;
                RaiseOnUpdate();
            }
        }

        public string Prefix
        {
            get { return prefix; }
            set
            {
                prefix = value;
                RaiseOnUpdate();
            }
        }

        public string CustomFormat
        {
            get { return customFormat; }
            set
            {
                customFormat = value;
                RaiseOnUpdate();
            }
        }

        private event EventHandler OnDataUpdate;
        private event EventHandler OnDataChanged;

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

        private string FormatKeywords(string str,string category,string group)
        {
            if (str.Contains("<?") == false && str.Contains("\\n") == false)
                return str;
            
            return str.Replace("<?category>", category).Replace("<?group>",group).Replace("\\n",Environment.NewLine);
        }
        private void FormatKeywords(StringBuilder builder, string category, string group)
        {
            builder.Replace("<?category>", category).Replace("<?group>", group).Replace("\\n", Environment.NewLine);
        }
        private string ValidString(string str)
        {
            if (str == null)
                return "";
            return str;
        }
        public void Format(StringBuilder builder,string data, string category, string group)
        {
            builder.Length = 0;
            builder.Append(ValidString(Prefix));
            builder.Append(data);
            builder.Append(ValidString(Suffix));
            FormatKeywords(builder, category, group);
        }
        public string Format(string data,string category,string group)
        {
            string tmp = ValidString(Prefix) + data + ValidString(Suffix);
            return FormatKeywords(tmp, category, group);
        }

    }
}
