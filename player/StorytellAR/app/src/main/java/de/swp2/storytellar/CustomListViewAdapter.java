package de.swp2.storytellar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.swp2.storytellar.StoryList_Activity.ListViewItem;

import java.util.List;

public class CustomListViewAdapter extends BaseAdapter
{

    LayoutInflater inflater;
    List<ListViewItem> items;

    public CustomListViewAdapter(Activity context, List<ListViewItem> items) {
        super();

        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        ImageView imageThumbnail = (ImageView) vi.findViewById(R.id.imgThumbnail);
        TextView textTitel = (TextView) vi.findViewById(R.id.textTitel);
        TextView textRegion = (TextView) vi.findViewById(R.id.textRegion);
        TextView textAutor = (TextView) vi.findViewById(R.id.textAutor);


        imageThumbnail.setImageResource(item.ThumbnailResource);

        textTitel.setText(item.Titel);
        textRegion.setText(item.Region);
        textAutor.setText(item.Autor);

        return vi;
    }
}
