
package de.hft_stuttgart.spirit.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
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
		//downloadedStories.add(new Story(1, "Dummy", "Beschreibung Dummy", "Lukas", "42", "24.05.2014", "9.484 45.457", "4", true));
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
				Story temp = new Story(
				Integer.valueOf((String)story.get("id")),
				(String) story.get("title"),
				(String) story.get("description"),
				(String) story.get("author"),
				(String) story.get("size"),
				(String) story.get("creation_date"),
				(String) story.get("location"),
				(String) story.get("radius"),
				false);
				
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
		try {
		//requestAllStories();
		Story temp = null;
		for (Story x : allStoriesData) {
			if(x.getId() == id)
				temp = x;
		}
		if(temp == null) {
			throw new RuntimeException("Story with ID: "+id+" can't be found on server! Download is cancelled.");
		}
		String xml = client.getStoryXML(id);
		
		Log.d(ContentDownloader.class.toString(), Environment.getExternalStorageDirectory()+"/StorytellAR/Content/"+id+"/arml.xml");
		String pathFolder = Environment.getExternalStorageDirectory()+"/StorytellAR/Content/"+id;
		File folder = new File(pathFolder);
		if (!folder.exists()) {
            folder.mkdirs();
        }
		
		File file = new File(folder.getAbsolutePath(), "arml.xml");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(xml.getBytes());
		fos.close();
		
		temp.setPathToXML("/StorytellAR/Content/"+id+"/arml.xml");
		temp.setStoryMediaData(parseMediaDataFromXML(temp.getPathToXML()));
		client.downloadMediaFiles(temp.getStoryMediaData(), temp);
		
		downloadedStories.add(temp);
		markDownloadedStories();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public ArrayList<Story> getAllStoriesData() {
		if(allStoriesData.isEmpty())
			requestAllStories();
		return allStoriesData;
	}
	
	/**
	 * Method returns the list with all downloaded stories.
	 * @return list of all downloaded stories
	 */
	public ArrayList<Story> getDownloadedStories() {
		return downloadedStories;
	}
	
	/**
	 * Media data from the given xml is parsed. The image or video files are returned in a map with 
	 * their id and the path to the respective file.
	 * @param path path to the xml file
	 * @return map with id and path to the respective media file
	 */
	public HashMap<String,String> parseMediaDataFromXML(String path){
		HashMap<String,String> mediaMap = new HashMap<String,String>();
		try {
		XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
		XmlPullParser parser = xmlFactoryObject.newPullParser();
		
		File file = new File(Environment.getExternalStorageDirectory()+path);
		FileInputStream stream = new FileInputStream(file);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        
        int event = parser.getEventType();
        String name = null;
        String video = null;
        String image = null;
        String pathToFile = null;
        
        while(event != parser.END_DOCUMENT) {
        	switch(event){
        	case XmlPullParser.START_TAG:
        		name = parser.getName();
        		if(name.equals("Video")){
        			video = parser.getAttributeValue(0);
        			Log.d(ContentDownloader.class.toString(), "Video-ID: "+video);
        		} else if (name.equals("Image")){
        			image = parser.getAttributeValue(0);
        			Log.d(ContentDownloader.class.toString(), "Image-ID: "+image);
        		}
        		if(video != null){
        			if(name.equals("Href")){
        				pathToFile = parser.getAttributeValue(0);
        				Log.d(ContentDownloader.class.toString(), "Video-ID: "+video+"  Path: "+pathToFile);
        				mediaMap.put(video, pathToFile);
        				video = null;
        			}
        		}
        		if(image != null){
        			if(name.equals("href")){
        				pathToFile = parser.getAttributeValue(0);
        				Log.d(ContentDownloader.class.toString(), "Image-ID: "+image+"  Path: "+pathToFile);
        				mediaMap.put(image, pathToFile);
        				image = null;
        			}
        		}
        		break;
        	}
        	event = parser.next();
        }
		
		} catch (Exception e){
			
		}
		
		return mediaMap;
	}
	
}
