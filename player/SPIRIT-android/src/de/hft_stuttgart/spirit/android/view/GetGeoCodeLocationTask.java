package de.hft_stuttgart.spirit.android.view;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.TextView;
import de.hft_stuttgart.spirit.android.Story;

public class GetGeoCodeLocationTask extends AsyncTask<Story, Void, String> {

    	TextView view;
    	Context context;
    	
    	public GetGeoCodeLocationTask(TextView view, Context context) {
			this.view = view;
			this.context = context;
		}
    	
    	
		@Override
		protected String doInBackground(Story... params) {
            Geocoder gc = new Geocoder(context);
            try {
				List<Address> address = gc.getFromLocation(params[0].getLatitude(), params[0].getLongitude(), 1);
				String city = address.get(0).getLocality();
				if(city == null) city = address.get(0).getAdminArea();
				if(city == null) city = address.get(0).getCountryName();
				if(city == null) return "Where is this !!!!";
				return city;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			view.setText(result);
		}
    	
    }
