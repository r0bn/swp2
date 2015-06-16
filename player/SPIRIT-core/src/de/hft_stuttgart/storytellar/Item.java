/**
 * 
 */
package de.hft_stuttgart.storytellar;

/**
 * Interaction of a scene as item to pick up
 * 
 * @author Oliver
 * 
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
	 * @return the description, as String
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
	 * @return the value of isCollected, as Boolean
	 */
	public Boolean getIsCollected() {
		return isCollected;
	}

	/**
	 * @param isCollected the value isCollected should be set to
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

	/**
	 * Print the object
	 */
	public String toString(){
		String strng = "Type: " + this.getClass().getName() + "\n";
		strng += "Description: " + description + "\n";
		strng += "IsCollected: " + isCollected.toString() + "\n";
		return strng;
	}
	
}
