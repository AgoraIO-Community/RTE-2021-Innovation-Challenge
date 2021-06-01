Shader "AVProVideo/Skybox/Sphere"
{
	Properties
	{
		_Tint ("Tint Color", Color) = (.5, .5, .5, .5)
		[Gamma] _Exposure ("Exposure", Range(0, 8)) = 1.0
		_Rotation ("Rotation", Range(0, 360)) = 0
		[NoScaleOffset] _MainTex ("MainTex (HDR)", 2D) = "grey" { }
		[NoScaleOffset] _ChromaTex ("Chroma", 2D) = "grey" { }
		[Toggle(APPLY_GAMMA)] _ApplyGamma("Apply Gamma", Float) = 0
		[Toggle(USE_YPCBCR)] _UseYpCbCr("Use YpCbCr", Float) = 0
	}

	SubShader
	{
		Tags { "Queue"="Background" "RenderType"="Background" "PreviewType"="Skybox" }
		Cull Off ZWrite Off

		CGINCLUDE
		#pragma multi_compile APPLY_GAMMA_OFF APPLY_GAMMA
		#pragma multi_compile USE_YPCBCR_OFF USE_YPCBCR
		#include "UnityCG.cginc"
		#include "AVProVideo.cginc"

		half4 _Tint;
		half _Exposure;
		float _Rotation;

		sampler2D _MainTex;
		float4 _MainTex_TexelSize;
		half4 _MainTex_HDR;
		float4 _MainTex_ST;
#if USE_YPCBCR
		sampler2D _ChromaTex;
		float4x4 _YpCbCrTransform;
#endif

		float3 RotateAroundYInDegrees (float3 vertex, float degrees)
		{
			const float CONST_PI = 3.14159265359f;
			float alpha = degrees * CONST_PI / 180.0;
			float sina, cosa;
			sincos(alpha, sina, cosa);
			float2x2 m = float2x2(cosa, -sina, sina, cosa);
			return float3(mul(m, vertex.xz), vertex.y).xzy;
		}

		inline float2 ToRadialCoords(float3 coords)
		{
			const float CONST_PI = 3.14159265359f;
			float3 normalizedCoords = normalize(coords);
			float latitude = acos(normalizedCoords.y);
			float longitude = atan2(normalizedCoords.z, normalizedCoords.x);
			float2 sphereCoords = float2(longitude, latitude) * float2(0.5/CONST_PI, 1.0/CONST_PI);
			float2 radial = float2(0.5,1.0) - sphereCoords;
			radial.x += 0.25;
			radial.x = fmod(radial.x, 1.0);
			return radial;
		}

		struct appdata_t {
			float4 vertex : POSITION;
			float2 texcoord : TEXCOORD0;
#ifdef UNITY_STEREO_INSTANCING_ENABLED
			UNITY_VERTEX_INPUT_INSTANCE_ID
#endif
		};
		
		struct v2f {
			float4 vertex : SV_POSITION;
			float3 texcoord : TEXCOORD0;
#ifdef UNITY_STEREO_INSTANCING_ENABLED
			UNITY_VERTEX_OUTPUT_STEREO
#endif
		};

		v2f sb_vert(appdata_t v)
		{
			v2f o;
#ifdef UNITY_STEREO_INSTANCING_ENABLED
			UNITY_SETUP_INSTANCE_ID(v);
			UNITY_INITIALIZE_VERTEX_OUTPUT_STEREO(o);
#endif
			float3 rotated = RotateAroundYInDegrees(v.vertex, _Rotation);
			o.vertex = XFormObjectToClip(float4(rotated, 0.0));
			o.texcoord = v.vertex.xyz;
			return o;
		}

		half4 frag(v2f i) : SV_Target
		{
			float2 tc = TRANSFORM_TEX(ToRadialCoords(i.texcoord), _MainTex);
			half4 tex;
#if USE_YPCBCR
			tex = SampleYpCbCr(_MainTex, _ChromaTex, tc, _YpCbCrTransform);
#else
			tex = SampleRGBA(_MainTex, tc);
#endif
			half3 c = tex;
			//c = DecodeHDR(tex, _MainTex_HDR);
			//c = c * _Tint.rgb;
			///c = c * unity_ColorSpaceDouble.rgb;
			c *= _Exposure;
			return half4(c, 1);
		}
		ENDCG

		Pass	// Pass
		{
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			#pragma target 2.0
			v2f vert(appdata_t v)
			{
				return sb_vert(v);
			}
			ENDCG
		}
		
	}
}
