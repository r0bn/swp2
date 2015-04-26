
package de.hft_stuttgart.spirit.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
 * @author Mirjam
 *
 */
public class ContentDownloader {
	
	private static ContentDownloader instance = null;
	public String URLsingleStory = "http://api.dev.la/story/";
	public String URLallStories = "http://api.dev.la/stories";

	private ArrayList<HashMap<String, String>> allStoriesData = new ArrayList<HashMap<String, String>>();
	private ArrayList<Story> downloadedStories = new ArrayList<Story>();
	
	/**
	 * Protected constructor for ContentDownloader
	 */
	protected ContentDownloader() {

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
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(URLallStories);
		getRequest.addHeader("accept", "application/json");
		HttpResponse response = client.execute(getRequest);
			
		if(response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed: HTTP error code : "+ response.getStatusLine().getStatusCode());
		}
			
		HttpEntity entity = response.getEntity();
		String data = EntityUtils.toString(entity);
			
		System.out.println(data);
			
		client.close();

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(data);
		JSONArray allStories = (JSONArray) obj;
		System.out.println(allStories.size());
		
		for(int i=0; i<allStories.size(); i++)
		{
			JSONObject story = (JSONObject) allStories.get(0);
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("id", (String) story.get("id"));
			temp.put("title", (String) story.get("title"));
			temp.put("description", (String) story.get("description"));
			temp.put("author", (String) story.get("author"));
			temp.put("size", (String) story.get("size"));
			temp.put("creation_date", (String) story.get("creation_date"));
			temp.put("location", (String) story.get("location"));
			temp.put("radius", (String) story.get("radius"));
			temp.put("already_downloaded", "0");
			
			allStoriesData.add(temp);
		}
		
		markDownloadedStories();
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
				if(x.getId().equals(allStoriesData.get(i).get("id")))
					allStoriesData.get(i).put("already_downloaded", "1");
			}
		}
	}

	/**
	 * Download of a single story specified by the id. Server returns the XML which includes media data with absolute URI. 
	 * @param id id of the story to download
	 */
	public void downloadStory(String id) {
		try {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(URLsingleStory+id);
		getRequest.addHeader("accept", "application/json");
		HttpResponse response = client.execute(getRequest);
			
		if(response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed: HTTP error code : "+ response.getStatusLine().getStatusCode());
		}
			
		HttpEntity entity = response.getEntity();
		String data = EntityUtils.toString(entity);
			
		System.out.println(data);
			
		client.close();

		//Parse media data from xml
		//Download media data
		
		//Save XML
		
		//Create story object and copy existing meta data + set references (XML, media data)
		HashMap<String,String> temp = new HashMap<String,String>();
		for(int i=0; i< allStoriesData.size(); i++)
		{
			if(id.equals(allStoriesData.get(i).get("id")))
				temp = allStoriesData.get(i);
		}
		
		Story downloadedStory = new Story(temp.get("id"), temp.get("title"), temp.get("description"), temp.get("author"), temp.get("size"), temp.get("creation_date"), temp.get("location"), temp.get("radius"));
		//downloadedStory.setPathToXML("xxx");
		//downloadedStory.setStoryMediaData(storyMediaData);
		
		//Add story object to list downloadedStories
		downloadedStories.add(downloadedStory);
		markDownloadedStories();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * Method returns the list of all requested stories (with meta data). If this method was called and no list
	 * of stories were requested before, the stories will be requested from the server and the list will be returned.
	 * @return list of requested stories with meta data
	 */
	public ArrayList<HashMap<String, String>> getAllStoriesData() {
		if(allStoriesData.isEmpty())
			requestAllStories();
		return allStoriesData;
	}
	
	public ArrayList<Story> getDownloadedStories() {
		return downloadedStories;
	}
}
