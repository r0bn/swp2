
package de.hft_stuttgart.spirit.android;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;



/**
 * @author Mirjam
 *
 */
public class ContentDownloader implements ContentDownloaderInterface {
	
	private static ContentDownloader instance = null;
	private Client client = null;
	public String URLsingleStory = "http://api.dev.la/story/";
	public String URLallStories = "http://api.dev.la/stories";

	private ArrayList<HashMap<String, String>> allStoriesData = new ArrayList<HashMap<String, String>>();
	
	/**
	 * Protected constructor for ContentDownloader
	 */
	protected ContentDownloader() {
		ClientConfig config = new DefaultClientConfig();
		client = Client.create(config);
	}
	
	/**
	 * Returns the existing instance of ContentDownloader. If an instance of ContentDownloader exists already, the instance will be returned.
	 * See Singleton pattern
	 * @return the instance of ContentDownloader
	 */
	@Override
	public ContentDownloader getInstance() {
		if(instance == null)
			instance = new ContentDownloader();
		return instance;
	}

	/**
	 * Requests the meta data from all stories on the server. See API documentation for the returned attributes of each existing story.
	 * Requested meta data will be saved in the ArrayList allStoriesData. This includes a map with the meta data of each story and the parameter
	 * 'already_downloaded' which marks downloaded stories.
	 */
	@Override
	public void requestAllStories() {
		try {
		WebResource resource = client.resource(getBaseURI(URLallStories));
		//Get JSON from server
		String data = resource.accept(MediaType.APPLICATION_JSON).get(String.class);
		System.out.println(data);

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
		}
	}
	
	/**
	 * Method compares the list of all requested stories with the list of the already downloaded stories and sets
	 * the parameter 'already_downloaded' in list allStoriesData if this story is already downloaded.
	 */
	private void markDownloadedStories() {
		
	}

	/**
	 * Download of a single story specified by the id. Server returns the XML which includes media data with absolute URI. 
	 * @param id id of the story to download
	 */
	@Override
	public void downloadStory(int id) {
		WebResource resource = client.resource(getBaseURI(URLsingleStory, id));
		
		//Get XML with absolute URI for media data from server
		System.out.println(resource.accept(MediaType.TEXT_XML).get(String.class));
		//Do something with XML here
	}
	
	public URI getBaseURI(String URL) {
		return UriBuilder.fromUri(URL).build();
	}
	
	public URI getBaseURI(String URL, int id) {
		return UriBuilder.fromUri(URL+id).build();
	}
	
	/**
	 * Method returns the list of all requested stories (with meta data). If this method was called and no list
	 * of stories were requested before, the stories will be requested from the server and the list will be returned.
	 * @return list of requested stories with meta data
	 */
	@Override
	public ArrayList<HashMap<String, String>> getAllStoriesData() {
		if(allStoriesData.isEmpty())
			requestAllStories();
		return allStoriesData;
	}
	
}
