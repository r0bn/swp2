package de.hft_stuttgart.spirit.android;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
/**
 * 
 * @author Mirjam
 *
 *Class 'Story' saves the data of a downloaded Story. This includes the meta data and the paths of the downloaded XML
 *and media data of the story. Class is serializable to load already downloaded stories to list 'downloadedStories' when the app starts.
 */
public class Story implements Serializable {
	private Integer id;
	private String title;
	private String description;
	private String author;
	private String size;
	private String size_uom;
	private String location;
	private String radius;
	private String radius_uom;
	private String created_at;
	private String updated_at;
	private String pathToXML;
	private boolean alreadyDownloaded;
	private HashMap<String,String> storyMediaData;
	private double longitude;
	private double latitude;
	private Date created_at_Date;
	private Date updated_at_Date;
	private boolean isUpToDate;
	
	private static final long serialVersionUID = 10050001;

	/**
	 * Constructor of the class 'Story'. A story can only be created when the following attributes are given.
	 * @param id id of the story, must be unique (meta data)
	 * @param title title of the story (meta data)
	 * @param description description of the story (meta data)
	 * @param author author of the story (meta data)
	 * @param size size of the story in MB (meta data)
	 * @param creation_date creation date of the story (meta data)
	 * @param location location in GPS coordinates (meta data)
	 * @param radius radius in km (meta data)
	 */
	public Story(Integer id, String title, String description, String author, String size, String size_uom, String location, String radius, String radius_uom, String created_at, String updated_at, boolean alreadyDownloaded) {
	
		this.setId(id);
		this.setTitle(title);
		this.setDescription(description);
		this.setAuthor(author);
		this.setSize(size);
		this.setSize_uom(size_uom);;
		this.setLocation(location);
		this.setRadius(radius);
		this.setRadius_uom(radius_uom);
		this.setCreated_at(created_at);
		this.setUpdated_at(updated_at);
		try {
			created_at_Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created_at);
			updated_at_Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(updated_at);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.setAlreadyDownloaded(alreadyDownloaded);
		String[] longLat = location.split(" ");
		latitude = Double.valueOf(longLat[0]);
		longitude = Double.valueOf(longLat[1]);
		setUpToDate(true);
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer integer) {
		this.id = integer;
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

	public HashMap<String,String> getStoryMediaData() {
		return storyMediaData;
	}

	public void setStoryMediaData(HashMap<String,String> storyMediaData) {
		this.storyMediaData = storyMediaData;
	}

	public boolean isAlreadyDownloaded() {
		return alreadyDownloaded;
	}

	public void setAlreadyDownloaded(boolean alreadyDownloaded) {
		this.alreadyDownloaded = alreadyDownloaded;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public double  getLatitude(){
		return latitude;
	}

	public String getSize_uom() {
		return size_uom;
	}

	public void setSize_uom(String size_uom) {
		this.size_uom = size_uom;
	}

	public String getRadius_uom() {
		return radius_uom;
	}

	public void setRadius_uom(String radius_uom) {
		this.radius_uom = radius_uom;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public Date getCreated_at_Date() {
		return created_at_Date;
	}
	
	public void setCreated_at_Date(Date created_at_Date) {
		this.created_at_Date = created_at_Date;
	}
	
	public Date getUpdated_at_Date() {
		return updated_at_Date;
	}
	
	public void setUpdated_at_Date(Date updated_at_Date) {
		this.updated_at_Date = updated_at_Date;
	}
	
	public boolean isUpToDate() {
		return isUpToDate;
	}
	
	public void setUpToDate(boolean isUpToDate) {
		this.isUpToDate = isUpToDate;
	}
}
