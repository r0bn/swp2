package de.hft_stuttgart.storytellar;

import java.util.List;
import java.util.Map;

import de.hft_stuttgart.spirit.Location;
import de.hft_stuttgart.spirit.PlaylistEntry;
import de.hft_stuttgart.spirit.SpiritEvent;
import de.hft_stuttgart.spirit.SpiritStoryEngine;
import de.hft_stuttgart.spirit.UIController;

public class StorytellAR_StoryEngine implements SpiritStoryEngine {

	private enum EngineStates {
		OPEN, 
		IN_SCENE_START, 
		IN_SCENE,
		IN_SCENE_END,
		IN_SCENE_PICTURE_START, 
		IN_SCENE_PICTURE, 
		IN_SCENE_PICTURE_END, 
		IN_SCENE_VIDEO_START, 
		IN_SCENE_VIDEO, 
		IN_SCENE_VIDEO_END,
		IN_SCENE_INTERACTION_START,
		IN_SCENE_INTERACTION_QUIZ,
		IN_SCENE_INTERACTION_ITEM,
		IN_SCENE_INTERACTION_CHOOSER
	}

	PlayableStory story;
	UIController facade;
	boolean doIt = true;
	EngineStates state;
	StoryPoint activeStoryPoint;
	Interaction activeInteraction;
	int buttonPressed;

	long millis;

	public StorytellAR_StoryEngine(UIController spiritFacade,
			PlayableStory story) {
		facade = spiritFacade;
		this.story = story;
		state = EngineStates.OPEN;
		buttonPressed = -1;
		addOpenStoryPointsToSonar();
		facade.showAllGhostNamesInRadar();
	}

