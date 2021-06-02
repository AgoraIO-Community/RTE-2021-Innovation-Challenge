#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    class ChartEditorCommon
    {
        internal static bool IsAlphaNum(string str)
        {
            if (string.IsNullOrEmpty(str))
                return false;

            for (int i = 0; i < str.Length; i++)
            {
                if (!(char.IsLetter(str[i])) && (!(char.IsNumber(str[i]))) && str[i] != ' ')
                    return false;
            }

            return true;
        }
        internal static bool HasAttributeOfType(Type type, string fieldName, Type attributeType)
        {
            FieldInfo inf = type.GetField(fieldName, BindingFlags.Instance | BindingFlags.NonPublic);
            if (inf == null)
                return false;
            object[] attrb = inf.GetCustomAttributes(attributeType, true);
            if (attrb == null)
                return false;
            return attrb.Length > 0;
        }

    }
}
