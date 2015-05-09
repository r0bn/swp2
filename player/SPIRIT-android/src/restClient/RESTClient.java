package restClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;

import android.os.Environment;
import android.util.Log;
import de.hft_stuttgart.spirit.android.ContentDownloader;
import de.hft_stuttgart.spirit.android.Story;

/**
 * 
 * @author Lukas
 *
 */
public class RESTClient {

    /**
     * The URL to use for requests
     */
    private String URLallStories = "http://api.storytellar.de/story";
    private String URLmediaData = "http://api.storytellar.de/media";

    /**
     * 
     * @return Returns an JSONArray containing the metadata for all available stories.
     * @throws Exception
     */
    public JSONArray getAvailableStories() throws Exception {
        URL url = new URL(URLallStories);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/string");
        InputStream stream = connection.getInputStream();
        return new JSONArray(readInput(stream));

    }

    /**
     * 
     * @return Returns an JSONArray containing the metadata for all available stories with a given id, title, author, size_max, 
     * creation_date_min, creation_date_max, location or radius.
     * 
     * @throws Exception
     */
    public JSONArray getAvailableStoriesWithParamenter(String queryParameter) throws Exception {
        //URL url = new URL(URLallStories + "?" + queryParameter);
        URL url = new URL(queryParameter);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/string");
        InputStream stream = connection.getInputStream();
        return new JSONArray(readInput(stream));
    }
    
    /**
     * 
     * @param id The id of the story.
     * @return Returns the XMLFile for the story with the given id as a String.
     */
    public String getStoryXML(int id) {
        
        URL url;
        String result = "";
		try {
			url = new URL(URLallStories+"/"+id);
			HttpURLConnection connection
			= (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/xml");
			
			result = readInput(connection.getInputStream());

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
    }

    /**
     * 
     * @param is The InputStream to read from.
     * @return Returns the content of the InputStream as String
     * @throws IOException
     */
    private String readInput(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        String result = "", line;
        while ((line = br.readLine()) != null) {
            result += line+"\n";
        }
        if(result != ""){        	
        	result = result.substring(0,result.lastIndexOf('\n'));
        }
        return result;
    }
    
    /**
     * All media files of a story will be downloaded form the server. Media files are given in a HashMap with their id and the path
     * to the respective file. 
     * @param mediaMap Map with media files (id and path to file)
     * @param story story object to which the media data belongs
     */
	public void downloadMediaFiles(HashMap<String,String> mediaMap, Story story){
		URL url;
		try {
			for (String value : mediaMap.values()){
				url = new URL(URLmediaData+"/"+story.getId()+"/"+value);
				Log.d(RESTClient.class.toString(), "Download File: "+URLmediaData+"/"+story.getId()+"/"+value);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setDoOutput(true);                   
				connection.connect();
				File file = new File(Environment.getExternalStorageDirectory()+"/StorytellAR/Content/"+story.getId(), value);
				
				FileOutputStream fos = new FileOutputStream(file);
				InputStream stream = connection.getInputStream();
				byte[] buffer = new byte[1024];
				int bufferLength = 0;
				
				while ( (bufferLength = stream.read(buffer)) > 0 ) {                 
				    fos.write(buffer, 0, bufferLength);                                  
				  }             
				  fos.close();
			}	
			
		} catch (Exception e) {
			
		}
	}
}