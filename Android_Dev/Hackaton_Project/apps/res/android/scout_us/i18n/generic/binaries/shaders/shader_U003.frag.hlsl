uniform float4 color_unif;

void main(	in float4 position : SV_Position,
			in float eyedist : EYEDIST,
			out float4 gl_FragColor : SV_Target0)
{
    gl_FragColor = color_unif;

    return;
}
