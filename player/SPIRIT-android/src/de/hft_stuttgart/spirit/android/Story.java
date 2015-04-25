package de.hft_stuttgart.spirit.android;

import java.util.ArrayList;
/**
 * 
 * @author Mirjam
 *
 *Class 'Story' saves the data of a downloaded Story. This includes the meta data and the paths of the downloaded XML
 *and media data of the story. 
 */
public class Story {
	private String id;
	private String title;
	private String description;
	private String author;
	private String size;
	private String creation_date;
	private String location;
	private String radius;
	private String pathToXML;
	private ArrayList<String> storyMediaData;

	/**
	 * Constructor of the class 'Story'. A story can only be created when the following attributes are given.
	 * @param id id of the story, must be unique (meta data)
	 * @param title title of the story (meta data)
	 * @param description description of the story (meta data)
	 * @param author author of the story (meta data)
	 * @param size size of the story in MB (meta data)
	 * @param creation_date creation date of the story (meta data)
	 * @param location location in GPS coordinates (meta data)
	 * @param radius radius (meta data)
	 */
	public Story(String id, String title, String description, String author, String size, String creation_date, String location, String radius) {
	
		this.setId(id);
		this.setTitle(title);
		this.setDescription(description);
		this.setAuthor(author);
		this.setSize(size);
		this.setCreation_date(creation_date);
		this.setLocation(location);
		this.setRadius(radius);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getPathToXML() {
		return pathToXML;
	}

	public void setPathToXML(String pathToXML) {
		this.pathToXML = pathToXML;
	}

	public ArrayList<String> getStoryMediaData() {
		return storyMediaData;
	}

	public void setStoryMediaData(ArrayList<String> storyMediaData) {
		this.storyMediaData = storyMediaData;
	}

}
