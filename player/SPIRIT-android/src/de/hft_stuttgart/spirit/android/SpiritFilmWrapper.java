package de.hft_stuttgart.spirit.android;

import com.badlogic.gdx.Gdx;

import android.content.Context;
import de.hft_stuttgart.spirit.FilmStartedCallback;
import de.hft_stuttgart.spirit.PlaylistEntry;
import de.hft_stuttgart.spirit.SpiritFilm;
import static android.opengl.Matrix.*;

public class SpiritFilmWrapper implements SpiritFilm {
	Context c;
	FilmShaderProgram filmShader;
	Film currentFilm = null;
	private final float[] projectionMatrix = new float[16];

	public SpiritFilmWrapper(Context c) {
		this.c = c;
	}

	@Override
	public void setup() {
		filmShader = new FilmShaderProgram(c);
	}

	public void prepareNextFilm(FilmStartedCallback fsc) {
		if (currentFilm != null) {
			destroy();
		}
		currentFilm = new Film(c, fsc);
	}

	public void startFilm(PlaylistEntry pe, int position) {
		if (currentFilm != null) {
			currentFilm.start(pe, position);
		}
	}

	public void draw() {
		if (currentFilm != null) {
			currentFilm.draw(filmShader, projectionMatrix);
		}
	}

	public void draw(float[] projectionMatrix, float[] modelViewMatrix,
			float[] pointOnScreen) {
		if (currentFilm != null) {
			float[] volume = new float[2];
			//System.out.println(pointOnScreen[0] + "x" + pointOnScreen[1]);
			volume[0] = 1 - pointOnScreen[0] / Gdx.graphics.getWidth();
			volume[1] = pointOnScreen[0] / Gdx.graphics.getWidth();
			for (int i = 0; i < volume.length; i++) {
				if (volume[i] < 0) {
					volume[i] = 0f;
				}
				if (volume[i] > 1) {
					volume[i] = 1f;
				}
			}
			currentFilm.draw(filmShader, projectionMatrix, modelViewMatrix,
					volume);
		}
	}

	@Override
	public void calcMatrix(int width, int height) {
		final float aspectRatio = width > height ? (float) width
				/ (float) height : (float) height / (float) width;
		if (width > height) {
			// Landscape

			orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f,
					-1f, 1f);
		} else {
			// Portrait or square
			orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio,
					-1f, 1f);
		}
	}

	@Override
	public void destroy() {
		if (currentFilm != null) {
			currentFilm.destroy();
			currentFilm = null;
		}
	}

	@Override
	public int getDuration() {
		if (currentFilm != null) {
			return currentFilm.getDuration();
		} else {
			return -1;
		}
	}

	@Override
	public int getPosition() {
		if (currentFilm != null) {
			return currentFilm.getPosition();
		} else {
			return -1;
		}
	}

	@Override
	public boolean isPlaying() {
		if (currentFilm != null) {
			return currentFilm.isPlaying();
		} else {
			return false;
		}
	}

	@Override
	public PlaylistEntry getPlaylistEntry() {
		if (currentFilm != null) {
			return currentFilm.playlistEntry;
		} else {
			return null;
		}
	}

	@Override
	public boolean isNull() {
		if (currentFilm != null) {
			return false;
		}
		return true;
	}

}
