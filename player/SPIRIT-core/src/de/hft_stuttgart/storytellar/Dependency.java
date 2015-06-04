/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Describes the scenes and items for a dependency of a scene
 * 
 * @author Oliver
 *
 */
public class Dependency {

	private List<String> storypoints;
	private List<String> items;
	
	public Dependency(){
		storypoints = new ArrayList<String>();
		items = new ArrayList<String>();
	}
	
	public Dependency(List<String> scenes, List<String> items){
		this.storypoints = scenes;
		this.items = items;
	}
	
	/**
	 * 
	 * @param storypoint
	 */
	
	public void addStorypoint( String storypoint ) {
		storypoints.add(storypoint);
	}
	
	/**
	 * 
	 * @param item
	 */
	
	public void addItem( String item ) {
		items.add(item);
	}
	
	/**
	 * @return the scenes
	 */
	public List<String> getStorypoints() {
		return storypoints;
	}
	/**
	 * @param scenes the scenes to set
	 */
	public void setStorypoints(List<String> storypoint) {
		this.storypoints = storypoint;
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
	 * Check if conditions of dependency are fulfilled.
	 * @return true if conditions are fulfilled, else false
	 */
	public Boolean isFulfilled(Map<String,Interaction> interactions, Map<String,StoryPoint> sPoints){
		//Check if required items are collected
		for (int i = 0; i < items.size(); i++) {
			if (!((Item)interactions.get(items.get(i))).getIsCollected()) {
				return false;
			}
		}
		//Check if required storypoints are fulfilled
		for (int i = 0; i < storypoints.size(); i++) {
			if (!sPoints.get(storypoints.get(i)).getStatus().equals(StorypointStatus.DONE)) {
				return false;
			}
		}
		//Return true if all dependencys are fulfilled
		return true;
	}
	
	public String toString(){
		String strng;
		strng = "Depending storypoints: ";
		for (int i = 0; i < storypoints.size()-1; i++) {
			strng += storypoints.get(i) + ", ";
		}
		if (storypoints.size()>0) {
			strng += storypoints.get(storypoints.size()-1);
		}
		strng += "\nDepending items: ";
		for (int i = 0; i < items.size()-1; i++) {
			strng += items.get(i) + ", ";
		}
		if (items.size()>0) {
			strng += items.get(items.size()-1);
		}
		return strng;
	}
	
}
