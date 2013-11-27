uniform float4x4 proj_unif;
uniform float4x4 modelview_unif;

void main(	in float3 pos_attr : pos_attr,
			out float4 position : SV_Position,
			out float eyedist : EYEDIST)
{    
    float4 pos = mul(float4(pos_attr.xyz, 1.0f), modelview_unif);
    position = mul(pos, proj_unif);

    eyedist = -pos.z;

    return;
}
