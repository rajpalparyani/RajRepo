uniform float2 tex_scale_unif;
uniform float2 tex_offset_unif;

uniform float4x4 proj_unif;
uniform float4x4 modelview_unif;

uniform float meter_scale_unif;
uniform sampler2D heightmap_id;

float elev_range = 4492.0;
float elev_min = -80.0;

void main(  in float2 uv_attr : uv_attr,
            in float2 pos_attr : pos_attr,
		    out float4 gl_Position : SV_Position,
		    out float2 uv : TEXCOORD,
		    out float eyedist : EYEDIST)
{
    float2 texture_uv = float2(pos_attr[0], pos_attr[1]);
    
    float4 heightmap_pixel = tex2D(heightmap_id, uv_attr);

    float z = (heightmap_pixel.x + heightmap_pixel.y * 255.0) / 256.0;
    float altitude = (elev_min + (z * elev_range)) * meter_scale_unif;

    float4 pos = float4(pos_attr[0], pos_attr[1], altitude, 1.0);
    
    float4 world_pos = mul(pos, modelview_unif);

    gl_Position = mul(world_pos, proj_unif);
    
    //set tex coords
    uv = texture_uv * tex_scale_unif + tex_offset_unif;
    
    //set eyedistance (for fog)
    eyedist = -world_pos.z;

    return;
}
