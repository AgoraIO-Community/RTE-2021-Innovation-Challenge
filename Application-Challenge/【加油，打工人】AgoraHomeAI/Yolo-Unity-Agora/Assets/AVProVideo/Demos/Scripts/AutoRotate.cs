using UnityEngine;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// Rotates a transform, randomly changing direction and speed every few seconds
	/// </summary>
	[RequireComponent(typeof(Transform))]
	public class AutoRotate : MonoBehaviour
	{
		private float x, y, z;
		private float _timer;

		private void Awake()
		{
			Randomise();
		}

		private void Update()
		{
			this.transform.Rotate(x * Time.deltaTime, y * Time.deltaTime, z * Time.deltaTime);
			_timer -= Time.deltaTime;
			if (_timer <= 0f)
			{
				Randomise();
			}
		}

		private void Randomise()
		{
			float s = 32f;
			x = Random.Range(-s, s);
			y = Random.Range(-s, s);
			z = Random.Range(-s, s);
			_timer = Random.Range(5f, 10f);
		}
	}
}