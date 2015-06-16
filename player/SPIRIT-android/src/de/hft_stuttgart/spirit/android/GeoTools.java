package de.hft_stuttgart.spirit.android;

import com.google.android.radar.GeoUtils;

import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GeoTools implements SensorListener, LocationListener {
	private LocationManager lm;
	private Location location = null;
	private SensorManager sm;
	private float mOrientation;
	private float standWinkel = 0;

	public enum Orientation {
		SENKRECHT, WAAGERECHT
	};

	public GeoTools(LocationManager lm, SensorManager sm) {
		this.lm = lm;
		this.sm = sm;
		// mit bekannter position initialisieren
		updateLastKnownLocation();
	}

	public void updateLastKnownLocation() {
		Location lastGPS = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location lastNetwork = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		updateLocation(lastNetwork);
		updateLocation(lastGPS);
	}

	private boolean updateLocation(Location newLocation) {
		// prüfen ob die neue Location akzeptiert wird
		// if (newLocation != null) {
		// Log.w("UpdateLocation",
		// newLocation.getProvider() + ": " + newLocation.getLongitude()
		// + " / " + newLocation.getLatitude() + ", "
		// + new Date(newLocation.getTime()));
		// }
		if (newLocation != null) {
			// wenn es aktuell keine gibt -> akzeptieren
			if (location == null) {
				location = newLocation;
				// Log.w("UpdateLocation","akzeptiert da noch keine Location bekannt");
				return true;
			} else {
				// es gibt bereits eine location
				// 2 grundfälle, aktuelle ist von GPS oder network
				if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
					// bisher GPS
					if (newLocation.getProvider().equals(
							LocationManager.GPS_PROVIDER)) {
						// neue location ist auch GPS, akzeptieren wenn neuer
						if (newLocation.getTime() > location.getTime()) {
							location = newLocation;
							// Log.w("UpdateLocation","akzeptiert, bisher GPS, jetzt GPS aber aktuell");
							return true;
						} else {
							// Log.w("UpdateLocation","nicht akzeptiert, bisher GPS, neues Ergebnis zu alt");
							return false;
						}
					} else {
						// neue location ist Network, nur akzeptieren wenn
						// deutlich
						// neuer (hier 10 sekunden)
						if (newLocation.getTime() - location.getTime() > 10000) {
							location = newLocation;
							// Log.w("UpdateLocation","akzeptiert, bisher GPS aber Network deutlich aktueller");
							return true;
						} else {
							// Log.w("UpdateLocation","nicht akzeptiert, bisher GPS, neu Network nicht aktuell genug");
							return false;
						}
					}
				} else {
					// bisher Network
					// -> immer akzeptieren wenn die neue Location neuer ist,
					// egal
					// von welcher Quelle
					// TODO:
					// http://developer.android.com/reference/android/location/Location.html#getElapsedRealtimeNanos()
					if (newLocation.getTime() > location.getTime()) {
						location = newLocation;
						// Log.w("UpdateLocation","akzeptiert, bisher Netzwerk, neues ist aktueller");
						return true;
					} else {
						// Log.w("UpdateLocation","nicht akzeptiert, neues nicht aktueller als bisheriges");
						return false;
					}
				}
			}

		} else {
			return false; // neue location ist null
		}
	}

	public float getDistance(Location dest) {
		if (dest != null && location != null) {
			return location.distanceTo(dest);
		} else {
			return Float.MAX_VALUE;
		}
	}

	public Location getLocation() {
		return location;
	}

	public Orientation getOrientation() {
		//Log.w("SPIRIT", "" + standWinkel);
		if (standWinkel <= 40) {
			return Orientation.WAAGERECHT;
		} else {
			return Orientation.SENKRECHT;
		}
	}

	public float getStandWinkel(){
		return standWinkel;
	}
	private double getBearingToLocation(Location target) {
		if (location != null) {
			double mBearing = GeoUtils.bearing(location.getLatitude(),
					location.getLongitude(), target.getLatitude(),
					target.getLongitude());
			double bearingToTarget = mBearing - mOrientation;
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

	public double getBearingToLocationRadians(Location target) {
		return getBearingToLocation(target);
	}

	public double getBearingToLocationDegrees(Location target) {
		return Math.toDegrees(getBearingToLocation(target));
	}

	@Override
	public void onLocationChanged(Location location) {
		updateLocation(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onSensorChanged(int sensor, float[] values) {
		mOrientation = values[0];
		standWinkel = (float) Math.abs(values[1]);

	}

	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {

	}

}
