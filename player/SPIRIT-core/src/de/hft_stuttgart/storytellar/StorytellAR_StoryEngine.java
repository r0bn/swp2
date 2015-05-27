package de.hft_stuttgart.storytellar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hft_stuttgart.spirit.Location;
import de.hft_stuttgart.spirit.NfcTagDetected;
import de.hft_stuttgart.spirit.PlaylistEntry;
import de.hft_stuttgart.spirit.Poi;
import de.hft_stuttgart.spirit.SpiritEvent;
import de.hft_stuttgart.spirit.SpiritStoryEngine;
import de.hft_stuttgart.spirit.UIController;

public class StorytellAR_StoryEngine implements SpiritStoryEngine {
	
	enum EngineStates {
		OPEN,IN_SCENE
	}

	PlayableStory story;
	UIController facade;
	boolean doIt = true;
	EngineStates state = EngineStates.OPEN;
	StoryPoint activeStoryPoint;

	public StorytellAR_StoryEngine(UIController spiritFacade, PlayableStory story) {
		facade = spiritFacade;
		this.story = story;
		
		
		addOpenStoryPointsToSonar();
		facade.showAllGhostNamesInRadar();
	}
	
	//Wird nach jedem Frame aufgerufen
	@Override
	public void update() {
		switch (state) {
		case OPEN:			
			Location loc = facade.getClosestGhost();
			Map<String,StoryPoint> sps = story.getStorypoints();
			StoryPoint closest = sps.get(loc.name);
			if (facade.getDistanceUserToClosestGhost() > 10) {
				activeStoryPoint = closest;
				activeStoryPoint.setStatus(StorypointStatus.ACTIVE);
				state = EngineStates.IN_SCENE;
				playVideo(closest);
			}
			
			// AR / Sonar wechsel durch Tabletwinkel
			if (facade.getTabletAngle() < 30) {
				facade.switchToSonarView();
			} else {
				facade.switchToARView();
			}
			break;
		case IN_SCENE:
			break;
		default:
			break;
		}
		
		checkEvents();
	}

	@Override
	public void reset() {

	}

	private void checkEvents() {
		// Events bearbeiten
		SpiritEvent[] events = facade.getEvents();
		for (int i = 0; i < events.length; i++) {
			switch (events[i].getEvent()) {
			case Filmende:
				activeStoryPoint.setStatus(StorypointStatus.DONE);
				state = EngineStates.OPEN;
				addOpenStoryPointsToSonar();
				break;
			case Filmstart:
				break;
			case ResetAllButtonPressed:
				break;
			case SkipButtonPressed:
				break;
			case NotSkipButtonPressed:
				break;
			case SonarButtonPressed:
				break;
			case StartManuellButtonPressed:
				break;
			case FadeEffectStarted:
				break;
			case FadeEffectStopped:
				break;
			case CustomButton0:
				break;
			case CustomButton1:
				break;
			case CustomButton2:
				break;
			case CustomButton3:
				break;
			case CustomButton4:
				break;
			case CustomButton5:
				facade.createReference();
				facade.startFilm();
				break;

			default:
				break;

			}
		}

	}
	
	private void addOpenStoryPointsToSonar() {
		facade.deleteAllGhosts();
		for(StoryPoint storyPoint : story.getStorypoints().values()) {
			if(storyPoint.getStatus() == StorypointStatus.OPEN) {
				Location loc = new Location(storyPoint.getName());
				loc.setLatitude(storyPoint.getLatitude());
				loc.setLongitude(storyPoint.getLongitude());
				facade.addGhostLocation(loc);
			}
		}
	}
	
	private void playVideo(StoryPoint storyPoint) {
		storyPoint.setStatus(StorypointStatus.ACTIVE);
		PlaylistEntry pe = new PlaylistEntry(storyPoint.getVideo());
		pe.setLoop(false);
		pe.setAutostart(false);
		facade.enqueueVideo(pe);
		facade.setButtonText(storyPoint.getName(), 5);
	}

}
