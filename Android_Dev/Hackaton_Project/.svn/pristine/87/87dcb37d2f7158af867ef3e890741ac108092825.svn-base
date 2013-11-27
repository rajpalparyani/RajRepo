uniform float2 tex_scale_unif;
uniform float2 tex_offset_unif;
uniform float4x4 proj_unif;
uniform float4x4 modelview_unif;

void main(	in float2 uv_attr : uv_attr,
			in float3 pos_attr : pos_attr,
			out float4 gl_Position : SV_Position,
			out float2 uv : TEXCOORD,
			out float eyedist : EYEDIST)
{
	float4 pos = float4(pos_attr.xyz, 1.0f);
	pos = mul(pos, modelview_unif);
	gl_Position = mul(pos, proj_unif);

	uv = uv_attr * tex_scale_unif + tex_offset_unif;
	eyedist = -pos.z;
	
	return;
}