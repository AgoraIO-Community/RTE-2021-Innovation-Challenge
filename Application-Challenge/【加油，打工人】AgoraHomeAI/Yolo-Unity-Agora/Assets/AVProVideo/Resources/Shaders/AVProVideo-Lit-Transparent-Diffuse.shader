Shader "AVProVideo/Lit/Transparent Diffuse (texture+color+fog+packed alpha)" 
{
	Properties
	{
		_Color("Main Color", Color) = (1,1,1,1)
		_MainTex("Base (RGB)", 2D) = "black" {}
		_ChromaTex("Chroma", 2D) = "black" {}

		[KeywordEnum(None, Top_Bottom, Left_Right)] AlphaPack("Alpha Pack", Float) = 0
		[Toggle(APPLY_GAMMA)] _ApplyGamma("Apply Gamma", Float) = 0
		[Toggle(USE_YPCBCR)] _UseYpCbCr("Use YpCbCr", Float) = 0
	}

	SubShader
	{
		Tags { "Queue"="Transparent" "IgnoreProjector"="True" "RenderType"="Transparent" }
		LOD 200
		ZWrite Off
		Blend SrcAlpha OneMinusSrcAlpha
		Cull Off

		CGPROGRAM
		#pragma surface surf Lambert vertex:VertexFunction alpha
		#pragma multi_compile ALPHAPACK_NONE ALPHAPACK_TOP_BOTTOM ALPHAPACK_LEFT_RIGHT

		// TODO: Change XX_OFF to __ for Unity 5.0 and above
		// this was just added for Unity 4.x compatibility as __ causes
		// Android and iOS builds to fail the shader
		#pragma multi_compile APPLY_GAMMA_OFF APPLY_GAMMA
		#pragma multi_compile USE_YPCBCR_OFF USE_YPCBCR

		#include "AVProVideo.cginc"

		uniform sampler2D _MainTex;
		uniform float4 _MainTex_ST;
		uniform float4 _MainTex_TexelSize;
#if USE_YPCBCR
		uniform sampler2D _ChromaTex;
		uniform float4x4 _YpCbCrTransform;
#endif
		uniform fixed4 _Color;
		uniform float3 _cameraPosition;

		struct Input 
		{
			float4 texcoords;
		};

		void VertexFunction(inout appdata_full v, out Input o)
		{
			UNITY_INITIALIZE_OUTPUT(Input, o);

			o.texcoords = OffsetAlphaPackingUV(_MainTex_TexelSize.xy, v.texcoord, _MainTex_ST.y < 0.0);
		}

		void surf(Input IN, inout SurfaceOutput o) 
		{
				fixed4 col;
#if USE_YPCBCR
				col = SampleYpCbCr(_MainTex, _ChromaTex, IN.texcoords.xy, _YpCbCrTransform);
#else
				col = SampleRGBA(_MainTex, IN.texcoords.xy);
#endif

#if ALPHAPACK_TOP_BOTTOM | ALPHAPACK_LEFT_RIGHT
				col.a = SamplePackedAlpha(_MainTex, IN.texcoords.zw);
#endif
				col *= _Color;
				o.Albedo = col.rgb;
				o.Alpha = col.a;
		}
		ENDCG
	}

	Fallback "Legacy Shaders/Transparent/VertexLit"
}