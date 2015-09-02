package com.example.aarthychandrasekhar.mapboxtest;

import java.util.List;

/**
 * Created by aarthychandrasekhar on 03/09/15.
 */
public interface NotesResultListener {
    void onFailure();
    void onSuccess(List<OSMNote> osmNotes);
}
