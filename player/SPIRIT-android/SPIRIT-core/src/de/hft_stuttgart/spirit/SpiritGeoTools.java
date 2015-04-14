package de.hft_stuttgart.spirit;

public interface SpiritGeoTools {
public Location getLocation();
public float getDistance(Location l);
public float getWinkel();
public float getBearingToLocationDegrees(Location l);
}
