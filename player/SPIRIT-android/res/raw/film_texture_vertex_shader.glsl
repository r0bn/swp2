uniform mat4 u_Matrix;
uniform mat4 u_Mtx;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying float v_alpha;
void main()
{
	float x = u_Mtx[0][0] * a_TextureCoordinates[0] + u_Mtx[1][0] * a_TextureCoordinates[1] + u_Mtx[3][0];
	float y = u_Mtx[0][1] * a_TextureCoordinates[0] + u_Mtx[1][1] * a_TextureCoordinates[1] + u_Mtx[3][1];
	v_TextureCoordinates = vec2(x,y);
	v_alpha = a_TextureCoordinates[1];
	gl_Position = u_Matrix * a_Position;

}
