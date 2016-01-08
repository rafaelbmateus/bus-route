package com.rafaelmateus.bus.webservices;

import org.json.JSONObject;

public class TaskResult {

    private int status = TaskResult.STS_UNKNOWN;

    public static final int STS_UNKNOWN = 1;

    public static final int STS_OK = 2;

    public static final int STS_ERR = 3;

    private String errMsg = "";

    private JSONObject jsonObj = null;

    public int getStatus() {
        return this.status;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        this.status = TaskResult.STS_ERR;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setJsonObj(JSONObject jsonObj) {
        this.jsonObj = jsonObj;
        this.status = TaskResult.STS_OK;
    }

    public JSONObject getJsonObj() {
        return this.jsonObj;
    }
}
