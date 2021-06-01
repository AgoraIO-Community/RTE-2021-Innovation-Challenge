Shader "AVProVideo/Background/Full Screen"
{
	Properties
	{
		_MainTex ("Texture", 2D) = "black" {}
		_ChromaTex ("Chroma", 2D) = "gray" {}
		_Color("Main Color", Color) = (1,1,1,1)
		[Toggle(APPLY_GAMMA)] _ApplyGamma("Apply Gamma", Float) = 0
		[Toggle(USE_YPCBCR)] _UseYpCbCr("Use YpCbCr", Float) = 0
	}
	SubShader
	{
		Tags { "Queue" = "Background" "RenderType"="Opaque" }
		LOD 100
		Cull Off
		ZWrite Off
		ZTest Always
		Lighting Off

		Pass
		{
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag

			// TODO: Change XX_OFF to __ for Unity 5.0 and above
			// this was just added for Unity 4.x compatibility as __ causes
			// Android and iOS builds to fail the shader
			#pragma multi_compile APPLY_GAMMA_OFF APPLY_GAMMA
			#pragma multi_compile USE_YPCBCR_OFF USE_YPCBCR

			#include "UnityCG.cginc"
			#include "AVProVideo.cginc"

			struct v2f
			{
				float4 vertex : SV_POSITION;
				float2 uv : TEXCOORD0;
			};

			uniform sampler2D _MainTex;
#if USE_YPCBCR
			uniform sampler2D _ChromaTex;
			uniform float4x4 _YpCbCrTransform;
#endif
			uniform float4 _MainTex_ST;
			uniform float4 _MainTex_TexelSize;
			uniform fixed4 _Color;

			v2f vert(appdata_img v)
			{
				v2f o;

				float2 scale = ScaleZoomToFit(_ScreenParams.x, _ScreenParams.y, _MainTex_TexelSize.z, _MainTex_TexelSize.w);
				float2 pos = ((v.vertex.xy) * scale * 2.0);		

				// we're rendering with upside-down flipped projection,
				// so flip the vertical UV coordinate too
				if (_ProjectionParams.x < 0.0)
				{
					pos.y = (1.0 - pos.y) - 1.0;
				}

				o.vertex = float4(pos.xy, UNITY_NEAR_CLIP_VALUE, 1.0);

				o.uv = TRANSFORM_TEX(v.texcoord, _MainTex);
				
				return o;
			}
			
			fixed4 frag (v2f i) : SV_Target
			{
				fixed4 col;
#if USE_YPCBCR
				col = SampleYpCbCr(_MainTex, _ChromaTex, i.uv, _YpCbCrTransform);
#else
				col = SampleRGBA(_MainTex, i.uv);
#endif
				col *= _Color;
				return fixed4(col.rgb, 1.0);
			}
			ENDCG
		}
	}
}
