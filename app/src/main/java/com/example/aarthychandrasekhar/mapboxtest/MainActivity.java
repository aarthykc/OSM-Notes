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

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    MapView myMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMapView = (MapView)findViewById(R.id.mapview);
        myMapView.setCenter(new LatLng(12.9667, 77.5667));
        myMapView.addMarker(new Marker("Tirumala", "A coool place", new LatLng(13.6795235, 79.3497522)));
        Marker marker = new Marker(myMapView,"bangalore", "sucks to be here", new LatLng(12.9667, 77.5667));
        myMapView.addMarker(marker);


        Button sendCoords = (Button) findViewById(R.id.send_coords);

        sendCoords.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                double n,s,e,w;
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
                URL url = new URL("http://www.openstreetmap.org/api/0.6/notes/feed?bbox="+w+","+s+","+e+","+n);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                result = conn.getContent().toString();

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
            else {

                Log.d("API_Response", s);
            }
        }
    }
}
