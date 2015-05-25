package de.hft_stuttgart.storytellar;

import java.util.List;

import de.hft_stuttgart.spirit.Location;
import de.hft_stuttgart.spirit.NfcTagDetected;
import de.hft_stuttgart.spirit.PlaylistEntry;
import de.hft_stuttgart.spirit.Poi;
import de.hft_stuttgart.spirit.SpiritEvent;
import de.hft_stuttgart.spirit.SpiritStoryEngine;
import de.hft_stuttgart.spirit.UIController;

public class StorytellAR_StoryEngine implements SpiritStoryEngine {

	PlayableStory story;
	UIController facade;
	boolean firstStart = true;
	boolean firstVideo = true;
	boolean ghostDetected = false;
	long[] vibratePattern = { 760, 600, 1080, 880, 1560, 840, 720, 2080, 2120,
			720, 2360, 360 }; // pattern:

	// pause,
	// vibrieren,
	// pause,
	// vibrieren,
	// ...
	boolean startNextVideo = false;
	long timerStartNextVideo = 0;

	public StorytellAR_StoryEngine(UIController spiritFacade, PlayableStory story) {
		facade = spiritFacade;
		this.story = story;
	}
	
	//Wird nach jedem Frame aufgerufen
	@Override
	public void update() {

		// bei erstem Start Playlist erstellen
		if (firstStart) {

		}

		checkEvents();
		checkGhost();
		setGuiStuff();

		// AR / Sonar wechsel durch Tabletwinkel
		if (facade.getTabletAngle() < 30) {
			facade.switchToSonarView();
		} else {
			facade.switchToARView();
		}

		if (startNextVideo) {
			// Log.w("SPIRIT", "Hallo");
			if (System.currentTimeMillis() > timerStartNextVideo) {
				// Log.w("SPIRIT", "Und los!");
				startNextVideo = false;
				facade.nextFilm();
			}
		}
	}

	@Override
	public void reset() {
		// Playlist Queue leeren
		// facade.removeAllVideos();
		// Aktuell laufendes Video abbrechen
		// facade.stopFilm();
		// GUI Elemente zurücksetzen
		// Geist wieder suchen
		ghostDetected = false;
		firstVideo = true;
		// und wieder neue playlist erstellen
		createPlaylist();
	}

	private void checkEvents() {
		// Events bearbeiten
		SpiritEvent[] events = facade.getEvents();
		for (int i = 0; i < events.length; i++) {
			switch (events[i].getEvent()) {
			case Filmende:
				break;
			case Filmstart:
				System.out.println("Film Start");
				if (firstVideo) {
					firstVideo = false;
					// facade.vibrate(vibratePattern);
					facade.setBrowserSize(0.7f, 0.7f);
					facade.setBrowserAlpha(0.6f);
					
				}
//				facade.openUrl("http://de.m.wikipedia.org/wiki/Wikipedia:Hauptseite");
				break;
			case ResetAllButtonPressed:
				facade.resetAll();
				break;
			case SkipButtonPressed:
				facade.startSignalToGhostEffect(300);
				startNextVideo = true;
				timerStartNextVideo = System.currentTimeMillis() + 500;
				break;
			case NotSkipButtonPressed:
				facade.enableSubtitle();
				break;
			case SonarButtonPressed:
				facade.switchToSonarView();
				break;
			case StartManuellButtonPressed:
				facade.startFilm();
				facade.createReference();
				break;
			case FadeEffectStarted:
				facade.vibrate(Integer.MAX_VALUE);
				break;
			case FadeEffectStopped:
				facade.cancelVibrate();
				break;
			case CustomButton0:
				// Log.w("SPIRIT", "Button 0");
				break;
			case CustomButton1:
				// Log.w("SPIRIT", "Button 1");
				break;
			case CustomButton2:
				// Log.w("SPIRIT", "Button 2");
				ghostDetected = false;
				break;
			case CustomButton3:
				// Log.w("SPIRIT", "Button 3");
				break;
			case CustomButton4:
				// Log.w("SPIRIT", "Button 4");
				break;
			case CustomButton5:
				// Log.w("SPIRIT", "Button 5");
				break;

			default:
				break;

			}
		}

	}

	private void checkGhost() {
		// System.out.println("Hallo @ "+facade.getTabletAngle());
		// Entfernung zum Geist prüfen

		// nur wenn Geist noch nicht gefunden
		if (!ghostDetected) {
			// Geist gefunden? -> über Entfernung in Metern

			if (facade.getTabletAngle() > 85) {
				createPlaylist();
				ghostDetected = true;
				facade.createReference();
				facade.startFilm();
				facade.disableArrow();
			}
		}
	}

	private void setGuiStuff() {

	}

	public void createPlaylist() {
		PlaylistEntry pe = new PlaylistEntry(story.getStorypoints().values().iterator().next().getVideo());
		pe.setAutostart(true);
		pe.setLoop(true);
		facade.enqueueVideo(pe);
	}

}
