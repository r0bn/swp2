package de.hft_stuttgart.spirit;

/**
 * Jede Storyengine Implementierung muss dieses Interface implementieren.
 */

public interface SpiritStoryEngine {
	/**
	 * Diese update() Methode wird regelmäßig von der App aufgerufen.
	 * Aktualisierungen der Storyengine hier einbauen. Rechenintensive Aufgaben
	 * möglichst in Threads auslagern. -> Hier die Facade-Aufrufe durchführen.
	 * Beispiele siehe StoryEngine.java
	 */
	public void update();

	/**
	 * Storyengine zurücksetzen.
	 * Vor allem für Entwicklungszwecke, so dass die App beim testen nicht
	 * immer neu gestartet werden muss.
	 */
	public void reset();
}
