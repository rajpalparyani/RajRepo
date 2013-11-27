uniform float4x4 proj_unif;
uniform float4x4 modelview_unif;

void main(	in float4 color_attr : color_attr,
			in float3 pos_attr : pos_attr,
			out float4 gl_Position : SV_Position,
			out float4 color : COLOR,
			out float eyedist : EYEDIST)
{
	float4 pos = mul(float4(pos_attr.xyz, 1.0f), modelview_unif);
	gl_Position = mul(pos, proj_unif);

	color = color_attr;
	eyedist = -pos.z;

	return;
}