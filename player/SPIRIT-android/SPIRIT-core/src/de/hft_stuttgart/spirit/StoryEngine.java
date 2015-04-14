package de.hft_stuttgart.spirit;

import java.util.List;

public class StoryEngine implements SpiritStoryEngine {
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

	public StoryEngine(UIController spiritFacade) {
		facade = spiritFacade;
	}

	@Override
	public void update() {

		// bei erstem Start Playlist erstellen
		if (firstStart) {

			/*
			 * Konstruktor mit String, kann später mit
			 * facade.getClosestGhost().getProvider() wieder aufgerufen werden,
			 * um den gefundenen Geist zu identifizieren.
			 */

			Location ghost = new Location("Stuttgart Stadtpark Mitte");
			// stuttgart stadtpark
			ghost.setLatitude(48.781143);
			ghost.setLongitude(9.173864);
			facade.addGhostLocation(ghost);
			ghost = new Location("HfT Stuttgart Eingang");
			ghost.setLatitude(48.780554);
			ghost.setLongitude(9.173248);
			facade.addGhostLocation(ghost);
			ghost = new Location("HfT Stuttgart Innenhof");
			ghost.setLatitude(48.780313);
			ghost.setLongitude(9.172541);
			facade.addGhostLocation(ghost);
			ghost = new Location("Wiesbaden HSRM");
			ghost.setLatitude(50.096967);
			ghost.setLongitude(8.217187);
			facade.addGhostLocation(ghost);
			firstStart = false;
			facade.showAllGhostNamesInRadar();
			// for (int i = 0;i<6;i++){
			// facade.setButtonText("Test "+i, i);
			// }
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
				System.out.println("Film Ende");
				if (facade.getQueueSize() == 0) {
					facade.setButtonText("Neustart", 2);
					facade.setButtonText("Kein Neustart", 3);
				}
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
				// facade.startFadeEffect();
				facade.startFilm();
				facade.disableArrow();
			}

			// Log.w("SPIRIT",""+facade.getClosestGhost().getProvider());

			// if (facade.getDistanceUserToGhost() < 15) {
			// ghostDetected = true; // nicht nochmal prüfen
			// facade.switchToARView(); // auf Kamera wechseln
			// facade.createReference(); // Referenzbild erstellen für die
			// // Platzierung der Videos
			// facade.startFilm(); // erstes Video manuell starten
			// facade.vibrate(vibratePattern); // und vibrieren
			// }
			if (facade.newNfcTagDiscovered()){
				NfcTagDetected[] tags = facade.getNfcTags();
				System.out.println("Storyengine: "+tags.length+" Tags gefunden xD");
				int i = 0;
				for (NfcTagDetected tag : tags){
					System.out.println(i+": "+tag.tag);
					i++;
					facade.removeAllVideos();
					facade.createReference();
					PlaylistEntry newEntry = new PlaylistEntry("xx_demo_3_Soldat_ErschrecktSichHandAnSchwert_front.mp4");
					newEntry.setAutostart(true);
					facade.enqueueVideo(newEntry);
				}
			}
		}
	}

	private void setGuiStuff() {

	}

	public void createPlaylist() {
		createCurrentPlaylist();
		List<Poi> places = facade.getArmlFromAssets("ARMLfile.xml");
		
		// --- Beispiel ---
		for (Poi p : places){
			System.out.println("" + p.getName() + ": "
					+ p.getLatitude() + "/" + p.getLongitude()
					+ "Video:" + p.getVideo());
		}
		
		// PlaylistEntry newEntry = new PlaylistEntry("Geist_alpha.mp4");
		// newEntry.setAutostart(false);
		// facade.enqueueVideo(newEntry);
		// newEntry = new PlaylistEntry("Geist_alpha2.mp4");
		// facade.enqueueVideo(newEntry);

		// PlaylistEntry newEntry = new PlaylistEntry("Building two_2.mp4");
		// facade.enqueueVideo(newEntry);
		// newEntry = new PlaylistEntry("Library_2.mp4");
		// facade.enqueueVideo(newEntry);
		// newEntry = new PlaylistEntry("Main Entrance_2.mp4");
		// facade.enqueueVideo(newEntry);
		// newEntry = new PlaylistEntry("Mesa_2.mp4");
		// facade.enqueueVideo(newEntry);
		// newEntry = new PlaylistEntry("Schlossplatz_2.mp4");
		// facade.enqueueVideo(newEntry);
		// newEntry = new PlaylistEntry("Welcome to Stuttgart_2.mp4");
		// facade.enqueueVideo(newEntry);
	}

	/*
	 * Playlist zum aktuellen internationalen Demonstrator
	 */
	private void createCurrentPlaylist() {
		PlaylistEntry newEntry = new PlaylistEntry(
				"BeamEffekt_nurEffekt_ohneGeist_17.09.14.mp4");
		newEntry.setAutostart(false);
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry(
				"la_demo_5_Soldat_zaehltInventarOhneEinsteckenBuch_front.mp4");
		newEntry.statusText = "Scanning...";
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry(
				"xx_demo_3_Soldat_ErschrecktSichHandAnSchwert_front.mp4");
		newEntry.statusText = "Identified.";
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry(
				"la_demo_5_Soldat_erschrecktSichFragtWoArmamentariaSindHandAnSchwert_front.mp4");
		newEntry.statusText = "Latin.";
		newEntry.buttonText = "translate";
		newEntry.subtitle = "Gods, you can see me?";
		newEntry.infoText = " ";
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry(
				"la_demo_5_Soldat_fragtSprichstDuLateinischeSprache_frontal.mp4");
		newEntry.buttonText = "translate";
		newEntry.infoText = " ";
		newEntry.subtitle = "I salute you. Do you speak Latin?";
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry("xx_demo_5_schautfragendwartend.mp4");
		newEntry.setLoop(true);
		newEntry.buttonText = "ask for english";
		newEntry.infoText = " ";
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry(
				"en_demo_5_Soldat_fragtSprichstDuLateinischeSprache_frontal.mp4");
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry("xx_demo_5_schautfragendwartend.mp4");
		newEntry.setLoop(true);
		newEntry.buttonText = "say no";
		newEntry.infoText = " ";
		facade.enqueueVideo(newEntry);
		// newEntry = new PlaylistEntry(
		// "en_demo__Soldat_fragtWasHastDuFuerGeraet_front.mp4");
		// facade.enqueueVideo(newEntry);
		// newEntry = new PlaylistEntry(
		// "en_demo_5_Soldat_fragtNachWunderdingDruiden_front.mp4");
		// facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry(
				"en_demo_5_Soldat_fragtNachGeraet+Wunderding_2VideosZusammen.mp4");
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry("xx_demo_5_schautfragendwartend.mp4");
		newEntry.setLoop(true);
		newEntry.buttonText = "say yes";
		newEntry.infoText = " ";
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry(
				"en_demo_5_Soldat_fordertUserAufDieSaalburgZuBesuchen_front.mp4");
		facade.enqueueVideo(newEntry);
		newEntry = new PlaylistEntry(
				"xx_demo_5_Soldat_DrehtSichUmUndLaeuftWeg_ohneSchritte.mp4");
		facade.enqueueVideo(newEntry);

	}

}
