package de.hft_stuttgart.spirit;

import java.util.List;

/**
 * Dieses Interface wird von der Facade implementiert.
 */
public interface UIController {
	// Location

	/**
	 * @return aktuelle Nutzerlocation
	 */
	public Location getUserLocation();

	/**
	 * Fügt einen neuen Geist hinzu, der dann im Radar als Ziel angezeigt wird.
	 * 
	 * @param Geistlocation
	 */
	public void addGhostLocation(Location ghost);

	/**
	 * Entfernt alle Geister
	 */
	public void deleteAllGhosts();

	/**
	 * Entfernt einen bestimmten Geist wieder
	 * 
	 * @param ghost
	 */
	public void deleteGhost(Location ghost);

	/**
	 * Ein Array mit allen aktuellen Geistern
	 * 
	 * @return Location Array
	 */
	public Location[] getGhostLocations();

	/**
	 * Gibt die Entfernung zum nahesten Geist als float zurück.
	 * 
	 * @return Entfernung in Metern
	 */
	public float getDistanceUserToClosestGhost();

	/**
	 * Der naheste Geist
	 * 
	 * @return nahester Geist
	 */
	public Location getClosestGhost();

	// Radar

	/**
	 * Im Radar werden die Namen der Geister angezeigt. Der Name kann direkt im
	 * Konstruktor gesetzt werden und mit .getProvider() ausgelesen werden.
	 * Beispiel: Location example = new Location("Name");
	 */
	public void showAllGhostNamesInRadar();

	/**
	 * Aktiviert die Namensanzeige für einen bestimmten Geist.
	 */
	public void showGostnameInRader(Location ghost);

	/**
	 * Kein Geistername wird im Radar angezeigt.
	 */
	public void hideAllGhostNamesInRadar();

	/**
	 * Deaktiviert die Namensanzeige eines bestimmten Geists.
	 */
	public void hideGostnameInRader(Location ghost);

	// Playlist

	/**
	 * Fügt einen neuen Eintrag zur Playlist hinzu.
	 * 
	 * @param PlaylistEntry
	 *            des neuen Films.
	 */
	public void enqueueVideo(PlaylistEntry pe);

	/**
	 * Leert die komplette Playlist
	 */
	public void removeAllVideos();

	/**
	 * @return Größe der Playlistqueue
	 */
	public int getQueueSize();

	/**
	 * Gibt die Position der aktuellen Wiedergabe an Wird aktuell kein Film
	 * abgespielt ist die Rückgabe -1
	 * 
	 * @return Position in Millisekunden
	 */
	public int getFilmPosition();

	/**
	 * Gibt die Länge des aktuellen Films an Wird aktuell kein Film abgespielt
	 * ist die Rückgabe -1
	 * 
	 * @return Länge in Millisekunden
	 */
	public int getFilmDuration();

	/**
	 * Bricht die aktuelle Wiedergabe ab.
	 */
	public void stopFilm();

	/**
	 * Startet den nächsten Eintrag in der Playlist. Wenn aktuell bereits ein
	 * Film läuft, hat dies nur Auswirkung, wenn der nächste Eintrag in der
	 * Playlist Autostart deaktiviert. Dieser würde dann automatisch gestartet
	 * werden. Um eine aktuelle Wiedergabe abzubrechen nextFilm() benutzen.
	 */
	public void startFilm();

	/**
	 * Bricht die aktuelle Wiedergabe ab und startet den nächsten Eintrag in der
	 * Playlist. Wird aktuell kein Film abgespielt, wird nur der nächste Film
	 * gestartet, wie bei startFilm().
	 */
	public void nextFilm();

	/**
	 * Bricht die aktuelle Wiedergabe ab, leer die Playlist und setzt die Story
	 * Engine zurück.
	 */
	public void resetAll();

	/**
	 * Erzeugt ein neues Referenzbild auf Basis des aktuellen Kamerabildes. Auf
	 * diese Weise wird das Video positioniert und an eine Position "gebunden"
	 */
	public void createReference();

	/**
	 * Gibt an, ob aktuell ein Video abgespielt wird true: ja false: nein
	 * 
	 * @return wird ein Video abgespielt true/false
	 */
	public boolean isFilmPlaying();

	// GUI

	/**
	 * Wechselt zur Sonaransicht
	 */
	public void switchToSonarView();

	/**
	 * Wechselt zur Kamerasicht
	 */
	public void switchToARView();

	/**
	 * Erstellt einen Button, der vom Nutzer angetippt werden kann. Es werden
	 * bis zu 6 solcher Buttons unterstützt. Wird ein Button angetippt werden
	 * alle ausgeblendet und es wird ein Event erzeugt.
	 * 
	 * @param text
	 *            Text des Buttons
	 * @param id
	 *            Button-id 0-5
	 */
	public void setButtonText(String text, int id);

	// Story Engine

	/**
	 * Aufruf um die Storyengine zu aktualisieren.
	 */
	public void updateStoryEngine();

	/**
	 * Die Storyengine zurücksetzen.
	 */
	public void resetStoryEngine();

	// sonstiges

	/**
	 * Lässt das Gerät vibrieren
	 * 
	 * @param Vibrationszeit
	 *            in Millisekunden
	 */
	public void vibrate(int time);

	/**
	 * Lässt das Gerät in einem bestimmten Muster zu vibrieren. Die erste Zahl
	 * gibt an, wie lange bis zum ersten Vibrieren gewartet werden soll. Dann
	 * abwechselnd Vibrationszeit und Pause, jeweils in Millisekunden.
	 * 
	 * @param Vibrationspattern
	 */
	public void vibrate(long[] pattern);

	/**
	 * Bricht das aktuelle Vibrieren ab.
	 */
	public void cancelVibrate();

	/**
	 * Gibt alle Events zurück, die seit dem letzten Abruf aufgetreten sind.
	 * 
	 * @return Array aller Events
	 */
	public SpiritEvent[] getEvents();

	/**
	 * Gibt den aktuellen Neigungswinkel des Tablets zurück
	 * 
	 * @return Winkel
	 */
	public float getTabletAngle();

	/**
	 * Startet manuell einen Fadeeffekt
	 */
	public void startFadeEffect();

	/**
	 * Aktiviert den Pfeil, der die Richtung zum nahesten Geist angibt.
	 */
	public void enableArrow();

	/**
	 * Deaktiviert den Pfeil, der die Richtung zum nahesten Geist angibt.
	 */
	public void disableArrow();

	/**
	 * Startet eine Animation, die ein Signalsenden an den Geist darstellt.
	 * 
	 * @param Zeit
	 *            in Millisekunden
	 */
	public void startSignalToGhostEffect(long time);

	/**
	 * Aktiviert die "Untertitel" für die aktuelle Wiedergabe.
	 */
	public void enableSubtitle();

	public boolean newNfcTagDiscovered();

	public NfcTagDetected[] getNfcTags();
	// public Context getContext();
	public void openUrl(String url);
	public void setBrowserAlpha(float alpha);
	public void hideBrowser();
	public void setBrowserSize(float width, float height);
	//f�r Parser 
	public List<Poi> getArmlFromAssets(String filename);
	public List<Poi> getArmlFromSdCard(String fullPath);
	
}
