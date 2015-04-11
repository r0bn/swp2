package de.hft_stuttgart.spirit.android;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import de.hft_stuttgart.spirit.Poi;


public class XmlPullParserHandler {

		List<Poi> places;
		
		private Poi place;
		private String text;
		
		public XmlPullParserHandler(){
			places = new ArrayList<Poi>();			
		}
		
		public List<Poi> getPlaces(){
			return places;
		}

		public List<Poi> parse(InputStream is){
			XmlPullParserFactory factory = null;
			XmlPullParser parser = null;
			
			try{
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
							
				parser = factory.newPullParser();
				parser.setInput(is, null);

				int eventType = parser.getEventType();
				while(eventType != XmlPullParser.END_DOCUMENT){
					String tagname = parser.getName();
					switch(eventType) {
					
					case XmlPullParser.START_TAG:
						
						if(tagname.equalsIgnoreCase("Feature")){
							place = new Poi();
						}
						break;
						
					case XmlPullParser.TEXT:
						
						text = parser.getText();
						break;
						
					case XmlPullParser.END_TAG:
						
						if (tagname.equalsIgnoreCase("anchors")){
							places.add(place);
						}else if (tagname.equalsIgnoreCase("name")){
							place.setName(text);
					    }else if (tagname.equals("pos")){
					    	place.setCoordinates(text);
					    }else if (tagname.equals("video")){
					    	place.setVideo(text);
					    }
						break;
						
						default: 
							break;
							
					}
					eventType = parser.next();
				}
							
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return places;
		}
		
		
}


