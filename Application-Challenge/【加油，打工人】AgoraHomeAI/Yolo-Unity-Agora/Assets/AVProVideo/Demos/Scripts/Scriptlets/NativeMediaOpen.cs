using UnityEngine;
using System;
using RenderHeads.Media.AVProVideo;

#if NETFX_CORE
using Windows.Storage;
using Windows.Storage.Pickers;
using Windows.Storage.Streams;
using System.Threading.Tasks;
#endif

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// Demonstration of how to use StorageFiles with AVProVideo in UWP builds
	/// The code is put behind NETFX_CORE macros as it is only valid in UWP
	/// This example loads a video picked by the user after clicking the Pick Video File button,
	/// and directly from the CameraRoll with the right app capabilities set
	/// </summary>
	public class NativeMediaOpen : MonoBehaviour
	{
		[SerializeField]
		private MediaPlayer _player = null;

#if NETFX_CORE
		private FileOpenPicker _picker;
		private IRandomAccessStreamWithContentType _fileStream;
		private string _fileName;
#endif

		private void Start()
		{
#if NETFX_CORE
			_picker = new FileOpenPicker();
			_picker.ViewMode = PickerViewMode.Thumbnail;
			_picker.SuggestedStartLocation = PickerLocationId.VideosLibrary;
			_picker.FileTypeFilter.Add(".mp4");
			_picker.FileTypeFilter.Add(".m4v");
			_picker.FileTypeFilter.Add(".mkv");
			_picker.FileTypeFilter.Add(".avi");
			_picker.FileTypeFilter.Add(".mp3");
			_picker.FileTypeFilter.Add(".aac");
#endif
		}

#if NETFX_CORE
		private void StartFilePicker()
		{
			// Loads file on UI thread (note you can also use PickSingleFileAndContinue for better compatibility)
			UnityEngine.WSA.Application.InvokeOnUIThread(async () => 
			{
				StorageFile file = await _picker.PickSingleFileAsync();
				if (file != null)
				{
					await LoadFile(file);
				}
			}, true);
		}

		private void StartDirectLoad(StorageFolder folder, string relativeFilePath)
		{
			UnityEngine.WSA.Application.InvokeOnUIThread(async () =>
			{
				// NOTE: In order to access Camera Roll you need to set permission in the capabilities manifest
				StorageFolder f = KnownFolders.CameraRoll;
				// TODO: catch file not found exceptions
				StorageFile file = await folder.GetFileAsync(relativeFilePath);
				if (file != null)
				{
					await LoadFile(file);
				}
			}, true);
		}

		private async Task<bool> LoadFile(StorageFile file)
		{
			if (file != null)
			{
				_fileStream = await file.OpenReadAsync();
				if (_fileStream != null)
				{
					_fileName = file.Name;
				}
			}
			return true;
		}
#endif

		private void Update()
		{
#if NETFX_CORE
			// The actual loading of the video must happen in the Unity thread, so we do it here
			if (_fileStream != null)
			{
				_player.OpenVideoFromStream(_fileStream, _fileName, true);
				_fileStream = null;
			}
#endif
		}

		private void OnGUI()
		{
			if (GUILayout.Button("Pick Video File"))
			{
#if NETFX_CORE
				StartFilePicker();
#endif
			}
			if (GUILayout.Button("Load 'myvideo.mp4' From Camera Roll"))
			{
#if NETFX_CORE
				StartDirectLoad(KnownFolders.CameraRoll, "myvideo.mp4");
#endif
			}

			if (_player != null && !string.IsNullOrEmpty(_player.m_VideoPath))
			{
				GUILayout.Label("Currently Playing: " + _player.m_VideoPath);
			}
		}
	}
}