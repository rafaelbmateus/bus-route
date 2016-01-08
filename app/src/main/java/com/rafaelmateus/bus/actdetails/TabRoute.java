package com.rafaelmateus.bus.actdetails;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rafaelmateus.bus.R;
import com.rafaelmateus.bus.models.RouteStop;

import org.json.JSONArray;
import org.json.JSONObject;

public class TabRoute extends Fragment {

    private ListView lsvStops = null;

    private JSONObject stopsJSON = null;

    /*public TabRoute() {
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpage_route, container, false);

        synchronized (TabRoute.this) {
            this.lsvStops = (ListView) view.findViewById(R.id.ViewPageRoute_LsvStops);
        }

        JSONObject stopsJSON = null;
        try {
            if (savedInstanceState != null) {
                String aux = savedInstanceState.getString("stopsJSON");
                if (aux != null)
                    stopsJSON = new JSONObject(aux);
            }
        } catch (Exception ex) {
            Toast.makeText(TabRoute.this.getActivity(), "Error restoring the route info", Toast.LENGTH_LONG).show();
        }

        this.updateStops(stopsJSON);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("stopsJSON", this.stopsJSON.toString());
    }

    public synchronized void updateStops(JSONObject _stopsJSON) {
        try {
            if (_stopsJSON != null) {
                this.stopsJSON = _stopsJSON;
            }else if (this.stopsJSON == null) {
                return;
            }

            if (this.lsvStops == null) {
                return;
            }

            final java.util.List<RouteStop> listStops = new java.util.ArrayList<>();

            JSONArray rows = this.stopsJSON.getJSONArray("rows");
            for (int index = 0; index < rows.length(); index++) {
                JSONObject stop = rows.getJSONObject(index);
                int id = stop.getInt("id");
                String name = stop.getString("name");
                int sequence = stop.getInt("sequence");
                listStops.add(new RouteStop(id, name, sequence));
            }

            TabRoute.this.lsvStops.setAdapter(new ArrayAdapter<RouteStop>(TabRoute.this.getActivity(), R.layout.listview_routestops, listStops) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View row;
                            if (convertView == null) {
                                LayoutInflater inflater = (LayoutInflater) TabRoute.this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                row = inflater.inflate(R.layout.listview_routestops, null);
                            } else {
                                row = convertView;
                            }
                            RouteStop stop = listStops.get(position);
                            ((TextView) row.findViewById(R.id.ListViewRouteStops_Text1)).setText(Integer.toString(stop.getSequence()));
                            ((TextView) row.findViewById(R.id.ListViewRouteStops_Text2)).setText(stop.getName());

                            return row;
                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(TabRoute.this.getActivity(), "Problem updating the Stops List", Toast.LENGTH_LONG).show();
        }
    }
}
