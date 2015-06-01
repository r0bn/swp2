package de.hft_stuttgart.spirit.android.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hft_stuttgart.spirit.android.Story;
import android.text.TextUtils;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.view.inputmethod.InputMethodManager;


/**
 * The StoryFilter Class is a paracelable class in which the FiterComponents are defined. It is used to pass custom 
 * objects between the FragmentActivitys, the FilterActivity and the StoryDetails_Activity.
 * It also provides a function to build the filter path URL which can be used to get filtered stories from the server. 
 * 
 * @author Stefan
 * 
 *
 */
public class StoryFilter implements Parcelable {
	
	String title;
	String author;
	String size_max;
	String creationDateMin; 
	String creationDateMax;
	String city;
	String latitude;
    String longitude;
	String radius;

	
	public StoryFilter(String title, String author, String size_max, String creationDateMin, String creationDateMax,String city, String latitude, String longitude, String radius ){
		this.title = title;
		this.author = author;
		this.size_max = size_max;
		this.creationDateMin = creationDateMin;
		this.creationDateMax = creationDateMax;
		this.city = city;
		this.latitude = latitude;
        this.longitude = longitude;
		this.radius = radius;
	}
	
/**
 * This method is used to make the class paracelable 
 * @param in
 */
	public StoryFilter(Parcel in){
		
		this.title = in.readString();
		this.author = in.readString();
		this.size_max = in.readString();
		this.creationDateMin = in.readString();
		this.creationDateMax = in.readString();
		this.city = in.readString();
		this.latitude = in.readString();
		this.longitude = in.readString();
		this.radius = in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(this.title);
	    dest.writeString(this.author);
	    dest.writeString(this.size_max);
	    dest.writeString(this.creationDateMin);
	    dest.writeString(this.creationDateMax);
	    dest.writeString(this.city);
	    dest.writeString(this.latitude);
	    dest.writeString(this.longitude);
	    dest.writeString(this.radius);
	}
	
	/**
	 * This method is used to make the class paracelable 
	 * @param in
	 */
	public static final Parcelable.Creator<StoryFilter> CREATOR = new Parcelable.Creator<StoryFilter>() {

		@Override
		public StoryFilter createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new StoryFilter(source);
		}

		@Override
		public StoryFilter[] newArray(int size) {
			// TODO Auto-generated method stub
			return new StoryFilter[size];
		}
	};
	
	
	
	
	/**
	 * This method resets all filter component values to their default. 
	 */
	public void resetToDefault(){
		this.title = "";
		this.author = "";
		this.size_max = "";
		this.creationDateMax = "Datum festlegen";
		this.creationDateMin = "Datum festlegen";
		this.city = "";
		this.latitude = "";
		this.longitude = "";
		this.radius = "1";
		
	}
	
	/**
	 * The filter component values for the storyfilter can be set here. 
	 */
	public void SetStoryFilter(String title, String author, String size_max,
			String creationDateMin, String creationDateMax, String city, String latitude, String longitude,
			String radius) {
		this.title = title;
		this.author = author;
		this.size_max = size_max;
		this.creationDateMin = creationDateMin;
		this.creationDateMax = creationDateMax;
		this.city = city;
		this.latitude = latitude;
        this.longitude = longitude;
		this.radius = radius;
	}
	
