package com.example.aarthychandrasekhar.mapboxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

public class MainActivity extends AppCompatActivity {

    MapView myMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMapView = (MapView)findViewById(R.id.mapview);
        myMapView.setCenter(new LatLng(12.9667, 77.5667));
        myMapView.addMarker(new Marker("Tirumala", "A coool place", new LatLng(13.6795235,79.3497522)));
        Marker marker = new Marker(myMapView,"bangalore", "sucks to be here", new LatLng(12.9667, 77.5667));
        myMapView.addMarker(marker);


        Button sendCoords = (Button) findViewById(R.id.send_coords);

        sendCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myMapView.getBoundingBox()==null) {
                    Log.d("myMapView", "Uhoh");
                }
                else {
                    Log.d("myMapView", "I have a value");
                }
            }
        });




    }
}