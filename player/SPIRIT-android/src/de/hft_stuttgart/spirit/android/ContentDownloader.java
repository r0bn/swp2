/**
 * 
 */
package de.hft_stuttgart.spirit.android;

import java.net.URI;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;



/**
 * @author Mirjam
 *
 */
public class ContentDownloader {
	
	private static ContentDownloader instance = null;
	private Client client = null;
	public String URLsingleStory = "http://api.dev.la/story/";
	public String URLallStories = "http://api.dev.la/stories";
	
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
	public static ContentDownloader getInstance() {
		if(instance == null)
			instance = new ContentDownloader();
		return instance;
	}

	/**
	 * Requests the meta data from all stories on the server. See API documentation for the returned attributes of each existing story.
	 */
	public void requestAllStories() {
		WebResource resource = client.resource(getBaseURI(URLallStories));
		
		//Get JSON from server
		System.out.println(resource.accept(MediaType.APPLICATION_JSON).get(String.class));
		//Do something with JSON here
	}
	
	/**
	 * Download of a single story specified by the id. Server returns the XML which includes media data with absolute URI. 
	 * @param id id of the story to download
	 */
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
	
}
