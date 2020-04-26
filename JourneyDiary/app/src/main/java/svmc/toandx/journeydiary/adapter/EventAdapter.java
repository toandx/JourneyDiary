package svmc.toandx.journeydiary.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import svmc.toandx.journeydiary.R;
import svmc.toandx.journeydiary.object.Event;
public class EventAdapter extends ArrayAdapter {
    private final Activity context;
    private TextView nameTextView,timeTextView,addressTextView;
    private ImageView imageView,favorImageView;
    private ArrayList<Event> value;
    private String image_uri;
    public EventAdapter(Activity context, ArrayList<Event> value)
    {
        super(context, R.layout.event_list_view,value);
        this.context=context;
        this.value=value;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.event_list_view, null,true);
        //this code gets references to objects in the listview_row.xml file
        nameTextView = (TextView) rowView.findViewById(R.id.eventName);
        timeTextView = (TextView) rowView.findViewById(R.id.eventTime);
        addressTextView = (TextView) rowView.findViewById(R.id.eventAddress);
        imageView = (ImageView) rowView.findViewById(R.id.eventImage);
        favorImageView=(ImageView) rowView.findViewById(R.id.eventFavor);
        //this code sets the values of the objects to values from the arrays
        nameTextView.setText(value.get(position).name);
        timeTextView.setText(value.get(position).time);
        if (value.get(position).favorite)
            favorImageView.setVisibility(View.VISIBLE);
        else
            favorImageView.setVisibility(View.GONE);
        if (value.get(position).location)
        {
            addressTextView.setText(value.get(position).address);
        } else
            addressTextView.setText("");
        image_uri=value.get(position).image_uri;
        if (image_uri.length()!=0) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(this.context).load(Uri.parse(image_uri)).into(imageView);
        } else imageView.setVisibility(View.GONE);

        return rowView;

    };
}
