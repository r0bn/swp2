package de.swp2.storytellar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class StoryList_Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        ListView listView = (ListView) findViewById(R.id.listView);
        final List<ListViewItem> items = new ArrayList<ListViewItem>();

        /*Dummydaten Start*/
        items.add(new ListViewItem()
        {{
                ThumbnailResource = R.drawable.abc_btn_check_material;
                Titel = "Story 1";
                Region = "Stuttgart";
                Autor = "Stefan";
            }});

        items.add(new ListViewItem()
        {{
                ThumbnailResource = R.mipmap.matlab;
                Titel = "Story 2";
                Region = "Calw";
                Autor = "Karl";
            }});

        for (int i = 3; i < 20 ; i++){
            final int x = i;
            items.add(new ListViewItem()
            {{
                    ThumbnailResource = R.mipmap.ic_launcher;
                    Titel = "Story "+x;
                    Region = "Region";
                    Autor = "Autor";
                }});
        }
        /*Dummydaten Ende*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),StoryDetails_Activity.class);
                startActivity(i);
            }
        });
        CustomListViewAdapter adapter = new CustomListViewAdapter(this, items);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ListViewItem{  //------
        public int ThumbnailResource;
        public String Titel;
        public String Region;
        public String Autor;
    }
}
