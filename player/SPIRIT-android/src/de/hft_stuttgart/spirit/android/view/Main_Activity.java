package de.hft_stuttgart.spirit.android.view;

import java.io.IOException;
import java.util.Locale;

import com.badlogic.gdx.scenes.scene2d.ui.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.GeolocationPermissions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import de.hft_stuttgart.spirit.android.R;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Context;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;

/**
 * 
 * @author Lukas
 *	This Activity is the Main Activity of the StorytellAR Application.
 *	It contains the two fragments which hold the installed stories and the stories which can be downloaded.
 */
@SuppressWarnings("deprecation")
public class Main_Activity extends ActionBarActivity implements ActionBar.TabListener{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     * This method is called, when the Activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    /**
     * This method is called when the OptionsMenu is created.
     * This method does nothing, because the underlying Fragments will initialize the OptionsMenu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        if (id == R.id.action_filter) {
        	
        	//Toast toast = Toast.makeText(getApplicationContext(),"Work in progress for Filter!",Toast.LENGTH_SHORT);
        	//toast.show();
            //return true;
        	
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popup = inflater.inflate(R.layout.filter_popup, null);
            final PopupWindow popupWindow = new PopupWindow(popup, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);


            final EditText radiusEditText = (EditText) popup.findViewById(R.id.radiusEditText);
            final SeekBar radiusSeekBar = (SeekBar) popup.findViewById(R.id.radiusSeekbar);

            //change the text using the seekbar:
            radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    radiusEditText.setText(String.valueOf(i));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            //change the seekbar using the text:
            radiusEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {


                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

				@Override
				public void afterTextChanged(Editable s) {
					 try {
	                        //Update Seekbar value after entering a number
	                        radiusSeekBar.setProgress(Integer.parseInt(s.toString()));
	                    } catch (Exception ex) {
	                        ex.printStackTrace();
	                    }
					
				}
            });

            popupWindow.setFocusable(true); //show keyboard after opening the popup


            Button getLocationButton = (Button) popup.findViewById(R.id.getLocation);

            
            //the getLocationButton calls the geoLocate function, and closes the filter_popup
            getLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        geoLocate(popup,radiusSeekBar.getProgress()); //with a reference to the popup view (to get the popup elements)
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    popupWindow.dismiss();
                }
            });
            
            
            //the resetfilterButton closes the filter_popup and shows all stories
            Button resetFilterButton = (Button) popup.findViewById(R.id.resetFilter);

            resetFilterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getAllStoriesFromServer();

                    popupWindow.dismiss();
                }
            });

            
            //get the display-size to place the popup window
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;


            popupWindow.showAsDropDown(getLocationButton, width / 2, height / 5);
            // popupWindow.showAsDropDown(edittext,width/2,height/4); //coordinates must be dynamic
          
            return true;
        } else {
        	return false;
        }
    }

    
    public void geoLocate(View v, int radius)throws IOException {

        EditText cityEditText = (EditText) v.findViewById(R.id.cityEditText);

        if (cityEditText.getText().toString().matches("")){ //no city was inserted

            getAllStoriesFromServer();

        }else { //look for coordinates
            String location = cityEditText.getText().toString();


            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);


            Geocoder gc = new Geocoder(this);
            java.util.List<Address> list = gc.getFromLocationName(location, 1);

            if (list.isEmpty()){
                Toast.makeText(this, "Stadt nicht gefunden", Toast.LENGTH_LONG).show();
            }else {
                Address address = list.get(0); //get only the first location (google finds more than one location)
                String locality = address.getLocality();
                Toast.makeText(this, locality, Toast.LENGTH_LONG).show(); //the found the location name in a small popup

                double latitude = address.getLatitude();
                double longitude = address.getLongitude();


                if (radius == 0) {   //input error => show all stories within a 1 km radius instead
                    getStoriesFromServer(latitude, longitude, 1);
                } else {
                    getStoriesFromServer(latitude, longitude, radius);
                }
            }
        }
    }

    //get stories from the server with parameters: platitude,longitude,radius
    void getStoriesFromServer(double latitude, double longitude, int radius){ //stub

    	
    	
        //=====only for testing purposes=========================================
        // TextView coordinateText = (TextView) findViewById(R.id.textTitel);//show the coordinates in the main menu
        // coordinateText.setText("latitude= " + latitude + ", longitude= " + longitude + ", radius= " + radius);//the coordinates for the server
    	Toast.makeText(this, "latitude= " + latitude + ", longitude= " + longitude + ", radius= " 
    			+ radius + " km", Toast.LENGTH_LONG).show();
    	//=====only for testing purposes=========================================
    	
    }
    
    //get all Stories from the Server
    void getAllStoriesFromServer(){    //stub

    	
    	//=====only for testing purposes=========================================
        // TextView coordinateText = (TextView) findViewById(R.id.textTitel);//show the coordinates in the main menu
        // coordinateText.setText("show all Stories");//the coordinates for the server
    	Toast.makeText(this, "show all Stories", Toast.LENGTH_LONG).show();
    	//=====only for testing purposes=========================================
    	
        
    }
    
        
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private StoryListInstalled_Fragment installed;
        private StoryListStore_Fragment store;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            installed = new StoryListInstalled_Fragment();
            store = new StoryListStore_Fragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return installed;
                case 1:
                    return store;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Installiert";
                case 1:
                    return "Store";
            }
            return null;
        }
    }
}
