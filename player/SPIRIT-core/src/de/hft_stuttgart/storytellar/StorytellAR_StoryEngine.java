package de.hft_stuttgart.storytellar;

import de.hft_stuttgart.spirit.SpiritStoryEngine;
import de.hft_stuttgart.spirit.UIController;

public class StorytellAR_StoryEngine implements SpiritStoryEngine {

	UIController facade;
	PlayableStory story;
	
	public StorytellAR_StoryEngine(UIController spiritFacade, PlayableStory story) {
		facade = spiritFacade;
		this.story = story;
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void reset() {
		
	}

}
