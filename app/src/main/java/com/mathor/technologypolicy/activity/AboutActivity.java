package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.mathor.technologypolicy.R;


public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
