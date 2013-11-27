uniform sampler2D tex_unif;
uniform float alpha_value_unif;
uniform float4 fog_color_unif;
uniform float fog_start_unif;
uniform float fog_end_unif;
uniform float4 eye_pos_unif;

float computeLinearFogFactor(in float eyedist)
{
    float factor;
    factor = (fog_end_unif - eyedist) / (fog_end_unif - fog_start_unif);
    factor = clamp(factor, 0.0f, 1.0f);
    return factor;
}

void main(	in float4 gl_Position : SV_Position,
            in float2 uv : TEXCOORD,
            in float eyedist : EYEDIST,
            out float4 gl_FragColor : SV_Target0)
{
    float alpha = tex2D(tex_unif, uv).a;
    if(alpha < alpha_value_unif) discard;
    gl_FragColor = lerp(fog_color_unif, tex2D(tex_unif, uv), computeLinearFogFactor(eyedist));

    return;
}