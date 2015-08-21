package de.hft_stuttgart.spirit.android;

import de.hft_stuttgart.spirit.OrbInfos;

public class OrbTools implements OrbInfos, OrbCallback {
	String infoText = "";
	double avg = Double.MAX_VALUE;
	double min = Double.MAX_VALUE;
	double max = Double.MAX_VALUE;
	boolean waitingForOrb = false;
	String name = ""; // bildname das geprüft wird
	String vergleich = ""; // name des bildes das abgeglichen werden soll
	String nameLastResult = ""; // bildname letztes ergebnis

	@Override
	public String getInfoText() {
		return infoText;
	}

	@Override
	public double getOrbAvg() {
		return min;
	}

	@Override
	public double getOrbMin() {
		return max;
	}

	@Override
	public double getOrbMax() {
		return avg;
	}

	@Override
	public void onOrbResult(orbResult or) {
		System.out.println("Ergebis: Min/Max/Avg: " + or.min + "/" + or.max
				+ "/" + or.avg+" | "+or.name);
		waitingForOrb = false;
		nameLastResult = or.name;
		min = or.min;
		max = or.max;
		avg = or.avg;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setVergleichName(String name) {
		vergleich = name;
	}

	@Override
	public String getLastResultName() {
		return nameLastResult;
	}

	@Override
	public boolean found(int minMax, int maxMax, int avgMax) {
		if (min <= minMax && max <= maxMax && avg <= avgMax) {
			return true;
		} else {
			return false;
		}
	}

}
