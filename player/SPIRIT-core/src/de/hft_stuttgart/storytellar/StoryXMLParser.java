/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * @author Oliver
 *
 */
public class StoryXMLParser {
	
	public PlayableStory parse( String xmlPath ){
		
		Document doc = null;
		
		try {
			System.out.println("Parsing XML file:");
			System.out.println(xmlPath);
			System.out.println("");
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
		
		return new PlayableStory();
	}

}
