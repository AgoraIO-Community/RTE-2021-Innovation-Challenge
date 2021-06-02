using UnityEngine;
using RenderHeads.Media.AVProVideo;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// Causes a video to play when the trigger collider is entered and rewind+pause when it is exited
	/// Audio is faded up and down too
	/// </summary>
	public class VideoTrigger : MonoBehaviour
	{
		[SerializeField]
		private MediaPlayer _mediaPlayer = null;

		[SerializeField]
		private float _fadeTimeMs = 500f;

		private float _fade;
		private float _fadeDirection;

		private void OnTriggerEnter(Collider other)
		{
			if (_mediaPlayer != null)
			{
				_mediaPlayer.Play();
				_fadeDirection = 1f;
			}
		}

		private void OnTriggerExit(Collider other)
		{
			if (_mediaPlayer != null)
			{
				_fadeDirection = -1f;
			}
		}

		private void Update()
		{
			if (_fadeDirection != 0f)
			{
				// Fade the value
				float speed = 1000 / _fadeTimeMs;
				_fade += Time.deltaTime * _fadeDirection * speed;

				if (_fade <= 0f)
				{
					// Complete the fade down
					_mediaPlayer.Rewind(true);
					_fadeDirection = 0f;
				}
				else if (_fade >= 1f)
				{
					// Complete the fade up
					_fadeDirection = 0f;
				}

				_fade = Mathf.Clamp01(_fade);

				// Set the volume
				if (_mediaPlayer != null && _mediaPlayer.Control != null)
				{
					_mediaPlayer.Control.SetVolume(_fade);
				}
			}
		}
	}
}