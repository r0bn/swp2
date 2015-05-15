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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.hft_stuttgart.spirit.android.ContentDownloader;
import de.hft_stuttgart.spirit.android.R;
import de.hft_stuttgart.spirit.android.Story;
import android.content.Context;

/**
 * 
 * @author Lukas
 *
 */
public class StoryListStore_Fragment extends Fragment {

	List<Story> items;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.main, menu);
	    //super.onCreateOptionsMenu(menu, inflater);
	}
	

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
	    menu.findItem(R.id.action_store_filter).setVisible(true);
	    super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Method is called when Fragment is created. It will do the following things:
	 * - request the stories from the server
	 * - handle clickEvents on items(which will lead to the details Activity)
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_list_store, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        items = new ArrayList<Story>();
        
        String adjustedQuery = "allStories";
	 
        final Intent oldIntent = getActivity().getIntent();
        
        if (oldIntent.hasExtra("StoreFragmentStoryFilter")) {
        	StoryFilter storyFilter = getActivity().getIntent().getParcelableExtra("StoreFragmentStoryFilter");
	    	adjustedQuery = storyFilter.getQuery();
		} else {
			adjustedQuery = "allStories"; //if no filter is used: adjustedQuery = "allStories" (show all stories)
		}
		
        GetStoreStoriesWithParameter task= new GetStoreStoriesWithParameter();

        //Toast.makeText(getActivity(), "query = "+adjustedQuery, Toast.LENGTH_LONG).show();
        
    	task.execute(adjustedQuery);	

        try {
			items = task.get();
		} catch (InterruptedException e) {
			//TODO Better implementation(maybe give Toast that stories could not been downloaded)
			Toast.makeText(getActivity(), "Stories konnten nicht heruntergeladen werden.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			//TODO Better implementation(maybe give Toast that stories could not been downloaded)
			Toast.makeText(getActivity(), "Stories konnten nicht heruntergeladen werden.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
       
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Story selectedStory = items.get(position);
                Intent i = new Intent(getActivity().getApplicationContext(),StoryDetails_Activity.class);
                i.putExtra(StoryDetails_Activity.EXTRA_STORYID, selectedStory.getId());
                i.putExtra(StoryDetails_Activity.EXTRA_STORYNAME, selectedStory.getTitle());
                i.putExtra(StoryDetails_Activity.EXTRA_DESCRIPTION, selectedStory.getDescription());
                i.putExtra(StoryDetails_Activity.EXTRA_LOCATION, selectedStory.getLocation());
                i.putExtra(StoryDetails_Activity.EXTRA_AUTHOR, selectedStory.getAuthor());
                i.putExtra(StoryDetails_Activity.EXTRA_UPDATEDAT, selectedStory.getUpdated_at());
                i.putExtra(StoryDetails_Activity.EXTRA_STOREORINSTALLED, "STORE");
                
    	 		if (oldIntent.hasExtra("StoreFragmentStoryFilter")){	
    	 			i.putExtra("StoreFragmentStoryFilter", oldIntent.getParcelableExtra("StoreFragmentStoryFilter"));
  		        }
    	 		if (oldIntent.hasExtra("InstalledFragmentStoryFilter")){	
    	 			i.putExtra("InstalledFragmentStoryFilter", oldIntent.getParcelableExtra("InstalledFragmentStoryFilter"));
   		        } 

                startActivity(i);
            }
        });
        CustomListViewAdapter adapter = new CustomListViewAdapter(this, items);
        listView.setAdapter(adapter);


        return rootView;
    }

	/**
	 * 
	 * @author Lukas
	 *
	 * This class handles the ListView and fills the items with the data from the downloaded stories.
	 */
    public class CustomListViewAdapter extends BaseAdapter
    {

        LayoutInflater inflater;
        List<Story> items;

        public CustomListViewAdapter(StoryListStore_Fragment context, List<Story> items) {
            super();

            this.items = items;
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
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Story item = items.get(position);
            View vi=convertView;

            if(convertView==null)
                vi = inflater.inflate(R.layout.listview_item_row, null);

            TextView textTitel = (TextView) vi.findViewById(R.id.textTitel);
            TextView textRegion = (TextView) vi.findViewById(R.id.textRegion);
            TextView textAutor = (TextView) vi.findViewById(R.id.textAutor);
            
            GetGeoCodeLocationTask task = new GetGeoCodeLocationTask(textRegion,getActivity());
            task.execute(item); //Array out of bound Exception


            textTitel.setText(item.getTitle());
            textAutor.setText(item.getAuthor());

            if(item.isAlreadyDownloaded()){            	
            	vi.setBackgroundColor(0xA040eb12);
            } else {
            	vi.setBackgroundColor(0x00000000);
            }
            
            
            return vi;
        }
    }
    
    /**
     * 
     * @author Lukas
     * This class requests the stories from the ContentDownloader
     */
    public class GetStoreStories extends AsyncTask<Void, Void, ArrayList<Story>> {

		@Override
		protected ArrayList<Story> doInBackground(Void... params) {

				return ContentDownloader.getInstance().getAllStoriesData();

		}
   	
    }
    
    /**
     * 
     * @author Stefan
     * This class requests the filtered stories from the ContentDownloader
     */
    public class GetStoreStoriesWithParameter extends AsyncTask<String, Void, ArrayList<Story>> {

		@Override
		protected ArrayList<Story> doInBackground(String... params) {
			if (params[0].equals("allStories")){
				return ContentDownloader.getInstance().getAllStoriesData();
			}else{
				return ContentDownloader.getInstance().requestAllStoriesWithParameter(params[0]);
			}
		}
		
		

    }
    

}
