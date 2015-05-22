/**
 * 
 */
package de.hft_stuttgart.storytellar;

/**
 * @author Oliver
 * Interaction of a scene as item to pick up
 */
public class Item implements Interaction{

	private String description;
	private Boolean isCollected;
	
	public Item(){
		isCollected = false;
	}
	
	public Item(String description,	Boolean isCollected){
		this.description = description;
		this.isCollected = isCollected;
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

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String next() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString(){
		String strng = "Type: " + this.getClass().getName() + "\n";
		strng += "Description: " + description + "\n";
		strng += "IsCollected: " + isCollected.toString() + "\n";
		return strng;
	}
	
}
