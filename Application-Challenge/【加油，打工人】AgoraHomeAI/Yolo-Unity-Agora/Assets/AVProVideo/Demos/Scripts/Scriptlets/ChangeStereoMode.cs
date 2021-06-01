using UnityEngine;

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// Small demonstration showing how to toggle stereo mode
	/// </summary>
	public class ChangeStereoMode : MonoBehaviour 
	{
		public MediaPlayer _mediaPlayer;
		public ApplyToMesh _applyToMesh;

		private void Update() 
		{
			if (_mediaPlayer == null || _applyToMesh == null)
			{
				Debug.LogError("Fields cannot be null");
				return;
			}
			
			// Press 1 to disable stereo packing
			if (Input.GetKeyDown(KeyCode.Alpha1))
			{
				_mediaPlayer.m_StereoPacking = StereoPacking.None;
				_applyToMesh.ForceUpdate();
			}
			// Press 2 to enable left-right stereo packing
			else if (Input.GetKeyDown(KeyCode.Alpha2))
			{
				_mediaPlayer.m_StereoPacking = StereoPacking.LeftRight;
				_applyToMesh.ForceUpdate();
			}
		}
	}
}