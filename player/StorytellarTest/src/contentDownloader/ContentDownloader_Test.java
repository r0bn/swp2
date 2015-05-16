package contentDownloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.junit.Test;

import android.os.Environment;
import android.test.InstrumentationTestCase;
import android.util.Log;
import restClient.RESTClient;
import de.hft_stuttgart.spirit.android.ContentDownloader;
import de.hft_stuttgart.spirit.android.Story;

public class ContentDownloader_Test extends InstrumentationTestCase{

	public ContentDownloader downloader;
	
	public ContentDownloader_Test() {
		
		downloader = ContentDownloader.getInstance();
		downloader.requestAllStories();
		
	}
	
	@Test
	public void test_requestMetaData() {
		downloader.requestAllStories();
		assertFalse("allStoriesData holds meta data from server", downloader.getAllStoriesData().isEmpty());
		
		assertNotNull("ID of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getId());
		assertNotNull("Title of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getTitle());
		assertNotNull("Description of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getDescription());
		assertNotNull("Author of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getAuthor());
		assertNotNull("Size of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getSize());
		assertNotNull("Created_at of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getCreated_at());
		assertNotNull("Updated_at of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getUpdated_at());
		assertNotNull("Location of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getLocation());
		assertNotNull("Latitude of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getLatitude());
		assertNotNull("Longitude of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getLongitude());
		assertNotNull("Radius of Story[0] in allStoriesData is not null", downloader.getAllStoriesData().get(0).getRadius());
		Log.d(ContentDownloader.class.toString(), ""+downloader.getAllStoriesData().get(0).getId());
		Log.d(ContentDownloader.class.toString(), ""+downloader.getAllStoriesData().get(0).getTitle());
		Log.d(ContentDownloader.class.toString(), ""+downloader.getAllStoriesData().get(0).isAlreadyDownloaded());
		Log.d(ContentDownloader.class.toString(), ""+downloader.getDownloadedStories().contains(downloader.getAllStoriesData().get(0)));
		
		boolean download = false;
		for (Story x : downloader.getDownloadedStories()) {
			if(x.getId() == downloader.getAllStoriesData().get(0).getId())
				download = true;
			}
		
		if(download)
			assertTrue("Already_downloaded of Story[0] in allStoriesData is false", downloader.getAllStoriesData().get(0).isAlreadyDownloaded());
		else
			assertFalse("Already_downloaded of Story[0] in allStoriesData is false", downloader.getAllStoriesData().get(0).isAlreadyDownloaded());
	}
	
	@Test
	public void test_getStoryXML() throws IOException {
		RESTClient client = new RESTClient();
		String xml = client.getStoryXML(1);

		InputStream stream = getInstrumentation().getTargetContext().getResources().getAssets().open("ExampleXML_withPaths.xml");
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
	    String inputLine;
	    String example_xml = "";
	      while ((inputLine = in.readLine()) != null)
	        example_xml += inputLine+"\n";
	      in.close();
	      example_xml = example_xml.trim();
		
		assertEquals("XML of Story[1] is equals the example XML", example_xml, xml);
	}
	
	@Test
	public void test_downloadStory() {
		downloader.requestAllStories();
		downloader.downloadStory(3);
		
		Story temp = null;
		for (Story x : downloader.getDownloadedStories()) {
			if (x.getId() == 3)
				temp = x;
		}
		
		assertNotNull(temp);
	}
	
	@Test
	public void test_downloadStoryFailed(){
		try {
		downloader.downloadStory(100);
		} catch (RuntimeException e){
			assertEquals(e.getMessage(),"Story with ID: 100 can't be found on server! Download is cancelled.");
		}
	}
	
	@Test
	public void test_markDownloadedStory() {
		downloader.downloadStory(1);
		Story temp = null;
		for (Story x : downloader.getAllStoriesData()) {
			if (x.getId() == 1)
				temp = x;
		}
		assertTrue(temp.isAlreadyDownloaded());
	}
	
	@Test
	public void test_parseMediaData() {
		downloader.downloadStory(1);
		
		Story temp = null;
		for (Story x : downloader.getDownloadedStories()) {
			if (x.getId() == 1)
				temp = x;
		}
		
		HashMap<String,String> mediaMap = downloader.parseMediaDataFromXML(temp.getPathToXML());
		
		assertEquals(mediaMap.get("Anfangsvideo"), "Anfangsvideo.mp4");
		assertEquals(mediaMap.get("Anfangsvideo_E1"), "Anfangsvideo_E1.mp4");
		assertEquals(mediaMap.get("Anfangsvideo_E2"), "Anfangsvideo_E2.mp4");
		assertEquals(mediaMap.get("Studentenvideo"), "Studentenvideo.mp4");
		assertEquals(mediaMap.get("Piratenzeugs"), "Piratenzeug.jpg");
		assertEquals(mediaMap.get("gebrauchter_Rucksack"), "alter_Rucksack.jpg");
		assertEquals(mediaMap.get("Piratengespraech"), "Piratengespraech.mp4");
		assertEquals(mediaMap.get("Studentengespraech"), "Studentengespraech.mp4");
		assertEquals(mediaMap.get("Ende"), "Ende.mp4");
	}
	
	@Test
	public void test_deleteStory() {
		downloader.requestAllStories();
		downloader.downloadStory(2);
		String pathToAppDir = Environment.getExternalStorageDirectory()+ "/StorytellAR";
		String pathFolder = pathToAppDir + "/Content/" + "2";
		File folder = new File(pathFolder);
		assertTrue(folder.exists());
		
		downloader.deleteStory(2);
		assertFalse(folder.exists());
	}
}
