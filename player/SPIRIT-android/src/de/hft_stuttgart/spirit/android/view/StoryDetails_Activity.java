package de.hft_stuttgart.spirit.android.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import de.hft_stuttgart.spirit.android.R;

public class StoryDetails_Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.story_details_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast toast;
        switch (id) {
		case R.id.action_start:
        	toast = Toast.makeText(getApplicationContext(),"Work in progress for Start!",Toast.LENGTH_SHORT);
        	toast.show();
			return true;
		case R.id.action_restart:
        	toast = Toast.makeText(getApplicationContext(),"Work in progress for Neustart!",Toast.LENGTH_SHORT);
        	toast.show();
			return true;
		case R.id.action_delete:
        	toast = Toast.makeText(getApplicationContext(),"Work in progress for Löschen!",Toast.LENGTH_SHORT);
        	toast.show();
			return true;
		default:
			return false;
		}
    }
}
