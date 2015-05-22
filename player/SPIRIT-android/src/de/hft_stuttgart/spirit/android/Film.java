package de.hft_stuttgart.spirit.android;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.translateM;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;
import de.hft_stuttgart.spirit.FilmStartedCallback;
import de.hft_stuttgart.spirit.PlaylistEntry;
import de.hft_stuttgart.spirit.android.FilmShaderProgram;
import de.hft_stuttgart.spirit.android.VertexArray;

public class Film implements OnCompletionListener, OnPreparedListener,
		OnInfoListener, OnErrorListener, OnBufferingUpdateListener,
		OnFrameAvailableListener {
	public static final int BYTES_PER_FLOAT = 4;
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT)
			* BYTES_PER_FLOAT;

	private static final float[] VERTEX_DATA = {
			// Order of coordinates: X, Y, S, T

			// // Triangle Fan
			0f, 0f, 0.5f, 0.75f, -1.0f, -1.0f, 0f, 0.5f, 1.0f, -1.0f, 1f, 0.5f,
			1.0f, 1.0f, 1f, 1.0f, -1.0f, 1.0f, 0f, 1.0f, -1.0f, -1.0f, 0f, 0.5f };

	private final VertexArray vertexArray;
	private int[] filmTexture;
	private Surface surface;
	private SurfaceTexture surfaceTexture;
	private boolean hasFinished = false;
	MediaPlayer mp;
	private boolean isPlaying = false;
	private ReentrantLock mMediaPlayerLock = null;
	Context context;
	float[] filmMtx = new float[16];
	private boolean newFrameAvailable = false; // aktualisieren
	private boolean frameAvailable = false; // ob jemals ein frame da war
	float alphamax = 0.7f;
	float ratio = 1.0f;
	private float[] modelMatrix = new float[16];
	public float[] finalMatrix = new float[16];
	long startTime = 0;
	boolean simpleDrawOnScreen;
	private int seekTo;
	PlaylistEntry playlistEntry;
	boolean destroyed = false;
	FilmStartedCallback filmStartedCallback;

	public Film(Context c, FilmStartedCallback fsc) {
		this.context = c;
		filmStartedCallback = fsc;
		vertexArray = new VertexArray(VERTEX_DATA);
		mp = new MediaPlayer();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayerLock = new ReentrantLock();
		filmTexture = new int[1];
		glGenTextures(1, filmTexture, 0);
		if (filmTexture[0] == 0) {
			Log.e("SPIRIT", "Fehler beim Textur generieren!");
		}
		glBindTexture(GL_TEXTURE_EXTERNAL_OES, filmTexture[0]);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER,
				GL_LINEAR);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER,
				GL_LINEAR);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S,
				GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T,
				GL_CLAMP_TO_EDGE);

		glBindTexture(GL_TEXTURE_EXTERNAL_OES, 0);

		surfaceTexture = new SurfaceTexture(filmTexture[0]);
		surface = new Surface(surfaceTexture);
		surfaceTexture.setOnFrameAvailableListener(this);

		startTime = System.currentTimeMillis();
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public boolean hasFinished() {
		return hasFinished;
	}

	public int getPosition() {
		if (isPlaying) {
			return mp.getCurrentPosition();
		} else {
			return -1;
		}
	}

	public int getDuration() {
		if (isPlaying) {
			return mp.getDuration();
		} else {
			return -1;
		}
	}

	public void stop() {
		mMediaPlayerLock.lock();
		isPlaying = false;
		mp.stop();
		mMediaPlayerLock.unlock();
	}

	public boolean simpleDrawOnScreen() {
		return simpleDrawOnScreen;
	}

	// public void start(String titel) {
	public void start(PlaylistEntry pe, int position) {
		mMediaPlayerLock.lock();
		playlistEntry = pe;
		this.simpleDrawOnScreen = playlistEntry.isSimpleDrawOnScreenEnabled();
		try {
			// mp.setDataSource("/sdcard/" + titel);
			// AssetFileDescriptor afd =
			// context.getAssets().openFd("spirit_demo_testvideo_1.mp4");
			// mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			// mp.prepareAsync();

			// mp = MediaPlayer.create(context, resId);
			// if (pe.isUrl()) {
			// mp.setDataSource(playlistEntry.getUrl());
			// } else {
			// AssetFileDescriptor afd = context.getResources()
			// .openRawResourceFd(playlistEntry.getId());
			// mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
			// afd.getLength());
			// }

			if(playlistEntry.getUrl().contains("storage") || playlistEntry.getUrl().contains("sdcard")){				
				File f = new File(playlistEntry.getUrl());
				FileInputStream fis = new FileInputStream(f);
				mp.setDataSource(fis.getFD(), 0, f.length());
				fis.close();
			} else {			
				AssetFileDescriptor afd = context.getAssets().openFd(
						playlistEntry.getUrl());
				mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						afd.getLength());
			}

			seekTo = position;
			// mp.start();
			// isPlaying = true;
			mp.setOnCompletionListener(this);
			mp.setOnErrorListener(this);
			mp.setOnInfoListener(this);
			mp.setOnPreparedListener(this);
			mp.setOnBufferingUpdateListener(this);
			mp.setLooping(playlistEntry.isLoopEnabled());
			mp.prepareAsync();

		} catch (Exception e) {
			e.printStackTrace();
		}

		mMediaPlayerLock.unlock();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mMediaPlayerLock.lock();
		if (!destroyed) {
			mp.seekTo(seekTo);
			mp.setSurface(surface);

			/*
			 * Workaround, Mediaplayer Android 4.x meldet andere Auflösung als
			 * Mediaplayer unter Android 5. Bei unseren Videos z.B.: Android 4:
			 * 640x720 Android 5: 1280x720 (korrekt) Dieser Workaround nimmt an,
			 * dass unsere Videos immer eine größere Breite als Höhe haben.
			 */

			if (mp.getVideoHeight() > mp.getVideoWidth()) {
				// Android 4
				ratio = (float) mp.getVideoWidth()
						/ ((float) mp.getVideoHeight() / 2f);
			} else {
				// Android 5
				ratio = (float) mp.getVideoWidth()
						/ (float) mp.getVideoHeight();
			}
			mp.start();
			// mp.start();
			isPlaying = true;
			filmStartedCallback.filmStarted();
		}
		mMediaPlayerLock.unlock();
	}

	public void bindData(FilmShaderProgram textureProgram) {
		vertexArray.setVertexAttribPointer(0,
				textureProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT, STRIDE);
		vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,
				textureProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
	}

	/*
	 * Video an Ort gebunden
	 */
	public void draw(FilmShaderProgram filmShaderProgram,
			float[] projectionMatrix, float[] modelViewMatrix, float[] volume) {
		modelMatrix = modelViewMatrix.clone();
		mMediaPlayerLock.lock();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		if (isPlaying) { // nochmal prüfen ob es auch noch spielt :)
			if (newFrameAvailable) {
				newFrameAvailable = false;
				surfaceTexture.updateTexImage();
				surfaceTexture.getTransformMatrix(filmMtx);
				frameAvailable = true;
			}
			if (frameAvailable) { // nur zeichen wenn schon mal ein frame da war
				float scale = 125f;

				float speedHeight = 0.07f;
				float speedWidth = 0.2f;
				double angleHeight = Math.toRadians(((System
						.currentTimeMillis() - startTime) * speedWidth) % 360);
				double angleWidth = Math
						.toRadians(((System.currentTimeMillis() - startTime) * speedHeight) % 360);
				float radiusHeight = 1.25f;
				float radiusWidth = 4.0f;

				translateM(modelMatrix, 0,
						(radiusWidth * (float) Math.cos(angleWidth)),
						(radiusHeight * (float) Math.sin(angleHeight)), 0);
				// rotateM(modelViewMatrix, 0, (mp.getCurrentPosition()/20f) %
				// 360, 0,
				// 0, 1);

				scaleM(modelMatrix, 0, ratio * scale, scale, scale);

				multiplyMM(finalMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
				filmShaderProgram.useProgram();

				filmShaderProgram.setUniforms(finalMatrix, filmTexture[0],
						filmMtx);

				bindData(filmShaderProgram);
				glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
				mp.setVolume(volume[0], volume[1]);
			}
		}
		glDisable(GL_BLEND);
		mMediaPlayerLock.unlock();
	}

	/*
	 * Video an Bildschirm gebunden
	 */
	public void draw(FilmShaderProgram filmShaderProgram,
			float[] projectionMatrix) {
		mMediaPlayerLock.lock();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		if (isPlaying) { // nochmal prüfen ob es auch noch spielt :)
			if (newFrameAvailable) {
				newFrameAvailable = false;
				surfaceTexture.updateTexImage();
				surfaceTexture.getTransformMatrix(filmMtx);
				frameAvailable = true;
			}
			if (frameAvailable) { // nur zeichen wenn schon mal ein frame da war
				float scale = 1f;
				float[] matrix = new float[16];
				Matrix.setIdentityM(matrix, 0);
				scaleM(matrix, 0, ratio * scale, scale, scale);

				multiplyMM(finalMatrix, 0, projectionMatrix, 0, matrix, 0);

				filmShaderProgram.useProgram();

				filmShaderProgram.setUniforms(finalMatrix, filmTexture[0],
						filmMtx);
				bindData(filmShaderProgram);
				glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

			}
		}
		glDisable(GL_BLEND);
		mMediaPlayerLock.unlock();

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mMediaPlayerLock.lock();
		isPlaying = false;
		hasFinished = true;
		filmStartedCallback.filmFinished();
		mMediaPlayerLock.unlock();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mMediaPlayerLock.lock();
		Log.w("onError", "Error: what: " + what + " extra: " + extra);
		isPlaying = false;
		hasFinished = true;
		mMediaPlayerLock.unlock();
		return true;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		mMediaPlayerLock.lock();
		Log.w("Info", "Info: what: " + what + " extra: " + extra);
		mMediaPlayerLock.unlock();
		return true;
	}

	public synchronized void destroy() {
		mMediaPlayerLock.lock();
		isPlaying = false;
		if (mp != null) {
			try {
				if (isPlaying)
					mp.stop();

			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				mp.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				mp.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mp = null;
		}
		if (surfaceTexture != null) {
			surfaceTexture.release();
			surfaceTexture = null;
		}
		if (surface != null) {
			surface.release();
			surface = null;
		}
		glDeleteTextures(1, filmTexture, 0);
		destroyed = true;
		mMediaPlayerLock.unlock();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
	}

	@Override
	public void onFrameAvailable(SurfaceTexture surfaceTexture) {
		mMediaPlayerLock.lock();
		newFrameAvailable = true;
		mMediaPlayerLock.unlock();
	}

	public PlaylistEntry getPlaylistEntry() {
		return playlistEntry;
	}

}
