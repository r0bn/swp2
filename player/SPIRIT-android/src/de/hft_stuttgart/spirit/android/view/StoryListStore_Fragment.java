package de.hft_stuttgart.spirit.android.view;

import de.hft_stuttgart.spirit.android.ContentDownloader;
import de.hft_stuttgart.spirit.android.R;
import de.hft_stuttgart.spirit.android.R.id;
import de.hft_stuttgart.spirit.android.R.layout;
import de.hft_stuttgart.spirit.android.R.menu;
import de.hft_stuttgart.spirit.android.Story;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 
 * @author Lukas
 *
 */
public class StoryListStore_Fragment extends Fragment {

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
        List<Story> items = new ArrayList<Story>();
        GetStoreStories task = new GetStoreStories();
        task.execute();
        try {
			items = task.get();
		} catch (InterruptedException e) {
			//TODO Better implementation(maybe give Toast that stories could not been downloaded)
			e.printStackTrace();
		} catch (ExecutionException e) {
			//TODO Better implementation(maybe give Toast that stories could not been downloaded)
			e.printStackTrace();
		}
       
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(),StoryDetails_Activity.class);
                i.putExtra(StoryDetails_Activity.EXTRA_STORYNAME, "Crazy Story");
                i.putExtra(StoryDetails_Activity.EXTRA_DESCRIPTION, "A crazy Story with some crazy shit");
                i.putExtra(StoryDetails_Activity.EXTRA_LOCATION, "48.780332 9.172515");
                i.putExtra(StoryDetails_Activity.EXTRA_AUTHOR, "Mr. Crazy Author");
                i.putExtra(StoryDetails_Activity.EXTRA_CREATIONDATE, "Crazy Date");
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
	 * This class handles the listview and fills the items with the data from the downloaded stories.
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
            // TODO Auto-generated method stub

            Story item = items.get(position);
            View vi=convertView;

            if(convertView==null)
                vi = inflater.inflate(R.layout.listview_item_row, null);

            TextView textTitel = (TextView) vi.findViewById(R.id.textTitel);
            TextView textRegion = (TextView) vi.findViewById(R.id.textRegion);
            TextView textAutor = (TextView) vi.findViewById(R.id.textAutor);

            textTitel.setText(item.getTitle());
            textRegion.setText(item.getLocation());
            textAutor.setText(item.getAuthor());

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
		protected ArrayList<Story> doInBackground(
				Void... params) {
			return ContentDownloader.getInstance().getAllStoriesData();
		}
    	
    }
}
