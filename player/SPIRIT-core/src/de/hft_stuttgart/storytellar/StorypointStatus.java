/**
 * 
 */
package de.hft_stuttgart.storytellar;

/**
 * Describe the status of a storypoint. The status can be :
 *  open	the storypoint is not played yet
 *  active	the storypoint is currently played
 *  done	the storypoint is finished
 *  queued	the storypoint is the next to be played
 * 
 * @author Oliver
 */
public enum StorypointStatus {
	OPEN, 
	ACTIVE, 
	DONE,
	QUEUED
}
