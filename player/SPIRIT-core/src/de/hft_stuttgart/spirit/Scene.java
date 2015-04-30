/**
 * 
 */
package de.hft_stuttgart.spirit;

import java.io.File;

/**
 * @author Marcel
 *That part of the Story that plays at a certain Location with Videos, images, etc.
 */
public class Scene {
	//maybe change status codes
	enum status {OPEN, ACTIVE, DONE};
	
	//TODO: Dependencies between scenes
	
	File video;
	Object interaction;		//TODO: implement class(es) for interaction
	
	/**
	 * starts the scene. not implemented yet.
	 */
	public void start(){
		
	}
}
