uniform sampler2D tex_unif;
uniform float alpha_value_unif;

void main(	in float4 gl_Position : SV_Position,
			in float2 uv : TEXCOORD,
			in float4 color : COLOR,
			in float eyedist : EYEDIST,
			out float4 gl_FragColor : SV_Target0)
{
	gl_FragColor = tex2D(tex_unif, uv) * color;

	if ((uv[1] < 0.0) ||
		(uv[1] >= 1.0) ||
		(gl_FragColor.a < alpha_value_unif))
	{
		discard;
	}

	return;
}