package de.hft_stuttgart.spirit.android.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;






import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.hft_stuttgart.spirit.android.ContentDownloader;
import de.hft_stuttgart.spirit.android.R;
import de.hft_stuttgart.spirit.android.Story;

/**
 * 
 * @author Lukas
 *
 */
public class StoryListInstalled_Fragment extends Fragment {

	List<Story> items;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.main, menu);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
	    menu.findItem(R.id.action_installed_filter).setVisible(true);
	    super.onPrepareOptionsMenu(menu);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_list_installed, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        items = new ArrayList<Story>();
        items = ContentDownloader.getInstance().getDownloadedStories();
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Story selectedStory = items.get(position);
                Intent i = new Intent(getActivity().getApplicationContext(),StoryDetails_Activity.class);
                i.putExtra(StoryDetails_Activity.EXTRA_STORYNAME, selectedStory.getTitle());
                i.putExtra(StoryDetails_Activity.EXTRA_DESCRIPTION, selectedStory.getDescription());
                i.putExtra(StoryDetails_Activity.EXTRA_LOCATION, selectedStory.getLocation());
                i.putExtra(StoryDetails_Activity.EXTRA_AUTHOR, selectedStory.getAuthor());
                i.putExtra(StoryDetails_Activity.EXTRA_UPDATEDAT, selectedStory.getUpdated_at());
                i.putExtra(StoryDetails_Activity.EXTRA_STOREORINSTALLED, "INSTALLED");
                i.putExtra(StoryDetails_Activity.EXTRA_STORYID, selectedStory.getId());
               
                Intent oldIntent = getActivity().getIntent();
    	 		if (oldIntent.hasExtra("StoreFragmentStoryFilter")){	
    	 			i.putExtra("StoreFragmentStoryFilter", oldIntent.getParcelableExtra("StoreFragmentStoryFilter"));
  		        }
    	 		if (oldIntent.hasExtra("InstalledFragmentStoryFilter")){	
    	 			i.putExtra("InstalledFragmentStoryFilter", oldIntent.getParcelableExtra("InstalledFragmentStoryFilter"));
   		        } 
    	 		
                startActivity(i);
            }
        });
        
        //CustomListViewAdapter adapter = new CustomListViewAdapter(this, filteredItems);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this, items);
        listView.setAdapter(adapter);

        return rootView;
    }

    public class CustomListViewAdapter extends BaseAdapter
    {

        LayoutInflater inflater;
        List<Story> items;

        public CustomListViewAdapter(StoryListInstalled_Fragment context, List<Story> items2) {
            super();

            this.items = items2;
            this.inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

        	Story item = items.get(position);
            View vi=convertView;

            if(convertView==null)
                vi = inflater.inflate(R.layout.listview_item_row, null);

            Intent intent = getActivity().getIntent();
            
            if (intent.hasExtra("InstalledFragmentStoryFilter")) {
            	StoryFilter storyFilter = getActivity().getIntent().getParcelableExtra("InstalledFragmentStoryFilter");
            	
            	if (!storyFilter.filterStoryItem(item)) {
            		vi=inflater.inflate(R.layout.row_null,null);
            	 	return vi;
            	}
            }
            
        	vi = inflater.inflate(R.layout.listview_item_row, null);
            TextView textTitel = (TextView) vi.findViewById(R.id.textTitel);
            TextView textRegion = (TextView) vi.findViewById(R.id.textRegion);
            TextView textAutor = (TextView) vi.findViewById(R.id.textAutor);

            GetGeoCodeLocationTask task = new GetGeoCodeLocationTask(textRegion,getActivity());
            task.execute(item);
            
            textTitel.setText(item.getTitle());
            textAutor.setText(item.getAuthor());
            
        	if(!item.isUpToDate()){
        		vi.setBackgroundColor(0xA0FF3300);
        	}

            return vi;
            
        }
    }
}
