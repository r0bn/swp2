package de.hft_stuttgart.spirit;

public interface OrbInfos {

	public String getInfoText();
	public double getOrbAvg();
	public double getOrbMin();
	public double getOrbMax();
	public String getName();
	public void setVergleichName(String name);
	public String getLastResultName();
	public boolean found(int minMax, int maxMax, int avgMax);
}
