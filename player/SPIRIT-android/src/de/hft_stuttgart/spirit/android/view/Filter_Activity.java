package de.hft_stuttgart.spirit.android.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;
import de.hft_stuttgart.spirit.android.AndroidLauncher;
import de.hft_stuttgart.spirit.android.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * This activity is used to filter the Stories in the StoryListInstalled_Fragment and StoryListStore_Fragment.
 *
 * @author Stefan
 *
 */
public class Filter_Activity extends ActionBarActivity {

    private Calendar calendar;
    private TextView topicTextView;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText sizeMaxEditText;
    private Button creationDateMinButton;
    private Button creationDateMaxButton;
    private EditText cityEditText;
    private SeekBar radiusSeekBar;
    private TextView radiusValueTextView;


    private int min_year, min_month, min_day;
    private int max_year, max_month, max_day; //initialize them to current date in onStart()/onCreate()

    private StoryFilter storyFilter;
    private StoryFilter defaultStoryFilter;
    private String filterFrom;
    private Intent oldIntent;
    
    private Date creationDateMin;
    private Date creationDateMax;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        
        topicTextView = (TextView) findViewById(R.id.topicTextView);
        titleEditText = (EditText)findViewById(R.id.titleEditText);
        authorEditText = (EditText)findViewById(R.id.authorEditText);
        sizeMaxEditText = (EditText)findViewById(R.id.sizeMaxEditText);
        creationDateMinButton = (Button) findViewById(R.id.creationDateMinButton);
        creationDateMaxButton = (Button) findViewById(R.id.creationDateMaxButton);
        cityEditText = (EditText)findViewById(R.id.cityEditText);
        radiusSeekBar = (SeekBar) findViewById(R.id.radiusSeekbar);
        radiusValueTextView = (TextView) findViewById(R.id.radiusValueTextView);



        titleEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50)});
        authorEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50)});
        cityEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50)});

        //set the standard values for the date picker:
        calendar = Calendar.getInstance();
        max_year = calendar.get(Calendar.YEAR);
        max_month = calendar.get(Calendar.MONTH);
        max_day = calendar.get(Calendar.DAY_OF_MONTH);

        min_year = max_year;
        min_month = max_month;
        min_day = max_day;

        creationDateMin= new Date(min_year,min_month,min_day); 
        creationDateMax= new Date(max_year,max_month,max_day);
        
        //display a toast message if the inserted creationDateMin is < creationDateMax, 
        //because no stories can be between creationDateMin and creationDateMax if: "creationDateMin  < creationDateMax":
        creationDateMinButton.addTextChangedListener(DateTextWatcher);
        creationDateMaxButton.addTextChangedListener(DateTextWatcher);
        
        //only show the radius elements when a city is specified
        radiusSeekBar.setVisibility(View.GONE);
        radiusValueTextView.setVisibility(View.GONE);
        cityEditText.addTextChangedListener(cityTextWatcher);


        //change the radiusValueTextView-text using the radiusSeekBar-SeekBar:
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                radiusSeekBar.requestFocus();
                radiusValueTextView.setText("Einzugsradius " + String.valueOf(i+1)+" km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //The parcelable class named StoryFilter has no standard constructor, therefore get an object with some values that won't be used
        //and reset them after that to the default values specified in the StoryFilter class:
        defaultStoryFilter = new StoryFilter("default", "default", "default", "default", "default", "default", "default", "default", "default");
        defaultStoryFilter.resetToDefault();	//reset all values to the default values described in the StoryFilter class
        storyFilter = defaultStoryFilter;
        
        //StoryFilter storyFilter = new StoryFilter("default", "default", "default", "default", "default", "default", "default", "default", "default");
        //storyFilter.resetToDefault();	//reset all values to the default values described in the StoryFilter class.
       
        oldIntent = this.getIntent();
        if (oldIntent.hasExtra("Filter_From")) {
        	
        	filterFrom = oldIntent.getExtras().getString("Filter_From");
		    if(filterFrom.equals("InstalledFragment")){
		    	topicTextView.setText("Installierte Stories filtern nach:");
		    	if (oldIntent.hasExtra("InstalledFragmentStoryFilter")){
		    		storyFilter = oldIntent.getParcelableExtra("InstalledFragmentStoryFilter");
		    	}
		    }else if (filterFrom.equals("StoreFragment")){
	    		topicTextView.setText("Store Stories filtern nach:");
		    	if (oldIntent.hasExtra("StoreFragmentStoryFilter")){
		    		storyFilter = oldIntent.getParcelableExtra("StoreFragmentStoryFilter");
		    	}
			}
        }
      
        //get the values from the StoryFilter class and set the GUI Values:
        titleEditText.setText(storyFilter.title);
        authorEditText.setText(storyFilter.author);
        sizeMaxEditText.setText(storyFilter.size_max);
        creationDateMinButton.setText(storyFilter.creationDateMin);
        creationDateMaxButton.setText(storyFilter.creationDateMax);
        cityEditText.setText(storyFilter.city);
        radiusSeekBar.setProgress(Integer.parseInt(storyFilter.radius)-1);
        ////months in android are starting at 0!!! TODO month -1 
        
        
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy");
			creationDateMin = sdf.parse(storyFilter.creationDateMin);
			creationDateMax = sdf.parse(storyFilter.creationDateMax);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.story_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_filter) {

        	 Address address = null;
        	 double latitude = 0;
	         double longitude = 0;
	         
        	 if (!cityEditText.getText().toString().matches("")){
        		 
                 try {
                     address = geoLocate(cityEditText.getText().toString());
                     latitude = address.getLatitude();
        	         longitude = address.getLongitude();
        	         //Toast.makeText(this, "latitude = " + latitude + "longitude = " + longitude, Toast.LENGTH_SHORT).show();

                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
        	 
        	 
	         storyFilter.SetStoryFilter(titleEditText.getText().toString(), authorEditText.getText().toString(), sizeMaxEditText.getText().toString(),
        			creationDateMinButton.getText().toString(), creationDateMaxButton.getText().toString(), cityEditText.getText().toString(),
        			String.valueOf(latitude),String.valueOf(longitude), String.valueOf(radiusSeekBar.getProgress()+1)); 

	         Intent newIntent = new Intent(getApplicationContext(), Main_Activity.class);
	         
	         if(filterFrom.equals("InstalledFragment")){
	        	 newIntent.putExtra("InstalledFragmentStoryFilter",storyFilter);
		         if (oldIntent.hasExtra("StoreFragmentStoryFilter")){			//the old filter also has to be saved
		        	 newIntent.putExtra("StoreFragmentStoryFilter", oldIntent.getParcelableExtra("StoreFragmentStoryFilter"));
		         }
	         }
	         
	         if (filterFrom.equals("StoreFragment")){
	        	 newIntent.putExtra("StoreFragmentStoryFilter",storyFilter);
		         if (oldIntent.hasExtra("InstalledFragmentStoryFilter")){		//the old filter also has to be saved
		        	 newIntent.putExtra("InstalledFragmentStoryFilter", oldIntent.getParcelableExtra("InstalledFragmentStoryFilter"));
		         }
	         }

	        newIntent.putExtra("Filter_From",filterFrom);
	        
	        Toast.makeText(this, "Filter wird übernommen...", Toast.LENGTH_SHORT).show();
	        //Toast.makeText(this, storyFilter.getQuery(), Toast.LENGTH_SHORT).show();
	        
	        startActivity(newIntent);
            return true;
        }else if (id == R.id.reset_filter) {
        	
        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        	        switch (which){
        	        case DialogInterface.BUTTON_POSITIVE:

        		         if(filterFrom.equals("InstalledFragment")){
        		        	 oldIntent.putExtra("InstalledFragmentStoryFilter",defaultStoryFilter);
        		         }
        		         
        		         if (filterFrom.equals("StoreFragment")){
        		        	 oldIntent.putExtra("StoreFragmentStoryFilter",defaultStoryFilter);
        		         }

        	        	finish();
        	        	startActivity(oldIntent);
        		        
        	            break;

        	        case DialogInterface.BUTTON_NEGATIVE:
        	        	//back to Filter
        	            break;
        	        }
        	    }
        	};

        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Möchten Sie wirklich den Filter zurücksetzen?").setPositiveButton("Ja", dialogClickListener)
        	    .setNegativeButton("Nein", dialogClickListener).show();
        	return true;
        }else{
        	return false;
        }
    
    }


    /**
     * The CityTextWatcher shows the radius components only if a city was specified by the user.
     */
    private TextWatcher cityTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3){}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if(cityEditText.getText().toString().matches(""))
            {
                radiusSeekBar.setVisibility(View.GONE);
                radiusValueTextView.setVisibility(View.GONE);

            }
            else
            {
                radiusSeekBar.setVisibility(View.VISIBLE);
                radiusValueTextView.setVisibility(View.VISIBLE);
            }            
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };


    /**
     * The DateTextWatcher displays a toast message if the inserted creationDateMin is > creationDateMax,
     * because no stories can be between creationDateMin and creationDateMax if: "creationDateMin  > creationDateMax". 
     */
    private TextWatcher DateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3){}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            if(creationDateMin.after(creationDateMax) || creationDateMax .before(creationDateMin)){
            	Toast.makeText(Filter_Activity.this, "Das \"Erstellt nach\" Datum, sollte vor dem \"Erstellt vor\" Datum liegen.", Toast.LENGTH_LONG).show();
            }
            
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    @SuppressWarnings("deprecation")
    public void setMinDate(View view) {
        showDialog(0);
    }

    @SuppressWarnings("deprecation")
    public void setMaxDate(View view) {
        showDialog(1);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch(id){
            case 0:
                return new DatePickerDialog(this, min_dateListener, min_year, min_month, min_day);
            case 1:
                return new DatePickerDialog(this, max_dateListener, max_year, max_month, max_day);
        }
        return null;
    }
