// Upgrade NOTE: replaced 'mul(UNITY_MATRIX_MVP,*)' with 'UnityObjectToClipPos(*)'

// Upgrade NOTE: replaced 'mul(UNITY_MATRIX_MVP,*)' with 'UnityObjectToClipPos(*)'

// this shader created a basic gradient between an initial color to an
Shader "Chart/Canvas/Solid"
{
	Properties
	{
		_MainTex("Texture", 2D) = "white" {}
		_Color("Color", Color) = (1,1,1,0)
		_StencilComp("Stencil Comparison", Float) = 8
		_Stencil("Stencil ID", Float) = 0
		_StencilOp("Stencil Operation", Float) = 0
		_StencilWriteMask("Stencil Write Mask", Float) = 255
		_StencilReadMask("Stencil Read Mask", Float) = 255
		_ColorMask("Color Mask", Float) = 15
	}
		SubShader{

		Tags{ "Queue" = "Transparent"
		"RenderType" = "Transparent" }

		Stencil
		{
			Ref[_Stencil]
			Comp[_StencilComp]
			Pass[_StencilOp]
			ReadMask[_StencilReadMask]
			WriteMask[_StencilWriteMask]
		}
		ColorMask[_ColorMask]

		Pass{
			Cull Off
			Lighting Off
			ZWrite Off
			ZTest[unity_GUIZTestMode]
			Blend SrcAlpha OneMinusSrcAlpha
			LOD 100

		CGPROGRAM
#pragma vertex lerpVertex  
#pragma fragment sampleFragment
#include "UnityCG.cginc"

	fixed4 _Color;
	sampler2D _MainTex;
	uniform float4 _MainTex_ST;

	struct vertexData
	{
		float2 uv : TEXCOORD0;
		float4 pos : SV_POSITION;
		fixed4 color : COLOR; 
	};

	vertexData lerpVertex(appdata_full v)
	{
		vertexData res;
		res.pos = UnityObjectToClipPos(v.vertex);
		res.color = _Color;		
		res.uv = TRANSFORM_TEX(v.texcoord, _MainTex);
		return res;
	}


	float4 sampleFragment(vertexData v) : COLOR
	{
		fixed4 texData = tex2D(_MainTex, v.uv) * v.color;
	return texData;
	}
		ENDCG
	}
	}
}