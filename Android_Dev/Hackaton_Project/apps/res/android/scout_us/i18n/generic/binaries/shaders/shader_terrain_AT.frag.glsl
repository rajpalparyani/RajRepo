
#ifdef GL_ES
precision mediump float;
#endif

varying vec2 uv;
varying vec4 color;

uniform sampler2D tex_unif;
uniform float alpha_value_unif;

void main()
{
   gl_FragColor = texture2D(tex_unif, uv) * color;
   if(gl_FragColor.a < alpha_value_unif) discard;
}
