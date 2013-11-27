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
			in float4 color : COLOR,
			in float eyedist : EYEDIST,
			out float4 gl_FragColor : SV_Target0)
{
    gl_FragColor = lerp(fog_color_unif, color, computeLinearFogFactor(eyedist));

    return;
}
