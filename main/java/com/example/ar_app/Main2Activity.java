package com.example.ar_app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class Main2Activity extends AppCompatActivity implements LocationListener {
    private DatabaseReference mDatabase;
    SharedPreferences preferences;
    SQLiteDatabase database;
    LocationManager locationManager;
    static final int REQ_CODE = 654;
    static Random rand = new Random();
    static final String user="Guest"+rand.nextInt(30);
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


       // txt=findViewById(R.id.textView);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("ElephantX", "37.9793");
        editor.putString("ElephantY", "23.9087");
        editor.putString("PenguinX", "37.9802");
        editor.putString("PenguinY", "23.9094");
        editor.putString("RhinoX", "37.9802");
        editor.putString("RhinoY", "23.9073");

        editor.apply();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //writeNewUser("id2","chronis2","email2");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void OnStartClick(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(
                    this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQ_CODE);
            //Location_enable("Enable");
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location_enable("Enable");
        }
    }

    public void Location_enable(String str){
        if(str=="Enable"){
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                // text1.setText("Location is \n enabled");
            }
        }else if (str=="Disable"){
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                //text1.setText("Location is disabled \n for battery efficiency");
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
      //  txt.setText(String.valueOf(location.getLongitude())+","+String.valueOf(location.getLatitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public void Contact(View view){
        Intent intent2 =new Intent(getApplicationContext(), Contact.class);
        startActivity(intent2);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void Text_information(View view){
        Intent intent2 =new Intent(getApplicationContext(), Text_information.class);
        startActivity(intent2);
    }
    public class User {

        public String username;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

    }
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void Bluetooth(View view ){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(
                    this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQ_CODE);
        }else
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, this);
        myRef.setValue("Hello, World!");
        Intent intent =new Intent(this, Main3Activity.class);
        startActivity(intent);
    }
}
