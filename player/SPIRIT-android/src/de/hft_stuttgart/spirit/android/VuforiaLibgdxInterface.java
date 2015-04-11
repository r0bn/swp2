package de.hft_stuttgart.spirit.android;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.scaleM;
import android.opengl.Matrix;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.qualcomm.vuforia.CameraDevice;
import com.qualcomm.vuforia.Matrix44F;
import com.qualcomm.vuforia.Renderer;
import com.qualcomm.vuforia.State;
import com.qualcomm.vuforia.Tool;
import com.qualcomm.vuforia.TrackableResult;
import com.qualcomm.vuforia.Vec2F;
import com.qualcomm.vuforia.Vec2I;
import com.qualcomm.vuforia.Vec3F;
import com.qualcomm.vuforia.VideoBackgroundTextureInfo;

import de.hft_stuttgart.spirit.Vuforia;

public class VuforiaLibgdxInterface implements Vuforia {
	AndroidLauncher al;
	boolean hasBeenResumed = false;
	private float[] projectionMatrixVuforia = new float[16];
	float[] modelViewMatrix = new float[16];
	private boolean trackableFound = false;

	float[] pose = new float[12];
	float[] pointOnScreen = new float[2];
	float[][] points = new float[4][2];
	private final float scale = 125;
	
	float[] cameraSize = new float[2];
	Rectangle targetRectangle = new Rectangle();

	public VuforiaLibgdxInterface(AndroidLauncher al) {
		this.al = al;
	}

	@Override
	public void draw() {
		trackableFound = false;

		if (al.vuforiaReady) {
			// Get the state from Vuforia and mark the beginning of a rendering
			// section

			State state = Renderer.getInstance().begin();
			// Explicitly render the Video Background
			Renderer.getInstance().drawVideoBackground();

			// Did we find any trackables this frame?
			for (int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++) {

				// Get the trackable:
				TrackableResult trackableResult = state
						.getTrackableResult(tIdx);

				Matrix44F modelViewMatrix_Vuforia = Tool
						.convertPose2GLMatrix(trackableResult.getPose());
				pointOnScreen = Tool.projectPoint(
						CameraDevice.getInstance().getCameraCalibration(),
						trackableResult.getPose(), new Vec3F(0, 0, 0))
						.getData();

				
				points[0] = Tool.projectPoint(
						CameraDevice.getInstance().getCameraCalibration(),
						trackableResult.getPose(), new Vec3F(-scale, scale, 0))
						.getData();

				points[1] = Tool.projectPoint(
						CameraDevice.getInstance().getCameraCalibration(),
						trackableResult.getPose(), new Vec3F(scale, -scale, 0))
						.getData();

				points[2] = Tool.projectPoint(
						CameraDevice.getInstance().getCameraCalibration(),
						trackableResult.getPose(), new Vec3F(scale, scale, 0))
						.getData();

				points[3] = Tool.projectPoint(
						CameraDevice.getInstance().getCameraCalibration(),
						trackableResult.getPose(), new Vec3F(-scale, -scale, 0))
						.getData();

				for (int i = 0; i < 4; i++) {
					points[i] = normalize(points[i]);
				}

				float minX = points[0][0];
				float minY = points[0][1];
				float maxX = points[0][0];
				float maxY = points[0][1];

				for (int i = 1; i < 4; i++) {
					if (points[i][0] < minX) {
						minX = points[i][0];
					}
					if (points[i][0] > maxX) {
						maxX = points[i][0];
					}
					if (points[i][1] < minY) {
						minY = points[i][1];
					}
					if (points[i][1] > maxY) {
						maxY = points[i][1];
					}
				}
				targetRectangle.x = minX;
				targetRectangle.y = minY;
				targetRectangle.width = maxX - minX;
				targetRectangle.height = maxY - minY;

				// normalize
				cameraSize = CameraDevice.getInstance().getCameraCalibration()
						.getSize().getData();

				modelViewMatrix = modelViewMatrix_Vuforia.getData();
				pose = trackableResult.getPose().getData();
				projectionMatrixVuforia = al.vuforiaSession
						.getProjectionMatrix().getData();

				trackableFound = true;

			}

			Renderer.getInstance().end();

		}
	}

	@Override
	public void onPause() {
		try {
			al.vuforiaSession.pauseAR();
		} catch (VuforiaException e) {
			e.printStackTrace();
		}
		;
	}

	@Override
	public void onResume() {
		try {
			al.vuforiaSession.resumeAR();
			hasBeenResumed = true;
		} catch (VuforiaException e) {
			e.printStackTrace();
		}
		;
	}

	@Override
	public void onDestroy() {
		try {
			al.vuforiaSession.stopAR();
		} catch (VuforiaException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSurfaceCreated() {
		al.vuforiaSession.onSurfaceCreated();
	}

	@Override
	public void onSurfaceChanged(int width, int height) {
		al.vuforiaSession.onSurfaceChanged(width, height);
	}

	@Override
	public boolean isReady() {
		return al.vuforiaReady;
	}

	/*
	 * call draw() first!
	 */
	@Override
	public boolean trackableFound() {
		return trackableFound;
	}

	/*
	 * call draw() first!
	 */
	@Override
	public float[] getVuforiaProjectionMatrix() {
		return projectionMatrixVuforia;
	}

	@Override
	public float[] getModelViewMatrix() {
		return modelViewMatrix;
	}

	@Override
	public void createNewReferencePicture() {
		al.startBuild();
	}

	private float[] normalize(float[] f) {
		float result[] = new float[2];
		result[0] = (f[0] / cameraSize[0]) * Gdx.graphics.getWidth();
		result[1] = Gdx.graphics.getHeight()
				- ((f[1] / cameraSize[1]) * Gdx.graphics.getHeight());
		return result;
	}

	@Override
	public float[] getPointOnScreen() {
		// normalize
		// float[] normalizedPoint = new float[2];
		// normalizedPoint[0] = (pointOnScreen[0] / cameraSize[0])
		// * Gdx.graphics.getWidth();
		// normalizedPoint[1] = Gdx.graphics.getHeight() - ((pointOnScreen[1] /
		// cameraSize[1])
		// * Gdx.graphics.getHeight());
		return normalize(pointOnScreen);
	}

	@Override
	public Rectangle getTargetRectangle() {

		return targetRectangle;
	}

}
