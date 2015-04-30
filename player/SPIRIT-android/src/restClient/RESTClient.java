package restClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;

/**
 * 
 * @author Lukas
 *
 */
public class RESTClient {

    /**
     * The URL to use for requests
     */
    private final String URLallStories = "http://api.storytellar.de/story";

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
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String result = "", line;
        while ((line = br.readLine()) != null) {
            result += line;
        }
        return result;
    }
}