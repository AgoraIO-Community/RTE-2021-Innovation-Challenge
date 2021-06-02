#define Graph_And_Chart_PRO
using SimpleJSON;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    class JsonParser : ChartParser
    {
        JSONNode mBaseJson;
        JSONNode mRelativePath;
        public JsonParser(string data)
        {

            mBaseJson = JSON.Parse(data);
            mRelativePath = mBaseJson;
        }

        object GetObjectFromRoot(JSONNode root, string name)
        {
            string[] parents = name.Split('>');
            object current = root;
            for (int i = 0; current != null && i < parents.Length; i++)
            {
                string nextNode = parents[i];
                current = GetChildObject(current, nextNode);
            }
            return current;
        }

        public override int GetArraySize(object arr)
        {
            var node = (JSONNode)arr;
            if (node.IsArray == false)
                return 0;
            return node.Count;
        }

        public override object GetChildObject(object obj, string name)
        {
            var node = (JSONNode)obj;
            if (name.Length <= 0)
                return obj;
            if (char.IsDigit(name[0]))  // if it is a number then find by order , atag name cannot start with a digit
            {
                if (node.IsArray == false)
                    return null;
                int index = 0;
                if (int.TryParse(name, out index) == false)  // try parsing the number
                    return null;
                if (index < 0 || index >= node.Count)
                    return null;
                return node[index];
            }
            if(name.Length>=2 && name[0] == '"' && name[name.Length-1] == '"')
            {
                name = name.Substring(1, name.Length - 2);  //strip quatation marks
            }
            return node[name];
        }


        public override bool SetPathRelativeTo(string pathObject)
        {
            mRelativePath = (JSONNode)GetObjectFromRoot(mBaseJson, pathObject);
            if (mRelativePath == null)
            {
                mRelativePath = mBaseJson;
                return false;
            }
            return true;
        }

        public override object GetObject(string name)
        {
            return GetObjectFromRoot(mRelativePath, name);
        }

        public override string GetItem(object arr, int item)
        {
            var element = arr as JSONNode;
            if (element == null)
                return null;
            var child = element[item] as JSONNode;
            if (child == null)
                return null;
            return ObjectValue(child);
        }

        public override object GetItemObject(object arr, int item)
        {
            var element = arr as JSONNode;
            if (element == null)
                return null;
            var child = element[item];
            return child;
        }

        public override string ObjectValue(object obj)
        {
            var element = obj as JSONNode;
            return element.Value;
        }

        public override string GetChildObjectValue(object obj, string name)
        {
            var element = obj as JSONNode;
            if (element == null)
                return null;
            try
            {
                var child = element[name] as JSONNode;
                return ObjectValue(child);
            }
            catch(Exception)
            {
              
            }
            return null;
        }

        public override IEnumerable<KeyValuePair<string, object>> GetAllChildObjects(object obj)
        {
            var node = (JSONNode)obj;
            if (node.IsObject == false)
                yield break;
            foreach(var key in node.Keys)
                yield return new KeyValuePair<string, object>(key, node[key]);
        }
    }
}
