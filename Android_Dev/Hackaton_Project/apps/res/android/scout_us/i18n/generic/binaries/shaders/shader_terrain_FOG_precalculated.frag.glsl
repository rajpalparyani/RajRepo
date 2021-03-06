
#ifdef GL_ES
precision mediump float;
#endif

varying vec2 uv;
varying float eyedist;

uniform sampler2D tex_unif;
uniform vec4 fog_color_unif;
uniform float fog_start_unif;
uniform float fog_end_unif;
uniform vec4 eye_pos_unif;

float computeLinearFogFactor()
{
    float factor;
    factor = (fog_end_unif - eyedist) / (fog_end_unif - fog_start_unif);
    factor = clamp(factor, 0.0, 1.0);
    return factor;
}

void main()
{
    gl_FragColor = mix(fog_color_unif, texture2D(tex_unif, uv), computeLinearFogFactor());
}
