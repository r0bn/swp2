#extension GL_OES_EGL_image_external : require
precision mediump float;

uniform samplerExternalOES u_TextureUnit;
varying vec2 v_TextureCoordinates;
varying float v_alpha;

void main()
{
	vec3 input_color = texture2D(u_TextureUnit, v_TextureCoordinates).rgb;
	float alphaS = v_TextureCoordinates[0];
	float alphaT = v_TextureCoordinates[1];
	vec2 alphaCoordinates = vec2(alphaS,alphaT+0.5);
	vec4 alphaVec = texture2D(u_TextureUnit, alphaCoordinates);
	float alpha = alphaVec.g;
	gl_FragColor = vec4(input_color,alpha);

}