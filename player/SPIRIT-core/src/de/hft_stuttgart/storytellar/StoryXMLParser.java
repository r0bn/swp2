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
	
	private Map<String,String> featureRef = new HashMap<String, String>();
	private Map<String,String> interactionRef = new HashMap<String, String>();
	
	public PlayableStory parse( String xmlPath ){
		
		Document doc = null;
		NodeList nodes;
		Map<String,Poi> storypoints = new HashMap<String, Poi>();
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
		Node arelementsnode = doc.getElementsByTagName("ARElements").item(0);
		Element arelementselement = (Element)arelementsnode;
		
		// Parse features
		nodes = arelementselement.getElementsByTagName("Feature");
		StoryPoint sPoint;
		for (int i = 0; i < nodes.getLength(); i++) {
			sPoint = getStoryPointfromNode(nodes.item(i));
			storypoints.put(sPoint.getName(), sPoint);
		}
		story.setStorypoints(storypoints);
		
		// Get dependency node
		Node dependencynode = doc.getElementsByTagName("Dependency").item(0);
		Element dependencyelement = (Element)dependencynode;
		
		// Parse dependencies
		nodes = dependencyelement.getElementsByTagName("Storypoint");
		for (int i = 0; i < nodes.getLength(); i++) {
			addDependencyfromNode(nodes.item(i),story);
			//featureref.put(sPoint.getName(), getFreatureReffromNode(nodes.item(i)));
			//System.out.println("Featureref: " + featureref.get(sPoint.getName()));
			//storypoints.put(sPoint.getName(), sPoint);
		}
		
		for (String key : story.getStorypoints().keySet()) {
			System.out.println(story.getStorypoints().get(key).toString());
		}
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
		
		String sPointId = node.getAttributes().getNamedItem("id").getNodeValue();
		sPointId = sPointId.replace("Punkt", "");
		sPointId = sPointId.replace("Feature", "");
		sPointId = sPointId.replace("_", "");
		sPoint.setName(sPointId);
		
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("anchors")) {
				Node anchors_subnode = subnode.getFirstChild();
				do {
					if (anchors_subnode.getNodeName().equals("Geometry")) {
						addVideofromNode(anchors_subnode,sPoint);
						addLocationfromNode(anchors_subnode,sPoint);
					}
					if (anchors_subnode.getNodeName().equals("InteractionList")) {
						interactionRef.put(sPoint.getName(), getInteractionReffromNode(anchors_subnode));
					}
					anchors_subnode = anchors_subnode.getNextSibling();
				} while(anchors_subnode!=null);
			}
			subnode = subnode.getNextSibling();
		} while(subnode!=null);
		
		return sPoint;
	}
	
	/**
	 * 
	 * @param node
	 * @param sPoint
	 */
	
	private void addVideofromNode( Node node, StoryPoint sPoint ){
		
		String videofile;
		
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("assets")) {
				Node assets_subnode = subnode.getFirstChild();
				do {
					if (assets_subnode.getNodeName().equals("Video")||assets_subnode.getNodeName().equals("Image")) {
						Node video_subnode = assets_subnode.getFirstChild();
						do {
							if (video_subnode.getNodeName().equalsIgnoreCase("href")) {
								videofile = video_subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
								sPoint.setVideo(videofile);
								return;
							}
							video_subnode = video_subnode.getNextSibling();
						} while(video_subnode!=null);
					}
					assets_subnode = assets_subnode.getNextSibling();
				} while(assets_subnode!=null);
			}
			subnode = subnode.getNextSibling();
		} while(subnode!=null);
		System.out.println("WARNING: No video found for storypoint " + sPoint.getName());
	}
	
	/**
	 * 
	 * @param node
	 * @param sPoint
	 */
	
	private void addLocationfromNode( Node node, StoryPoint sPoint ){
		
		String[] point;
		
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("gml:Point")) {
				Node gmlpoint_subnode = subnode.getFirstChild();
				do {
					if (gmlpoint_subnode.getNodeName().equals("gml:pos")) {
						point = gmlpoint_subnode.getTextContent().split("[ ]+");
						sPoint.setLatitude(point[0]);
						sPoint.setLongitude(point[1]);
						return;
					}
					gmlpoint_subnode = gmlpoint_subnode.getNextSibling();
				} while(gmlpoint_subnode!=null);
			}
			subnode = subnode.getNextSibling();
		} while(subnode!=null);
		System.out.println("WARNING: No coordinates found for storypoint " + sPoint.getName());
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	
	private String getInteractionReffromNode( Node node ){
		
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("InteractionRef")) {
				return subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
			}
			subnode = subnode.getNextSibling();
		} while (subnode.getNextSibling()!=null);
		return null;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	
	private void addDependencyfromNode( Node node, PlayableStory story ) {
		
		Dependency dependency = new Dependency();
		String strng;
		
		String sPointId = node.getAttributes().getNamedItem("id").getNodeValue();
		
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("Container")) {
				Node cont_subnode = subnode.getFirstChild();
				do {
					if (cont_subnode.getNodeName().equals("Storypointlist")) {
						Node spointRef = cont_subnode.getFirstChild();
						while (spointRef!=null) {
							if (spointRef.getNodeName().equals("StorypointRef")) {
								strng = spointRef.getAttributes().getNamedItem("xlink:href").getNodeValue();
								strng = strng.replace("#", "");
								dependency.addStorypoint(strng);
							}
							spointRef = spointRef.getNextSibling();
						}
					}
					if (cont_subnode.getNodeName().equals("Itemlist")) {
						Node spointRef = cont_subnode.getFirstChild();
						while (spointRef!=null) {
							if (spointRef.getNodeName().equals("ItemRef")) {
								strng = spointRef.getAttributes().getNamedItem("xlink:href").getNodeValue();
								strng = strng.replace("#", "");
								dependency.addItem(strng);
							}
							spointRef = spointRef.getNextSibling();
						}
					}
					cont_subnode = cont_subnode.getNextSibling();
				} while (cont_subnode!=null);
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
		
		((StoryPoint)story.getStorypoints().get(sPointId)).addDependency(dependency);
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
