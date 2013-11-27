uniform sampler2D tex_unif;
uniform float alpha_value_unif;

void main(	in float4 gl_Position : SV_Position,
			in float2 uv : TEXCOORD,
			in float eyedist : EYEDIST,
			out float4 gl_FragColor : SV_Target0)
{
   gl_FragColor = tex2D(tex_unif, uv);
   if(gl_FragColor.a < alpha_value_unif) discard;

   return;
}