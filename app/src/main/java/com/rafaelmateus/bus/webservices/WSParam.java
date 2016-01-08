package com.rafaelmateus.bus.webservices;

import org.json.JSONObject;

public class WSParam {

    private String url = null;

    public static final String URL_FIND_ROUTES_BY_STOPNAME = "https://api.appglu.com/v1/queries/findRoutesByStopName/run";

    public static final String URL_FIND_STOPS_BY_ROUTEID = "https://api.appglu.com/v1/queries/findStopsByRouteId/run";

    public static final String URL_FIND_DEPARTURES_BY_ROUTEID = "https://api.appglu.com/v1/queries/findDeparturesByRouteId/run";

    private JSONObject body = new JSONObject();

    public WSParam(String url, String paramName, int paramValue) throws org.json.JSONException {
        {
            this.setUrl(url);
            this.setParam(paramName, paramValue);
        }
    }

    public WSParam(String url, String paramName, String paramValue) throws org.json.JSONException {
        {
            this.setUrl(url);
            this.setParam(paramName, paramValue);
        }
    }

    private void setParam(String name, int value) throws org.json.JSONException {
        this.body.put(name, value);
    }

    private void setParam(String name, String value) throws org.json.JSONException {
        this.body.put(name, value);
    }

    public void resetParams() {
        this.body = new JSONObject();
    }

    public byte[] buildBody() throws Exception {
        JSONObject result = new JSONObject();
        result.put("params", this.body);
        return result.toString().getBytes("UTF-8");
    }

    public String getUrl() {
        return this.url;
    }

    private void setUrl(String url) {
        this.url = url;
    }
}
