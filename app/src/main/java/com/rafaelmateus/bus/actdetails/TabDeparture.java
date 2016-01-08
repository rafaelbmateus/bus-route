package com.rafaelmateus.bus.actdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.rafaelmateus.bus.R;
import com.rafaelmateus.bus.models.Day;
import com.rafaelmateus.bus.models.Hour;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.TreeMap;

public class TabDeparture extends Fragment {

    private JSONObject departureJSON = null;

    private Spinner spnDays = null;

    private ExpandableListView lsvDepartureHours = null;

    public TabDeparture() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpage_departure, container, false);

        synchronized (TabDeparture.this) {
            this.spnDays = (Spinner) view.findViewById(R.id.ViewPageDeparture_SpnDays);
            this.lsvDepartureHours = (ExpandableListView) view.findViewById(R.id.ViewPageDeparture_LsvDeparture);
        }

        JSONObject departureJSON = null;
        try {
            if (savedInstanceState != null) {
                String aux = savedInstanceState.getString("departureJSON");
                if (aux != null)
                    departureJSON = new JSONObject(aux);
            }
        } catch (Exception ex) {
            Toast.makeText(TabDeparture.this.getActivity(), "Error restoring the route Departures", Toast.LENGTH_LONG).show();
        }

        this.updateDeparture(departureJSON);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("departureJSON", this.departureJSON.toString());
    }

    public synchronized void updateDeparture(JSONObject _departureJSON) {
        try {
            if (_departureJSON != null) {
                this.departureJSON = _departureJSON;
            }else if (this.departureJSON == null) {
                return;
            }

            if (this.spnDays == null) {
                return;
            }

            final TreeMap<String, Day> hashDepartureDays = new TreeMap<>();
            Day day = null;
            Hour hour = null;

            JSONArray rows = this.departureJSON.getJSONArray("rows");
            for (int index = 0; index < rows.length(); index++) {
                JSONObject departure = rows.getJSONObject(index);
                int id = departure.getInt("id");
                String calendar = departure.getString("calendar");
                String time = departure.getString("time");

                if ((day == null) || (!day.getDay().equals(calendar))) {
                    day = hashDepartureDays.get(calendar);
                    if (day == null) {
                        day = new Day(calendar);
                        hashDepartureDays.put(calendar, day);
                    }
                }

                String timeHour = "ERR";
                if (time.length() == 5)
                    timeHour = time.substring(0, 2);
                if ((hour == null) || (!hour.getHour().equals(timeHour))) {
                    hour = day.getHashMapHours().get(timeHour);
                    if (hour == null) {
                        hour = new Hour(timeHour);
                        day.getHashMapHours().put(timeHour, hour);
                    }
                }

                hour.getListDeparture().add(time);
            }

            final Day[] arrayDays = hashDepartureDays.values().toArray(new Day[0]);

            this.spnDays.setAdapter(new ArrayAdapter<Day>(TabDeparture.this.getActivity(), R.layout.spinner_item, arrayDays));
            this.spnDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Day day = arrayDays[position];
                        final Hour[] arrayHours = day.getHashMapHours().values().toArray(new Hour[0]);
                        TabDeparture.this.lsvDepartureHours.setAdapter(new ExpandableListAdapter(arrayHours));
                    } catch (Exception ex) {
                        Toast.makeText(TabDeparture.this.getActivity(), "Problem updating the Day's Departures List", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception ex) {
            Toast.makeText(TabDeparture.this.getActivity(), "Problem updating the Departures List", Toast.LENGTH_LONG).show();
        }
    }
}
