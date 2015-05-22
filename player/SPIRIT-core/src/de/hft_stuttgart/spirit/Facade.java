package de.hft_stuttgart.spirit;

import java.util.List;

import de.hft_stuttgart.storytellar.PlayableStory;
import de.hft_stuttgart.storytellar.StorytellAR_StoryEngine;

public class Facade implements UIController {

	SpiritApp app; // App Implementierung
	SpiritStoryEngine engine; // Storyengine Implementierung

	public Facade(SpiritApp app, PlayableStory story) {
		this.app = app;
		engine = new StorytellAR_StoryEngine(this, story);
		//engine = new StoryEngine(this); // Hier andere Storyengine-Implementierung starten
	}

	@Override
	public void enqueueVideo(PlaylistEntry pe) {
		app.getPlaylist().addFilm(pe);
	}

	@Override
	public void removeAllVideos() {
		app.getPlaylist().resetPlaylist();
	}

	@Override
	public void updateStoryEngine() {
		engine.update();
	}

	@Override
	public Location getUserLocation() {
		return app.getGeoTools().getLocation();
	}

	@Override
	public float getDistanceUserToClosestGhost() {
		return app.getGeoTools().getDistance(app.getClosestGhost());
	}

	@Override
	public int getQueueSize() {
		return app.getPlaylist().getRemainingPlaylistEntries();
	}

	@Override
	public int getFilmPosition() {
		if (app.getFilm() != null) {
			return app.getFilm().getPosition();
		} else {
			return -1;
		}
	}

	@Override
	public int getFilmDuration() {
		if (app.getFilm() != null) {
			return app.getFilm().getDuration();
		} else {
			return -1;
		}
	}

	@Override
	public void resetStoryEngine() {
		engine.reset();
	}

	@Override
	public void stopFilm() {
		app.stopFilm();
	}

	@Override
	public void startFilm() {
		app.startFilm();
	}

	@Override
	public void createReference() {
		app.createReference();
	}

	@Override
	public void vibrate(int time) {
		app.vibrate(time);
	}

	@Override
	public void vibrate(long[] pattern) {
		app.vibrate(pattern);
	}

	@Override
	public boolean isFilmPlaying() {
		if (app.getFilm() != null) {
			return app.getFilm().isPlaying();
		} else {
			return false;
		}
	}

	@Override
	public SpiritEvent[] getEvents() {
		return app.getEvents();
	}

	@Override
	public void nextFilm() {
		app.nextFilm();
	}

	public void resetAll() {
		app.resetAll();
	}

	@Override
	public void switchToSonarView() {
		app.switchToSonarView();
	}

	@Override
	public void cancelVibrate() {
		app.cancelVibrate();
	}

	@Override
	public void switchToARView() {
		app.switchToARView();
	}

	@Override
	public float getTabletAngle() {
		return app.getTabletAngle();
	}

	@Override
	public void startFadeEffect() {
		app.startFadeEffect();
	}

	@Override
	public void enableArrow() {
		app.enableArrow();
	}

	@Override
	public void disableArrow() {
		app.disableArrow();
	}

	@Override
	public void startSignalToGhostEffect(long time) {
		app.startSignalToGhostEffect(time);
	}

	@Override
	public void enableSubtitle() {
		app.enableSubtitle();
	}

	@Override
	public void addGhostLocation(Location ghost) {
		app.addGhostLocation(ghost);
	}

	@Override
	public Location[] getGhostLocations() {
		return app.getGhostLocations();
	}

	@Override
	public Location getClosestGhost() {
		return app.getClosestGhost();
	}

	@Override
	public void showAllGhostNamesInRadar() {
		app.showAllGhostNamesInRadar();
	}

	@Override
	public void showGostnameInRader(Location ghost) {
		app.showGostnameInRader(ghost);
	}

	@Override
	public void hideAllGhostNamesInRadar() {
		app.hideAllGhostNamesInRadar();
	}

	@Override
	public void hideGostnameInRader(Location ghost) {
		app.hideGostnameInRader(ghost);
	}

	@Override
	public void setButtonText(String text, int id) {
		app.setCustomTextButton(text, id);
	}

	@Override
	public void deleteAllGhosts() {
		app.deleteAllGhosts();
	}

	@Override
	public void deleteGhost(Location ghost) {
		app.deleteGhost(ghost);
	}

	@Override
	public boolean newNfcTagDiscovered() {
		return app.newNfcTagDiscovered();
	}

	@Override
	public NfcTagDetected[] getNfcTags() {
		return app.getNfcTags();
	}

	@Override
	public void openUrl(String url) {
		app.openUrl(url);
	}

	@Override
	public void setBrowserAlpha(float alpha) {
		app.setBrowserAlpha(alpha);
	}

	@Override
	public void hideBrowser() {
		app.hideBrowser();
	}

	@Override
	public void setBrowserSize(float width, float height) {
		app.setBrowserSize(width, height);
	}

	@Override
	public List<Poi> getArmlFromAssets(String filename) {
		return app.getArmlFromAssets(filename);
	}

	@Override
	public List<Poi> getArmlFromSdCard(String fullPath) {
		return app.getArmlFromSdCard(fullPath);
	}

}
