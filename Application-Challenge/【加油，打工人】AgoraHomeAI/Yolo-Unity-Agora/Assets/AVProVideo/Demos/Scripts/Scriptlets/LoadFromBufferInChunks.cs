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
	public class LoadFromBufferInChunks : MonoBehaviour
	{
		[SerializeField]
		private MediaPlayer _mp = null;

		[SerializeField]
		private string _filename = string.Empty;

		private void Start()
		{
			if (_mp != null)
			{
				int chunkSize = 512;
				byte[] chunk = new byte[chunkSize];
				FileStream fs = File.OpenRead(_filename);
				long fileSize = fs.Length;
				if (!_mp.StartOpenChunkedVideoFromBuffer((ulong)fileSize))
				{
					_mp.CloseVideo();
				}

				ulong currOffset = 0;
				while (true)
				{
					int bytesRead = fs.Read(chunk, 0, chunkSize);
					if (bytesRead > 0)
					{
						if (!_mp.AddChunkToVideoBuffer(chunk, currOffset, (ulong)bytesRead))
						{
							_mp.CloseVideo();
						}
					}
					else break;

					currOffset += (ulong)bytesRead;
				}

				if (!_mp.EndOpenChunkedVideoFromBuffer())
				{
					_mp.CloseVideo();
				}
			}

			System.GC.Collect();
		}
	}
}

