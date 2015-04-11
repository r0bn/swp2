package de.hft_stuttgart.spirit.android;

import static android.opengl.GLES20.glUseProgram;
import android.content.Context;

public class ShaderProgram {
	// Uniform constants
	protected static final String U_MATRIX = "u_Matrix";
	protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
	protected static final String U_COLOR = "u_Color";
	
	// Attribute constants
	protected static final String A_POSITION = "a_Position";
	protected static final String A_COLOR = "a_Color";
	protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

	// Shader Program

	protected final int program;

	protected ShaderProgram(Context context, int vertexShaderResourceId,
			int fragmentShaderResourceId) {
		program = ShaderHelper.buildProgram(TextResourceReader
				.readTextFromResource(context, vertexShaderResourceId),
				TextResourceReader.readTextFromResource(context,
						fragmentShaderResourceId));

	}
	
	public void useProgram(){
		// Set the current OpenGL shader program to this program
		glUseProgram(program);
	}

}
