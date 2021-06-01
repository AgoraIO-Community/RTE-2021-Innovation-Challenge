// Upgrade NOTE: replaced 'mul(UNITY_MATRIX_MVP,*)' with 'UnityObjectToClipPos(*)'

// Upgrade NOTE: replaced 'mul(UNITY_MATRIX_MVP,*)' with 'UnityObjectToClipPos(*)'

// this shader created a basic gradient between an initial color to an
Shader "Chart/Canvas/Gradient" 
{
	Properties
	{
		_MainTex("Texture", 2D) = "white" {}
		_Angle("Angle", Float) = 0
		_Combine("Combine", Color) = (1,1,1,0)
		_ColorFrom("From", Color) = (1,1,1,1)
		_ColorTo("To", Color) = (1,1,1,1)
		_ChartTiling("Tiling",Float) = 1
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

		fixed _Angle;
		fixed _ChartTiling;
		fixed4 _Combine;
		fixed4 _ColorFrom;
		fixed4 _ColorTo;
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
		fixed angle = _Angle * 3.14159 *2 / 360.0;
		fixed lerpValue = (v.texcoord.x/ _ChartTiling -0.5) * sin(angle) + (v.texcoord.y-0.5) * cos(angle);
		lerpValue = lerpValue + 0.5;
		res.color = lerp(_ColorFrom,_ColorTo, lerpValue);
		lerpValue = _Combine.a;
		fixed alpha = res.color.a;
		res.color = lerp(res.color, _Combine, lerpValue);
		res.color.a = alpha;
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