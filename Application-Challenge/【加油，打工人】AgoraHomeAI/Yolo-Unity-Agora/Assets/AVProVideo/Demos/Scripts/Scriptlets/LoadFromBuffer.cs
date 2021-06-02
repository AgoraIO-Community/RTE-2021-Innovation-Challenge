using UnityEngine;
using System.IO;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// Demonstration of how to load from a video from a byte array.
	/// NOTE: Only Windows using DirectShow API currently supports this feature.
	/// </summary> 
	public class LoadFromBuffer : MonoBehaviour
	{
#if !UNITY_WEBPLAYER
		[SerializeField]
		private MediaPlayer _mp = null;

		[SerializeField]
		private string _filename = string.Empty;

		void Start()
		{
			if (_mp != null)
			{
				byte[] buffer = File.ReadAllBytes(_filename);

				if (buffer != null)
				{
					_mp.OpenVideoFromBuffer(buffer);
				}
			}

			System.GC.Collect();
		}
#endif
	}
}

