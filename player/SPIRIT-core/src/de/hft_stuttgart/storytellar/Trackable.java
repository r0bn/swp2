package de.hft_stuttgart.storytellar;

import java.io.Serializable;

/**
 * Saves a trackable image. The trackable image is needed to define the position of a video/image on the screen.
 * Trackable images were parsed from the XML and downloaded with the other media files.
 * 
 * 
 * @author Mirjam
 *
 */

public class Trackable implements Serializable{
	
	private String id;
	private String algorithm;
	private String enabled;
	private String src;
	private String size;
	
	/**
	 * Constructor for class Trackable. Is created while parsing the XML-file in the StoryXMLParser
	 * @param id ID for Tracker item in XML
	 * @param algorithm specific algorithm
	 */
	public Trackable(String id, String algorithm) {
		this.setId(id);
		this.algorithm = algorithm;
	}
	
	/**
	 * Get the algorithm for the trackable item
	 * @return
	 */
	public String getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * Set the algorithm. Used by StoryXMLParser
	 * @param algorithm algorithm specified in Tracker-Tag
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	/**
	 * Get enabled
	 * @return
	 */
	public String isEnabled() {
		return enabled;
	}
	
	/**
	 * Set the enabled variable from the Trackable-Tag in the XML
	 * @param string
	 */
	public void setEnabled(String string) {
		this.enabled = string;
	}
	
	/**
	 * Returns the path to the trackable image
	 * @return
	 */
	public String getSrc() {
		return src;
	}
	
	/**
	 * Set the path to the trackable image from the Trackable-Tag in the XML
	 * @param src path to trackable image
	 */
	public void setSrc(String src) {
		this.src = src;
	}
	
	/**
	 * Returns the size of the trackable image
	 * @return
	 */
	public String getSize() {
		return size;
	}
	
	/**
	 * Sets the size of the trackable image from the Trackable-Tag in the XML
	 * @param size Size of the trackable image
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Returns the ID of the Trackable object
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the ID of the trackable object from the Tracker-tag in the XML
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	public String toString(){
		String s;
		s = "ID: " + this.getId() + "\n";
		s += "Algorithm: " + this.getAlgorithm() + "\n";
		s += "enabled: " + this.isEnabled() + "\n";
		s += "src: " + this.getSrc() + "\n";
		s += "size: " + this.getSize() + "\n";
		return s;
	}
	
}
