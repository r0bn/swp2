/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hft_stuttgart.spirit.Poi;

/**
 * That part of the Story that plays at a certain Location with Videos, images, etc.
 * 
 * @author Marcel
 */
public class StoryPoint extends Poi implements Serializable{
	
	//maybe change status codes
	//private String name;
	private StorypointStatus status;
	private List<Dependency> dependencies; // Instead of the, in the xml-documentation described, containers are multipe dependencies used.
	private File video;
	private String interaction;
	private Boolean isEndStorypoint;
	private Trackable trackable_image = null;
	
	public StoryPoint(){
		super();
		this.dependencies = new ArrayList<Dependency>();
		this.video = new File("");
		this.status = StorypointStatus.OPEN;
		this.isEndStorypoint = false;
	}
	
	public StoryPoint(StorypointStatus status,	List<Dependency> dependencies, File video, String interaction, Boolean isEndStorypoint){
		super();
		this.status = status;
		this.dependencies = dependencies;
		this.video = video;
		this.interaction = interaction;
		this.isEndStorypoint = isEndStorypoint;
	}
	
	/**
	 * Check if one of the dependencys of this storypoint is fulfilled
	 * @param interactions Interaction-objects of the story
	 * @param sPoints StoryPoint-objects of the story
	 * @return true if at least one dependency is fulfilled
	 */
	
	public Boolean isPlayable(Map<String,Interaction> interactions, Map<String,StoryPoint> sPoints){
		for (int i = 0; i < dependencies.size(); i++) {
			if (!dependencies.get(i).isFulfilled(interactions, sPoints)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param dependency the dependency to be added
	 */
	
	public void addDependency( Dependency dependency ){
		dependencies.add(dependency);
	}
	
	/**
	 * @return the name
	 */
	/*public String getName() {
		return name;
	}*/

	/**
	 * @param name the name to set
	 */
	/*public void setName(String name) {
		this.name = name;
	}*/

	/**
	 * @return the status, as StorypointStatus
	 */
	public StorypointStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StorypointStatus status) {
		this.status = status;
	}

	/**
	 * @return the dependencys, as list
	 */
	public List<Dependency> getDependency() {
		return dependencies;
	}

	/**
	 * @param dependency the dependency to set
	 */
	public void setDependency(List<Dependency> dependency) {
		this.dependencies = dependency;
	}

	/**
	 * @return the video
	 */
	/*public File getVideo() {
		return video;
	}*/

	/**
	 * @param video the video to set
	 */
	/*public void setVideo(File video) {
		this.video = video;
	}*/

	/**
	 * @return the interaction, as string
	 */
	public String getInteraction() {
		return interaction;
	}

	/**
	 * @param interaction the interaction to set
	 */
	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}

	/**
	 * @return the isEndScene
	 */
	public Boolean getIsEndStorypoint() {
		return isEndStorypoint;
	}

	/**
	 * @param isEndScene the isEndScene to set
	 */
	public void setIsEndStorypoint(Boolean isEndScene) {
		this.isEndStorypoint = isEndScene;
	}
	
	/**
	 * Print the object
	 */
	@Override
	public String toString(){
		String strng;
		strng = ">>> Storypoint: " + super.getName() + " <<<\n";
		strng += "Status: " + status.toString() + "\n";
		strng += "Video: " + super.getVideo() + "\n";
		strng += "Interaction: " + interaction + "\n";
		strng += "IsEndStorypoint: " + isEndStorypoint.toString() + "\n";
		strng += "Dependencis:\n";
		for (int i = 0; i < dependencies.size(); i++) {
			strng += dependencies.get(i).toString() + "\n";
		}
		strng += "Trackable: "+trackable_image + "\n";
		strng += super.toString();
		return strng;
	}

	/**
	 * 
	 * @return the trackable_image, as Trackable
	 */
	public Trackable getTrackable_image() {
		return trackable_image;
	}

	/**
	 * 
	 * @param trackable_image the Trackable to set
	 */
	public void setTrackable_image(Trackable trackable_image) {
		this.trackable_image = trackable_image;
	}
}
