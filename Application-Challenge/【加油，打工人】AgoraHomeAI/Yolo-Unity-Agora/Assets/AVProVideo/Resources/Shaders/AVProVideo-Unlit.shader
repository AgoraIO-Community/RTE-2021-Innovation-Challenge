Shader "AVProVideo/Unlit/Opaque (texture+color+fog+stereo support)"
{
	Properties
	{
		_MainTex ("Base (RGB)", 2D) = "black" {}
		_ChromaTex ("Chroma", 2D) = "gray" {}
		_Color("Main Color", Color) = (1,1,1,1)

		[KeywordEnum(None, Top_Bottom, Left_Right, Custom_UV)] Stereo("Stereo Mode", Float) = 0
		[Toggle(STEREO_DEBUG)] _StereoDebug("Stereo Debug Tinting", Float) = 0
		[Toggle(APPLY_GAMMA)] _ApplyGamma("Apply Gamma", Float) = 0
		[Toggle(USE_YPCBCR)] _UseYpCbCr("Use YpCbCr", Float) = 0
	}
	SubShader
	{
		Tags { "RenderType"="Opaque" "IgnoreProjector"="False" "Queue"="Geometry" }
		LOD 100
		Lighting Off
		Cull Off

		Pass
		{
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			#pragma multi_compile_fog
			#pragma multi_compile MONOSCOPIC STEREO_TOP_BOTTOM STEREO_LEFT_RIGHT STEREO_CUSTOM_UV

			// TODO: Change XX_OFF to __ for Unity 5.0 and above
			// this was just added for Unity 4.x compatibility as __ causes
			// Android and iOS builds to fail the shader
			#pragma multi_compile STEREO_DEBUG_OFF STEREO_DEBUG
			#pragma multi_compile APPLY_GAMMA_OFF APPLY_GAMMA
			#pragma multi_compile USE_YPCBCR_OFF USE_YPCBCR

			#include "UnityCG.cginc"
			#include "AVProVideo.cginc"

			struct appdata
			{
				float4 vertex : POSITION;
				float2 uv : TEXCOORD0;
#if STEREO_CUSTOM_UV
				float2 uv2 : TEXCOORD1;	// Custom uv set for right eye (left eye is in TEXCOORD0)
#endif
			};

			struct v2f
			{
				float4 vertex : SV_POSITION;
				float2 uv : TEXCOORD0;
#if UNITY_VERSION >= 500
				UNITY_FOG_COORDS(1)
#endif
#if STEREO_DEBUG
				float4 tint : COLOR;
#endif
			};

			uniform sampler2D _MainTex;
#if USE_YPCBCR
			uniform sampler2D _ChromaTex;
			uniform float4x4 _YpCbCrTransform;
#endif
			uniform float4 _MainTex_ST;
			uniform fixed4 _Color;
			uniform float3 _cameraPosition;

			v2f vert (appdata v)
			{
				v2f o;

				o.vertex = XFormObjectToClip(v.vertex);
				o.uv.xy = TRANSFORM_TEX(v.uv, _MainTex);

#if STEREO_TOP_BOTTOM | STEREO_LEFT_RIGHT
				float4 scaleOffset = GetStereoScaleOffset(IsStereoEyeLeft(_cameraPosition, UNITY_MATRIX_V[0].xyz), _MainTex_ST.y < 0.0);
				o.uv.xy *= scaleOffset.xy;
				o.uv.xy += scaleOffset.zw;
#elif STEREO_CUSTOM_UV
				if (!IsStereoEyeLeft(_cameraPosition, UNITY_MATRIX_V[0].xyz))
				{
					o.uv.xy = TRANSFORM_TEX(v.uv2, _MainTex);
				}
#endif

#if STEREO_DEBUG
				o.tint = GetStereoDebugTint(IsStereoEyeLeft(_cameraPosition, UNITY_MATRIX_V[0].xyz));
#endif

#if UNITY_VERSION >= 500
				UNITY_TRANSFER_FOG(o, o.vertex);
#endif

				return o;
			}
			
			fixed4 frag (v2f i) : SV_Target
			{
				fixed4 col;
#if USE_YPCBCR
				col = SampleYpCbCr(_MainTex, _ChromaTex, i.uv.xy, _YpCbCrTransform);
#else
				col = SampleRGBA(_MainTex, i.uv.xy);
#endif
				col *= _Color;
#if STEREO_DEBUG
				col *= i.tint;
#endif				

#if UNITY_VERSION >= 500
				UNITY_APPLY_FOG(i.fogCoord, col);
#endif

				return col;
			}
			ENDCG
		}
	}
}
