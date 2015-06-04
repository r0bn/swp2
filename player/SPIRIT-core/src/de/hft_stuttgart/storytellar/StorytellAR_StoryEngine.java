package de.hft_stuttgart.storytellar;

import java.util.Map;

import de.hft_stuttgart.spirit.Location;
import de.hft_stuttgart.spirit.PlaylistEntry;
import de.hft_stuttgart.spirit.SpiritEvent;
import de.hft_stuttgart.spirit.SpiritStoryEngine;
import de.hft_stuttgart.spirit.UIController;

public class StorytellAR_StoryEngine implements SpiritStoryEngine {
	
	enum EngineStates {
		OPEN,
		IN_SCENE_START,
		IN_SCENE,
		IN_SCENE_PICTURE_START,
		IN_SCENE_PICTURE,
		IN_SCENE_PICTURE_END,
		IN_SCENE_VIDEO_START,
		IN_SCENE_VIDEO,
		IN_SCENE_VIDEO_END
	}

	PlayableStory story;
	UIController facade;
	boolean doIt = true;
	EngineStates state;
	StoryPoint activeStoryPoint;
	
	long millis;

	public StorytellAR_StoryEngine(UIController spiritFacade, PlayableStory story) {
		facade = spiritFacade;
		this.story = story;
		state = EngineStates.OPEN;
		addOpenStoryPointsToSonar();
		facade.showAllGhostNamesInRadar();
	}
	
	//Wird nach jedem Frame aufgerufen
	@Override
	public void update() {
		switch (state) {
		case OPEN:			
			Location loc = facade.getClosestGhost();
			if(loc != null) {				
				Map<String,StoryPoint> sps = story.getStorypoints();
				StoryPoint closest = sps.get(loc.name);
				if (facade.getDistanceUserToClosestGhost() >/*TODO: Muss < sein in final Release*/ 10) {
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

			if(activeStoryPoint.getVideo().contains("mp4")) {			
				PlaylistEntry pe = new PlaylistEntry(activeStoryPoint.getVideo());
				pe.setLoop(false);
				pe.setAutostart(false);
				facade.enqueueVideo(pe);
				//state = EngineStates.IN_SCENE_VIDEO_START;
			} else {
				//state = EngineStates.IN_SCENE_PICTURE_START;
			}
			
			facade.setButtonText(activeStoryPoint.getName(), 5);
			state = EngineStates.IN_SCENE;
			break;
		case IN_SCENE:
			break;
		case IN_SCENE_PICTURE_START:
			facade.showPicture(activeStoryPoint.getVideo());
			millis = System.currentTimeMillis();
			state = EngineStates.IN_SCENE_PICTURE;
			break;
		case IN_SCENE_PICTURE:
			if(System.currentTimeMillis() - millis > 4000) {
				state = EngineStates.IN_SCENE_PICTURE_END;
			}
			break;
		case IN_SCENE_PICTURE_END:
			facade.hidePicture();
			activeStoryPoint.setStatus(StorypointStatus.DONE);
			addOpenStoryPointsToSonar();
			state = EngineStates.OPEN;
			break;
		case IN_SCENE_VIDEO_START:
			facade.createReference();
			facade.startFilm();
			state = EngineStates.IN_SCENE_VIDEO;
			break;
		case IN_SCENE_VIDEO:
			break;
		case IN_SCENE_VIDEO_END:
			facade.removeAllVideos();
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
				if(activeStoryPoint.getVideo().contains("mp4")) {
					state = EngineStates.IN_SCENE_VIDEO_START;					
				} else {					
					state = EngineStates.IN_SCENE_PICTURE_START;
				}
				break;

			default:
				break;

			}
		}

	}
	
	private void addOpenStoryPointsToSonar() {
		facade.deleteAllGhosts();
		for(StoryPoint storyPoint : story.getStorypoints().values()) {
			boolean isFullfilled = storyPoint.isPlayable(story.getInteractions(),story.getStorypoints());
			/*for(Dependency dep : storyPoint.getDependency()){
				if(!dep.isFulfilled(story.getInteractions(),story.getStorypoints())){
					isFullfilled = false;
					break;
				}
			}*/
			if(storyPoint.getStatus() == StorypointStatus.OPEN && isFullfilled) {
				Location loc = new Location(storyPoint.getName());
				loc.setLatitude(storyPoint.getLatitude());
				loc.setLongitude(storyPoint.getLongitude());
				facade.addGhostLocation(loc);
			}
		}
	}
}
