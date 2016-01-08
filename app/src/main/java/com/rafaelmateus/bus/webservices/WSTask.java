package com.rafaelmateus.bus.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import org.json.JSONObject;
import java.io.IOException;
import java.net.SocketException;

public class WSTask extends AsyncTask<WSParam, Integer, TaskResult> {
    private final ProgressDialog dialog;

    private final TaskCallBack callBack;

    public WSTask(Activity activity, TaskCallBack callBack) {
        this.dialog = new ProgressDialog(activity);
        this.callBack = callBack;
    }

    @Override
    protected TaskResult doInBackground(WSParam... wsParams) {
        return this.consumeWS(wsParams[0]);
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }

        if (this.callBack != null) {
            this.callBack.onComplete(result);
        }
    }

    private TaskResult consumeWS(WSParam wsParam) {
        try {
            java.net.URL url = new java.net.URL(wsParam.getUrl());
            javax.net.ssl.HttpsURLConnection connection = (javax.net.ssl.HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString("WKD4N7YMA1uiM8V:DtdTtzMLQlA0hk2C1Yi5pLyVIlAQ68".getBytes(), Base64.NO_WRAP));
            connection.setRequestProperty("X-AppGlu-Environment", "staging");
            connection.connect();

            byte[] outputInBytes = wsParam.buildBody();
            java.io.OutputStream os = connection.getOutputStream();
            os.write(outputInBytes);
            os.flush();
            os.close();

            java.io.BufferedReader bfReader;
            try {
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    TaskResult result = new TaskResult();
                    result.setErrMsg("Sorry. The Web Service did not respond well");
                    return result;
                }

                bfReader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            } catch (SocketException ex) {
                TaskResult result = new TaskResult();
                result.setErrMsg("Problem unknown at the Web Service.");
                return result;
            } catch (IOException ex) {
                TaskResult result = new TaskResult();
                result.setErrMsg("Problem unknown at the Web Service.");
                return result;
            } catch (Exception ex) {
                TaskResult result = new TaskResult();
                result.setErrMsg("Problem unknown at the Web Service.");
                return result;
            }

            JSONObject response;

            try {
                String bfrLine;
                StringBuilder responseJSON = new StringBuilder();
                while ((bfrLine = bfReader.readLine()) != null) {
                    responseJSON.append(bfrLine);
                }

                connection.disconnect();

                response = new JSONObject(responseJSON.toString());
            } catch (OutOfMemoryError ex) {
                TaskResult result = new TaskResult();
                result.setErrMsg("Problem unexpected 1.");
                return result;
            }

            TaskResult result = new TaskResult();
            result.setJsonObj(response);
            return result;
        } catch (Exception ex) {
            TaskResult result = new TaskResult();
            result.setErrMsg("Problem unexpected 2.");
            return result;
        }
    }
}
