package de.hft_stuttgart.storytellar;

/**
 * Interface for interactions of a scene
 * 
 * @author Oliver
 *
 */
public interface Interaction {

	/**
	 * Start interaction.
	 */
	public void start();
	
	/**
	 * Get key for next scene.
	 * @return Key for next scene
	 */
	public String next();
	
}
