package de.hft_stuttgart.spirit.android.view;

import de.hft_stuttgart.spirit.android.R;
import de.hft_stuttgart.spirit.android.R.id;
import de.hft_stuttgart.spirit.android.R.layout;
import de.hft_stuttgart.spirit.android.R.menu;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class StoryListStore_Fragment extends Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_list_store, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        final List<ListViewItem> items = new ArrayList<ListViewItem>();

        /*Dummydaten Start*/
        items.add(new ListViewItem()
        {{
                Titel = "Story 100";
                Region = "Stuttgart";
                Autor = "Stefan";
            }});

        items.add(new ListViewItem()
        {{
                Titel = "Story 200";
                Region = "Calw";
                Autor = "Karl";
            }});

        for (int i = 3; i < 20 ; i++){
            final int x = i;
            items.add(new ListViewItem()
            {{
                    Titel = "Story "+x*100;
                    Region = "Region";
                    Autor = "Autor";
                }});
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(),StoryDetails_Activity.class);
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
        List<ListViewItem> items;

        public CustomListViewAdapter(StoryListStore_Fragment context, List<ListViewItem> items) {
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
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ListViewItem item = items.get(position);
            View vi=convertView;

            if(convertView==null)
                vi = inflater.inflate(R.layout.listview_item_row, null);

            TextView textTitel = (TextView) vi.findViewById(R.id.textTitel);
            TextView textRegion = (TextView) vi.findViewById(R.id.textRegion);
            TextView textAutor = (TextView) vi.findViewById(R.id.textAutor);

            textTitel.setText(item.Titel);
            textRegion.setText(item.Region);
            textAutor.setText(item.Autor);

            return vi;
        }
    }
}
