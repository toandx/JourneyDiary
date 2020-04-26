package svmc.toandx.journeydiary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import svmc.toandx.journeydiary.R;
import svmc.toandx.journeydiary.adapter.EventAdapter;
import svmc.toandx.journeydiary.object.Event;

public class FavoriteActivity extends AppCompatActivity {
    private ListView eventList;
    private ImageButton btnBack;
    private EventAdapter eventAdapter;
    private int selectedEventPos;
    private ArrayList<Event> favorEvent;
    private ArrayList<Integer> eventID;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        eventList=findViewById(R.id.favor_activ_event_list);
        btnBack=findViewById(R.id.favor_activ_back_btn);
        favorEvent=new ArrayList<>();
        eventID=new ArrayList<>();
        for(int i=0;i<MainActivity.value.size();++i)
            if (MainActivity.value.get(i).favorite)
            {
                favorEvent.add(MainActivity.value.get(i));
                eventID.add(i);
            }
        eventAdapter=new EventAdapter(FavoriteActivity.this,favorEvent);
        eventList.setAdapter(eventAdapter);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                intent=new Intent(FavoriteActivity.this, AddEventActivity.class);
                intent.setAction("edit");
                selectedEventPos=position;
                intent.putExtra("event_id",eventID.get(selectedEventPos));
                startActivityForResult(intent,100);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if((requestCode==100) && (resultCode==RESULT_OK))
        {
            if (!MainActivity.value.get(eventID.get(selectedEventPos)).favorite)
                favorEvent.remove(selectedEventPos); else
            favorEvent.set(selectedEventPos,MainActivity.value.get(eventID.get(selectedEventPos)));
            eventAdapter.notifyDataSetChanged();
            eventList.invalidateViews();
        }
    }
}
