/**
 * 
 */
package de.hft_stuttgart.storytellar;

/**
 * @author Oliver
 * Interaction of a scene as item to pick up
 */
public class Item implements Interaction{

	String description;
	Boolean isCollected;
	String nextScene;
	
	public Item(){
		
	}
	
	public Item(String description,	Boolean isCollected, String nextScene){
		this.description = description;
		this.isCollected = isCollected;
		this.nextScene = nextScene;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isCollected
	 */
	public Boolean getIsCollected() {
		return isCollected;
	}

	/**
	 * @param isCollected the isCollected to set
	 */
	public void setIsCollected(Boolean isCollected) {
		this.isCollected = isCollected;
	}


	/**
	 * @param nextScene the nextScene to set
	 */
	public void setNextScene(String nextScene) {
		this.nextScene = nextScene;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String next() {
		// TODO Auto-generated method stub
		return null;
	}

}
