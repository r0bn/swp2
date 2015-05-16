/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.scene.Scene;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.hft_stuttgart.spirit.Poi;


/**
 * @author Oliver
 *
 */
public class StoryXMLParser {
	
	public PlayableStory parse( String xmlPath ){
		
		Document doc = null;
		NodeList nodes;
		Map<String,Poi> storypoints = new HashMap<String, Poi>();
		Map<String,String> featureref = new HashMap<String, String>();
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
			return null;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
		
		// Get arelements node
		Node dependencynode = doc.getElementsByTagName("Dependency").item(0);
		Element dependencyelement = (Element)dependencynode;
		
		nodes = dependencyelement.getElementsByTagName("Storypoint");
		StoryPoint sPoint;
		for (int i = 0; i < nodes.getLength(); i++) {
			sPoint = getStoryPointfromNode(nodes.item(i));
			System.out.println("\n" + sPoint.toString());
			featureref.put(sPoint.getName(), getFreatureReffromNode(nodes.item(i)));
			System.out.println("Featureref: " + featureref.get(sPoint.getName()));
			storypoints.put(sPoint.getName(), sPoint);
		}
		
		// Get arelements node
		Node arelementsnode = doc.getElementsByTagName("ARElements").item(0);
		Element arelementselement = (Element)arelementsnode;
		
		nodes = arelementselement.getElementsByTagName("Feature");
		
		story.setStorypoints(storypoints);
		
		
		System.out.println("Finished parsing XML file");
		return story;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	
	private StoryPoint getStoryPointfromNode( Node node ){
		
		StoryPoint sPoint = new StoryPoint();
		
		sPoint.setName(node.getAttributes().getNamedItem("id").getNodeValue());
		
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("Container")) {
				sPoint.addDependency(getDependencyfromNode(subnode));
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
		
		return sPoint;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	
	private Dependency getDependencyfromNode( Node node ) {
		
		Dependency dependency = new Dependency();
		String strng;
		
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("Storypointlist")) {
				Node spointRef = subnode.getFirstChild();
				while (spointRef!=null) {
					if (spointRef.getNodeName().equals("StorypointRef")) {
						strng = spointRef.getAttributes().getNamedItem("xlink:href").getNodeValue();
						strng = strng.replace("#", "");
						dependency.addStorypoint(strng);
					}
					spointRef = spointRef.getNextSibling();
				}
			}
			if (subnode.getNodeName().equals("Itemlist")) {
				Node spointRef = subnode.getFirstChild();
				while (spointRef!=null) {
					if (spointRef.getNodeName().equals("ItemRef")) {
						strng = spointRef.getAttributes().getNamedItem("xlink:href").getNodeValue();
						strng = strng.replace("#", "");
						dependency.addItem(strng);
					}
					spointRef = spointRef.getNextSibling();
				}
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
		
		return dependency;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	
	private String getFreatureReffromNode( Node node ){
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("FeatureRef")) {
				return subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
			}
			subnode = subnode.getNextSibling();
		} while (subnode.getNextSibling()!=null);
		return null;
	}

}
