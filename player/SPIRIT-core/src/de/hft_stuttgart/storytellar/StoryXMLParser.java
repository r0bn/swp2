/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import javafx.scene.Scene;


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
 * The StoryXMLParser class is used to parse a arml-File into a PlayableStory-object
 * 
 * @author Oliver
 *
 */
public class StoryXMLParser {
	
	private Map<String,String> featureRef = new HashMap<String, String>();
	private Map<String,String> interactionRef = new HashMap<String, String>();
	private Map<String,StoryPoint> trackableRef = new HashMap<String, StoryPoint>();
	String pathToContent;
	
	/**
	 * Parses a XML-file and creates a PlayableStory-object from it.
	 * @param xmlPath	Path of xml-file to parse.
	 * @return	PlayableStory-object from XML.
	 */
	
	public PlayableStory parse( String xmlPath ){
		
		pathToContent = xmlPath.substring(0, xmlPath.length()-8);
		Document doc = null;
		NodeList nodes;
		Map<String,StoryPoint> storypoints = new HashMap<String, StoryPoint>();
		PlayableStory story = new PlayableStory();
		
		// open xml file with dom parser
		try {
			System.out.println(">>>> Parsing XML file <<<<");
			System.out.println("Path: " + xmlPath);
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
	//	story.setID(Integer.parseInt(storyelement.getElementsByTagName("id").item(0).getTextContent())); //java.lang.runtimeException 
		story.setTitle(storyelement.getElementsByTagName("Title").item(0).getTextContent());
		
		story.setDescription(storyelement.getElementsByTagName("Description").item(0).getTextContent());
		
		// Not in xml anymore
		//story.setCreationDate(Date.valueOf(storyelement.getElementsByTagName("CreationDate").item(0).getTextContent()));
		//System.out.println("CreationDate: " + story.getCreationDate().toString());
		
		story.setSize(Double.valueOf(storyelement.getElementsByTagName("Size").item(0).getTextContent()));
		
		String[] locationstring = storyelement.getElementsByTagName("Location").item(0).getTextContent().trim().split("[ ]+");
		story.setLatitude(Double.valueOf(locationstring[0]));
		story.setLongitude(Double.valueOf(locationstring[1]));
		
		story.setRadius(Double.valueOf(storyelement.getElementsByTagName("Radius").item(0).getTextContent()));
		
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
		
		nodes = arelementselement.getElementsByTagName("Interactions");
		if (nodes!=null && nodes.getLength()>0) {
			addInteractionsfromNode(nodes.item(0),story);
		}
		
		// Get dependency node
		Node dependencynode = doc.getElementsByTagName("Dependency").item(0);
		Element dependencyelement = (Element)dependencynode;
		
		// Parse dependencies
		nodes = dependencyelement.getElementsByTagName("Storypoint");
		for (int i = 0; i < nodes.getLength(); i++) {
			addDependencyfromNode(nodes.item(i),story);
		}
		
		//Get trackeables
		nodes = arelementselement.getElementsByTagName("Tracker");
		NodeList trackables = arelementselement.getElementsByTagName("Trackable");
		for (int i = 0; i < nodes.getLength(); i++) {
			addTrackeablefromNode(nodes.item(i), trackables);
		}
		
		System.out.println(story.toString() + "\n");
		System.out.println(">>>> Finished parsing XML file <<<<");
		return story;
	}
	
	/**
	 * Create a StoryPoint-object from a "Feature"-node.
	 * @param node	"Feature"-node to be parsed
	 * @return	StoryPoint-object from node
	 */
	
	private StoryPoint getStoryPointfromNode( Node node ){
		
		StoryPoint sPoint = new StoryPoint();
		
		String sPointId = node.getAttributes().getNamedItem("id").getNodeValue();
		if (sPointId.endsWith("_Feature")) {
			sPointId = (String) sPointId.subSequence(0, sPointId.lastIndexOf("_Feature"));
		}
		if (sPointId.endsWith("Feature")) {
			sPointId = (String) sPointId.subSequence(0, sPointId.lastIndexOf("Feature"));
		}
		sPoint.setName(sPointId);
		featureRef.put(node.getAttributes().getNamedItem("id").getNodeValue(), sPoint.getName());
		
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
						String val = getInteractionReffromNode(anchors_subnode);
						if (val!=null) {
							interactionRef.put(val, sPoint.getName());
						}
					}
					if (anchors_subnode.getNodeName().equals("anchorRef")) {
						trackableRef.put(anchors_subnode.getAttributes().getNamedItem("xlink:href").getNodeValue().replace("#", ""), sPoint);
						System.out.println("Add Anchor: "+anchors_subnode.getAttributes().getNamedItem("xlink:href").getNodeValue().replace("#", ""));
					}
					anchors_subnode = anchors_subnode.getNextSibling();
				} while(anchors_subnode!=null);
			}
			subnode = subnode.getNextSibling();
		} while(subnode!=null);
		
		return sPoint;
	}
	
	/**
	 * Get a video-file path from a "Geometry"-node and add it to a StoryPoint-object 
	 * @param node	"Geometry"-node to be parsed
	 * @param sPoint	StoryPoint-object where video-file path should be added to
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
								sPoint.setVideo(pathToContent+videofile);
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
	 * Get a location (latitude and longitude) from a "Geometry"-node and add it to a StoryPoint-object 
	 * @param node	"Geometry"-node to be parsed
	 * @param sPoint	StoryPoint-object where location should be added to
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
	 * Get a interaction reference as String from a "InteractionList"-node.
	 * @param node	"InteractionList"-node tobe parsed
	 * @return	String with a interaction reference
	 */
	
	private String getInteractionReffromNode( Node node ){
		
		Node subnode = node.getFirstChild();

		do {
			if (subnode.getNodeName().equals("InteractionRef")) {
				return subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
		return null;
	}
	
	/**
	 * Get Interaction-objects from a "Interactions"-node and add them to a PlayableStory-object
	 * @param node	"Interactions"-node to be parsed
	 * @param story	PlayableStory-object where Interaction-objects should be added to
	 */
	
	private void addInteractionsfromNode( Node node, PlayableStory story ){
		
		Element element = (Element)node;
		if (element==null) {
			return;
		}
		
		NodeList subnodes = element.getElementsByTagName("Quiz");
		if (subnodes!=null) {
			for (int i = 0; i < subnodes.getLength(); i++) {
				addQuizfromNode(subnodes.item(i), story);
			}
		}
		
		subnodes = element.getElementsByTagName("Item");
		if (subnodes!=null) {
			for (int i = 0; i < subnodes.getLength(); i++) {
				addItemfromNode(subnodes.item(i), story);
			}
		}
		
		subnodes = element.getElementsByTagName("Chooser");
		if (subnodes!=null) {
			for (int i = 0; i < subnodes.getLength(); i++) {
				addWaychooserfromNode(subnodes.item(i), story);
			}
		}
	}
	
	/**
	 * Get a Quiz-object from a "Quiz"-node and add it to a PlayableStory-object
	 * @param node	"Quiz"-node to be parsed
	 * @param story	PlayableStory-object where the Quiz-object should be added to
	 */
	
	private void addQuizfromNode ( Node node, PlayableStory story ){
		
		String featureref = "";
		String ontrueref = "";
		String onfalseref = "";
		String ontrue = "";
		String onfalse = "";
		String ontrueanswer = "";
		String onfalseanswer = "";
		String quizId = node.getAttributes().getNamedItem("id").getNodeValue();
		Quiz quiz = new Quiz();
		
		// Get feature reference
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("FeatureRef")) {
				featureref = subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
				featureref = featureref.replace("#", "");
			}
			if (subnode.getNodeName().equals("Question")) {
				quiz.setQuestion(subnode.getTextContent());
			}
			if (subnode.getNodeName().equals("OnTrue")) {
				ontrue = subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
				ontrue = ontrue.replace("#", "");
				ontrueref = ontrue;
				if (ontrue.endsWith("_Feature")) {
					ontrue = (String) ontrue.subSequence(0, ontrue.lastIndexOf("_Feature"));
				}
				if (ontrue.endsWith("Feature")) {
					ontrue = (String) ontrue.subSequence(0, ontrue.lastIndexOf("Feature"));
				}
			}
			if (subnode.getNodeName().equals("OnFalse")) {
				onfalse = subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
				onfalse = onfalse.replace("#", "");
				onfalseref = onfalse;
				if (onfalse.endsWith("_Feature")) {
					onfalse = (String) onfalse.subSequence(0, onfalse.lastIndexOf("_Feature"));
				}
				if (onfalse.endsWith("Feature")) {
					onfalse = (String) onfalse.subSequence(0, onfalse.lastIndexOf("Feature"));
				}
			}
			if (subnode.getNodeName().equals("Answer")) {
				String status = ((Element)subnode).getElementsByTagName("Status").item(0).getTextContent();
				if (status.toLowerCase().trim().equals("true")) {
					ontrueanswer = ((Element)subnode).getElementsByTagName("Text").item(0).getTextContent();
				} else {
					onfalseanswer = ((Element)subnode).getElementsByTagName("Text").item(0).getTextContent();
				}
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
		
		quiz.setAnswers(Arrays.asList(ontrueanswer,onfalseanswer));
		quiz.setNextStorypoints(Arrays.asList(ontrue,onfalse));
		
		StoryPoint sPoint = ((StoryPoint)story.getStorypoints().get(featureRef.get(featureref)));
		if (!featureRef.containsKey(featureref)) {
			System.out.println("WARNING: Reference for id " + featureref + " not found in featureRef. featureRefs are: "
			+ featureRef.keySet().toString());
			return;
		}else{
			if (sPoint==null) {
				System.out.println("WARNING: Storypoint for id " + featureRef.get(featureref) + " not found in storypoints. Storypoints are: "
				+ story.getStorypoints().keySet().toString());
				return;
			}
		}
		sPoint.setInteraction(quizId);
		story.addInteraction(quizId, quiz);
		
		Dependency dependency = new Dependency();
		dependency.addStorypoint(sPoint.getName());
		if (!featureRef.containsKey(ontrueref)) {
			System.out.println("WARNING: OnTrue-Reference for id " + ontrueref + " not found in featureRef. featureRefs are: "
					+ featureRef.keySet().toString());
		}else{
			if (!story.getStorypoints().containsKey(featureRef.get(ontrueref))) {
				System.out.println("WARNING: Storypoint for id " + featureRef.get(ontrueref) + " not found in storypoints. Storypoints are: "
				+ story.getStorypoints().keySet().toString());
			} else {
				((StoryPoint)story.getStorypoints().get(featureRef.get(ontrueref))).addDependency(dependency);
			}
		}
		
		if (!featureRef.containsKey(onfalseref)) {
			System.out.println("WARNING: OnFalse-Reference for id " + onfalseref + " not found in featureRef. featureRefs are: "
					+ featureRef.keySet().toString());
		} else{
			if (!story.getStorypoints().containsKey(featureRef.get(onfalseref))) {
				System.out.println("WARNING: Storypoint for id " + featureRef.get(onfalseref) + " not found in storypoints. Storypoints are: "
				+ story.getStorypoints().keySet().toString());
			} else {
				((StoryPoint)story.getStorypoints().get(featureRef.get(onfalseref))).addDependency(dependency);
			}
		}
	}
	
	/**
	 * Get a Quiz-object from a "WayChooser"-node and add it to a PlayableStory-object
	 * @param node	"WayChooser"-node to be parsed
	 * @param story	PlayableStory-object where the Quiz-object should be added to
	 */
	
	private void addWaychooserfromNode ( Node node, PlayableStory story ){
		
		String ref;
		String featureref = "";
		String quizId = node.getAttributes().getNamedItem("id").getNodeValue();
		Chooser chooser = new Chooser();
		List<String> answers = new ArrayList<String>();
		List<String> items = new ArrayList<String>();
		List<String> sPoints = new ArrayList<String>();
		
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("FeatureRef")) {
				featureref = subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
				featureref = featureref.replace("#", "");
			}
			if (subnode.getNodeName().equals("Question")) {
				chooser.setQuestion(subnode.getTextContent());
			}
			if (subnode.getNodeName().equals("Answer")) {
				answers.add(((Element)subnode).getElementsByTagName("Text").item(0).getTextContent());
				if (((Element)subnode).getElementsByTagName("ItemRef").getLength()>0) {
					ref = ((Element)subnode).getElementsByTagName("ItemRef").item(0)
							.getAttributes().getNamedItem("xlink:href").getNodeValue();
					ref = ref.replace("#", "");
					items.add(ref);
				}else{
					items.add("");
				}
				if (((Element)subnode).getElementsByTagName("FeatureRef").getLength()>0) {
					ref = ((Element)subnode).getElementsByTagName("FeatureRef").item(0)
							.getAttributes().getNamedItem("xlink:href").getNodeValue();
					ref = ref.replace("#", "");
					sPoints.add(ref);
				} else {
					sPoints.add("");
				}
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
		chooser.setAnswers(answers);
		chooser.setItems(items);
		chooser.setNextScenes(sPoints);
		
		StoryPoint sPoint = ((StoryPoint)story.getStorypoints().get(featureRef.get(featureref)));
		if (!featureRef.containsKey(featureref)) {
			System.out.println("WARNING: Reference for id " + featureref + " not found in featureRef. featureRefs are: "
			+ featureRef.keySet().toString());
			return;
		}else{
			if (sPoint==null) {
				System.out.println("WARNING: Storypoint for id " + featureRef.get(featureref) + " not found in storypoints. Storypoints are: "
				+ story.getStorypoints().keySet().toString());
				return;
			}
		}
		sPoint.setInteraction(quizId);
		story.addInteraction(quizId, chooser);
	}
	
	/**
	 * Get a Item-object from a "Item"-node and add it to a PlayableStory-object
	 * @param node	"Item"-node to be parsed
	 * @param story	PlayableStory-object where the Item-object should be added to
	 */
	
	private void addItemfromNode ( Node node, PlayableStory story ){
		
		String featureref = "";
		String itemId = node.getAttributes().getNamedItem("id").getNodeValue();
		Item item = new Item();
		
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("FeatureRef")) {
				featureref = subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
				featureref = featureref.replace("#", "");
			}
			if (subnode.getNodeName().equals("Description")) {
				item.setDescription(subnode.getTextContent());
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
		
		StoryPoint sPoint = ((StoryPoint)story.getStorypoints().get(featureRef.get(featureref)));
		if (!featureRef.containsKey(featureref)) {
			System.out.println("WARNING: Reference for id " + featureref + " not found in featureRef. featureRefs are: "
			+ featureRef.keySet().toString());
			return;
		}else{
			if (sPoint==null) {
				System.out.println("WARNING: Storypoint for id " + featureRef.get(featureref) + " not found in storypoints. Storypoints are: "
				+ story.getStorypoints().keySet().toString());
				return;
			}
		}
		sPoint.setInteraction(itemId);
		story.addInteraction(itemId, item);
	}
	
	/**
	 * Get a Dependency-objects from a "Dependency"-node and add them to a PlayableStory-object
	 * @param node	"Dependency"-node to be parsed
	 * @param story PlayableStory-object where the Dependency-objects should be added to
	 */
	
	private void addDependencyfromNode( Node node, PlayableStory story ) {
		
		Dependency dependency = new Dependency();
		Boolean endpoint = false;
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
			if (subnode.getNodeName().equals("EndOfStory") && subnode.getTextContent().equals("true")) {
				endpoint = true;
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
		
		StoryPoint sPoint = (StoryPoint)story.getStorypoints().get(sPointId);
		if (sPoint==null) {
			System.out.println("WARNING: Storypoint for id " + sPointId + " not found in storypoints. Storypoints are: " + story.getStorypoints().keySet().toString());
		}else{
			sPoint.addDependency(dependency);
			if (endpoint) {
				sPoint.setIsEndStorypoint(true);
			}
		}
	}

	private void addTrackeablefromNode(Node node, NodeList trackables){
		System.out.println(node.getAttributes().getNamedItem("id").getNodeValue());
		StoryPoint sPoint = trackableRef.get(node.getAttributes().getNamedItem("id").getNodeValue());
		Trackable temp = new Trackable(node.getAttributes().getNamedItem("id").getNodeValue(), getTrackableUri(node));
		for (int i = 0; i < trackables.getLength(); i++) {
			System.out.println(temp.getId()+"  equals  "+trackables.item(i).getAttributes().getNamedItem("id").getNodeValue().replace("Trackable", "Tracker"));
			System.out.println(temp.getId().equals(trackables.item(i).getAttributes().getNamedItem("id").getNodeValue().replace("Trackable", "Tracker")));
			if(temp.getId().equals(trackables.item(i).getAttributes().getNamedItem("id").getNodeValue().replace("Trackable", "ImageTracker"))){
				getTrackableValues(trackables.item(i), temp);
				break;
			}
		}
		System.out.println(temp.toString());
		sPoint.setTrackable_image(temp);
		
	}

	private String getTrackableUri(Node node){
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("uri")) {
				return subnode.getAttributes().getNamedItem("xlink:href").getNodeValue();
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
		return null;
	}
	
	private void getTrackableValues(Node node, Trackable temp){
		Node subnode = node.getFirstChild();
		do {
			if (subnode.getNodeName().equals("enabled")) {
				temp.setEnabled(subnode.getTextContent());
			}
			if (subnode.getNodeName().equals("size")) {
				temp.setSize(subnode.getTextContent());
			}
			if (subnode.getNodeName().equals("config")) {
				Node subsubnode = subnode.getFirstChild();
				do{
					if (subsubnode.getNodeName().equals("src")) {
						temp.setSrc(pathToContent+subsubnode.getTextContent());
					}
					subsubnode = subsubnode.getNextSibling();
				} while(subsubnode.getNextSibling()!=null);
			}
			subnode = subnode.getNextSibling();
		} while (subnode!=null);
	}
}
