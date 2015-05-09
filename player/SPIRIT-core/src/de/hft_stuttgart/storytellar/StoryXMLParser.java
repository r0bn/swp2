/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * @author Oliver
 *
 */
public class StoryXMLParser {
	
	public PlayableStory parse( String xmlPath ){
		
		Document doc = null;
		PlayableStory story = new PlayableStory();
		
		// open xml file with dom parser
		try {
			System.out.println("Parsing XML file:");
			System.out.println(xmlPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new File(xmlPath));
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Get story node
		Node storynode = doc.getElementsByTagName("Story").item(0);
		Element storyelement = (Element)storynode;
		
		String revision = storyelement.getElementsByTagName("Revision").item(0).getTextContent();
		if(Double.parseDouble(revision)<7.0){
			System.out.println("WARNING: Revision is to low. The Revision should be 7 or higher.");
		}
		
		// Parse metadata
		story.setTitle(storyelement.getElementsByTagName("Title").item(0).getTextContent());
		System.out.println("Title: " + story.getTitle());
		
		story.setDescription(storyelement.getElementsByTagName("Description").item(0).getTextContent());
		System.out.println("Description: " + story.getDescription());
		
		// Not in xml anymore
		//story.setCreationDate(Date.valueOf(storyelement.getElementsByTagName("CreationDate").item(0).getTextContent()));
		//System.out.println("CreationDate: " + story.getCreationDate().toString());
		
		story.setSize(Double.valueOf(storyelement.getElementsByTagName("Size").item(0).getTextContent()));
		System.out.println("Size: " + story.getSize().toString());
		
		String[] locationstring = storyelement.getElementsByTagName("Location").item(0).getTextContent().trim().split("[ ]+");
		story.setLatitude(Double.valueOf(locationstring[0]));
		story.setLongitude(Double.valueOf(locationstring[1]));
		System.out.println("Latitude: " + story.getLatitude().toString());
		System.out.println("Longitude: " + story.getLongitude().toString());
		
		story.setRadius(Double.valueOf(storyelement.getElementsByTagName("Radius").item(0).getTextContent()));
		System.out.println("Radius: " + story.getRadius());
		
		System.out.println("Finished parsing XML file");
		return story;
	}

}
