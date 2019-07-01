package com.rashed.md.securityeye;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class SmsShowActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textView;
    Bundle bundle;
    String message=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_show);
        toolbar=findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Incoming sms");

        textView=findViewById(R.id.smsShowTextViewId);

        bundle=getIntent().getExtras();
        if (bundle!=null){
            message=bundle.getString("message");
        }

        textView.setText(message);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
