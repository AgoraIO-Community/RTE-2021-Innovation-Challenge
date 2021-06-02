#if UNITY_5_4_OR_NEWER || (UNITY_5 && !UNITY_5_0 && !UNITY_5_1 && !UNITY_5_2 && !UNITY_5_3_0 && !UNITY_5_3_1 && !UNITY_5_3_2)
	#define UNITY_HAS_VRCLASS
#endif

using UnityEngine;

//-----------------------------------------------------------------------------
// Copyright 2015-2020 RenderHeads Ltd.  All rights reserved.
//-----------------------------------------------------------------------------

namespace RenderHeads.Media.AVProVideo.Demos
{
	/// <summary>
	/// A demo for playing back 360 video on a mesh, handles rotation of the main camera
	/// Supports rotation by VR device, gyroscope or mouse
	/// </summary>
	public class SphereDemo : MonoBehaviour
	{
		[SerializeField]
		private bool _zeroCameraPosition = true;

		#pragma warning disable 0414    // suppress value not used warning
		[SerializeField]
		private bool _allowRecenter = false;

		[SerializeField]
		private bool _allowVrToggle = false;

		[SerializeField]
		private bool _lockPitch = false;

		#pragma warning restore 0414    // restore value not used warning

		private float _spinX;
		private float _spinY;

		private void Start()
		{
#if UNITY_HAS_VRCLASS
			if (UnityEngine.XR.XRDevice.isPresent)
			{
				return;
			}
#endif
			if (SystemInfo.supportsGyroscope)
			{
				Input.gyro.enabled = true;
				if (this.transform.parent != null)
				{
					this.transform.parent.Rotate(new Vector3(90f, 0f, 0f));
				}
			}
		}

		private void OnDestroy()
		{
			if (SystemInfo.supportsGyroscope)
			{
				Input.gyro.enabled = false;
			}
		}

		void Update()
		{
#if UNITY_HAS_VRCLASS
			if (UnityEngine.XR.XRDevice.isPresent)
			{
				// Mouse click translates to gear VR touch to reset view
				if (_allowRecenter && (Input.GetMouseButtonDown(0) || Input.GetKeyDown(KeyCode.Space)))
				{
					#if UNITY_2019_3_OR_NEWER
					// TODO: should be using XRInputSubsystem.TryRecenter();
					#else
					UnityEngine.VR.InputTracking.Recenter();
					#endif
				}
#if UNITY_EDITOR || UNITY_STANDALONE_WIN
				if (_allowVrToggle && Input.GetKeyDown(KeyCode.V))
				{
					UnityEngine.XR.XRSettings.enabled = !UnityEngine.XR.XRSettings.enabled;
				}
#endif
			}
			else
#endif
			{
				if (SystemInfo.supportsGyroscope)
				{
					// Invert the z and w of the gyro attitude
					this.transform.localRotation = new Quaternion(Input.gyro.attitude.x, Input.gyro.attitude.y, -Input.gyro.attitude.z, -Input.gyro.attitude.w);
				}
				// Also rotate from mouse / touch input
				else 
				{
					if (Input.GetMouseButton(0))
					{
						float h = 40.0f * -Input.GetAxis("Mouse X") * Time.deltaTime;
						float v = 0f;
						if (!_lockPitch)
						{
							v = 40.0f * Input.GetAxis("Mouse Y") * Time.deltaTime;
						}						
						h = Mathf.Clamp(h, -0.5f, 0.5f);
						v = Mathf.Clamp(v, -0.5f, 0.5f);
						_spinX += h;
						_spinY += v;
					}
					if (!Mathf.Approximately(_spinX, 0f) || !Mathf.Approximately(_spinY, 0f))
					{
						this.transform.Rotate(Vector3.up, _spinX);
						this.transform.Rotate(Vector3.right, _spinY);

						_spinX = Mathf.MoveTowards(_spinX, 0f, 5f * Time.deltaTime);
						_spinY = Mathf.MoveTowards(_spinY, 0f, 5f * Time.deltaTime);
					}
				}
			}
		}

		void LateUpdate()
		{
			if (_zeroCameraPosition)
			{
				Camera.main.transform.position = Vector3.zero;
			}
		}
	}
}