package de.hft_stuttgart.spirit;

public class SpiritEvent {
/*
 * Filmstart: Abspielen eines Filmes wurde gestartet
 * Filmende: Filmwiedergabe beendet
 * NotSkipButtonPressed: Der Button rechts unten im Demonstrator wurde gedrückt (aktuell "translate")
 * SkipButtonPressed: zur Zeit nicht in Benutzung
 * SonarButtonPressed: zur Zeit nicht in Benutzung
 * StartManuellButtonPressed: zur Zeit nicht in Benutzung 
 * ResetAllButtonPressed: zur Zeit nicht in Benutzung
 * FadeEffectStarted: Der Fadeeffekt zwischen 2 Videos wurde gestartet
 * FadeEffectStopped: Wiedergabe Fadeeffekt beendet
 * CustomButton0: CustomButton0 wurde gedrückt
 * CustomButton1: CustomButton1 wurde gedrückt
 * CustomButton2: CustomButton2 wurde gedrückt
 * CustomButton3: CustomButton3 wurde gedrückt
 * CustomButton4: CustomButton4 wurde gedrückt
 * CustomButton5: CustomButton5 wurde gedrückt
 */
	public enum Event {
		Filmstart, Filmende, NotSkipButtonPressed, SkipButtonPressed, SonarButtonPressed, StartManuellButtonPressed, ResetAllButtonPressed, FadeEffectStarted, FadeEffectStopped, CustomButton0, CustomButton1, CustomButton2, CustomButton3, CustomButton4, CustomButton5
	};

	private Event event;
	
	public SpiritEvent(Event event) {
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}
	
}
