package com.margsapp.waterorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.margsapp.waterorder.Model.Users;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class Personal_InfoActivity extends AppCompatActivity {

    AppCompatButton save;



    Dialog username_dialog;


    CardView username_box,dob_box,address_box;

    TextView username,dob,address;


    final Calendar myCalendar= Calendar.getInstance();

    private ResultReceiver resultReceiver;

    MaterialProgressBar address_loader;

    private FirebaseFirestore firestore;

    private RadioGroup gender;

    String username_ = null,gender_ = null,Address_ = null;
    //Date dob_;
    Geocoder latitude,longitude;

    FirebaseUser firebaseUser;


    String phoneNo;


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        gender = findViewById(R.id.gender);

        save = findViewById(R.id.save);



        phoneNo = getIntent().getStringExtra("phoneNo");

        username_box = findViewById(R.id.name_card);
        username = findViewById(R.id.username);

        /*
        dob_box = findViewById(R.id.dob_card);
        dob = findViewById(R.id.dob);

         */
        username_dialog = new Dialog(Personal_InfoActivity.this);
        address_box = findViewById(R.id.address_card);
        address = findViewById(R.id.address);
        address_loader = findViewById(R.id.address_loader);

        resultReceiver = new AddressResultReciever(new Handler());



        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                gender_ = radioButton.getText().toString();

            }
        });



        username_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptions();
            }
        });

        /*

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };



        dob_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Personal_InfoActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

         */




        address_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            Personal_InfoActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_LOCATION_PERMISSION
                    );
                }else {
                    getCurrentLocation();
                    address_loader.setVisibility(View.VISIBLE);
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectedId = gender.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(Personal_InfoActivity.this,
                                    "No gender has been selected",
                                    Toast.LENGTH_SHORT)
                            .show();

                }if(TextUtils.isEmpty(username_)){
                    Toast.makeText(getApplicationContext(),"Please enter username",Toast.LENGTH_SHORT).show();
                }if(TextUtils.isEmpty(Address_)){
                    Toast.makeText(getApplicationContext(),"Please enter Address",Toast.LENGTH_SHORT).show();
                } if(selectedId != -1 && !TextUtils.isEmpty(username_)  && !TextUtils.isEmpty(Address_)) {
                    
                    addDataToFirestore(gender_,username_,phoneNo,Address_);

                }


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }else {
                showSettingsAlert();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(Personal_InfoActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(Personal_InfoActivity.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            int LatestLocationIndex = locationResult.getLocations().size() -1;

                            double latitude = locationResult.getLocations().get(LatestLocationIndex).getLatitude();
                            double logitude = locationResult.getLocations().get(LatestLocationIndex).getLongitude();

                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(logitude);
                            fetchAddressFromLatLong(location);




                        }
                    }
                }, Looper.getMainLooper());

    }

    private void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent(this,FetchAddress.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }


    class AddressResultReciever extends ResultReceiver{
        public AddressResultReciever(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if (resultCode == Constants.SUCCESS_RESULT){
                address.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                Address_ = resultData.getString(Constants.RESULT_DATA_KEY);
                address_loader.setVisibility(View.INVISIBLE);
            }else {

            }
        }
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Personal_InfoActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Personal_InfoActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    public void onOptions() {



        username_dialog.setContentView(R.layout.full_name_box);

        AppCompatButton Save = username_dialog.findViewById(R.id.save_btn);
        AppCompatEditText username_txt = username_dialog.findViewById(R.id.username);


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setText(username_txt.getText());
                username_ = Objects.requireNonNull(username_txt.getText()).toString();
                username_dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = username_dialog.getWindow();
        username_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        username_dialog.getWindow().setGravity(Gravity.BOTTOM);
        username_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        username_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        username_dialog.show();
    }




    private void addDataToFirestore( String gender_,String username_, String PhoneNumber, String Address_) {

        // creating a collection reference
        // for our Firebase Firetore database.
        CollectionReference dbUsers = firestore.collection("Users");

        // adding our data to our courses object class.
        Users user = new Users(gender_, username_, PhoneNumber, Address_);

        // below method is use to add data to Firebase Firestore.
        dbUsers.document(firebaseUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Personal_InfoActivity.this, "Data has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Personal_InfoActivity.this,MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(Personal_InfoActivity.this, "Fail to add data \n" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }


}