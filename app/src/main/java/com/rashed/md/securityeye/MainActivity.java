package com.rashed.md.securityeye;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Button setPhoneNumberButton;
    ImageButton sendFindSmsButton,
            sendStatusSmsButton,
            sendAlertSmsButton,
            sendEasySmsButton,
            sendOnSmsButton,
            sendOffSmsButton,
            sendStartSmsButton,
            sendCarOffSmsButton,
            sendCarOnSmsButton;
//            lockCallButton,
//            unlockCallButton;
    Button voiceCommandButon,liveTrackButton;
    Intent intent;
    public static String phoneNumber = null;
    String findTextMessage = "6690000";
    String statusTextMessage = "RCONF";
    String alertTextMessage = "8880000";
    String easyTextMessage = "8890000";
    String onTextMessage = "9400000";
    String offTextMessage = "9410000";
    String startTextMessage = "Start";
    String carOffTextMessage = "9400000";
    String carOnTextMessage = "9410000";
    SharedPreferences sharedPreferences, phoneNumberValueSharedPreference;
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver sentBroadcastReceiver, deliveredBroadcastReceiver;
    TextView showPhoneNumberTextView;
    Button contactCall, contactFacebook;
    String lastButtonId;
    TextToSpeech textToSpeech;
    boolean voiceSendSms=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        LayoutInflater inflater=getLayoutInflater();
        View view= (View) inflater.inflate(R.layout.appbar_custom_view,null);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        setPhoneNumberButton = findViewById(R.id.deviceNumberButtonId);
        sendFindSmsButton = findViewById(R.id.findSmsButtonId);
        sendStatusSmsButton = findViewById(R.id.statusSmsButtonId);
        sendAlertSmsButton = findViewById(R.id.alertSmsButtonId);
        sendEasySmsButton = findViewById(R.id.easySmsButtonId);
        sendOnSmsButton = findViewById(R.id.onSmsButtonId);
        sendOffSmsButton = findViewById(R.id.offSmsButtonId);
        sendStartSmsButton = findViewById(R.id.startSmsButtonId);
        sendCarOffSmsButton = findViewById(R.id.carOffSmsButtonId);
        sendCarOnSmsButton = findViewById(R.id.carOnSmsButtonId);
//        lockCallButton = findViewById(R.id.lockCallButtonId);
//        unlockCallButton = findViewById(R.id.unLockCallButtonId);
        showPhoneNumberTextView = findViewById(R.id.showPhoneNumberTextViewId);
        contactCall = findViewById(R.id.contactUsButtonId);
        contactFacebook = findViewById(R.id.facebookButtonId);
        voiceCommandButon=findViewById(R.id.voiceCommandButtonId);
        liveTrackButton=findViewById(R.id.openWebsiteButtonId);


        setPhoneNumberButton.setOnClickListener(this);
        sendFindSmsButton.setOnClickListener(this);
        sendStatusSmsButton.setOnClickListener(this);
        sendAlertSmsButton.setOnClickListener(this);
        sendEasySmsButton.setOnClickListener(this);
        sendOnSmsButton.setOnClickListener(this);
        sendOffSmsButton.setOnClickListener(this);
        sendStartSmsButton.setOnClickListener(this);
        sendCarOffSmsButton.setOnClickListener(this);
        sendCarOnSmsButton.setOnClickListener(this);
