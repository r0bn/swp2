package de.hft_stuttgart.spirit;

import com.badlogic.gdx.math.Rectangle;

public interface Vuforia {

	public void draw();
	public void onPause();
	public void onResume();
	public void onDestroy();
	public void onSurfaceCreated();
	public void onSurfaceChanged(int width, int height);
	public boolean isReady();
	public boolean trackableFound();
	public float[] getVuforiaProjectionMatrix();
	public float[] getModelViewMatrix();
	public void createNewReferencePicture();
	public float[] getPointOnScreen();
	public Rectangle getTargetRectangle();
}
