package de.hft_stuttgart.spirit.android;

import static android.opengl.GLES20.*;
import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import android.content.Context;

public class FilmShaderProgram extends ShaderProgram{
	// Uniform locations
	private final int uMatrixLocation;
	private final int uTextureUnitLocation;
	private final int uMtxLocation;
	// Attribute locations
	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;
	
	public FilmShaderProgram(Context context){
		//super(context, R.raw.film_texture_vertex_shader, R.raw.film_texture_fragment_shader);
		// alpha test
		super(context, R.raw.film_texture_vertex_shader, R.raw.film_texture_fragment_shader);
		
		// Retrieve uniform locations for the shader program
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
		uMtxLocation = glGetUniformLocation(program,"u_Mtx");
		// Retrieve attribute locations for the shader program
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
	}
	
	public void setUniforms(float[] matrix, int textureId, float[] mtx){
		// Pass the matrix into the shader program
		glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
		glUniformMatrix4fv(uMtxLocation,1,false,mtx,0);
		// Set the active texture unit to texture unit 0
		glActiveTexture(GL_TEXTURE2);
		// Bind the texture to this unit
		glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureId);
		// Tell the texture uniform sampler to use this texture in the shader by telling it to read from texture unit 0
		glUniform1i(uTextureUnitLocation, 2);
		
	}

	public int getPositionAttributeLocation(){
		return aPositionLocation;
	}
	
	public int getTextureCoordinatesAttributeLocation(){
		return aTextureCoordinatesLocation;
	}
}
