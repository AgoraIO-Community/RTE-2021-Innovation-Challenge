#if !UNITY_WEBPLAYER
#if UNITY_STANDALONE_WIN || UNITY_STANDALONE_OSX || UNITY_EDITOR
	#define AVPRO_FILESYSTEM_SUPPORT
#endif
#endif
using UnityEngine;
using System.Collections;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// A demo that shows how to extract a series of frames from the video,
	/// copy them into textures, and then save them to disk as images
	/// </summary>
	public class FrameExtract : MonoBehaviour
	{
		private const int NumFrames = 8;
		public MediaPlayer _mediaPlayer;
		public bool _accurateSeek = false;
		public int _timeoutMs = 250;
		public GUISkin _skin;
		public bool _asyncExtract = false;

#if AVPRO_FILESYSTEM_SUPPORT
		public bool _saveToJPG = false;
		private string _filenamePrefix;
#endif
		private bool _busyProcessingFrame = false;
		private float _timeStepSeconds;
		private int _frameIndex = -1;
		private Texture2D _texture;
		private RenderTexture _displaySheet;

		void Start()
		{
			_mediaPlayer.Events.AddListener(OnMediaPlayerEvent);

			// Create a texture to draw the thumbnails on
			_displaySheet = RenderTexture.GetTemporary(Screen.width, Screen.height, 0);
			_displaySheet.useMipMap = false;
			#if UNITY_5_5_OR_NEWER
			_displaySheet.autoGenerateMips = false;
			#else
			_displaySheet.generateMips = false;
			#endif
			_displaySheet.antiAliasing = 1;
			_displaySheet.Create();

			// Clear the texture
			RenderTexture.active = _displaySheet;
			GL.Clear(false, true, Color.black, 0f);
			RenderTexture.active = null;
		}

		public void OnMediaPlayerEvent(MediaPlayer mp, MediaPlayerEvent.EventType et, ErrorCode errorCode)
		{
			switch (et)
			{
				case MediaPlayerEvent.EventType.MetaDataReady:
#if !UNITY_EDITOR && UNITY_ANDROID
					// Android platform doesn't display its first frame until poked
					mp.Play();
					mp.Pause();
#endif
					break;
				case MediaPlayerEvent.EventType.FirstFrameReady:
					OnNewMediaReady();
					break;
			}
		}

		private void OnNewMediaReady()
		{
			IMediaInfo info = _mediaPlayer.Info;

			// Create a texture the same resolution as our video
			if (_texture != null)
			{
				Texture2D.Destroy(_texture);
				_texture = null;
			}

			int textureWidth = info.GetVideoWidth();
			int textureHeight = info.GetVideoHeight();
#if UNITY_EDITOR_OSX || UNITY_STANDALONE_OSX || UNITY_IPHONE || UNITY_IOS || UNITY_TVOS
			Orientation ori = Helper.GetOrientation(_mediaPlayer.Info.GetTextureTransform());
			if (ori == Orientation.Portrait || ori == Orientation.PortraitFlipped)
			{
				textureWidth = info.GetVideoHeight();
				textureHeight = info.GetVideoWidth();
			}
#endif

			_texture = new Texture2D(textureWidth, textureHeight, TextureFormat.ARGB32, false);

			_timeStepSeconds = (_mediaPlayer.Info.GetDurationMs() / 1000f) / (float)NumFrames;

#if AVPRO_FILESYSTEM_SUPPORT
			_filenamePrefix = System.IO.Path.GetFileName(_mediaPlayer.m_VideoPath);
#endif
		}

		void OnDestroy()
		{
			if (_texture != null)
			{
				Texture2D.Destroy(_texture);
				_texture = null;
			}

			if (_displaySheet != null)
			{
				RenderTexture.ReleaseTemporary(_displaySheet);
				_displaySheet = null;
			}
		}

		void Update()
		{
			if (_texture != null && _frameIndex >=0 && _frameIndex < NumFrames)
			{
				ExtractNextFrame();
			}
		}

		private void ProcessExtractedFrame(Texture2D texture)
		{
#if AVPRO_FILESYSTEM_SUPPORT
			// Save frame to JPG
			if (_saveToJPG)
			{
				string filePath = _filenamePrefix + "-" + _frameIndex + ".jpg";
				Debug.Log("Writing frame to file: " + filePath);
				System.IO.File.WriteAllBytes(filePath, texture.EncodeToJPG());
			}
#endif

			// Draw frame to the thumbnail sheet texture
			GL.PushMatrix();
			RenderTexture.active = _displaySheet;
			GL.LoadPixelMatrix(0f, _displaySheet.width, _displaySheet.height, 0f);
			Rect sourceRect = new Rect(0f, 0f, 1f, 1f);

			float thumbSpace = 8f;
			float thumbWidth = ((float)_displaySheet.width / (float)NumFrames) - thumbSpace;
			float thumbHeight = thumbWidth / ((float)texture.width / (float)texture.height);
			float thumbPos = ((thumbWidth + thumbSpace) * (float)_frameIndex);

			Rect destRect = new Rect(thumbPos, (_displaySheet.height / 2f) - (thumbHeight / 2f), thumbWidth, thumbHeight);

			Graphics.DrawTexture(destRect, texture, sourceRect, 0, 0, 0, 0);
			RenderTexture.active = null;
			GL.PopMatrix();
			GL.InvalidateState();

			_busyProcessingFrame = false;
			_frameIndex++;
		}

		private void ExtractNextFrame()
		{
			// Extract the frame to Texture2D
			if (!_busyProcessingFrame)
			{
				_busyProcessingFrame = true;

				float timeSeconds = _frameIndex * _timeStepSeconds;

				if (!_asyncExtract)
				{
					_texture = _mediaPlayer.ExtractFrame(_texture, timeSeconds, _accurateSeek, _timeoutMs);
					ProcessExtractedFrame(_texture);
				}
				else
				{
					_mediaPlayer.ExtractFrameAsync(_texture, ProcessExtractedFrame, timeSeconds, _accurateSeek, _timeoutMs);
				}
			}
		}

		void OnGUI()
		{
			GUI.skin = _skin;

			if (_displaySheet != null)
			{
				GUI.DrawTexture(new Rect(0f, 0f, Screen.width, Screen.height), _displaySheet, ScaleMode.ScaleToFit, false);
			}

			float debugGuiScale = 4f * (Screen.height / 1080f);
			GUI.matrix = Matrix4x4.TRS(Vector3.zero, Quaternion.identity, new Vector3(debugGuiScale, debugGuiScale, 1.0f));

			GUILayout.Space(16f);
			GUILayout.BeginHorizontal(GUILayout.ExpandWidth(true), GUILayout.Width(Screen.width / debugGuiScale));
			GUILayout.FlexibleSpace();
			if (GUILayout.Button("Start Extracting Frames"))
			{
				_frameIndex = 0;
				RenderTexture.active = _displaySheet;
				GL.Clear(false, true, Color.black, 0f);
				RenderTexture.active = null;
			}
			GUILayout.FlexibleSpace();
			GUILayout.EndHorizontal();
		}
	}
}