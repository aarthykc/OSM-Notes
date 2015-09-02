package com.example.aarthychandrasekhar.mapboxtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MapView myMapView;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMapView = (MapView)findViewById(R.id.mapview);
        myMapView.setCenter(new LatLng(12.9667, 77.5667));

        Button sendCoords = (Button) findViewById(R.id.send_coords);

        sendCoords.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                if(myMapView.getBoundingBox()==null) {
                    Log.d("coordinates", "Not available");
                }
                else {
                    Log.d("Coordinates",myMapView.getBoundingBox().toString());
                    new fetchNotes().execute(myMapView.getBoundingBox());

                }
            }
        });

    }

    private class fetchNotes extends AsyncTask<BoundingBox, Void, String> {

        @Override
        protected String doInBackground(BoundingBox... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String result = null;

            BoundingBox bbox = params[0];
            String n = String.valueOf(bbox.getLatNorth());
            String s = String.valueOf(bbox.getLatSouth());
            String e = String.valueOf(bbox.getLonEast());
            String w = String.valueOf(bbox.getLonWest());

            // smaller_longitude,smaller_latitude,larger_longitude,larger_latitude
            try {

                HttpGet httpget= new HttpGet("http://api.openstreetmap.org/api/0.6/notes.json?bbox="+w+","+s+","+e+","+n);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpget);
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }



            } catch (Exception e1) {
                e1.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null) {
                //uh oh
                Log.d("API_Reponse", "uh oh");
            }
            else { Log.d("API_RESPONSE", s);


                try {

                    JSONObject object = new JSONObject(s);
                    JSONArray features = object.getJSONArray("features");

                    for(int i =0; i<features.length(); i++){
                        JSONObject note = new JSONObject(features.getString(i));
                        Log.d("Note", note.getJSONObject("properties").getJSONArray("comments").getJSONObject(0).getString("text"));
                        double lon = note.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0);
                        double lat = note.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1);
                        myMapView.addMarker(new Marker("Note", note.getJSONObject("properties").getJSONArray("comments").getJSONObject(0).getString("text"), new LatLng(lat,lon)));

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                }
            }
        }

    }

