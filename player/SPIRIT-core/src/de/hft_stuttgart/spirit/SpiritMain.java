package de.hft_stuttgart.spirit;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier.FaceDirection;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

import de.hft_stuttgart.spirit.SpiritEvent.Event;
import de.hft_stuttgart.spirit.TextButton.positionA;
import de.hft_stuttgart.spirit.TextButton.positionB;
import de.hft_stuttgart.storytellar.PlayableStory;
import de.hft_stuttgart.storytellar.StoryXMLParser;

public class SpiritMain extends ApplicationAdapter implements
		FilmStartedCallback, SpiritApp {

	public PerspectiveCamera cam;
	public Model model;
	public Environment environment;
	public CameraInputController camController;
	ModelInstance instance;
	public ModelBatch modelBatch;
	BitmapFont font;
	Sprite webViewX;

	float x = 0f;
	float y = 0f;
	float z = 0f;
	Vuforia vuforia;
	SpiritFilm spiritFilm;
	ArrayList<SpiritEvent> events;
	Playlist playlist = new Playlist();
	boolean startFilm = false;
	boolean resumeFilm = false;

	GeoTools geoTools;
	ArrayList<GhostLocation> ghosts = new ArrayList<GhostLocation>();
	Facade spiritFacade;

	TextureAtlas guiAtlas;
	TextureAtlas sonarAtlas;
	Gui gui;
	Sonar sonar;
	private SpriteBatch batch;
	private SpiritState state = SpiritState.AR;
	private NfcInterface nfc;
	private ArmlParser armlParser;
	long lastVuforiaTrackableFound = 0;
	SpiritWebviewHandler webview;
	SignalToGhostEffect signalToGhostEffect;
	FadeEffect fadeEffect;
	TextButton[] customTextButton;
	String storyXMLPath;	//Storytellar
	PlayableStory story;	//Storytellar
	
	public SpiritMain(Vuforia v, SpiritFilm spiritFilm, SpiritGeoTools sgt,
			NfcInterface nfc, SpiritWebviewHandler webview, ArmlParser arml) {
		vuforia = v;
		this.spiritFilm = spiritFilm;
		geoTools = new GeoTools(sgt);
		this.nfc = nfc;
		this.webview = webview;
		armlParser = arml;
	}
	
	//Storytellar
	public SpiritMain(Vuforia v, SpiritFilm spiritFilm, SpiritGeoTools sgt,
			NfcInterface nfc, SpiritWebviewHandler webview, ArmlParser arml, String storyXMLPath) {
		vuforia = v;
		this.spiritFilm = spiritFilm;
		geoTools = new GeoTools(sgt);
		this.nfc = nfc;
		this.webview = webview;
		armlParser = arml;
		this.storyXMLPath = storyXMLPath;	
	}

	@Override
	public void create() {
		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(5f, 5f, 5f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		vuforia.onSurfaceCreated();
		spiritFilm.setup();
		events = new ArrayList<SpiritEvent>();
		
		story = new StoryXMLParser().parse(storyXMLPath);	//Storytellar
			
		spiritFacade = new Facade(this, story);
		guiAtlas = new TextureAtlas(Gdx.files.internal("gui.pack"));
		sonarAtlas = new TextureAtlas(Gdx.files.internal("sonar.pack"));
		webViewX = guiAtlas.createSprite("stopp");
		float scale = 0.1f * Gdx.graphics.getHeight() / webViewX.getHeight();
		webViewX.setSize(scale * webViewX.getWidth(),
				scale * webViewX.getHeight());
		font = new BitmapFont(Gdx.files.internal("roboto.fnt"),
				Gdx.files.internal("roboto.png"), false);
		gui = new Gui(guiAtlas, font);
		sonar = new Sonar(sonarAtlas, font, geoTools);
		signalToGhostEffect = new SignalToGhostEffect(guiAtlas);
		fadeEffect = new FadeEffect();
		batch = new SpriteBatch();
		customTextButton = new TextButton[6];
		customTextButton[0] = new TextButton(guiAtlas, font,positionA.Left,positionB.TOP);
		customTextButton[1] = new TextButton(guiAtlas, font,positionA.Right,positionB.TOP);
		customTextButton[2] = new TextButton(guiAtlas, font,positionA.Left,positionB.CENTER);
		customTextButton[3] = new TextButton(guiAtlas, font,positionA.Right,positionB.CENTER);
		customTextButton[4] = new TextButton(guiAtlas, font,positionA.Left,positionB.BOTTOM);
		customTextButton[5] = new TextButton(guiAtlas, font,positionA.Right,positionB.BOTTOM);
	}

	boolean workaround = false;


	@Override
	public void render() {
		
		if (Gdx.input.justTouched()) {
			handleTouch();
		}

		if (!workaround && vuforia.isReady()) {
			workaround = true;
			vuforia.onSurfaceCreated();
			vuforia.onSurfaceChanged(Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
		}

		// erst logik usw
		spiritLogik();

		// dann alles malen :-)
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (state == SpiritState.AR) {
			vuforia.draw();
		}

		if (vuforia.trackableFound()) {
			lastVuforiaTrackableFound = System.currentTimeMillis();
			if (state == SpiritState.AR) {
				spiritFilm.draw(vuforia.getVuforiaProjectionMatrix(),
						vuforia.getModelViewMatrix(),
						vuforia.getPointOnScreen());
			}
		} else {
			if (System.currentTimeMillis() - lastVuforiaTrackableFound > 2000) {
				/*
				 * wenn mehr als 2 sekunden keine trackable gefunden wurde und
				 * aktuell auch ein film läuft -> neue trackable erstellen
				 */
				if (spiritFilm.isPlaying()) {
					createReference();
					lastVuforiaTrackableFound = System.currentTimeMillis(); // verhindern
																			// dass
																			// dies
																			// zu
																			// oft
																			// aufgerufen
																			// wird
				}
			}
		}

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		camController.update();

		modelBatch.begin(cam);
		// modelBatch.render(instance, environment);
		modelBatch.end();

		batch.begin();
		if (state == SpiritState.AR) {
			gui.draw(batch);

		} else if (state == SpiritState.SONAR) {
			sonar.draw(batch, ghosts);
		}

		fadeEffect.draw(batch,vuforia.getTargetRectangle());
		signalToGhostEffect.draw(batch,vuforia.getTargetRectangle());
		
		for (int i = 0;i<customTextButton.length;i++){
			if (customTextButton[i].isActive){
				customTextButton[i].draw(batch);
			}
		}
		
		if (webview.isWebviewVisible()) {
			webViewX.setPosition(
					Gdx.graphics.getWidth() / 2f + webview.getWebviewWidth()
							/ 2f, webview.getWebviewHeight());
			webViewX.draw(batch);
		}

		batch.end();
	}

	private void handleTouch() {
		// System.out.println("Touch: " + Gdx.input.getX() + "/"
		// + Gdx.input.getY());
		// // Menü
		if (isWeiterButtonTouched()) {
			if (spiritFilm.getPlaylistEntry().isLoopEnabled()) {
				// wenn film im loop-modus -> button bedeutet weiter
				events.add(new SpiritEvent(Event.SkipButtonPressed));
			} else {
				// wenn film nicht im loop-modus -> button bedeutet nicht weiter
				// (meistens untertitel an o.ä.)
				events.add(new SpiritEvent(Event.NotSkipButtonPressed));
			}
		}
		// CustomTextButtons
		if (isCustomButtonPressed(0)) {
			events.add(new SpiritEvent(Event.CustomButton0));
			disableAllCustomTextButtons();
		}
		if (isCustomButtonPressed(1)) {
			events.add(new SpiritEvent(Event.CustomButton1));
			disableAllCustomTextButtons();
		}
		if (isCustomButtonPressed(2)) {
			events.add(new SpiritEvent(Event.CustomButton2));
			disableAllCustomTextButtons();
		}
		if (isCustomButtonPressed(3)) {
			events.add(new SpiritEvent(Event.CustomButton3));
			disableAllCustomTextButtons();
		}
		if (isCustomButtonPressed(4)) {
			events.add(new SpiritEvent(Event.CustomButton4));
			disableAllCustomTextButtons();
		}
		if (isCustomButtonPressed(5)) {
			events.add(new SpiritEvent(Event.CustomButton5));
			disableAllCustomTextButtons();
		}

		// webviewbutton
		if (webview.isWebviewVisible()) {
			if (webViewX.getBoundingRectangle().contains(Gdx.input.getX(),
					Gdx.graphics.getHeight() - Gdx.input.getY())) {
				hideBrowser();
			}
		}

	}

	public boolean isWeiterButtonTouched() {
		return gui.isWeiterButtonTouched();
	}

	public boolean isCustomButtonPressed(int nummer) {
		return customTextButton[nummer].isButtonPressed(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY());
	}

	private void disableAllCustomTextButtons() {
		 for (int i = 0; i < customTextButton.length; i++) {
		 customTextButton[i].setActive(false);
		 }
	}

	public void spiritLogik() {
		// storyengine usw aktualisieren
		spiritFacade.updateStoryEngine();

		// Filme starten und stoppen
		// Fall 1: Film abbrechen
		if (!spiritFilm.isNull()) {
			if (spiritFilm.isPlaying()) {
				if (spiritFilm.getPlaylistEntry().cancelVideoAtPosition > 0) {
					if (spiritFilm.getPosition() > spiritFilm
							.getPlaylistEntry().cancelVideoAtPosition) {
						filmFinished(); // zerstören + event auslösen
					}
				}
			}
		} else {
			if (startFilm) {
				if (playlist.isNextFilmAvailable()) {
					// Fall 2: Film gezielt starten
					spiritFilm.prepareNextFilm(this);
					spiritFilm.startFilm(playlist.getNextPlaylistEntry(), 0);
					gui.statusText = spiritFilm.getPlaylistEntry().statusText;
					gui.buttonText = spiritFilm.getPlaylistEntry().buttonText;
					gui.infoText = spiritFilm.getPlaylistEntry().infoText;

				}
				startFilm = false;
			} else if (resumeFilm) {
				// Fall 3: Resume?
				// TODO
			} else if (playlist.isNextFilmAvailable()
					&& playlist.isNextFilmAutostart()) {
				// Fall 4: Autostart
				startFilm = true;
			}
		}
		
		
		// nebel starten und stoppen
//		if (!spiritFilm.isNull() && spiritFilm.isPlaying()){
//			if (spiritFilm.getPosition() > spiritFilm.getDuration() - 500){
//				if (!fadeEffect.isActive){
//					startFadeEffect();
//				}
//			}else if (spiritFilm.getPosition() > 500){
//				if (fadeEffect.isActive){
//					stopFadeEffect();
//				}
//			}
//		}
//		if(spiritFilm.isNull() && !spiritFilm.isPlaying() && !playlist.isNextFilmAvailable()){
//			stopFadeEffect();
//		}

	}

	@Override
	public void dispose() {
		model.dispose();
		vuforia.onDestroy();
		guiAtlas.dispose();
		sonarAtlas.dispose();
		batch.dispose();
		font.dispose();
		fadeEffect.dispose();
	}

	@Override
	public void resume() {
		vuforia.onResume();
	}

	@Override
	public void resize(int width, int height) {
		vuforia.onSurfaceChanged(width, height);
		spiritFilm.calcMatrix(width, height);
	}

	@Override
	public void pause() {
		vuforia.onPause();
		spiritFilm.destroy();
	}

	// SPIRIT APP Interfaces -> Zugriff StoryEngine usw
	@Override
	public void filmStarted() {
		events.add(new SpiritEvent(Event.Filmstart));
	}

	@Override
	public Playlist getPlaylist() {
		return playlist;
	}

	@Override
	public void deleteAllGhosts() {
		ghosts = new ArrayList<GhostLocation>();
	}

	@Override
	public Gui getGui() {
		return gui;
	}

	@Override
	public SpiritFilm getFilm() {
		return spiritFilm;
	}

	@Override
	public void stopFilm() {
		spiritFilm.destroy();
	}

	@Override
	public void startFilm() {
		startFilm = true;
	}

	@Override
	public void nextFilm() {
		stopFilm();
		startFilm();
	}

	@Override
	public void resetAll() {
		spiritFacade.removeAllVideos();
		spiritFacade.stopFilm();
		spiritFacade.resetStoryEngine();
		startFilm = false;
		stopFadeEffect();
	}

	@Override
	public void switchToSonarView() {
		state = SpiritState.SONAR;
	}

	@Override
	public void switchToARView() {
		state = SpiritState.AR;
	}

	@Override
	public void createReference() {
		vuforia.createNewReferencePicture();
	}

	@Override
	public void vibrate(int time) {
		Gdx.input.vibrate(time);
	}

	@Override
	public void vibrate(long[] pattern) {
		Gdx.input.vibrate(pattern, -1);
	}

	@Override
	public void cancelVibrate() {
		Gdx.input.cancelVibrate();
	}

	@Override
	public SpiritEvent[] getEvents() {
		SpiritEvent[] se = new SpiritEvent[events.size()];
		for (int i = 0; i < events.size(); i++) {
			se[i] = events.get(i);
		}
		// alle events abgerufen -> leeren
		events.clear();
		return se;
	}

	@Override
	public float getTabletAngle() {
		// System.out.println("Roll: "+Gdx.input.getRoll()+" Azimuth: "+Gdx.input.getAzimuth()+" Pitch: "+Gdx.input.getPitch());
		// System.out.println("Accelerator: "+Gdx.input.getAccelerometerX()+"/"+Gdx.input.getAccelerometerY()+"/"+Gdx.input.getAccelerometerZ());
		return geoTools.sgt.getWinkel();
		// return Math.abs(Gdx.input.getRoll());
	}

	@Override
	public void startFadeEffect() {
		fadeEffect.start();
		events.add(new SpiritEvent(Event.FadeEffectStarted));

	}

	@Override
	public void disableArrow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableArrow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startSignalToGhostEffect(long time) {
		signalToGhostEffect.startEffect(time);
	}

	@Override
	public void enableSubtitle() {
		gui.infoText = spiritFilm.getPlaylistEntry().subtitle;
	}

	@Override
	public void showAllGhostNamesInRadar() {
		for (GhostLocation g : ghosts) {
			g.showNameInRadar = true;
		}
	}

	@Override
	public void hideAllGhostNamesInRadar() {
		for (GhostLocation g : ghosts) {
			g.showNameInRadar = false;
		}
	}

	@Override
	public void setCustomTextButton(String text, int button) {
		if (button < customTextButton.length && button >= 0){
			customTextButton[button].setActive(true);
			customTextButton[button].setText(text);
		}
	}

	@Override
	public void addGhostLocation(Location ghost) {
		ghosts.add(new GhostLocation(ghost));
	}

	@Override
	public void deleteGhost(Location ghost) {
		GhostLocation toRemove = null;

		for (GhostLocation check : ghosts) {
			if (check.location == ghost) {
				toRemove = check;
			}
		}

		if (toRemove != null && ghosts.size() > 0) {
			ghosts.remove(toRemove);
		}
	}

	@Override
	public void showGostnameInRader(Location ghost) {
		for (GhostLocation g : ghosts) {
			if (g.location == ghost) {
				g.showNameInRadar = true;
			}
		}
	}

	@Override
	public void hideGostnameInRader(Location ghost) {
		for (GhostLocation g : ghosts) {
			if (g.location == ghost) {
				g.showNameInRadar = false;
			}
		}
	}

	@Override
	public GeoTools getGeoTools() {
		return geoTools;
	}

	@Override
	public Location[] getGhostLocations() {
		return (Location[]) ghosts.toArray();
	}

	@Override
	public Location getClosestGhost() {
		Location closestGhost = null;

		for (int g = 0; g < ghosts.size(); g++) {
			if (closestGhost == null) {
				closestGhost = ghosts.get(g).location;
			} else {
				if (geoTools.getDistance(ghosts.get(g).location) < geoTools
						.getDistance(closestGhost)) {
					closestGhost = ghosts.get(g).location;
				}
			}
		}
		return closestGhost;
	}

	@Override
	public void filmFinished() {
		spiritFilm.destroy();
		events.add(new SpiritEvent(Event.Filmende));
		gui.statusText = "";
		gui.buttonText = "";
		gui.infoText = "";
	}

	@Override
	public boolean newNfcTagDiscovered() {
		return nfc.newTagDetected();
	}

	@Override
	public NfcTagDetected[] getNfcTags() {
		return nfc.getTags();
	}

	@Override
	public void openUrl(String url) {
		webview.setWebviewVisible(true);
		webview.openUrl(url);

	}

	@Override
	public void setBrowserAlpha(float alpha) {
		webview.setAlpha(alpha);
	}

	@Override
	public void hideBrowser() {
		webview.setWebviewVisible(false);
	}

	@Override
	public void setBrowserSize(float width, float height) {
		webview.setWebviewSize((int) (Gdx.graphics.getWidth() * width),
				(int) (Gdx.graphics.getHeight() * height));

	}

	private void stopFadeEffect() {
		fadeEffect.stop();
		events.add(new SpiritEvent(Event.FadeEffectStopped));
	}

	@Override
	public List<Poi> getArmlFromAssets(String filename) {
		return armlParser.getPlacesFromAssets(filename);
	}

	@Override
	public List<Poi> getArmlFromSdCard(String fullPath) {
		return armlParser.getPlacesFromSdCard(fullPath);
	}

	/** Storytellar
	 * @return the story
	 */
	public PlayableStory getStory() {
		return story;
	}

	/** Storytellar
	 * @param story the story to set
	 */
	public void setStory(PlayableStory story) {
		this.story = story;
	}
}
