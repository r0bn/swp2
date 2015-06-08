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
	 * F�gt einen neuen Geist hinzu, der dann im Radar als Ziel angezeigt wird.
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
	 * Gibt die Entfernung zum nahesten Geist als float zur�ck.
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
	 * Aktiviert die Namensanzeige f�r einen bestimmten Geist.
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
	 * F�gt einen neuen Eintrag zur Playlist hinzu.
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
	 * @return Gr��e der Playlistqueue
	 */
	public int getQueueSize();

	/**
	 * Gibt die Position der aktuellen Wiedergabe an Wird aktuell kein Film
	 * abgespielt ist die R�ckgabe -1
	 * 
	 * @return Position in Millisekunden
	 */
	public int getFilmPosition();

	/**
	 * Gibt die L�nge des aktuellen Films an Wird aktuell kein Film abgespielt
	 * ist die R�ckgabe -1
	 * 
	 * @return L�nge in Millisekunden
	 */
	public int getFilmDuration();

	/**
	 * Bricht die aktuelle Wiedergabe ab.
	 */
	public void stopFilm();

	/**
	 * Startet den n�chsten Eintrag in der Playlist. Wenn aktuell bereits ein
	 * Film l�uft, hat dies nur Auswirkung, wenn der n�chste Eintrag in der
	 * Playlist Autostart deaktiviert. Dieser w�rde dann automatisch gestartet
	 * werden. Um eine aktuelle Wiedergabe abzubrechen nextFilm() benutzen.
	 */
	public void startFilm();

	/**
	 * Bricht die aktuelle Wiedergabe ab und startet den n�chsten Eintrag in der
	 * Playlist. Wird aktuell kein Film abgespielt, wird nur der n�chste Film
	 * gestartet, wie bei startFilm().
	 */
	public void nextFilm();

	/**
	 * Bricht die aktuelle Wiedergabe ab, leer die Playlist und setzt die Story
	 * Engine zur�ck.
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
	 * bis zu 6 solcher Buttons unterst�tzt. Wird ein Button angetippt werden
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
	 * Die Storyengine zur�cksetzen.
	 */
	public void resetStoryEngine();

	// sonstiges

	/**
	 * L�sst das Ger�t vibrieren
	 * 
	 * @param Vibrationszeit
	 *            in Millisekunden
	 */
	public void vibrate(int time);

	/**
	 * L�sst das Ger�t in einem bestimmten Muster zu vibrieren. Die erste Zahl
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
	 * Gibt alle Events zur�ck, die seit dem letzten Abruf aufgetreten sind.
	 * 
	 * @return Array aller Events
	 */
	public SpiritEvent[] getEvents();

	/**
	 * Gibt den aktuellen Neigungswinkel des Tablets zur�ck
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
	 * Aktiviert die "Untertitel" f�r die aktuelle Wiedergabe.
	 */
	public void enableSubtitle();

	public boolean newNfcTagDiscovered();

	public NfcTagDetected[] getNfcTags();
	// public Context getContext();
	public void openUrl(String url);
	public void setBrowserAlpha(float alpha);
	public void hideBrowser();
	public void setBrowserSize(float width, float height);
	//f??arser 
	public List<Poi> getArmlFromAssets(String filename);
	public List<Poi> getArmlFromSdCard(String fullPath);
	
	public void showPicture(String picturePath);

	public void hidePicture();

	public void endStory();

	public boolean vuforiaIsReady();

	public void setText(String question);

	public void hideText();

	public void log(String who, String what);
}
