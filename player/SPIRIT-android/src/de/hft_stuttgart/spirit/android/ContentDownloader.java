
package de.hft_stuttgart.spirit.android;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import restClient.RESTClient;

/**
 * @author Mirjam
 *
 */
public class ContentDownloader {
	
	private static ContentDownloader instance;
	private RESTClient client;

	private ArrayList<Story> allStoriesData;
	private ArrayList<Story> downloadedStories;
	
	
	/**
	 * Protected constructor for ContentDownloader
	 */
	protected ContentDownloader() {

		allStoriesData = new ArrayList<Story>();
		downloadedStories = new ArrayList<Story>();
		client = new RESTClient();
		
		/*DummyDaten*/
		downloadedStories.add(new Story(1, "Dummy", "Beschreibung Dummy", "Lukas", "42", "24.05.2014", "9.484 45.457", "4", true));
	}

	/**
	 * Returns the existing instance of ContentDownloader. If an instance of ContentDownloader exists already, the instance will be returned.
	 * See Singleton pattern
	 * @return the instance of ContentDownloader
	 */
	public static ContentDownloader getInstance() {
		if(instance == null)
			instance = new ContentDownloader();
		return instance;
	}

	/**
	 * Requests the meta data from all stories on the server. See API documentation for the returned attributes of each existing story.
	 * Requested meta data will be saved in the ArrayList allStoriesData. This includes a map with the meta data of each story and the parameter
	 * 'already_downloaded' which marks downloaded stories.
	 */
	public void requestAllStories() {
		try {
			allStoriesData.clear();
			JSONArray allStories = client.getAvailableStories();
			for(int i=0; i<allStories.length(); i++)
			{
				JSONObject story = (JSONObject) allStories.get(i);
				Story temp = new Story();
				temp.setId(Integer.valueOf((String)story.get("id")));
				temp.setTitle((String) story.get("title"));
				temp.setDescription((String) story.get("description"));
				temp.setAuthor((String) story.get("author"));
				temp.setSize((String) story.get("size"));
				temp.setCreation_date((String) story.get("creation_date"));
				temp.setLocation((String) story.get("location"));
				temp.setRadius((String) story.get("radius"));
				
				allStoriesData.add(temp);
			}
			markDownloadedStories();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * Method compares the list of all requested stories with the list of the already downloaded stories and sets
	 * the parameter 'already_downloaded' in list allStoriesData if this story is already downloaded.
	 */
	private void markDownloadedStories() {
		
		for (Story x : downloadedStories) {
			for(int i=0; i< allStoriesData.size(); i++)
			{
				if(x.getId().equals(allStoriesData.get(i).getId()))
					allStoriesData.get(i).setAlreadyDownloaded(true);
			}
		}
	}

	/**
	 * Download of a single story specified by the id. Server returns the XML which includes media data with absolute URI. 
	 * @param id id of the story to download
	 */
	public void downloadStory(int id) {
		requestAllStories();
		Story temp = null;
		for (Story x : allStoriesData) {
			if(x.getId() == id)
				temp = x;
		}
		if(temp == null) {
			throw new RuntimeException("Story with ID: "+id+" can't be found on server! Download is cancelled.");
		}
		//String xml = client.getStoryXML(id);
		
		
		//Get XML with absolute URI for media data from server
		//System.out.println(resource.accept(MediaType.TEXT_XML).get(String.class));

		//Parse media data from xml
		//Download media data
		
		//Save XML
		
		//Create story object and copy existing meta data + set references (XML, media data)
		//Add story object to list downloadedStories
		//call markDownloadedStories()
		
	}
	
	/**
	 * Method returns the list of all requested stories (with meta data). If this method was called and no list
	 * of stories were requested before, the stories will be requested from the server and the list will be returned.
	 * @return list of requested stories with meta data
	 */
	public ArrayList<Story> getAllStoriesData() {
		if(allStoriesData.isEmpty())
			requestAllStories();
		return allStoriesData;
	}
	
	public ArrayList<Story> getDownloadedStories() {
		return downloadedStories;
	}
	
	public HashMap<String,String> parseMediaDataFormXML(String node, String xml){
		HashMap<String,String> mediaMap = new HashMap<String,String>();
		try {
		XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
		XmlPullParser parser = xmlFactoryObject.newPullParser();
		
		} catch (Exception e){
			
		}
		
		return mediaMap;
	}
	
}
