package de.hft_stuttgart.spirit.android.view;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import de.hft_stuttgart.spirit.android.AndroidLauncher;
import de.hft_stuttgart.spirit.android.ContentDownloader;
import de.hft_stuttgart.spirit.android.R;
import de.hft_stuttgart.spirit.android.Story;
import de.hft_stuttgart.spirit.android.view.StoryListStore_Fragment.GetStoreStories;

public class StoryDetails_Activity extends ActionBarActivity {

	public final static String EXTRA_STORYID = "de.hft_stuttgart.spirit.android.view.STORYID";
	public final static String EXTRA_STORYNAME = "de.hft_stuttgart.spirit.android.view.STORYNAME";
	public final static String EXTRA_DESCRIPTION = "de.hft_stuttgart.spirit.android.view.DESCRIPTION";
	public final static String EXTRA_LOCATION = "de.hft_stuttgart.spirit.android.view.LOCATION";
	public final static String EXTRA_AUTHOR = "de.hft_stuttgart.spirit.android.view.AUTHOR";
	public final static String EXTRA_CREATIONDATE = "de.hft_stuttgart.spirit.android.view.CREATIONDATE";
	public final static String EXTRA_STOREORINSTALLED = "de.hft_stuttgart.spirit.android.view.STOREORINSTALLED";
	
	private final static String TAG = StoryDetails_Activity.class.toString();
	
    
	@Override
	protected void onStart() {
		
		TextView textv;
		Intent intent = getIntent();
		String logmessage = "Method onStart called:\n";
		
		// Update storyname in activity
		textv = (TextView)findViewById(R.id.StorynameView);
		if (intent.hasExtra(EXTRA_STORYNAME)) {
			textv.setText(intent.getStringExtra(EXTRA_STORYNAME));
			logmessage += "Set roomname by intent to \"" + textv.getText() + "\"\n";
		} else {
			textv.setText("#UNDEF");
			logmessage += "Set roomname by default to \"" + textv.getText() + "\"\n";
		}
		
		// Update description in activity
		textv = (TextView)findViewById(R.id.DescriptionView);
		if (intent.hasExtra(EXTRA_DESCRIPTION)) {
			textv.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
			logmessage += "Set description by intent to \"" + textv.getText() + "\"\n";
		} else {
			textv.setText("#UNDEF");
			logmessage += "Set description by default to \"" + textv.getText() + "\"\n";
		}
		
		// Update author in activity
		textv = (TextView)findViewById(R.id.AuthorView);
		if (intent.hasExtra(EXTRA_AUTHOR)) {
			textv.setText(intent.getStringExtra(EXTRA_AUTHOR));
			logmessage += "Set author by intent to \"" + textv.getText() + "\"\n";
		} else {
			textv.setText("#UNDEF");
			logmessage += "Set author by default to \"" + textv.getText() + "\"\n";
		}
		
		// Update creation date in activity
		textv = (TextView)findViewById(R.id.CreationDateView);
		if (intent.hasExtra(EXTRA_CREATIONDATE)) {
			textv.setText(intent.getStringExtra(EXTRA_CREATIONDATE));
			logmessage += "Set creation date by intent to \"" + textv.getText() + "\"\n";
		} else {
			textv.setText("#UNDEF");
			logmessage += "Set creation date by default to \"" + textv.getText() + "\"\n";
		}
		
		// Update location in activity
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		if (intent.hasExtra(EXTRA_LOCATION)) {
			String[] tmp = intent.getStringExtra(EXTRA_LOCATION).split("[ ]+");
			double coord1 = Double.parseDouble(tmp[0]);
			double coord2 = Double.parseDouble(tmp[1]);
			try {
				final LatLng point = new LatLng(coord1, coord2);
				Marker markr = map.addMarker(new MarkerOptions().position(point).title("Position"));
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
				logmessage += "Set creation date by intent to \"" + markr.getPosition().toString() + "\"\n";
			} catch (Exception e) {
				// This error may be thrown because the smartphone/emulator can't display the map
				logmessage += "WARNING: Unable to set marker on map, this error may be thrown because the smartphone/emulator can't display the map.\n";
				e.printStackTrace();
			}
		} else {
			try {
				final LatLng point = new LatLng(48.775846 , 9.182932);
				Marker markr = map.addMarker(new MarkerOptions().position(point).title("Position"));
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
				logmessage += "Set creation date by default to \"" + markr.getPosition().toString() + "\"\n";
			} catch (Exception e) {
				// This error may be thrown because the smartphone/emulator can't display the map
				logmessage += "WARNING: Unable to set marker on map, this error may be thrown because the smartphone/emulator can't display the map.\n";
				e.printStackTrace();
			}
		}
		
		Log.d(TAG, logmessage);
		super.onStart();
	}


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
    	if (getIntent().hasExtra(EXTRA_STOREORINSTALLED)){  		
    		if(getIntent().getStringExtra(EXTRA_STOREORINSTALLED).equals("STORE")){
    			getMenuInflater().inflate(R.menu.story_details_store, menu);
    		} 
    		else {
    			getMenuInflater().inflate(R.menu.story_details_installed, menu);
    		}
    	} 
    	else {    		
    		getMenuInflater().inflate(R.menu.story_details_, menu);
    	}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = getIntent();
        Toast toast;
        switch (id) {
		case R.id.action_start:
        	startActivity(new Intent(getApplicationContext(),AndroidLauncher.class));
			return true;
		case R.id.action_restart:
			startActivity(new Intent(getApplicationContext(),AndroidLauncher.class));
			return true;
		case R.id.action_delete:
        	toast = Toast.makeText(getApplicationContext(),"Work in progress for Löschen!",Toast.LENGTH_SHORT);
        	toast.show();
			return true;
		case R.id.action_download:
			toast = Toast.makeText(getApplicationContext(),"Download Story "+intent.getStringExtra(EXTRA_STORYNAME)+" (ID: "+intent.getIntExtra(EXTRA_STORYID, -1)+")",Toast.LENGTH_SHORT);
        	toast.show();
        	DownloadStory task = new DownloadStory();
        	task.execute();
	        try {
				task.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		default:
			return false;
		}
    }
    
    public class DownloadStory extends AsyncTask<Void, Void, Void> {

    	@Override
    	protected Void doInBackground(
    			Void... params) {
            Intent intent = getIntent();
    		ContentDownloader.getInstance().downloadStory(intent.getIntExtra(EXTRA_STORYID, -1));
			return null;
    	}
    	
    }
}

