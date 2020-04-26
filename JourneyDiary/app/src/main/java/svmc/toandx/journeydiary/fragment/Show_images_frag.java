package svmc.toandx.journeydiary.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import svmc.toandx.journeydiary.R;
import svmc.toandx.journeydiary.activity.AddEventActivity;
import svmc.toandx.journeydiary.activity.MainActivity;
import svmc.toandx.journeydiary.adapter.GridAdapter;
import svmc.toandx.journeydiary.object.Event;

import static android.app.Activity.RESULT_OK;

public class Show_images_frag extends Fragment {
    private View view;
    private ArrayList<Event> value;
    private ArrayList<Integer> eventID;
    private int selectedEventPos;
    private Intent intent;
    private GridView gridView;
    private GridAdapter gridAdapter;
    public Show_images_frag() {
        // Required empty public constructor
    }
    public static Show_images_frag newInstance(String param1, String param2) {
        Show_images_frag fragment = new Show_images_frag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_show_images_frag, container, false);
        value=new ArrayList<>();
        eventID=new ArrayList<>();
        for(int i=0;i<MainActivity.value.size();++i)
            if (MainActivity.value.get(i).image_uri.length()!=0)
            {
                value.add(MainActivity.value.get(i));
                eventID.add(i);
            }
        gridAdapter=new GridAdapter(getActivity(), value);
        gridView = view.findViewById(R.id.grid_view);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                intent=new Intent(getActivity(), AddEventActivity.class);
                intent.setAction("edit");
                selectedEventPos=position;
                intent.putExtra("event_id",eventID.get(selectedEventPos));
                startActivityForResult(intent,100);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if((requestCode==100) && (resultCode==RESULT_OK))
        {
            value.set(selectedEventPos,MainActivity.value.get(eventID.get(selectedEventPos)));
            gridAdapter.notifyDataSetChanged();
            gridView.invalidateViews();
        }
    }
}
