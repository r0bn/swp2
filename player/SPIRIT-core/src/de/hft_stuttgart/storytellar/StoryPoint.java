/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcel
 *That part of the Story that plays at a certain Location with Videos, images, etc.
 */
public class StoryPoint {
	
	//maybe change status codes
	Status status;
	List<Dependency> dependencies; // Instead of the, in the xml-documentation described, containers are multipe dependencies used.
	File video;
	Interaction interaction;
	Boolean isEndScene;
	
	public StoryPoint(){
		this.dependencies = new ArrayList<Dependency>();
		this.video = new File("");
		this.interaction = new Quiz(); // Default for interaction is quiz
	}
	
	public StoryPoint(Status status,	List<Dependency> dependencies, File video, Interaction interaction, Boolean isEndScene){
		this.status = status;
		this.dependencies = dependencies;
		this.video = video;
		this.interaction = interaction;
		this.isEndScene = isEndScene;
	}
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
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
	public File getVideo() {
		return video;
	}

	/**
	 * @param video the video to set
	 */
	public void setVideo(File video) {
		this.video = video;
	}

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
	public Boolean getIsEndScene() {
		return isEndScene;
	}

	/**
	 * @param isEndScene the isEndScene to set
	 */
	public void setIsEndScene(Boolean isEndScene) {
		this.isEndScene = isEndScene;
	}

	/**
	 * starts the scene. not implemented yet.
	 */
	public void start(){
		
	}
}
