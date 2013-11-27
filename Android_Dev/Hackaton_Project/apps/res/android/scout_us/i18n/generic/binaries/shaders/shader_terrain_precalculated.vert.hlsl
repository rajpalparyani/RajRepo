uniform float2 tex_scale_unif;
uniform float2 tex_offset_unif;

uniform float4x4 proj_unif;
uniform float4x4 modelview_unif;

void main(  in float4 pos_attr : pos_attr,
		    out float4 gl_Position : SV_Position,
		    out float2 uv : TEXCOORD,
		    out float eyedist : EYEDIST)
{
    float2 texture_uv = float2(pos_attr[0], pos_attr[1]);
    
    float4 world_pos = mul(pos_attr, modelview_unif);

    gl_Position = mul(world_pos, proj_unif);
    
    //set tex coords
    uv = texture_uv * tex_scale_unif + tex_offset_unif;
    
    //set eyedistance (for fog)
    eyedist = -world_pos.z;

    return;
}
