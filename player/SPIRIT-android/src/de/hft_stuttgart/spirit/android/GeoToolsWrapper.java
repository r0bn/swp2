package de.hft_stuttgart.spirit.android;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import de.hft_stuttgart.spirit.Location;
import de.hft_stuttgart.spirit.SpiritGeoTools;

public class GeoToolsWrapper implements SpiritGeoTools {
	GeoTools geoTools;
	LocationManager lm;
	SensorManager sm;

	public GeoToolsWrapper(Context c) {
		lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		sm = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		geoTools = new GeoTools(lm, sm);
	}

	public void pause() {
		lm.removeUpdates(geoTools);
		sm.unregisterListener(geoTools);
	}

	public void resume() {
		geoTools.updateLastKnownLocation();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 0,
				geoTools);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 0,
				geoTools);
		sm.registerListener(geoTools, SensorManager.SENSOR_ORIENTATION,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void stop() {

	}

	@Override
	public Location getLocation() {
		Location location = new Location(geoTools.getLocation().getProvider());
		location.setLatitude(geoTools.getLocation().getLatitude());
		location.setLongitude(geoTools.getLocation().getLongitude());
		return location;
	}

	@Override
	public float getDistance(Location l) {
		android.location.Location dest = new android.location.Location(l.name);
		dest.setLatitude(l.lat);
		dest.setLongitude(l.lon);
		return geoTools.getDistance(dest);
	}

	@Override
	public float getWinkel() {
		return geoTools.getStandWinkel();
	}

	@Override
	public float getBearingToLocationDegrees(Location l) {
		android.location.Location dest = new android.location.Location(l.name);
		dest.setLatitude(l.lat);
		dest.setLongitude(l.lon);
		return (float) geoTools.getBearingToLocationDegrees(dest);
	}
}
