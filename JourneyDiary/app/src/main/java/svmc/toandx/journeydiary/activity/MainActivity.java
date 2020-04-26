package svmc.toandx.journeydiary.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import svmc.toandx.journeydiary.R;
import svmc.toandx.journeydiary.fragment.CalendarFrag;
import svmc.toandx.journeydiary.fragment.Show_event_fragment;
import svmc.toandx.journeydiary.fragment.Show_images_frag;
import svmc.toandx.journeydiary.object.Event;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;
    private FragmentTransaction ft;
    private Show_event_fragment show_event_fragment;
    private Show_images_frag show_images_frag;
    private CalendarFrag calendarFrag;
    private Toolbar toolbar;
    private BottomNavigationView botNavView;
    private NavigationView navigationView;
    private Intent intent;
    public static ArrayList<Event> value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        value=new ArrayList<>();
        /**/
        DrawerLayout drawerLayout=findViewById(R.id.layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        /**/
        show_event_fragment=new Show_event_fragment();
        show_images_frag = new Show_images_frag();
        calendarFrag=new CalendarFrag();
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.replace(R.id.main_frame,show_event_fragment);
        ft.commit();
        botNavView=findViewById(R.id.navigation);
        botNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_list:
                        ft=fm.beginTransaction();
                        ft.replace(R.id.main_frame,show_event_fragment);
                        ft.commit();
                        return(true);
                    case R.id.navigation_image:
                        ft=fm.beginTransaction();
                        ft.replace(R.id.main_frame,show_images_frag);
                        ft.commit();
                        return(true);
                    case R.id.navigation_calendar:
                        ft=fm.beginTransaction();
                        ft.replace(R.id.main_frame,calendarFrag);
                        ft.commit();
                        return(true);
                    case R.id.navigation_map:
                        return(true);
                }
                return(false);
            }
        });
        navigationView=findViewById(R.id.main_activ_navig);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_place:
                        Toast.makeText(MainActivity.this,"Place",Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.nav_fav:
                        intent=new Intent(MainActivity.this,FavoriteActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_new_journal:
                        intent=new Intent(MainActivity.this,AddEventActivity.class);
                        intent.setAction("add");
                        startActivity(intent);
                        return true;
                    case R.id.nav_settings:
                        Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_LONG).show();
                        return true;
                }
                return false;
            }
        });

    }
}
