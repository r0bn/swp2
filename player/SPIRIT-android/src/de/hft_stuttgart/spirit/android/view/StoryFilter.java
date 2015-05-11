package de.hft_stuttgart.spirit.android.view;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hft_stuttgart.spirit.android.Story;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.view.inputmethod.InputMethodManager;


/**
 * @author Stefan
 * 
 * The StoryFilter Class defines the FiterComponents to pass them between the FragmentActivitys and the FilterActivity. 
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
	 * this method resets all Values to their default 
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

	public boolean filterStoryItem(Story item){
		
		boolean show = true;

		if (!this.title.equals("") && !item.getTitle().contains(this.title)&& !item.getTitle().toLowerCase().contains(this.title.toLowerCase())){

			show = false;
		}
		
		if (!this.author.equals("") && !item.getAuthor().contains(this.author) && !item.getAuthor().toLowerCase().contains(this.author.toLowerCase()) && show){
			show = false;
		}
		
		if (!this.size_max.equals("") && show){
			
			if(Integer.parseInt(this.size_max) < Integer.parseInt(item.getSize())){
				show = false;
			}	
		}

		if (!this.creationDateMin.equals("Datum festlegen") && show){
				
			Date dateMin = null;
			Date itemDate = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy");
				dateMin = sdf.parse(this.creationDateMin);
				//"created_at" format: "2015-05-01 17:08:31",
				//sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				itemDate = sdf.parse(item.getCreated_at());
				
				if(itemDate.before(dateMin) && !itemDate.equals(dateMin) && show){	
					show = false;
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
				
			
			
		if (!this.creationDateMax.equals("Datum festlegen") && show){

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
					show = false;
				}					
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		

		if (!this.city.equals("") && !this.latitude.equals("") && !this.longitude.equals("") && show){
			
			Location filterLocation = new Location("filterLocation");
			Location itemLocation = new Location("itemLocation");
				
			filterLocation.setLatitude(Double.parseDouble(this.latitude));
			filterLocation.setLongitude(Double.parseDouble(this.longitude));
			
			itemLocation.setLatitude(item.getLatitude());
			itemLocation.setLongitude(item.getLongitude());
			
			float distance = filterLocation.distanceTo(itemLocation)/1000; //approximate distance in meters/1000 for km
			
			if(distance > (double)Double.parseDouble(this.radius)){ 
				show = false;		
			}
		}	
		
		return show;
	}
	

	/**
	 * The getQuery function puts the query-URL together, that is needed to get filtered stories from the server.
	 * @return the query-URL
	 */
	public String getQuery(){
		//id, title, description, author, size, size_min, size_max, creation_date, creation_date_min, creation_date_max, location, radius
		//Example: http://api.storytellar.de/story?author=arno+claus&size_max=20
		//String URL = "http://api.storytellar.de/story?";
		StringBuilder getQuery = new StringBuilder();
		getQuery.append("http://api.storytellar.de/story?"); //already defined in RESTclient.java
		
		
		if (!this.title.matches("")) getQuery.append("title=" + this.title+"&");
		if (!this.author.matches("")) getQuery.append("author=" + this.author+"&");
		if (!this.size_max.matches("")) getQuery.append("size_max=" + this.size_max.toString()+"&");
		if (!this.creationDateMin.matches("Datum festlegen")) getQuery.append("creation_date_min="
		       + creationDateMin.replaceAll("\\.", "")+"&");
		if (!this.creationDateMax.matches("Datum festlegen")) getQuery.append("creation_date_max="
			       + creationDateMax.replaceAll("\\.", "")+"&");
		if (!city.matches("")){
	           getQuery.append("location=" + this.latitude + "+" + this.longitude +"&"
	                   +"radius=" + this.radius+"&");		
		}
		

		 String adjustedQuery = getQuery.substring(0, getQuery.length()-1); //delete the last &

         //delete double whitespaces
        //adjustedQuery.replaceAll("\\s+", " ");
        adjustedQuery = adjustedQuery.trim().replaceAll(" +", " ");

        adjustedQuery = adjustedQuery.replaceAll(" ", "+");
		
		return adjustedQuery;
	}
	
	



        
	//standard getters and setters:
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSize_max() {
		return size_max;
	}
	public void setSize_max(String size_max) {
		this.size_max = size_max;
	}
	public String getCreationDateMin() {
		if(this.creationDateMin.equals("Datum festlegen")){
			return "";	//default String not changed
		}else {
			return creationDateMin;
		}
	}
	public void setCreationDateMin(String creationDateMin) {
		this.creationDateMin = creationDateMin;
	}
	public String getCreationDateMax() {
		if(this.creationDateMax.equals("Datum festlegen")){
			return "";	//default String not changed
		}else {
			return creationDateMax;
		}
	}
	public void setCreationDateMax(String creationDateMax) {
		this.creationDateMax = creationDateMax;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	

}