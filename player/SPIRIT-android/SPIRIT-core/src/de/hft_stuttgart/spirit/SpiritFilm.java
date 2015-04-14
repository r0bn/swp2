package de.hft_stuttgart.spirit;

public interface SpiritFilm {

	public void setup();
	public void prepareNextFilm(FilmStartedCallback fsc);
	public void startFilm(PlaylistEntry pe, int position);
	public void draw();
	public void draw(float[] projectionMatrix, float[] modelViewMatrix, float[] volume);
	public void calcMatrix(int width, int height);
	public void destroy();
	public int getDuration();
	public int getPosition();
	public boolean isPlaying();
	public PlaylistEntry getPlaylistEntry();
	public boolean isNull();
}
