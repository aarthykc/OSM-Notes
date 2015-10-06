package com.example.aarthychandrasekhar.mapboxtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aarthychandrasekhar on 02/09/15.
 */
public class Controller {
    public Controller() {
    }
    public static void getNotesFromBoundingBox(BoundingBox boundingBox, NotesResultListener notesResultListener) {
        new fetchNotesTask(notesResultListener).execute(boundingBox);
    }

    public static void resolveNote(OSMNote osmNote, ResolveNoteListener resolveNoteListener) {
        new resolveNoteTask(resolveNoteListener).execute(osmNote);

    }
    public static void addComment(OSMNote osmNote, ResolveNoteListener resolveNoteListener){
        new addCommentsTask().execute(osmNote);

    }

    public static void saveCredentials(Credentials credentials, Context c){
        SharedPreferences sharedData = PreferenceManager.getDefaultSharedPreferences(c);
        sharedData.edit().putString("username", credentials.getUsername());

    }

    public static Credentials getCredentials(Context c){
        return null;
    }

    private static class resolveNoteTask extends AsyncTask<OSMNote, Void, String> {
        ResolveNoteListener resolveNoteListener;

        public resolveNoteTask(ResolveNoteListener resolveNoteListener) {
            this.resolveNoteListener = resolveNoteListener;
        }

        @Override
        protected String doInBackground(OSMNote... params) {
            String result = null;
            try {

                HttpPost httpPost = new HttpPost("http://api.openstreetmap.org/api/0.6/notes/427012/close.json");

                // Encode username:password to base64 and add as a header
                String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                        ("username:password").getBytes(),
                        Base64.NO_WRAP);
                httpPost.setHeader("Authorization", base64EncodedCredentials);


                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpPost);
                int status = response.getStatusLine().getStatusCode();

                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);




            } catch (Exception e1) {
                e1.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Log.d("API_Reponse", "uh oh");
                resolveNoteListener.onFailure();
            } else {
                Log.d("API_RESPONSE", s);
                resolveNoteListener.onSuccess();
            }
        }
    }

    private static class addCommentsTask extends AsyncTask<OSMNote,Void, String>{
        @Override
        protected String doInBackground(OSMNote... params) {
            return null;
        }
    }

    private static class fetchNotesTask extends AsyncTask<BoundingBox, Void, String> {

        NotesResultListener notesResultListener;

        public fetchNotesTask(NotesResultListener notesResultListener) {
            this.notesResultListener = notesResultListener;
        }

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
                notesResultListener.onFailure();
                Log.d("API_Reponse", "uh oh");
            }
            else {
                Log.d("API_RESPONSE", s);

                try {

                    JSONObject object = new JSONObject(s);
                    JSONArray features = object.getJSONArray("features");
                    List<OSMNote> osmNotes = new ArrayList<>();
                    for(int i =0; i<features.length(); i++){
                        List<OSMComment> osmComments = new ArrayList<>();
                        JSONObject note = new JSONObject(features.getString(i));

                        Log.d("Note", note.getJSONObject("properties").getJSONArray("comments").getJSONObject(0).getString("text"));
                        double lon = note.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0);
                        double lat = note.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1);
                        String id = String.valueOf(note.getJSONObject("properties").getInt("id"));
                        String status= note.getJSONObject("properties").getString("status");

                        JSONArray commentsList = note.getJSONObject("properties").getJSONArray("comments");
                        for (int j=0; j<commentsList.length(); j++) {

                            String text = commentsList.getJSONObject(j).getString("text");
                            String user = commentsList.getJSONObject(j).optString("user", "anonymous");
                            osmComments.add(new OSMComment(user,text));
                        }

                        osmNotes.add(new OSMNote(id, new LatLng(lat, lon), status, osmComments));
                    }
                    notesResultListener.onSuccess(osmNotes);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    notesResultListener.onFailure();
                }
            }
        }
    }


}