//        lockCallButton.setOnClickListener(this);
//        unlockCallButton.setOnClickListener(this);
        voiceCommandButon.setOnClickListener(this);
        contactCall.setOnClickListener(this);
        contactFacebook.setOnClickListener(this);
        liveTrackButton.setOnClickListener(this);


        int PERMISSION_ALL = 1;
        String[] permissions = {Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.INTERNET,
                Manifest.permission.VIBRATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if (!hasPermission(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
        }


        sharedPreferences = getSharedPreferences("phone", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", null);
        if (phone != null) {
            phoneNumber = phone;
            String subNumb=phoneNumber.substring(phoneNumber.length()-2,phoneNumber.length());
            showPhoneNumberTextView.setText("Last Two Digit Of Number:- "+subNumb);
        }



        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    } else {
                        textToSpeech.setSpeechRate(0.7f);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Initialization Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateLastButtonStatus();
//-----------------------onCreate method end-------------------------------
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.deviceNumberButtonId:
                vibrateCreation();
                if (Build.VERSION.SDK_INT > 22) {
                    String[] permissions = {
                            Manifest.permission.SEND_SMS
                    };
                    if (!hasPermission(this, permissions)) {
                        Toast.makeText(this, "You need to provide some permission to use the app. To grant the permission please close the app and reopen to see the permissions dialog.Thank you", Toast.LENGTH_SHORT).show();
                    }else {
                        intent = new Intent(MainActivity.this, InputPhoneNumberActivity.class);
                        startActivityForResult(intent, 100);
                    }
                } else {
                    intent = new Intent(MainActivity.this, InputPhoneNumberActivity.class);
                    startActivityForResult(intent, 100);
                }
                break;

            case R.id.findSmsButtonId:
                sendSms(findTextMessage);
                break;

            case R.id.statusSmsButtonId:
                sendSms(statusTextMessage);
                break;


            case R.id.alertSmsButtonId:
                sendSms(easyTextMessage);
                break;

            case R.id.easySmsButtonId:
                sendSms(easyTextMessage);
                break;

            case R.id.onSmsButtonId:
                sendSms(onTextMessage);
                break;

            case R.id.offSmsButtonId:
                sendSms(offTextMessage);
                break;

            case R.id.startSmsButtonId:
                sendSms(startTextMessage);
                break;

            case R.id.carOffSmsButtonId:
                sendSms(carOffTextMessage);
                break;

            case R.id.carOnSmsButtonId:
                sendSms(carOnTextMessage);
                break;

//            case R.id.lockCallButtonId:
//                makeCall(phoneNumber,",*,*,*,*");
//                break;
//
//            case R.id.unLockCallButtonId:
//                makeCall(phoneNumber,",%23,%23,%23,%23");
//                break;

            case R.id.contactUsButtonId:
                makeCall("01718171529",null);
                break;

            case R.id.facebookButtonId:
                openUrl("https://www.facebook.com/naogaon.tinyscientist");
                break;

            case R.id.openWebsiteButtonId:
                openUrl("http://www.sinotracking.com/app/Login.html");
                break;

            case R.id.voiceCommandButtonId:
                vibrateCreation();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    voiceSendSms = true;
                    startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(MainActivity.this, "Your device don't support voice command", Toast.LENGTH_SHORT).show();
                }
                break;
        }
//----------------------onClick method end---------------------------------
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK && requestCode == 100) {
            String phoneNumberData = data.getStringExtra("phoneNumber");
            sharedPreferences = getSharedPreferences("phone", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("phone", phoneNumberData);
            editor.apply();
            phoneNumber = phoneNumberData;
            String subNumb=phoneNumberData.substring(phoneNumberData.length()-2,phoneNumberData.length());
            showPhoneNumberTextView.setText("Last Two Digit Of Number:- "+subNumb);
            Toast.makeText(this, phoneNumberData + " Successfully set as your device phone number", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 100 && resultCode != RESULT_OK) {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }




        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = null;
                if (data != null) {
                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                } else {
                    return;
                }
                for (int i = 0; i < result.size(); i++) {
                    String value = result.get(i).toLowerCase();
                    if (value.contains("location")) {
                        voiceCommand(findTextMessage,"ok, trying to retrieve your bike location");
                    }

                    else if (value.contains("status")) {
                        voiceCommand(statusTextMessage,"ok, trying to retrieve your bike status");
                    }

                    else if (value.contains("sensor of") || value.contains("sensor off")) {
                        voiceCommand(easyTextMessage,"ok, trying to turn off your bike sensor");
                    }

                    else if (value.contains("sensor on") || value.contains("sensor 1")) {
                        voiceCommand(alertTextMessage,"ok, trying to turn on your bike sensor");
                    }

                    else if (value.contains("bike on") || value.contains("mike on")) {
                        voiceCommand(onTextMessage,"ok, trying to turn on your bike");
                    }

                    else if (value.contains("bike off") || value.contains("bike of") || value.contains("mike off") || value.contains("mike of")) {
                        voiceCommand(offTextMessage,"ok, trying to turn off your bike");
                    }

                    else if (value.contains("bike start")) {
                        voiceCommand(startTextMessage,"ok, trying to start your bike");
                    }


                    else if (value.contains("car unlock")) {
                        voiceCommand(carOnTextMessage,"ok, trying to unlock your car");
                    }


                    else if (value.contains("car lock")) {
                        voiceCommand(carOffTextMessage,"ok, trying to lock your car");
                    }
                    else {
                        voiceCommand(null,"your command is incorrect, please try again");
                    }
                }
            } else {
                Toast.makeText(this, "Not Detected", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void voiceCommand(String smsCommand, String voiceCommand){
        if (smsCommand!=null && !smsCommand.isEmpty()){
            if (voiceSendSms == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(voiceCommand, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    textToSpeech.speak(voiceCommand, TextToSpeech.QUEUE_FLUSH, null);
                }
                try {
                    if (!TextUtils.isEmpty(phoneNumber)){
                        double num = Double.parseDouble(phoneNumber);
                    }else {
                        Toast.makeText(this, "Please set your phone number", Toast.LENGTH_SHORT).show();
                    }
                    sendSms(smsCommand);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Phone number not valid", Toast.LENGTH_SHORT).show();
                }
                voiceSendSms = false;
            }
        }else {
            if (voiceSendSms == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(voiceCommand, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    textToSpeech.speak(voiceCommand, TextToSpeech.QUEUE_FLUSH, null);
                }
                voiceSendSms = false;
            }
        }

    }


    public void makeCall(String number, String symbole){
        vibrateCreation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak("lock", TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak("lock", TextToSpeech.QUEUE_FLUSH, null);
        }
        lastButtonId = "lockCallButtonId";
        try {
            storeLastButtonId();
            updateLastButtonStatus();
            if (phoneNumber != null) {
                double num = Double.parseDouble(phoneNumber);
            } else {
                Toast.makeText(this, "Please set your phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > 22) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need to provide some permission to use the app. To grant the permission please close the app and reopen to see the permissions dialog.Thank you", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    callHandler(phoneNumber,symbole);
                }
            } else {
                callHandler(phoneNumber,symbole);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please set your phone number", Toast.LENGTH_SHORT).show();
        }

    }
    private void callHandler(String number,String symbole){
        if (symbole!=null && !symbole.isEmpty()){
            String callNumber = number + symbole;
            Uri uri = Uri.parse("tel:" + callNumber);
            intent = new Intent(Intent.ACTION_CALL, uri);
            startActivity(intent);
        }else {
            Uri uri = Uri.parse("tel:" + number);
            intent = new Intent(Intent.ACTION_CALL, uri);
            startActivity(intent);
        }
    }



    public void sendSms(String smsCommand){
        vibrateCreation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak("location", TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak("location", TextToSpeech.QUEUE_FLUSH, null);
        }
        lastButtonId = "findSmsButtonId";
        try {
            storeLastButtonId();
            updateLastButtonStatus();
            if (phoneNumber != null) {
                double num = Double.parseDouble(phoneNumber);
            } else {
                Toast.makeText(this, "Please set your phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > 22) {
                String[] permissions = {
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.VIBRATE
                };
                if (!hasPermission(this, permissions)) {
                    Toast.makeText(this, "You need to provide some permission to use the app. To grant the permission please close the app and reopen to see the permissions dialog.Thank you", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    sendMessageHandle(smsCommand);
                }
            } else {
                sendMessageHandle(smsCommand);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please set your phone number", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessageHandle(String command){
        try {
            messageHandleMethod();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, command, sentPI, deliveredPI);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }


    private void openUrl(String url){
        vibrateCreation();
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need to provide some permission to use the app. To grant the permission please close the app and reopen to see the permissions dialog.Thank you", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    openUrlHandler("https://www.facebook.com/naogaon.tinyscientist");
                }
            } else {
                openUrlHandler("https://www.facebook.com/naogaon.tinyscientist");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void openUrlHandler(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        Intent chooser=Intent.createChooser(i,"Choose Browser");
        startActivity(chooser);
    }

    public void updateLastButtonStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("LastButton", MODE_PRIVATE);
        String lastButton = sharedPreferences.getString("lastButton", null);
        if (lastButton != null) {
            int resID = getResources().getIdentifier(lastButton, "id", getPackageName());
            ImageButton button = ((ImageButton) findViewById(resID));
            sendFindSmsButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            sendStatusSmsButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            sendAlertSmsButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            sendEasySmsButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            sendOnSmsButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            sendOffSmsButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            sendStartSmsButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            sendCarOffSmsButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            sendCarOnSmsButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            lockCallButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            unlockCallButton.setBackgroundResource(R.drawable.green_and_white_border_button);
            button.setBackgroundResource(R.drawable.red_and_black_border_button);

        }
    }

    public void storeLastButtonId(){
        if (lastButtonId != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("LastButton", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lastButton", lastButtonId);
            editor.apply();
        }
    }


    public void messageHandleMethod() {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        sentBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Failed to send",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Failed to send",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Failed to send",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Failed to send",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        registerReceiver(sentBroadcastReceiver, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        deliveredBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        registerReceiver(deliveredBroadcastReceiver, new IntentFilter(DELIVERED));
    }


    public void vibrateCreation() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }


    public static boolean hasPermission(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Back Button Pressed");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_warning);
        builder.setMessage("Do you want to exit?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (sentBroadcastReceiver != null) {
            unregisterReceiver(sentBroadcastReceiver);
        }
        if (deliveredBroadcastReceiver != null) {
            unregisterReceiver(deliveredBroadcastReceiver);
        }
        sentPI = null;
        deliveredPI = null;
        super.onDestroy();
    }
}

