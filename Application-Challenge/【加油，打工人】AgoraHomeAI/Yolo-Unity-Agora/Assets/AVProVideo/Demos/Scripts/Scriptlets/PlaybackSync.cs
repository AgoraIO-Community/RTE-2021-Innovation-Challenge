using UnityEngine;
using RenderHeads.Media.AVProVideo;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// Small demonstration of how you might synchronise multiple video playback
	/// Ideally the videos should be encoded with as many key-frames (I-frames) as possible
	/// </summary>
	public class PlaybackSync : MonoBehaviour
	{
		private enum State
		{
			Loading,
			Playing,
			Finished,
		}

		public MediaPlayer _masterPlayer;
		public MediaPlayer[] _slavePlayers;
		public float _toleranceMs = 30f;
		public bool _matchVideo = true;
		public bool _muteSlaves = true;
		private State _state = State.Loading;

		void Start()
		{
			// Setup the slaves
			for (int i = 0; i < _slavePlayers.Length; i++)
			{
				_slavePlayers[i].m_Muted = true;
				if (_matchVideo)
				{
					_slavePlayers[i].OpenVideoFromFile(_masterPlayer.m_VideoLocation, _masterPlayer.m_VideoPath, false);
				}
			}
		}

		void LateUpdate()
		{
			if (_state == State.Loading)
			{
				// Finished loading?
				if (IsAllVideosLoaded())
				{
					// Play the videos
					_masterPlayer.Play();
					for (int i = 0; i < _slavePlayers.Length; i++)
					{
						_slavePlayers[i].Play();
					}
					_state = State.Playing;
				}
			}

			if (_state == State.Finished)
			{
				Debug.Log("Do Something");
			}
			else if (_state == State.Playing)
			{
				if (_masterPlayer.Control.IsPlaying())
				{
					// Keep the slaves synced
					float masterTime = _masterPlayer.Control.GetCurrentTimeMs();
					for (int i = 0; i < _slavePlayers.Length; i++)
					{
						MediaPlayer slave = _slavePlayers[i];
						float slaveTime = slave.Control.GetCurrentTimeMs();
						float deltaTime = Mathf.Abs(masterTime - slaveTime);
						if (deltaTime > _toleranceMs)
						{
							slave.Control.SeekFast(masterTime + (_toleranceMs * 0.5f)); // Add a bit to allow for the delay in playback start
							if (slave.Control.IsPaused())
							{
								slave.Play();
							}
						}

						// TODO: add per-frame micropauses to get videos in sync.  This can be done with a Pause() in Update() and a Play() in LateUpdate()
						// to slowly shift slave videos that are ahead back in sync.
					}
				}
				else
				{
					// Pause slaves
					for (int i = 0; i < _slavePlayers.Length; i++)
					{
						MediaPlayer slave = _slavePlayers[i];
						slave.Pause();
					}
				}

				// Finished?
				if (IsPlaybackFinished(_masterPlayer))
				{
					_state = State.Finished;
				}
			}
		}

		private bool IsAllVideosLoaded()
		{
			bool result = false;
			if (IsVideoLoaded(_masterPlayer))
			{
				result = true;
				for (int i = 0; i < _slavePlayers.Length; i++)
				{
					if (!IsVideoLoaded(_slavePlayers[i]))
					{
						result = false;
						break;
					}
				}
			}
			return result;
		}

		private static bool IsVideoLoaded(MediaPlayer player)
		{
			return (player != null && player.Control != null && player.Control.HasMetaData() && player.Control.CanPlay() && player.TextureProducer.GetTextureFrameCount() > 0);
		}

		private static bool IsPlaybackFinished(MediaPlayer player)
		{
			bool result = false;
			if (player != null && player.Control != null)
			{
				if (player.Control.IsFinished())
				{
					result = true;
				}
			}
			return result;
		}
	}
}