/**
 * This function checks if the filter values equal the item-values. 
 * @param item the item which should be compared to the filter. 
 * @return true if the item does pass the filter and false if the item doesn't pass through the filter.
 */
	public boolean filterStoryItem(Story item){

		if (this.title.trim().length() > 0){	//checks if String contains characters != whitespace 
			boolean show = false;
			String split[]= TextUtils.split(this.title, " ");
			for (int i=0; i < split.length ; i++){
				//if only one part of the String is enough to let an item through the filter:
//				if((item.getTitle().toLowerCase().contains(split[i].toLowerCase()) && (split[i].trim().length() > 0)){
//					//return false;
//					show = true;
//				}
//			}
//				if(!show) return false;
				
				//if all parts of the ItemString must be in the FilterString to let an item through the filter:
				if( !(item.getTitle().toLowerCase().contains(split[i].toLowerCase())) && (split[i].trim().length() > 0) ){
					return false;
				}
			}
		}

		if (this.author.trim().length() > 0){	//checks if String contains characters != whitespace
			boolean show = false;
			String split[]= TextUtils.split(this.author, " ");
			for (int i=0; i < split.length ; i++){
				//if only one part of the String is enough to let an item through the filter:
//				if(item.getAuthor().toLowerCase().contains(split[i].toLowerCase()) && (split[i].trim().length() > 0)){
//					//return false;
//					show = true;
//				}
//			}
//			if(!show) return false;
				//if all parts of the ItemString must be in the FilterString to let an item through the filter:
				if( !(item.getAuthor().toLowerCase().contains(split[i].toLowerCase())) && (split[i].trim().length() > 0) ){
					return false;
				}
			}	
		}
		
		if (this.size_max.trim().length() > 0){	//checks if String contains characters != whitespace
			
			if(Integer.parseInt(this.size_max) < Integer.parseInt(item.getSize())){
				return false;
			}	
		}

		if (!this.creationDateMin.equals("Datum festlegen")){
				
			Date dateMin = null;
			Date itemDate = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy");
				dateMin = sdf.parse(this.creationDateMin);
				//"created_at" format: "2015-05-01 17:08:31",
				//sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				itemDate = sdf.parse(item.getCreated_at());
				
				if(itemDate.before(dateMin) && !itemDate.equals(dateMin)){	
					return false;
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
				
			
			
		if (!this.creationDateMax.equals("Datum festlegen")){

			Date dateMax = null;
			Date itemDate = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy");
				dateMax = sdf.parse(this.creationDateMax);
				//"created_at" format: "2015-05-01 17:08:31",
				//sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				itemDate = sdf.parse(item.getCreated_at());
				if(itemDate.after(dateMax) && !itemDate.equals(dateMax)){
					return false;
				}					
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		

		//if (!this.city.equals("") && !this.latitude.equals("") && !this.longitude.equals("")){
		if ((this.city.trim().length() > 0) && (this.latitude.trim().length() > 0) && (this.longitude.trim().length() > 0)){	
			Location filterLocation = new Location("filterLocation");
			Location itemLocation = new Location("itemLocation");
				
			filterLocation.setLatitude(Double.parseDouble(this.latitude));
			filterLocation.setLongitude(Double.parseDouble(this.longitude));
			
			itemLocation.setLatitude(item.getLatitude());
			itemLocation.setLongitude(item.getLongitude());
			
			float distance = filterLocation.distanceTo(itemLocation)/1000; //approximate distance in meters/1000 for km
			
			if(distance > (double)Double.parseDouble(this.radius)){ 
				return false;		
			}
		}	
		
		return true;
	}
	

	/**
	 * The getQuery function puts the query-URL that is needed to get filtered stories from the server, together.
	 * @return the query-URL
	 */
	public String getQuery(){
		//id, title, description, author, size, size_min, size_max, creation_date, creation_date_min, creation_date_max, location, radius
		//Example: http://api.storytellar.de/story?author=arno+claus&size_max=20
		//String URL = "http://api.storytellar.de/story?";
		StringBuilder getQuery = new StringBuilder();
		//getQuery.append("http://api.storytellar.de/story?"); 
		getQuery.append("http://api.storytellar.de/temp?");
		
		if (this.title.trim().length() > 0){
			String title = this.title;
			try {
				title = URLEncoder.encode(title, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace(); 	
			}
			getQuery.append("title=" + title+"&");
		}
		
		if (this.author.trim().length() > 0){ 
			String author = this.author;
			try {
				author = URLEncoder.encode(author, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace(); 	
			}
			getQuery.append("author=" + author + "&");
		}
		
		if (this.size_max.trim().length() > 0) getQuery.append("size_max=" + this.size_max.trim()+"&");
		if (!this.creationDateMin.matches("Datum festlegen")) getQuery.append("creation_date_min="
		       + creationDateMin.replaceAll("\\.", "")+"&");
		if (!this.creationDateMax.matches("Datum festlegen")) getQuery.append("creation_date_max="
			       + creationDateMax.replaceAll("\\.", "")+"&");
		if (this.city.trim().length() > 0){
	           getQuery.append("gps_point=" + this.latitude + "+" + this.longitude +"&"
	                   +"gps_point_radius=" + this.radius+"&");		
		}
		

		 String adjustedQuery = getQuery.substring(0, getQuery.length()-1); //delete the last &

         //delete double whitespaces
        adjustedQuery = adjustedQuery.trim().replaceAll(" +", " ");

        adjustedQuery = adjustedQuery.replaceAll(" ", "+");
		
		return adjustedQuery;
	}
}
