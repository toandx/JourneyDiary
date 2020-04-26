package svmc.toandx.journeydiary.adapter;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import svmc.toandx.journeydiary.R;
import svmc.toandx.journeydiary.object.Event;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Event> value;
    private ImageView imageView;
    private TextView textView;
    private LayoutInflater layoutInflater;
    private String image_uri;
    private ConstraintLayout blockImage;
    public GridAdapter(Context c,ArrayList<Event> value) {
        this.mContext = c;
        this.value=value;
        this.layoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return this.value.size();
    }

    @Override
    public Object getItem(int position) {
        return this.value.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gridview_layout, null);
        }
        imageView=convertView.findViewById(R.id.grid_image);
        textView=convertView.findViewById(R.id.grid_text);
        image_uri=value.get(position).image_uri;
        Glide.with(this.mContext).load(value.get(position).image_uri).into(imageView);
        textView.setText(value.get(position).time);
        return convertView;
    }
}
