package svmc.toandx.journeydiary.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import svmc.toandx.journeydiary.R;
import svmc.toandx.journeydiary.activity.AddEventActivity;
import svmc.toandx.journeydiary.activity.MainActivity;
import svmc.toandx.journeydiary.adapter.EventAdapter;
import svmc.toandx.journeydiary.object.Event;

import static android.app.Activity.RESULT_OK;

public class Show_event_fragment extends Fragment {
    private ListView eventList;
    private FloatingActionButton btnAdd;
    private ArrayList<Event> event;
    private EventAdapter eventAdapter;
    private View view;
    private Intent intent;
    private final int ADD_EVENT_REQ=-1;
    /*private String name,time,image_uri,address;
    private Boolean hasLocation,favorite;
    private double latitude,longitude;*/
    public Show_event_fragment() {
    }
    public static Show_event_fragment newInstance() {
        Show_event_fragment fragment = new Show_event_fragment();
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
        View view=inflater.inflate(R.layout.fragment_show_event_fragment, container, false);
        eventList=view.findViewById(R.id.show_event_frag_list);
        btnAdd=view.findViewById(R.id.show_event_frag_add_btn);
        //event=new ArrayList<>();
        //event=MainActivity.value.clone();
        eventAdapter=new EventAdapter(getActivity(), MainActivity.value);
        eventList.setAdapter(eventAdapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(getActivity(), AddEventActivity.class);
                intent.setAction("add");
                startActivityForResult(intent,100);
            }
        });
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                intent=new Intent(getActivity(), AddEventActivity.class);
                intent.setAction("edit");
                intent.putExtra("event_id",position);
                startActivityForResult(intent,100);
                //Toast.makeText(getActivity(),Integer.toString(position),Toast.LENGTH_SHORT).show();
            }
        });
        return(view);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if((requestCode==100) && (resultCode==RESULT_OK))
        {
            eventAdapter.notifyDataSetChanged();
            eventList.invalidateViews();
        }
    }
}
