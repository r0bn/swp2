package de.hft_stuttgart.spirit;

import java.util.List;

/**
 * Die App muss diese Methoden implementieren. Sie werden von der Storyengine
 * über die Facade aufgerufen.
 */
public interface SpiritApp {
	/**
	 * Gibt die komplette Playlist zurück. Es handelt sich hier um eine Queue,
	 * d.h. das aktuell spielende und bereits abgespielte Videos sind hier nicht
	 * mehr enthalten.
	 * 
	 * @return Playlist
	 */
	public Playlist getPlaylist();

	public GeoTools getGeoTools();

	public void addGhostLocation(Location ghost);

	public void deleteAllGhosts();

	public void deleteGhost(Location ghost);

	public Location[] getGhostLocations();

	public Location getClosestGhost();

	public Gui getGui();

	public SpiritFilm getFilm();

	public void stopFilm();

	public void startFilm();

	public void nextFilm();

	public void resetAll();

	public void switchToSonarView();

	public void switchToARView();

	public void createReference();

	public void vibrate(int time);

	public void vibrate(long[] pattern);

	public void cancelVibrate();

	public SpiritEvent[] getEvents();

	public float getTabletAngle();

	public void startFadeEffect();

	public void disableArrow();

	public void enableArrow();

	public void startSignalToGhostEffect(long time);

	public void enableSubtitle();

	public void showAllGhostNamesInRadar();

	public void showGostnameInRader(Location ghost);

	public void hideAllGhostNamesInRadar();

	public void hideGostnameInRader(Location ghost);

	public void setCustomTextButton(String text, int button);

	public boolean newNfcTagDiscovered();

	public NfcTagDetected[] getNfcTags();

	public void openUrl(String url);
	public void setBrowserAlpha(float alpha);
	public void hideBrowser();
	public void setBrowserSize(float width, float height);
	// public Context getContext();
	public List<Poi> getArmlFromAssets(String filename);
	public List<Poi> getArmlFromSdCard(String fullPath);
}
