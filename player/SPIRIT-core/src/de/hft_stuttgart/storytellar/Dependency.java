/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oliver
 * Describes the scenes and items for a dependency of a scene
 */
public class Dependency {

	List<String> scenes;
	List<String> items;
	
	public Dependency(){
		scenes = new ArrayList<String>();
		items = new ArrayList<String>();
	}
	
	public Dependency(List<String> scenes, List<String> items){
		this.scenes = scenes;
		this.items = items;
	}
	
	/**
	 * @return the scenes
	 */
	public List<String> getScenes() {
		return scenes;
	}
	/**
	 * @param scenes the scenes to set
	 */
	public void setScenes(List<String> scenes) {
		this.scenes = scenes;
	}
	/**
	 * @return the items
	 */
	public List<String> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<String> items) {
		this.items = items;
	}
	
	/**
	 * Check if conditions of dependency are fulfilled. not implemented yet.
	 * @return true if conditions are fulfilled, else false
	 */
	public Boolean isFulfilled(){
		return null;
	}
	
}
