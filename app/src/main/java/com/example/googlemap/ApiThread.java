package com.example.googlemap;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiThread extends AsyncTask<Void, Void, String> {
    private double lat;
    private double lng;
    MapsActivity instance;
    public ApiThread(double lat,double lng, MapsActivity instance){
        this.lat=lat;
        this.lng=lng;
        this.instance = instance;
    }
    @Override
    protected String doInBackground(Void... voids) {

        try {
            URL url = new URL("https://api.sunrise-sunset.org/json?lat="+lat+"&"+"lng="+lng);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String data = bufferedReader.readLine();
            Log.i("loction",data);

            return data;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    protected void onPostExecute(String data){
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(data);
            jObject = jObject.getJSONObject("results");
            String sunrise = jObject.getString("sunrise");
            Log.i("logtest", "------>" + sunrise);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
