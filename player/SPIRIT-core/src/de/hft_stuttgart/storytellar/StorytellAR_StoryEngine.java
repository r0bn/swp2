package de.hft_stuttgart.storytellar;

import java.util.List;
import java.util.Map;

import de.hft_stuttgart.spirit.Location;
import de.hft_stuttgart.spirit.PlaylistEntry;
import de.hft_stuttgart.spirit.SpiritEvent;
import de.hft_stuttgart.spirit.SpiritStoryEngine;
import de.hft_stuttgart.spirit.UIController;

/**
 * This class handles the scenes in a PlayableStory
 * @author Lukas
 *
 */
public class StorytellAR_StoryEngine implements SpiritStoryEngine {

	/**
	 * This enum is used for the state machine in StorytellAR_StoryEngine, the EngineStates can be:
	 * OPEN								no storypoint is currently active
	 * IN_SCENE_START					a new storypoint is started
	 * IN_SCENE_END						the current storypoint is finished
	 * IN_SCENE_PICTURE_START			a new picture is started
	 * IN_SCENE_PICTURE					a picture is displayed
	 * IN_SCENE_PICTURE_END				the current picture is finished
	 * IN_SCENE_VIDEO_START				a new video is started
	 * IN_SCENE_VIDEO					a video is played
	 * IN_SCENE_VIDEO_END				the current video is finished
	 * IN_SCENE_INTERACTION_START		a new interaction is started
	 * IN_SCENE_INTERACTION_QUIZ		a quiz is displayed
	 * IN_SCENE_INTERACTION_ITEM		a item is displayed
	 * IN_SCENE_INTERACTION_CHOOSER		a chooser is displayed
	 * 
	 * @author Lukas
	 *
	 */
	private enum EngineStates {
		OPEN, 
		IN_SCENE_START,
		IN_SCENE_REFERENCE_SEARCH,
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
	EngineStates state;
	StoryPoint activeStoryPoint;
	Interaction activeInteraction;
	int buttonPressed;

	long millis;

	/**
	 * Creates a new instance of the engine
	 * @param spiritFacade The facade that handles the interaction
	 * @param story The story to be played.
	 */
	public StorytellAR_StoryEngine(UIController spiritFacade,
			PlayableStory story) {
		facade = spiritFacade;
		this.story = story;
		state = EngineStates.OPEN;
		buttonPressed = -1;
		addOpenStoryPointsToSonar();
	}

