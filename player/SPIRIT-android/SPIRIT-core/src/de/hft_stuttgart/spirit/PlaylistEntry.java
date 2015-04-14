package de.hft_stuttgart.spirit;

public class PlaylistEntry {
	private int id = -1;
	private String url = "";
	private boolean isUrl = false;
	private boolean loop = false;
	private boolean autostart = true;
	private boolean simpleDrawOnScreen = false;
	public String buttonText = "";
	public String infoText = "";
	public String statusText = "";
	public String subtitle = "";
	public long cancelVideoAtPosition = 0;
	/**
	 * Erstellt einen Eintrag für die Video Playlist
	 * 
	 * @param id
	 *            Android Ressourcen Id, z.b. R.raw.foo
	 */
	public PlaylistEntry(int id) {
		this.id = id;
	}

	/**
	 * Erstellt einen Eintrag für die Video Playlist
	 * 
	 * @param url
	 *            eine Url zu einem streambaren Video
	 */
	public PlaylistEntry(String url) {
		this.url = url;
		isUrl = true;
	}

	/**
	 * Gibt die Id der Android Ressource zurück. -1 wenn nicht gesetzt.
	 * 
	 * @return Ressourcen Id
	 */
	public int getId() {
		return id;
	}
	/**
	 * 
	 * @return Url zum Video
	 */
	public String getUrl(){
		return url;
	}

	/**
	 * @return 
	 * true wenn URL, false wenn Id	
	 */
	public boolean isUrl(){
		return isUrl;
	}
	
	/**
	 * @return
	 * loop: Video wird bis zum Abbruch wiederholt
	 * true oder false
	 */
	public boolean isLoopEnabled(){
		return loop;
	}
	
	/**
	 * @return
	 * true: Video wird automatisch gestartet wenn es der erste Playlisteneintrag ist
	 * und das vorherige Video beendet ist.
	 */
	public boolean isAutostartEnabled(){
		return autostart;
	}
	
	/**
	 * 
	 * @return
	 * true: Das Video wird normal zentriert auf dem Bildschirm dargestellt
	 * false: Das Video wird an ein Kamerabild gebunden
	 */
	public boolean isSimpleDrawOnScreenEnabled(){
		return simpleDrawOnScreen;
	}
	
	/**
	 * Wenn autostart true ist, wird das Video automatisch gestartet,
	 * sobald es an erster Stelle in der Playlist Queue steht und 
	 * kein anderes Video abgespielt wird.
	 * Wenn es true ist muss es manuell gestartet werden.
	 * Default ist true
	 * @param autostart Automatischer Videostart
	 */
	public void setAutostart(boolean autostart){
		this.autostart = autostart;
	}
	
	/**
	 * Wenn loop true ist, wird das Video so lange wiederholt, bis es abgebrochen wird.
	 * Ist loop false, wird das Video genau 1 mal abgespielt und dann beendet.
	 * Default ist false
	 * @param loop Dauerschleife an oder aus
	 */
	public void setLoop(boolean loop){
		this.loop = loop;
	}
	
	/**
	 * Gibt an, ob das Video an ein Bild gebunden und entsprechend skaliert wird oder ob es einfach 
	 * zentriert auf dem Bildschirm wiedergegeben wird.
	 * True: Zentriert auf dem Bildschirm
	 * False: An (Kamera-)Bild gebunden
	 * Default ist false
	 * @param simpleDrawOnScreen Wiedergabe "Vollbild" zentriert
	 */
	public void setSimpleDrawOnScreen(boolean simpleDrawOnScreen){
		this.simpleDrawOnScreen = simpleDrawOnScreen;
	}
}
