package de.hft_stuttgart.spirit.android.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.hft_stuttgart.spirit.android.ContentDownloader;
import de.hft_stuttgart.spirit.android.R;
import de.hft_stuttgart.spirit.android.Story;

public class StoryListInstalled_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_list_installed, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        List<Story> items = new ArrayList<Story>();
        items = ContentDownloader.getInstance().getDownloadedStories();
 
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

    class ListViewItem{  //------
        public int ThumbnailResource;
        public String Titel;
        public String Region;
        public String Autor;
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

            TextView textTitel = (TextView) vi.findViewById(R.id.textTitel);
            TextView textRegion = (TextView) vi.findViewById(R.id.textRegion);
            TextView textAutor = (TextView) vi.findViewById(R.id.textAutor);

            textTitel.setText(item.getTitle());
            textRegion.setText(item.getLocation());
            textAutor.setText(item.getAuthor());

            return vi;
        }
    }
}
