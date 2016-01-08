package com.rafaelmateus.bus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rafaelmateus.bus.actdetails.DetailsActivity;
import com.rafaelmateus.bus.models.Route;
import com.rafaelmateus.bus.webservices.TaskCallBack;
import com.rafaelmateus.bus.webservices.TaskResult;
import com.rafaelmateus.bus.webservices.WSParam;
import com.rafaelmateus.bus.webservices.WSTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText editTextSearch = null;

    private ListView lisViewSearch = null;

    private String routesJSON = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById(R.id.ButtonSearch).setOnClickListener(this.btnSearch_Click);
        this.lisViewSearch = (ListView) this.findViewById(R.id.ListViewSearch);
        this.editTextSearch = (EditText) this.findViewById(R.id.EditTextSearch);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("searchText", this.editTextSearch.getText().toString());
        savedInstanceState.putString("routesJSON", this.routesJSON);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null)
            return;

        try {
            String searchText = savedInstanceState.getString("searchText");
            if (searchText != null) {
                this.editTextSearch.setText(searchText);
                this.editTextSearch.setSelection(searchText.length());
            }
            String routesJSON = savedInstanceState.getString("routesJSON");
            if (routesJSON != null) {
                this.updateRoutes(new JSONObject(routesJSON));
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Error restoring the routes", Toast.LENGTH_LONG).show();
        }
    }

    private final View.OnClickListener btnSearch_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(MainActivity.this.editTextSearch.getWindowToken(), 0);

                MainActivity.this.searchRoutes();
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Error calling the Web Service", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void searchRoutes() {
        try {
            WSParam wsParam = new WSParam(WSParam.URL_FIND_ROUTES_BY_STOPNAME, "stopName", "%" + MainActivity.this.editTextSearch.getText() + "%");
            WSTask wsTask = new WSTask(MainActivity.this, MainActivity.this.searchCallBack);
            wsTask.execute(wsParam);
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Error making the routes' search", Toast.LENGTH_LONG).show();
        }
    }

    private final TaskCallBack searchCallBack = new TaskCallBack() {
        @Override
        public void onComplete(TaskResult result) {
            try {
                switch (result.getStatus()) {
                    case TaskResult.STS_OK:
                        MainActivity.this.updateRoutes(result.getJsonObj());
                        break;

                    case TaskResult.STS_ERR:
                        Toast.makeText(MainActivity.this, result.getErrMsg(), Toast.LENGTH_LONG).show();
                        break;

                    case TaskResult.STS_UNKNOWN:
                    default:
                        Toast.makeText(MainActivity.this, "Status unknown!", Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Problem unexpected", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void updateRoutes(JSONObject routesJSON) {
        try {
            final java.util.List<Route> listRoutes = new java.util.ArrayList<>();
            this.routesJSON = routesJSON.toString();

            JSONArray rows = routesJSON.getJSONArray("rows");
            for (int index = 0; index < rows.length(); index++) {
                JSONObject route = rows.getJSONObject(index);
                int id = route.getInt("id");
                String shortName = route.getString("shortName");
                String longName = route.getString("longName");
                listRoutes.add(new Route(id, shortName, longName));
            }

            MainActivity.this.lisViewSearch.setAdapter(new ArrayAdapter<Route>(this, R.layout.listview_routes, listRoutes) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View row;
                    if (convertView == null) {
                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        row = inflater.inflate(R.layout.listview_routes, null);
                    } else {
                        row = convertView;
                    }
                    Route route = listRoutes.get(position);
                    ((TextView) row.findViewById(R.id.ListViewRoute_Text1)).setText(route.getShortName());
                    ((TextView) row.findViewById(R.id.ListViewRoute_Text2)).setText(route.getLongName());

                    return row;
                }
            });

            MainActivity.this.lisViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Route route = listRoutes.get(position);

                    Intent actDetailsIntent = new android.content.Intent(MainActivity.this, DetailsActivity.class);
                    actDetailsIntent.putExtra("routeId", route.getId());
                    actDetailsIntent.putExtra("routeName", route.getLongName());

                    MainActivity.this.startActivity(actDetailsIntent);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Problem updating the Route List", Toast.LENGTH_LONG).show();
        }
    }
}