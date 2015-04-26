
package de.hft_stuttgart.spirit.android;

import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.core.UriBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import restClient.AndrestClient;
import restClient.RESTException;



/**
 * @author Mirjam
 *
 */
public class ContentDownloader {
	
	private static ContentDownloader instance = null;
	private AndrestClient client = null;
	public String URLsingleStory = "http://api.storytellar.de/story/";
	public String URLallStories = "http://api.storytellar.de/story";

	private ArrayList<Story> allStoriesData = new ArrayList<Story>();
	private ArrayList<Story> downloadedStories = new ArrayList<Story>();
	
	/**
	 * Protected constructor for ContentDownloader
	 */
	protected ContentDownloader() {
		//26.04 Lukas Aberle added another restClient
		//ClientConfig config = new DefaultClientConfig();
		//client = Client.create(config);
		
		client = new AndrestClient();
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
// 26.04 Lukas Aberle added another RestClient
//		try {
//		WebResource resource = client.resource(getBaseURI(URLallStories));
//		//Get JSON from server
//		String data = resource.accept(MediaType.APPLICATION_JSON).get(String.class);
//		System.out.println(data);
//
//		JSONParser parser = new JSONParser();
//		Object obj = parser.parse(data);
//		JSONArray allStories = (JSONArray) obj;
//		System.out.println(allStories.size());
//		
//		for(int i=0; i<allStories.size(); i++)
//		{
//			JSONObject story = (JSONObject) allStories.get(0);
//			HashMap<String, String> temp = new HashMap<String, String>();
//			temp.put("id", (String) story.get("id"));
//			temp.put("title", (String) story.get("title"));
//			temp.put("description", (String) story.get("description"));
//			temp.put("author", (String) story.get("author"));
//			temp.put("size", (String) story.get("size"));
//			temp.put("creation_date", (String) story.get("creation_date"));
//			temp.put("location", (String) story.get("location"));
//			temp.put("radius", (String) story.get("radius"));
//			temp.put("already_downloaded", "0");
//			
//			allStoriesData.add(temp);
//		}
//		
//		markDownloadedStories();
//		
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			JSONArray allStories = client.request(URLallStories, "GET", null);
			for(int i=0; i<allStories.length(); i++)
			{
				JSONObject story = (JSONObject) allStories.get(i);
				Story temp = new Story();
				temp.setId((String) story.get("id"));
				temp.setTitle((String) story.get("title"));
				temp.setDescription((String) story.get("description"));
				temp.setAuthor((String) story.get("author"));
				temp.setSize((String) story.get("size"));
				temp.setCreation_date((String) story.get("creation_date"));
				temp.setLocation((String) story.get("location"));
				temp.setRadius((String) story.get("radius"));
				
				allStoriesData.add(temp);
			}
		} catch (RESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
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
		//WebResource resource = client.resource(getBaseURI(URLsingleStory, id));
		
		//Get XML with absolute URI for media data from server
		//System.out.println(resource.accept(MediaType.TEXT_XML).get(String.class));

		//Parse media data from xml
		//Download media data
		
		//Save XML
		
		//Create story object and copy existing meta data + set references (XML, media data)
		//Add story object to list downloadedStories
		//call markDownloadedStories()
		
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
	public ArrayList<Story> getAllStoriesData() {
		if(allStoriesData.isEmpty())
			requestAllStories();
		return allStoriesData;
	}
	
	public ArrayList<Story> getDownloadedStories() {
		return downloadedStories;
	}
}
