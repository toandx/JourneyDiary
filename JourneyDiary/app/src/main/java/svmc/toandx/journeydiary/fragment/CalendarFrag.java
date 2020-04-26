package svmc.toandx.journeydiary.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import svmc.toandx.journeydiary.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFrag extends Fragment {

    private CalendarView calendarView;
    private View view;
    public CalendarFrag() {
    }
    public static CalendarFrag newInstance() {
        CalendarFrag fragment = new CalendarFrag();
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
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView=view.findViewById(R.id.cal_frag_calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
                Toast.makeText(getActivity(),Integer.toString(date)+" "+Integer.toString(month)+" "+Integer.toString(year),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