/**
 * The min_dateListener gets the date from the DatePicker and puts it separated by ". " into a String. 
 */
    private DatePickerDialog.OnDateSetListener min_dateListener= new DatePickerDialog.OnDateSetListener(){

        public void onDateSet(DatePicker datapicker, int year, int month, int day) {
        	//months in android are starting at 0
            creationDateMinButton.setText(new StringBuilder().append(day).append(". ").append(month+1).append(". ").append(year));
            creationDateMin = new Date(year,month,day);    
        }
    };
    
/**
 * The max_dateListener gets the date from the DatePicker and puts it separated by ". " into a String. 
 */
    private DatePickerDialog.OnDateSetListener max_dateListener= new DatePickerDialog.OnDateSetListener(){

        public void onDateSet(DatePicker datapicker, int year, int month, int day) {
        	//months in android are starting at 0
            creationDateMaxButton.setText(new StringBuilder().append(day).append(". ").append(month+1).append(". ").append(year)); 
            creationDateMax = new Date(year,month,day);
        }
    };

    
    
    /**
     * Returns the first found address of the given location.
     * @return the address
     * @throws IOException if no adress was found
     */
    public Address geoLocate(String location)throws IOException {

    	location = location.trim();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       // imm.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

        Geocoder gc = new Geocoder(this);
        java.util.List<Address> list = gc.getFromLocationName(location, 1);

        if (list.isEmpty()){
            //Toast.makeText(this, "Stadt nicht gefunden", Toast.LENGTH_LONG).show();
            return null;
        }else {

            Address address = list.get(0); //get only the first location (google finds more than one location)
            return address;

        }
       
    }
}
