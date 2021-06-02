using UnityEngine;
using UnityEditor;
using System.Collections.Generic;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Editor
{
	/// <summary>
	/// Editor for the ApplyToMaterial component
	/// </summary>
	[CanEditMultipleObjects]
	[CustomEditor(typeof(ApplyToMaterial))]
	public class ApplyToMaterialEditor : UnityEditor.Editor
	{
		private static readonly GUIContent _guiTextTextureProperty = new GUIContent("Texture Property");

		private SerializedProperty _propTextureOffset;
		private SerializedProperty _propTextureScale;
		private SerializedProperty _propMediaPlayer;
		private SerializedProperty _propMaterial;
		private SerializedProperty _propTexturePropertyName;
		private SerializedProperty _propDefaultTexture;
		private GUIContent[] _materialTextureProperties = new GUIContent[0];

		void OnEnable()
		{
			_propTextureOffset = serializedObject.FindProperty("_offset");
			_propTextureScale = serializedObject.FindProperty("_scale");
			_propMediaPlayer = serializedObject.FindProperty("_media");
			_propMaterial = serializedObject.FindProperty("_material");
			_propTexturePropertyName = serializedObject.FindProperty("_texturePropertyName");
			_propDefaultTexture = serializedObject.FindProperty("_defaultTexture");
		}

		public override void OnInspectorGUI()
		{
			serializedObject.Update();

			if (_propMaterial == null)
			{
				return;
			}

			EditorGUI.BeginChangeCheck();

			EditorGUILayout.PropertyField(_propMediaPlayer);
			EditorGUILayout.PropertyField(_propDefaultTexture);
			EditorGUILayout.PropertyField(_propMaterial);

			bool hasKeywords = false;
			int texturePropertyIndex = 0;
			if (_propMaterial.objectReferenceValue != null)
			{
				Material mat = (Material)(_propMaterial.objectReferenceValue);

				if (mat.shaderKeywords.Length > 0)
				{
					hasKeywords = true;
				}

				MaterialProperty[] matProps = MaterialEditor.GetMaterialProperties(new UnityEngine.Object[] { mat });

				List<GUIContent> items = new List<GUIContent>(16);
				foreach (MaterialProperty matProp in matProps)
				{
					if (matProp.type == MaterialProperty.PropType.Texture)
					{
						if (matProp.name == _propTexturePropertyName.stringValue)
						{
							texturePropertyIndex = items.Count;
						}
						items.Add(new GUIContent(matProp.name));
					}
				}
				_materialTextureProperties = items.ToArray();
			}

			int newTexturePropertyIndex = EditorGUILayout.Popup(_guiTextTextureProperty, texturePropertyIndex, _materialTextureProperties);
			if (newTexturePropertyIndex >= 0 && newTexturePropertyIndex < _materialTextureProperties.Length)
			{
				_propTexturePropertyName.stringValue = _materialTextureProperties[newTexturePropertyIndex].text;
			}

			if (hasKeywords && _propTexturePropertyName.stringValue != "_MainTex")
			{
				EditorGUILayout.HelpBox("When using an uber shader you may need to enable the keywords on a material for certain texture slots to take effect.  You can sometimes achieve this (eg with Standard shader) by putting a dummy texture into the texture slot.", MessageType.Info);
			}

			EditorGUILayout.PropertyField(_propTextureOffset);
			EditorGUILayout.PropertyField(_propTextureScale);

			serializedObject.ApplyModifiedProperties();

			bool wasModified = EditorGUI.EndChangeCheck();

			if (Application.isPlaying && wasModified)
			{
				foreach (Object obj in this.targets)
				{
					((ApplyToMaterial)obj).ForceUpdate();
				}
			}
		}
	}
}