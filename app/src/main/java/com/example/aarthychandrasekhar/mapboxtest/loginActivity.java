package com.example.aarthychandrasekhar.mapboxtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by aarthychandrasekhar on 27/09/15.
 */

public class LoginActivity extends Activity {
    Button submit;
    EditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(submitListener);
    }
    View.OnClickListener submitListener;

    {
        submitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u, p;
                u = username.getText().toString();
                p = password.getText().toString();

            }
        };
    }
}
