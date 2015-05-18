/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hft_stuttgart.spirit.Poi;

/**
 * @author Marcel
 *That part of the Story that plays at a certain Location with Videos, images, etc.
 */
public class StoryPoint extends Poi{
	
	//maybe change status codes
	private String name;
	private StorypointStatus status;
	private List<Dependency> dependencies; // Instead of the, in the xml-documentation described, containers are multipe dependencies used.
	private File video;
	private Interaction interaction;
	private Boolean isEndStorypoint;
	
	public StoryPoint(){
		super();
		this.dependencies = new ArrayList<Dependency>();
		this.video = new File("");
		this.interaction = new Quiz(); // Default for interaction is quiz
		this.status = StorypointStatus.OPEN;
		this.isEndStorypoint = false;
	}
	
	public StoryPoint(StorypointStatus status,	List<Dependency> dependencies, File video, Interaction interaction, Boolean isEndStorypoint){
		super();
		this.status = status;
		this.dependencies = dependencies;
		this.video = video;
		this.interaction = interaction;
		this.isEndStorypoint = isEndStorypoint;
	}
	
	/**
	 * 
	 * @param dependency
	 */
	
	public void addDependency( Dependency dependency ){
		dependencies.add(dependency);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
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
	 * @return the dependency
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
	 * @return the interaction
	 */
	public Interaction getInteraction() {
		return interaction;
	}

	/**
	 * @param interaction the interaction to set
	 */
	public void setInteraction(Interaction interaction) {
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
	 * starts the scene. not implemented yet.
	 */
	public void start(){
		
	}
	
	@Override
	public String toString(){
		String strng;
		strng = ">>> Storypoint: " + name + " <<<\n";
		strng += "Status: " + status.toString() + "\n";
		strng += "Video: " + super.getVideo() + "\n";
		strng += "Interaction:\n" + interaction.toString() + "\n";
		strng += "IsEndStorypoint: " + isEndStorypoint.toString() + "\n";
		strng += "Dependencis:\n";
		for (int i = 0; i < dependencies.size(); i++) {
			strng += dependencies.get(i).toString() + "\n";
		}
		strng += super.toString();
		return strng;
	}
}
