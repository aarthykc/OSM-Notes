package com.example.aarthychandrasekhar.mapboxtest;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;

import java.util.List;

/**
 * Created by aarthychandrasekhar on 02/09/15.
 */
public class OSMNote extends Marker {
    String id;
    LatLng coordinates;
    String status;
    List<OSMComment> comments;

    public OSMNote(String id, LatLng coordinates, String status, List<OSMComment> comments) {
        super("Note", comments.get(0).getText(), coordinates);
        this.id = id;
        this.coordinates = coordinates;
        this.status = status;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OSMComment> getComments() {
        return comments;
    }

    public void setComments(List<OSMComment> comments) {
        this.comments = comments;
    }
}
