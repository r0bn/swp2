package restClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.xml.sax.SAXException;

/**
 * Main controller for the REST client. The request you wish to make can be
 * called by either calling the request() method and passing the type of request
 * you will be to make, or by accessing the methods directly.
 *
 * Uses the RESTException to report errors, but this is extremely basic, and
 * left alone due to the fact people should rather implement their own error
 * codes and systems. You can pass a JSONObject to the Exception by calling
 * createErrorObject().
 *
 * @author Isaac Whitfield
 * @version 09/03/2014
 *
 */
public class RESTClient {

    // The client to use for requests
    private final String URLallStories = "http://api.storytellar.de/story";

    /**
     *
     * @return Returns an JSONArray containing the metadata for all available
     * stories on the server
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
     * @param id
     * @throws ProtocolException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public String getStoryXML(int id) {
        
        URL url;
        StringBuilder xmlBuilder = new StringBuilder();
		try {
			url = new URL(URLallStories+"/"+id);
			HttpURLConnection connection
			= (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/xml");
			
			BufferedReader xmlReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String xmlLine = xmlReader.readLine();
			while(xmlLine != null) {
				xmlBuilder.append(xmlLine);
			}
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xmlBuilder.toString();
    }

    /**
     * Generic handler to retrieve the result of a request. Simply reads the
     * input stream and returns the string;
     *
     * @param is	the InputStream we're reading, usually from
     * getEntity().getContent()
     * @return	The data as a String
     */
    private String readInput(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String json = "", line;
        while ((line = br.readLine()) != null) {
            json += line;
        }
        return json;
    }
}