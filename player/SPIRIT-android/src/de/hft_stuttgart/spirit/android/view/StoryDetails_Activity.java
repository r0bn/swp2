package de.hft_stuttgart.spirit.android.view;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

/**
 * StoryDetails_Activity is a activity to display the details of a story and download/play the story.
 * 
 * @author Oliver
 *
 */
public class StoryDetails_Activity extends ActionBarActivity {

	public final static String EXTRA_STORYID = "de.hft_stuttgart.spirit.android.view.STORYID";
	public final static String EXTRA_STORYNAME = "de.hft_stuttgart.spirit.android.view.STORYNAME";
	public final static String EXTRA_DESCRIPTION = "de.hft_stuttgart.spirit.android.view.DESCRIPTION";
	public final static String EXTRA_LOCATION = "de.hft_stuttgart.spirit.android.view.LOCATION";
	public final static String EXTRA_AUTHOR = "de.hft_stuttgart.spirit.android.view.AUTHOR";
	public final static String EXTRA_UPDATEDAT = "de.hft_stuttgart.spirit.android.view.CREATIONDATE";
	public final static String EXTRA_STOREORINSTALLED = "de.hft_stuttgart.spirit.android.view.STOREORINSTALLED";
	public final static String EXTRA_SIZE = "de.hft_stuttgart.spirit.android.view.SIZE";
	
	private final static String TAG = StoryDetails_Activity.class.toString();
	
    /**
     * Get the informations about a story from a intent and display the informations in the activity.
     */
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
		if (intent.hasExtra(EXTRA_UPDATEDAT)) {
			textv.setText(intent.getStringExtra(EXTRA_UPDATEDAT));
			logmessage += "Set creation date by intent to \"" + textv.getText() + "\"\n";
		} else {
			textv.setText("#UNDEF");
			logmessage += "Set creation date by default to \"" + textv.getText() + "\"\n";
		}
		
		// Update storySize in activity
		textv = (TextView)findViewById(R.id.StorySize);
		if (intent.hasExtra(EXTRA_SIZE)) {
			textv.setText(intent.getStringExtra(EXTRA_SIZE) + " MB");
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

    /**
     * Handle clicks on the start, restart, delete and download buttons. On click of the start button, 
     * the story is started in spirit. On click of the restart button, a old savestate of the story is 
     * started. On click of the delete button, the local files are removed from the device. On click of
     * the download button, the download of the story from the server is started.
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final Intent intent = getIntent();
        Toast toast;
        switch (id) {
		case R.id.action_start:
			Intent start_intent = new Intent(getApplicationContext(),AndroidLauncher.class);
			start_intent.putExtra(EXTRA_STORYID, intent.getIntExtra(EXTRA_STORYID, -1));
        	startActivity(start_intent);
			return true;
		case R.id.action_restart:
			Intent restart_intent = new Intent(getApplicationContext(),AndroidLauncher.class);
			restart_intent.putExtra(EXTRA_STORYID, intent.getIntExtra(EXTRA_STORYID, -1));
        	startActivity(restart_intent);
			return true;
		case R.id.action_delete:
			toast = Toast.makeText(getApplicationContext(),"Delete Story "+intent.getStringExtra(EXTRA_STORYNAME)+" (ID: "+intent.getIntExtra(EXTRA_STORYID, -1)+")",Toast.LENGTH_SHORT);
        	toast.show();
			ContentDownloader.getInstance().deleteStory(intent.getIntExtra(EXTRA_STORYID, -1));
        	Intent i_delete = new Intent(getApplicationContext(), Main_Activity.class);
	 		if (intent.hasExtra("StoreFragmentStoryFilter")){	
	 			i_delete.putExtra("StoreFragmentStoryFilter", intent.getParcelableExtra("StoreFragmentStoryFilter"));
	 		}
	 		if (intent.hasExtra("InstalledFragmentStoryFilter")){	
	 			i_delete.putExtra("InstalledFragmentStoryFilter", intent.getParcelableExtra("InstalledFragmentStoryFilter"));
	 		} 
        	startActivity(i_delete);
			return true;
		case R.id.action_download:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			  //  builder.setTitle("Confirm");
			   // builder.setMessage("Jetzt die komplette Story herunterladen?");EXTRA_SIZE
				builder.setTitle("Jetzt die vollen "+intent.getStringExtra(EXTRA_SIZE) +" MB für die Story herunterladen?");
			    builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

			        public void onClick(DialogInterface dialog, int which) {
			        	Toast.makeText(getApplicationContext(),"Download Story "+intent.getStringExtra(EXTRA_STORYNAME)+" (ID: "+intent.getIntExtra(EXTRA_STORYID, -1)+")",Toast.LENGTH_SHORT).show();
			        	DownloadStory task = new DownloadStory();
			        	task.execute();
			        	Intent i_download = new Intent(getApplicationContext(),Main_Activity.class);
				 		if (intent.hasExtra("StoreFragmentStoryFilter")){	
				 			i_download.putExtra("StoreFragmentStoryFilter", intent.getParcelableExtra("StoreFragmentStoryFilter"));
				 		}
				 		if (intent.hasExtra("InstalledFragmentStoryFilter")){	
				 			i_download.putExtra("InstalledFragmentStoryFilter", intent.getParcelableExtra("InstalledFragmentStoryFilter"));
				 		} 
			        	startActivity(i_download);
			            dialog.dismiss();
			        }

			    });

			    builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			            // Do nothing
			            dialog.dismiss();
			        }
			    });

			    AlertDialog alert = builder.create();
			    alert.show();

		default:
			return false;
		}
    }
    
    /**
     * The DownloadStory clas is used to download a story in a asynchronous task
     * @author Oliver
     *
     */
    public class DownloadStory extends AsyncTask<Void, Void, Void> {

    	/**
    	 * Download the story in a asynchronous task
    	 */
    	@Override
    	protected Void doInBackground(
    			Void... params) {
            Intent intent = getIntent();
    		ContentDownloader.getInstance().downloadStory(intent.getIntExtra(EXTRA_STORYID, -1));
			return null;
    	}
    	
    }
}

