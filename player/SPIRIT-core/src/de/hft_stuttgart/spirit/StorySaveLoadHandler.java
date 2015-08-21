package de.hft_stuttgart.spirit;

import de.hft_stuttgart.storytellar.PlayableStory;

public interface StorySaveLoadHandler {
	public void saveStory(PlayableStory story);
	public PlayableStory loadStory(Integer ID);
}
