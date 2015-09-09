package com.example.aarthychandrasekhar.mapboxtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;
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

        myMapView.setMapViewListener(new MapViewListener() {
            @Override
            public void onShowMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onHideMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onTapMarker(MapView mapView, Marker marker) {

                if(marker instanceof OSMNote) {
                    Controller.resolveNote((OSMNote) marker, new ResolveNoteListener() {
                        @Override
                        public void onFailure() {

                        }

                        @Override
                        public void onSuccess() {

                        }
                    });

                }

            }

            @Override
            public void onLongPressMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onTapMap(MapView mapView, ILatLng iLatLng) {

            }

            @Override
            public void onLongPressMap(MapView mapView, ILatLng iLatLng) {

            }
        });

        sendCoords.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (myMapView.getBoundingBox() == null) {
                    Log.d("coordinates", "Not available");
                } else {
                    Log.d("Coordinates", myMapView.getBoundingBox().toString());
                    Controller.getNotesFromBoundingBox(myMapView.getBoundingBox(), new NotesResultListener() {
                        @Override
                        public void onFailure() {
                            //uh-oh
                        }

                        @Override
                        public void onSuccess(List<OSMNote> osmNotes) {
                            for (OSMNote note : osmNotes) {
                                myMapView.addMarker(note);
                            }

                        }
                    });

                }
            }
        });

    }

    }

