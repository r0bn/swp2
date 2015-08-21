package de.hft_stuttgart.spirit.android;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.radar.GeoUtils;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import de.hft_stuttgart.spirit.Location;
import de.hft_stuttgart.spirit.SpiritGeoTools;

public class GeoToolsWrapper implements SpiritGeoTools,ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
	GeoTools geoTools;
	LocationManager lm;
	SensorManager sm;
	
	GoogleApiClient locationManager;
	LocationRequest request;
	android.location.Location location;

	public GeoToolsWrapper(Context c) {
		locationManager = new GoogleApiClient.Builder(c).
				addConnectionCallbacks(this).
				addOnConnectionFailedListener(this).
				addApi(LocationServices.API).build();
		request = new LocationRequest();
		request.setInterval(1000);
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		location = new android.location.Location("");
		
		lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		sm = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		geoTools = new GeoTools(lm, sm);
	}

	public void pause() {
		//lm.removeUpdates(geoTools);
		sm.unregisterListener(geoTools);
		
		LocationServices.FusedLocationApi.removeLocationUpdates(locationManager, this);
	}

	public void resume() {
		//geoTools.updateLastKnownLocation();
		//lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 0,
		//		geoTools);
		//lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 0,
		//		geoTools);
		sm.registerListener(geoTools, SensorManager.SENSOR_ORIENTATION,
				SensorManager.SENSOR_DELAY_GAME);
		
		if(locationManager.isConnected()) {			
			LocationServices.FusedLocationApi.requestLocationUpdates(locationManager, request, this);
		} else if(!locationManager.isConnecting()){
			locationManager.connect();
		}
	}

	public void stop() {

	}

	@Override
	public Location getLocation() {
		Location location = new Location(this.location.getProvider());
		location.setLatitude(this.location.getLatitude());
		location.setLongitude(this.location.getLongitude());
		return location;
	}

	@Override
	public float getDistance(Location l) {
		android.location.Location dest = new android.location.Location(l.name);
		dest.setLatitude(l.lat);
		dest.setLongitude(l.lon);
		return location.distanceTo(dest);
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
		return (float) Math.toDegrees(getBearingToLocation(dest));
	}

	@Override
	public void onLocationChanged(android.location.Location locationNew) {
		Log.d("Coordinates", "Provider: "+locationNew.getProvider() + "; Latitude: " + locationNew.getLatitude() + "; Longitude: " + locationNew.getLongitude());
		if(location!= null) {
			location = locationNew;
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		LocationServices.FusedLocationApi.requestLocationUpdates(locationManager, request, this);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		locationManager.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		locationManager.connect();
	}
	
	private double getBearingToLocation(android.location.Location target) {
		if (location != null) {
			double mBearing = GeoUtils.bearing(location.getLatitude(),
					location.getLongitude(), target.getLatitude(),
					target.getLongitude());
			double bearingToTarget = mBearing - geoTools.getMOrientation();
			double drawingAngle = Math.toRadians(bearingToTarget)
					- (Math.PI / 2);
			// if (angle < 0){
			// angle = angle + 360;
			// }
			return drawingAngle;
		} else {
			return Double.MAX_VALUE;
		}
	}
}
