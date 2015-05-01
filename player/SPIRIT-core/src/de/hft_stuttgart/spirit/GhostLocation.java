package de.hft_stuttgart.spirit;


/*
 * GhostLocation:
 * enthält die Location eines Geistes und zusätzlich Daten zur Darstellung auf dem Radar.
 */
public class GhostLocation {
	private long lastDetected = 0;
	private int fadeTime = 1500;
	Location location;
	boolean updateAlphaPossible = true;
	float lastScanRadius = Float.MAX_VALUE;
	public boolean showNameInRadar = false;
	
	public GhostLocation(Location location) {
		this.location = location;
	}

	public void updateAlpha(float x, float y, float scanRadius, float size) {
		if (updateAlphaPossible) {
			if (scanRadius + size / 2f >= Math.sqrt(x * x + y * y)) {
				lastDetected = System.currentTimeMillis();
				updateAlphaPossible = false;
			}
		} else {
			// wenn aktueller scanradius kleiner als der letzte -> neue "radarsuche"
			if (scanRadius < lastScanRadius){
				updateAlphaPossible = true;
			}
		}
		lastScanRadius = scanRadius;
	}

	protected float getAlpha() {
		// zeit berechnen seit letztem erkennen
		long time = System.currentTimeMillis() - lastDetected;
		if (time > fadeTime) {
			return 0.0f;
		} else {
			return ((float) fadeTime - (float) time) / (float) fadeTime;
		}
	}
}
