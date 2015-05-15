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
import android.view.MenuInflater;
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
import android.content.Intent;
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
        
        Intent intent = this.getIntent();
        if (intent.hasExtra("Filter_From")){
        	if (intent.getExtras().getString("Filter_From").equals("StoreFragment")){
        		mViewPager.setCurrentItem(1);
        	}else{ //set the startfragment to the default fragment.
        		mViewPager.setCurrentItem(0);
        	}
        }
        
    }


    /**
     * This method is called when the OptionsMenu is created.
     * This method does nothing, because the underlying Fragments will initialize the OptionsMenu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

    	switch (item.getItemId()) {
    	 	case R.id.action_installed_filter:
    	 		
    	 		//Toast.makeText(this, "installedFragment", Toast.LENGTH_LONG).show();
    	 		Intent oldIntent = this.getIntent();
    	 		Intent installedFragmentIntent = new Intent();
    	 		
    	 		installedFragmentIntent.setClass(Main_Activity.this,Filter_Activity.class);
    	 		installedFragmentIntent.putExtra("Filter_From","InstalledFragment");      	 		
    	 		if (oldIntent.hasExtra("StoreFragmentStoryFilter")){	
    	 			installedFragmentIntent.putExtra("StoreFragmentStoryFilter", oldIntent.getParcelableExtra("StoreFragmentStoryFilter"));
  		        }
    	 		if (oldIntent.hasExtra("InstalledFragmentStoryFilter")){	
    	 			installedFragmentIntent.putExtra("InstalledFragmentStoryFilter", oldIntent.getParcelableExtra("InstalledFragmentStoryFilter"));
   		        }  
    	 		 
            	startActivity(installedFragmentIntent); 
    	        return true;
    	        
    	    case R.id.action_store_filter:
    	    	
    	    	//Toast.makeText(this, "storeFragment", Toast.LENGTH_LONG).show();
    	 		Intent oIntent = this.getIntent();
    	    	Intent storeFragmentIntent = new Intent();
    	    	storeFragmentIntent.setClass(Main_Activity.this,Filter_Activity.class);
    	    	storeFragmentIntent.putExtra("Filter_From","StoreFragment");
    	 		if (oIntent.hasExtra("StoreFragmentStoryFilter")){	
    	 			storeFragmentIntent.putExtra("StoreFragmentStoryFilter", oIntent.getParcelableExtra("StoreFragmentStoryFilter"));
  		        }
    	 		if (oIntent.hasExtra("InstalledFragmentStoryFilter")){	
    	 			storeFragmentIntent.putExtra("InstalledFragmentStoryFilter", oIntent.getParcelableExtra("InstalledFragmentStoryFilter"));
   		        }
            	startActivity(storeFragmentIntent); 
    	        return true;
    	        
    	    default:
    	        break;
    	    }

    	    return false;
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
