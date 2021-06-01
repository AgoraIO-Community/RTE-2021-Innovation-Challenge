using UnityEngine;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// This component just stores description text related to a specific demo scene 
	/// The MediaPlayer in the scene can also be referenced to allow for scene change 
	/// fade-from-black to only start once the video has loaded and has a valid frame
	/// </summary>
	public class DemoInfo : MonoBehaviour 
	{
		public string _title;

		[Multiline]
		public string _description;
		public MediaPlayer	_media;
	}
}