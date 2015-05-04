package de.hft_stuttgart.spirit.android.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
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
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Filter_Activity extends ActionBarActivity {

    private Calendar calendar;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText sizeMaxEditText;
    private Button creationDateMinButton;
    private Button creationDateMaxButton;
    private EditText cityEditText;
    private SeekBar radiusSeekBar;
    private TextView radiusValueTextView;


    int min_year, min_month, min_day;
    int max_year, max_month, max_day; //initialize them to current date in onStart()/onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        

        titleEditText = (EditText)findViewById(R.id.titleEditText);
        authorEditText = (EditText)findViewById(R.id.authorEditText);
        sizeMaxEditText = (EditText)findViewById(R.id.sizeMaxEditText);
        creationDateMinButton = (Button) findViewById(R.id.creationDateMinButton);
        creationDateMaxButton = (Button) findViewById(R.id.creationDateMaxButton);
        cityEditText = (EditText)findViewById(R.id.cityEditText);
        radiusSeekBar = (SeekBar) findViewById(R.id.radiusSeekbar);
        radiusValueTextView = (TextView) findViewById(R.id.radiusValueTextView);

        //only show the radius elements when a city is specified
        radiusSeekBar.setVisibility(View.GONE);
        radiusValueTextView.setVisibility(View.GONE);

        /*
        titleEditText.setFilters(new InputFilter[] { filter });
        authorEditText.setFilters(new InputFilter[] { filter });
        cityEditText.setFilters(new InputFilter[] { filter });
        */

        titleEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20)});
        authorEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20)});
        cityEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20)});

        calendar = Calendar.getInstance();
        max_year = calendar.get(Calendar.YEAR);
        max_month = calendar.get(Calendar.MONTH);
        max_day = calendar.get(Calendar.DAY_OF_MONTH);

        creationDateMaxButton.setText(new StringBuilder().append(max_day).append(". ").append(max_month).append(". ").append(max_year));

        min_year = max_year-2;
        min_month = max_month;
        min_day = max_day;
        creationDateMinButton.setText(new StringBuilder().append(min_day).append(". ").append(min_month).append(". ").append(min_year));

        //set listener to show the radius components only if a city is specified
        cityEditText.addTextChangedListener(textWatcher);


        //change the text using the seekbar:
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

            //id, title, description, author, size, size_min, size_max, creation_date, creation_date_min, creation_date_max, location, radius
            //Example: http://api.storytellar.de/story?author=arno+claus&size_max=20

            StringBuilder getQuery = new StringBuilder();
            getQuery.append("http://api.storytellar.de/story?"); //already defined in RESTclient.java
            if (!titleEditText.getText().toString().matches("")) getQuery.append("title=" + titleEditText.getText().toString()+"&");
            if (!authorEditText.getText().toString().matches("")) getQuery.append("author=" + authorEditText.getText().toString()+"&");
            if (!sizeMaxEditText.getText().toString().matches("")) getQuery.append("size_max=" + sizeMaxEditText.getText().toString()+"&");
            if (!creationDateMinButton.getText().toString().matches("")) getQuery.append("creation_date_min="
                    + creationDateMinButton.getText().toString().replaceAll("\\.", "")+"&");
            if (!creationDateMaxButton.getText().toString().matches("")) getQuery.append("creation_date_max="
                   + creationDateMaxButton.getText().toString().replaceAll("\\.", "")+"&");

            if (!cityEditText.getText().toString().matches("")){
                try {
                    Address address = geoLocate(cityEditText.getText().toString());
                    if (address != null) {
                        String locality = address.getLocality();

                        double latitude = address.getLatitude();
                        double longitude = address.getLongitude();

                        getQuery.append("location=" + String.valueOf(latitude) + "+" + String.valueOf(longitude)+"&"
                                +"radius=" + String.valueOf(radiusSeekBar.getProgress()+1)+"&");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


            String adjustedQuery = getQuery.substring(0, getQuery.length()-1); //delete the last &

             //delete double whitespaces
            //adjustedQuery.replaceAll("\\s+", " ");
            adjustedQuery = adjustedQuery.trim().replaceAll(" +", " ");

            adjustedQuery = adjustedQuery.replaceAll(" ", "+");

            //Toast.makeText(this, adjustedQuery, Toast.LENGTH_LONG).show();

            Toast.makeText(this, adjustedQuery, Toast.LENGTH_SHORT).show();
            
            
            Intent intent = new Intent(getApplicationContext(), Main_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("query", adjustedQuery); //Your id
            intent.putExtras(bundle); //Put your id to your next Intent
            startActivity(intent);
            
            finish();
         //   startActivity(new Intent(getApplicationContext(),Main_Activity.class));
            
          //  Intent myIntent = new Intent(Filter_Activity.this, Main_Activity.class);
          //  myIntent.putExtra("query", adjustedQuery);
          //  Filter_Activity.this.startActivity(myIntent);
            
            //startActivity(new Intent(getApplicationContext(),Main_Activity.class));
            return true;
        }else {
        	return false;
        }
    
    }


    //TextWatcher
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

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
        public void afterTextChanged(Editable editable) {
        }
    };

/*
    private InputFilter filter = new InputFilter() {
    //removes non-alphanumeric characters
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int count) {

          // final Pattern USER_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
            final Pattern USER_NAME_PATTERN = Pattern.compile("[a-zA-Z|\\s]");

            if(!USER_NAME_PATTERN.matcher(source).matches()||end > 20){
                return "";
            }
            return null;
            }


            for (int i = start; i < end; i++) {
                if ((!Character.isLetter(source.charAt(i))&& !Character.isWhitespace(source.charAt(i)))|| count > 20) {
                    return "";
                }
            }
            return null;
        }
    };
*/
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

    private DatePickerDialog.OnDateSetListener min_dateListener= new DatePickerDialog.OnDateSetListener(){

        public void onDateSet(DatePicker datapicker, int year, int month, int day) {
            creationDateMinButton.setText(new StringBuilder().append(day).append(". ").append(month).append(". ").append(year));
        }


    };
    private DatePickerDialog.OnDateSetListener max_dateListener= new DatePickerDialog.OnDateSetListener(){

        public void onDateSet(DatePicker datapicker, int year, int month, int day) {
            creationDateMaxButton.setText(new StringBuilder().append(day).append(". ").append(month).append(". ").append(year));
        }
    };

    /**
     * Returns the first found address of the given location
     * @return the address
     * @throws IOException
     */
    public Address geoLocate(String location)throws IOException {

        if (cityEditText.getText().toString().matches("")){ //no city was inserted

            return null;

        }else { //look for coordinates
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

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

}
