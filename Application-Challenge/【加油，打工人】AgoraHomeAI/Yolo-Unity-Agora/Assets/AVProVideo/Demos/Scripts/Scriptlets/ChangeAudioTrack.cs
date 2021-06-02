using UnityEngine;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// Small demonstration showing how to script the changing of audio tracks
	/// </summary>
	public class ChangeAudioTrack : MonoBehaviour
	{
		public MediaPlayer _mediaPlayer;
		public int _trackIndex;
		private bool _isQueued;

		private void OnEnable()
		{
			_isQueued = true;
		}

		private void Update()
		{
			if (_isQueued && IsVideoLoaded())
			{
				DoChangeAudioTrack(_mediaPlayer, _trackIndex);
				_isQueued = false;
			}
		}

		private bool IsVideoLoaded()
		{
			return (_mediaPlayer != null && _mediaPlayer.Info != null && _mediaPlayer.Control.HasMetaData());
		}

		private static bool DoChangeAudioTrack(MediaPlayer mp, int index)
		{
			bool result = false;
			int numAudioTracks = mp.Info.GetAudioTrackCount();
			if (index >= 0 && index < numAudioTracks)
			{
				mp.Control.SetAudioTrack(index);
				result = true;
			}
			else
			{
				Debug.LogWarning("[AVProVideo] Audio track index is out of range: " + index);
			}
			return result;
		}
	}
}