	/**
	 * The update-method is called after every frame. the game logic is implemented here.
	 */
	@Override
	public void update() {
		/*Do not handle any logic when the camera(vuforia) is not ready.*/
		if (!facade.vuforiaIsReady()) {
			return;
		}
	
		/*In this statemachine the logic is handled. See "StorytellAR_StoryEngine.png" in documentation for more details*/
		switch (state) {
		case OPEN:

			/*If a StoryPoint is queued, start it*/
			boolean breaker = false;
			for(StoryPoint point: story.getStorypoints().values()) {
				if(point.getStatus() == StorypointStatus.QUEUED) {
					activeStoryPoint = point;
					state = EngineStates.IN_SCENE_START;
					breaker = true;
				}
			}
			if(breaker)break;
			
			/*If the distance to an available Storypoint is under 10m start it.*/
			Location loc = facade.getClosestGhost();
			if (loc != null) {
				Map<String, StoryPoint> sps = story.getStorypoints();
				StoryPoint closest = sps.get(loc.name);
				if (facade.getDistanceUserToClosestGhost() >= 15) {
					activeStoryPoint = closest;
					if(activeStoryPoint.getTrackable_image() == null){
						state = EngineStates.IN_SCENE_START;
					} else {
						
					if(!activeStoryPoint.getTrackable_image().getSrc().isEmpty()) {
						facade.setText("Finde");
						facade.setPictureAlpha(0.5f);
						facade.showPicture(activeStoryPoint.getTrackable_image().getSrc());						
					}
					state = EngineStates.IN_SCENE_REFERENCE_SEARCH;
					}
				}
			} else {
				facade.endStory();
			}

			/* AR / Sonar change with tabletangle*/
			if (facade.getTabletAngle() < 30 && state == EngineStates.OPEN) {
				facade.switchToSonarView();
			} else {
				facade.switchToARView();
			}
			break;
		case IN_SCENE_REFERENCE_SEARCH:
			facade.getOrbInfos().setVergleichName(activeStoryPoint.getTrackable_image().getSrc()); 	//Set Picture that is searched
			
			if(activeStoryPoint.getTrackable_image().getSrc().isEmpty() || facade.getOrbInfos().getLastResultName().equals(activeStoryPoint.getTrackable_image().getSrc())
				|| facade.getOrbInfos().getLastResultName().equals("")) 
				{
					if(activeStoryPoint.getTrackable_image().getSrc().isEmpty() && facade.getTabletAngle() > 75 || facade.getOrbInfos().found(30, 40, 40))
					{
						facade.hideText();
						facade.hidePicture();
						facade.setPictureAlpha(1.0f);
						state = EngineStates.IN_SCENE_START;
					} else {
						//System.out.println("StorytellAR_StoryEngine: Result of image tracking is too bad!");
					}
				} else {
					//System.out.println("Name isn't matching!");
					//System.out.println("Searched: "+closest.getTrackable_image().getSrc()+" Found: "+facade.getOrbInfos().getLastResultName());
				}
			break;
		case IN_SCENE_START:/*Start the video or Picture of the actual scene*/
			facade.switchToARView();
			activeStoryPoint.setStatus(StorypointStatus.ACTIVE);

			if (activeStoryPoint.getVideo().contains("mp4")) {
				 state = EngineStates.IN_SCENE_VIDEO_START;
			} else {
				 state = EngineStates.IN_SCENE_PICTURE_START;
			}
			break;
		case IN_SCENE_PICTURE_START:/*Show picture of the scene*/
			facade.showPicture(activeStoryPoint.getVideo());
			millis = System.currentTimeMillis();
			state = EngineStates.IN_SCENE_PICTURE;
			break;
		case IN_SCENE_PICTURE:/*Wait 3s then hide picture*/
			if (System.currentTimeMillis() - millis > 3000) {
				state = EngineStates.IN_SCENE_PICTURE_END;
			}
			break;
		case IN_SCENE_PICTURE_END:/*Start the interaction of the scene*/
			facade.hidePicture();
			state = EngineStates.IN_SCENE_INTERACTION_START;
			break;
		case IN_SCENE_VIDEO_START:/*Start the video of the scene*/
			PlaylistEntry pe = new PlaylistEntry(
					activeStoryPoint.getVideo());
			pe.setLoop(false);
			pe.setAutostart(false);
			facade.enqueueVideo(pe);
			facade.createReference();
			facade.startFilm();
			state = EngineStates.IN_SCENE_VIDEO;
			break;
		case IN_SCENE_VIDEO:/*Wait for video to end(is caused by the Filmende-event in checkEvents())*/
			break;
		case IN_SCENE_VIDEO_END:/*Start interaction*/
			facade.removeAllVideos();
			state = EngineStates.IN_SCENE_INTERACTION_START;
			break;
		case IN_SCENE_INTERACTION_START:/*If an interaction exists for the scene, start it*/
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
				facade.setText("Du nimmst " + activeItem.getDescription() + " auf.");
				facade.setButtonText("Ok", 0);
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
				story.getStorypoints().get(possibleNextStoryPoints.get(buttonPressed)).setStatus(StorypointStatus.QUEUED);
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
		case IN_SCENE_END:/*Close active storypoint and search for new enabled storypoints*/
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

	/**
	 * Adds enabled storypoints to the Sonar
	 */
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
