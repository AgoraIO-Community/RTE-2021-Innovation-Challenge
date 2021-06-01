#if UNITY_4_6 || UNITY_4_7 || UNITY_4_8 || UNITY_5 || UNITY_5_4_OR_NEWER
	#define UNITY_FEATURE_UGUI
#endif

using UnityEngine;
#if UNITY_FEATURE_UGUI
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// A demo that shows multiple videos being loaded and displayed at once
	/// Each time a button is pressed a new video instance is added/removed
	/// </summary>
	public class SampleApp_Multiple : MonoBehaviour
	{
		[SerializeField]
		private string m_videoPath = "BigBuckBunny_360p30.mp4";

		[SerializeField]
		private MediaPlayer.FileLocation m_videoLocation = MediaPlayer.FileLocation.RelativeToStreamingAssetsFolder;

		private int	m_NumVideosAdded = 0;
		private List<DisplayUGUI>	m_aAddedVideos = new List<DisplayUGUI>();
		

		private void Update()
		{
#if UNITY_ANDROID
			// To handle 'back' button on Android devices
			if (Input.GetKeyDown(KeyCode.Escape))
			{
				Application.Quit();
			}
#endif

			foreach( DisplayUGUI gui in m_aAddedVideos )
			{
				if( gui.gameObject != null && gui.gameObject.activeSelf == false && 
					gui._mediaPlayer != null && gui._mediaPlayer.Control != null &&
					gui._mediaPlayer.TextureProducer.GetTexture() != null )
				{
					gui.gameObject.SetActive( true );
				}
			}
		}

		private void UpdateVideosLayout()
		{
			GameObject canvasPanel = GameObject.Find("Canvas/Panel");
			RectTransform canvasRectTransform = ( canvasPanel ) ? canvasPanel.GetComponent<RectTransform>() : null;
			if( canvasRectTransform )
			{
				Vector2 canvasSize = canvasRectTransform.sizeDelta;
				Vector2 halfCanvasSize = new Vector2( canvasSize.x * 0.5f, canvasSize.y * 0.5f );

				int iNumMovies = m_aAddedVideos.Count;
				int iNumColRows = Mathf.CeilToInt (Mathf.Sqrt( iNumMovies ));

				float fWidth = (1.0f / iNumColRows) * canvasSize.x;
				float fHeight = (1.0f / iNumColRows) * canvasSize.y;

				for( int i = 0; i < iNumMovies; ++i )
				{
					DisplayUGUI gui = m_aAddedVideos[ i ];

					int x = i % iNumColRows;
					int y = i / iNumColRows;

					gui.rectTransform.anchoredPosition = new Vector2( ((fWidth * x) - halfCanvasSize.x), ((fHeight * -y) + halfCanvasSize.y) );
					gui.rectTransform.sizeDelta = new Vector2( fWidth, fHeight );
				}
			}
		}
		
		public void AddVideoClicked()
		{
			++m_NumVideosAdded;

			// New media player
			GameObject newMediaPlayerGameObject = new GameObject( "AVPro MediaPlayer " + m_NumVideosAdded.ToString() );
			MediaPlayer newMediaPlayer = newMediaPlayerGameObject.AddComponent<MediaPlayer>();
			newMediaPlayer.m_Loop = true;
			newMediaPlayer.OpenVideoFromFile(m_videoLocation, m_videoPath, true);

			// New uGUI object
			GameObject canvasPanel = GameObject.Find("Canvas/Panel");
			if( canvasPanel != null )
			{
				// New game object, and make it a child of the canvas panel
				GameObject newGuiGameObject = new GameObject( "AVPro Video uGUI " + m_NumVideosAdded.ToString() );
				newGuiGameObject.transform.parent = canvasPanel.transform;
				newGuiGameObject.SetActive( false );

				// Add the components
				newGuiGameObject.AddComponent<RectTransform>();
				newGuiGameObject.AddComponent<CanvasRenderer>();

				// Add the DisplayUGUI;
				DisplayUGUI newGuiObject = newGuiGameObject.AddComponent<DisplayUGUI>();
				newGuiObject._mediaPlayer = newMediaPlayer;
				newGuiObject._scaleMode = ScaleMode.StretchToFill;
				newGuiObject.rectTransform.localScale = Vector3.one; 
				newGuiObject.rectTransform.pivot = new Vector2( 0.0f, 1.0f );
				m_aAddedVideos.Add( newGuiObject );

				// Update layout
				UpdateVideosLayout();
			}
		}

		public void RemoveVideoClicked()
		{
			if (m_aAddedVideos.Count > 0)
			{
				// Pick a random element to remove
				int index = Random.Range(0, m_aAddedVideos.Count);
				DisplayUGUI gui = m_aAddedVideos[index];

				// Stop and destroy the MediaPlayer
				if (gui._mediaPlayer != null)
				{
					gui._mediaPlayer.CloseVideo();
					GameObject.Destroy(gui._mediaPlayer.gameObject);
					gui._mediaPlayer = null;
					
				}
				// Destroy the Display object
				GameObject.Destroy(gui.gameObject);

				// Update variables
				m_aAddedVideos.RemoveAt(index);
				m_NumVideosAdded--;
			}
		}

		void OnDestroy()
		{
			foreach( DisplayUGUI gui in m_aAddedVideos )
			{
				if( gui._mediaPlayer )
				{
					gui._mediaPlayer = null;
				}
			}
			m_aAddedVideos.Clear();
		}
	}
}
#endif