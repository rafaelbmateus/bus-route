package com.rafaelmateus.bus.actdetails;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;


import com.rafaelmateus.bus.R;
import com.rafaelmateus.bus.webservices.TaskCallBack;
import com.rafaelmateus.bus.webservices.TaskResult;
import com.rafaelmateus.bus.webservices.WSParam;
import com.rafaelmateus.bus.webservices.WSTask;

public class DetailsActivity extends AppCompatActivity {

    private TabRoute tabRoute = null;

    private TabDeparture tabDeparture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.ActDetails_Toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) this.findViewById(R.id.ActDetails_ViewPager);
        this.defineViewPages(viewPager);

        TabLayout tabLayout = (TabLayout) this.findViewById(R.id.ActDetails_Tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent actDetailsIntent = this.getIntent();
        String routeName = actDetailsIntent.getStringExtra("routeName");
        if (routeName != null)
            this.getSupportActionBar().setTitle(routeName);

        if (actDetailsIntent.hasExtra("routeId")) {
            int routeId = actDetailsIntent.getIntExtra("routeId", 0);
            this.getRouteInfo(routeId);
            actDetailsIntent.removeExtra("routeId");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void getRouteInfo(int routeId) {
        try {
            WSParam wsParam = new WSParam(WSParam.URL_FIND_STOPS_BY_ROUTEID, "routeId", routeId);
            WSTask wsTask = new WSTask(DetailsActivity.this, DetailsActivity.this.stopsCallBack);
            wsTask.execute(wsParam);

            wsParam = new WSParam(WSParam.URL_FIND_DEPARTURES_BY_ROUTEID, "routeId", routeId);
            wsTask = new WSTask(DetailsActivity.this, DetailsActivity.this.departureCallBack);
            wsTask.execute(wsParam);
        } catch (Exception ex) {
            Toast.makeText(DetailsActivity.this, "Problem getting the Route Info", Toast.LENGTH_LONG).show();
        }
    }

    private void defineViewPages(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        this.tabRoute = new TabRoute();
        viewPagerAdapter.addFragment(this.tabRoute, "Route");

        this.tabDeparture = new TabDeparture();
        viewPagerAdapter.addFragment(this.tabDeparture, "Departure");

        viewPager.setAdapter(viewPagerAdapter);
    }

    private final TaskCallBack stopsCallBack = new TaskCallBack() {
        @Override
        public void onComplete(TaskResult result) {
            try {
                switch (result.getStatus()) {
                    case TaskResult.STS_OK:
                        DetailsActivity.this.tabRoute.updateStops(result.getJsonObj());
                        break;

                    case TaskResult.STS_ERR:
                        Toast.makeText(DetailsActivity.this, result.getErrMsg(), Toast.LENGTH_LONG).show();
                        break;

                    case TaskResult.STS_UNKNOWN:
                    default:
                        Toast.makeText(DetailsActivity.this, "Status unknown!", Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception ex) {
                Toast.makeText(DetailsActivity.this, "Problem unexpected", Toast.LENGTH_LONG).show();
            }
        }
    };

    private final TaskCallBack departureCallBack = new TaskCallBack() {
        @Override
        public void onComplete(TaskResult result) {
            try {
                switch (result.getStatus()) {
                    case TaskResult.STS_OK:
                        DetailsActivity.this.tabDeparture.updateDeparture(result.getJsonObj());
                        break;

                    case TaskResult.STS_ERR:
                        Toast.makeText(DetailsActivity.this, result.getErrMsg(), Toast.LENGTH_LONG).show();
                        break;

                    case TaskResult.STS_UNKNOWN:
                    default:
                        Toast.makeText(DetailsActivity.this, "Status unknown!", Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception ex) {
                Toast.makeText(DetailsActivity.this, "Problem unexpected", Toast.LENGTH_LONG).show();
            }
        }
    };
}