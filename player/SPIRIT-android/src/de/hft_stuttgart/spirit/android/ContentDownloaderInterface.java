package de.hft_stuttgart.spirit.android;

import java.util.ArrayList;
import java.util.HashMap;


public interface ContentDownloaderInterface {

	public void requestAllStories();
	public ContentDownloader getInstance();
	public void downloadStory(int id);
	public ArrayList<HashMap<String, String>> getAllStoriesData();
}