	// Wird nach jedem Frame aufgerufen
	@Override
	public void update() {
		if (!facade.vuforiaIsReady()) {
			return;
		}
		
		//Logging
		for(StoryPoint point : story.getStorypoints().values()) {
			String log = "";
			log += "Punkt: " + point.getName() + "; ";
			log += "Dependencies: ";
			for(Dependency dep : point.getDependency()) {
				log+= dep.isFulfilled(story.getInteractions(),story.getStorypoints()) + " ";
			}
			log+= "Status: " + point.getStatus();
			facade.log("Story Engine", log);
		}
		switch (state) {
		case OPEN:
			
			//If a StoryPoint is already Activated, start it
			boolean breaker = false;
			for(StoryPoint point: story.getStorypoints().values()) {
				if(point.getStatus() == StorypointStatus.QEUED) {
					activeStoryPoint = point;
					state = EngineStates.IN_SCENE_START;
					breaker = true;
				}
			}
			if(breaker)break;
			
			Location loc = facade.getClosestGhost();
			if (loc != null) {
				Map<String, StoryPoint> sps = story.getStorypoints();
				StoryPoint closest = sps.get(loc.name);
				if (facade.getDistanceUserToClosestGhost() != 10) {
					activeStoryPoint = closest;
					state = EngineStates.IN_SCENE_START;
				}
			} else {
				facade.endStory();
			}

			// AR / Sonar wechsel durch Tabletwinkel
			if (facade.getTabletAngle() < 30 && state == EngineStates.OPEN) {
				facade.switchToSonarView();
			} else {
				facade.switchToARView();
			}
			break;
		case IN_SCENE_START:
			facade.switchToARView();
			activeStoryPoint.setStatus(StorypointStatus.ACTIVE);

			if (activeStoryPoint.getVideo().contains("mp4")) {
				 state = EngineStates.IN_SCENE_VIDEO_START;
			} else {
				 state = EngineStates.IN_SCENE_PICTURE_START;
			}
			break;
		case IN_SCENE:
			break;
		case IN_SCENE_PICTURE_START:
			facade.showPicture(activeStoryPoint.getVideo());
			millis = System.currentTimeMillis();
			state = EngineStates.IN_SCENE_PICTURE;
			break;
		case IN_SCENE_PICTURE:
			if (System.currentTimeMillis() - millis > 3000) {
				state = EngineStates.IN_SCENE_PICTURE_END;
			}
			break;
		case IN_SCENE_PICTURE_END:
			facade.hidePicture();
			state = EngineStates.IN_SCENE_INTERACTION_START;
			break;
		case IN_SCENE_VIDEO_START:
			PlaylistEntry pe = new PlaylistEntry(
					activeStoryPoint.getVideo());
			pe.setLoop(false);
			pe.setAutostart(false);
			facade.enqueueVideo(pe);
			facade.createReference();
			facade.startFilm();
			state = EngineStates.IN_SCENE_VIDEO;
			break;
		case IN_SCENE_VIDEO:
			break;
		case IN_SCENE_VIDEO_END:
			facade.removeAllVideos();
			state = EngineStates.IN_SCENE_INTERACTION_START;
			break;
		case IN_SCENE_INTERACTION_START:
			activeInteraction = story.getInteractions().get(activeStoryPoint.getInteraction());
			if(activeInteraction instanceof Quiz) {	
				Quiz activeQuiz = (Quiz) activeInteraction;
				facade.setText(activeQuiz.getQuestion());
				for(int i = 0; i< activeQuiz.getAnswers().size();i++) {
					facade.setButtonText(activeQuiz.getAnswers().get(i), i);
				}
				state = EngineStates.IN_SCENE_INTERACTION_QUIZ;
			} else if (activeInteraction instanceof Chooser) {
				Chooser activeChooser = (Chooser) activeInteraction;
				facade.setText(activeChooser.getQuestion());
				for(int i = 0; i< activeChooser.getAnswers().size();i++) {
					facade.setButtonText(activeChooser.getAnswers().get(i), i);
				}
				state = EngineStates.IN_SCENE_INTERACTION_CHOOSER;
			} else if (activeInteraction instanceof Item) {
				Item activeItem = (Item) activeInteraction;
				facade.setText("Willst du " + activeItem.getDescription() + " aufnehmen?");
				facade.setButtonText("Ja", 0);
				state = EngineStates.IN_SCENE_INTERACTION_ITEM;
			} else {				
				state = EngineStates.IN_SCENE_END;
			}
			break;
		case IN_SCENE_INTERACTION_QUIZ:
			if(buttonPressed != -1) {
				facade.hideText();
				Quiz activeQuiz = (Quiz) activeInteraction;
				List<String> possibleNextStoryPoints = activeQuiz.getNextStorypoints();
				for (int i = 0; i < possibleNextStoryPoints.size(); i++) {
					if(!possibleNextStoryPoints.get(i).equals(possibleNextStoryPoints.get(buttonPressed))) {
						story.getStorypoints().get(possibleNextStoryPoints.get(i)).setStatus(StorypointStatus.DONE);
					} 
				}
				story.getStorypoints().get(possibleNextStoryPoints.get(buttonPressed)).setStatus(StorypointStatus.QEUED);
				buttonPressed = -1;
				state = EngineStates.IN_SCENE_END;
			}
			break;
		case IN_SCENE_INTERACTION_CHOOSER:
			if(buttonPressed != -1) {
				facade.hideText();
				Chooser chooser = (Chooser) activeInteraction;
				Item selectedItem = (Item) story.getInteractions().get(chooser.getItems().get(buttonPressed));
				selectedItem.setIsCollected(true);
				buttonPressed = -1;
				state = EngineStates.IN_SCENE_END;
			}
			break;
		case IN_SCENE_INTERACTION_ITEM:
			if(buttonPressed != -1) {
				facade.hideText();
				Item item = (Item) activeInteraction;
				item.setIsCollected(true);
				buttonPressed = -1;
				state = EngineStates.IN_SCENE_END;
			}
			break;
		case IN_SCENE_END:
			activeStoryPoint.setStatus(StorypointStatus.DONE);
			addOpenStoryPointsToSonar();
			state = EngineStates.OPEN;
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
				state = EngineStates.IN_SCENE_VIDEO_END;
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
				buttonPressed = 0;
				break;
			case CustomButton1:
				buttonPressed = 1;
				break;
			case CustomButton2:
				buttonPressed = 2;
				break;
			case CustomButton3:
				buttonPressed = 3;
				break;
			case CustomButton4:
				buttonPressed = 4;
				break;
			case CustomButton5:
				buttonPressed = 5;
				break;

			default:
				break;

			}
		}

	}

	//TODO als Graph aufbauen
	private void addOpenStoryPointsToSonar() {
		facade.deleteAllGhosts();
		for(StoryPoint storyPoint : story.getStorypoints().values()) {
			boolean isFullfilled = storyPoint.isPlayable(story.getInteractions(),story.getStorypoints());
			if(storyPoint.getStatus() == StorypointStatus.OPEN && isFullfilled) {
				Location loc = new Location(storyPoint.getName());
				loc.setLatitude(storyPoint.getLatitude());
				loc.setLongitude(storyPoint.getLongitude());
				facade.addGhostLocation(loc);
			}
		}
	}
}
