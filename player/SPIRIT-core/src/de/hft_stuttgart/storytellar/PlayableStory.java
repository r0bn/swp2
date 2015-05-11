/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hft_stuttgart.spirit.Poi;


/**
 * @author Marcel
 * Is the actual Story for the StoryTellar project
 */
public class PlayableStory {
	
	// Metadata:
	String title;
	String description;
	Double size;
	String author;
	Date creationDate;
	Double latitude;
	Double longitude;
	Double radius;
	
	// Other Data:
	Map<String,Poi> storypoints;
	String initialStorypoint;
	String currentStorypoint;
	
	public PlayableStory(){
		storypoints = new HashMap<String, Poi>();
	}
	
	public PlayableStory(String title, String description, Double size,	String author, Date creationDate, Double latitude, 
			Double longitude, Double radius, Map<String,Poi> scenes, String initialScene, String currentScene){
		this.title = title;
		this.description = description;
		this.size = size;
		this.author = author;
		this.creationDate = creationDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
		this.storypoints = scenes;
		this.initialStorypoint = initialScene;
		this.currentStorypoint = currentScene;
	}
	
	// Get & Set
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
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getRadius() {
		return radius;
	}
	public void setRadius(Double radius) {
		this.radius = radius;
	}
	public Map<String, Poi> getStorypoints() {
		return storypoints;
	}
	public void setStorypoints(Map<String, Poi> scenes) {
		this.storypoints = scenes;
	}
	public String getInitialScene() {
		return initialStorypoint;
	}
	public void setInitialScene(String initialScene) {
		this.initialStorypoint = initialScene;
	}
	public String getCurrentStorypoint() {
		return currentStorypoint;
	}
	public void setCurrentStorypoint(String currentScene) {
		this.currentStorypoint = currentScene;
	}

	//Functionallity:
	
	/**
	 * starts the story. not implemented yet.
	 */
	public void start(){
		
	}
	/**
	 * ends the story. not implemented yet.
	 */
	public void end(){
		
	}
	/**
	 * updates the story lifecycle. not implemented yet.
	 */
	public void update(){
		
	}
	/**
	 * saves the current progress. not implemented yet.
	 */
	public void save(){
		
	}
	/**
	 * loads the savestate of the story. not implemented yet.
	 */
	public void load(){
		
	}
}