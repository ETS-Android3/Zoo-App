package com.example.ar_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.transform.Result;

import static com.example.ar_app.Main2Activity.MY_PREFS_NAME;
import static com.example.ar_app.Main2Activity.user;

public class Main3Activity extends AppCompatActivity implements LocationListener {
    boolean elephant,penguin,rhino;
    SQLiteDatabase database;
    android.widget.ArrayAdapter<String> adapter;
    SharedPreferences pr;
    String StartX,StartY,LastX,LastY;
    Button b1,b3,b5;
    static final int REQ_CODE = 654;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private TextView text;
    private ArrayList<String> arrayList;
    private Set<String>mArrayAdapter;
    private Set<BluetoothDevice>arrayOfFoundBTDevices;
    private BluetoothAdapter mBluetoothAdapter;
    ListView lv;
    String something;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
       // text=findViewById(R.id.Cords);


         pr = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        database = openOrCreateDatabase("User_routes_f.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS Routes(User TEXT,Animal TEXT,Timestamp TEXT)");
        b1 =  findViewById(R.id.button13);
        //b2=findViewById(R.id.button2);
        b3=findViewById(R.id.button3);

       // b4=findViewById(R.id.button4);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        BA = BluetoothAdapter.getDefaultAdapter();
        //lv = findViewById(R.id.listView);
    }

    public void on(View v){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BA.cancelDiscovery();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Location_enable("Enable");
    }

    public void Scan(View v){
        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        if(BA!=null){
            if(!BA.isEnabled())startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
        }
        BA.startDiscovery();
        final BroadcastReceiver mReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                ArrayList list = new ArrayList();

                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    Log.i("Device Name: " , "device " + device.getName());
                    if(device.getName().equals("AirPods")){
                        list.add(device.getName());
                         ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, list);
//                        lv.setAdapter(adapter);
                        Intent intent2 =new Intent(getApplicationContext(), MainActivity.class);
                        intent2.putExtra("Poly","Mesh_Penguin.sfb");
                       startActivity(intent2);
                    }else if(device.getName().equals("Rhino")){
                        list.add(device.getName());
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, list);
                        lv.setAdapter(adapter);
                        Intent intent2 =new Intent(getApplicationContext(), MainActivity.class);
                        intent2.putExtra("Poly","model_Rhino_20171206_154717133.sfb");
                        startActivity(intent2);
                    }
                    // Add the name and address to an array adapter to show in a ListView
                    //  mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    something+=device.getName();

                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        System.out.println("Device:"+something);
        // BA.disable();

    }


    public void Cord_test(View view){
        penguin=true;
        rhino=true;
        elephant=true;
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
    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }


    public void list(View v){
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
    }


   public void OnStoppClick(View view) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location_enable("Disable");
            startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));

        } String str= Results();
       new AlertDialog.Builder(this)
               .setTitle("User route")
               .setMessage(str)
               .setCancelable(false)
               .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       // Whatever...
                   }
               }).show();
    }
    public String Results(){
        String information="";
        Cursor cursor = database.rawQuery("Select User,Animal,Timestamp  from Routes where User='"+user+"' ;",null);
        if (cursor.getCount()==0) {
            Toast.makeText(this,"No records found!",Toast.LENGTH_LONG).show();
        }else {
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()){
                buffer.append(cursor.getString(0));
                buffer.append(",  ");
                buffer.append(cursor.getString(1));
                buffer.append(",   ");
                buffer.append(cursor.getString(2));
                buffer.append(",  ");
                buffer.append("\n");
            }
           // arrayList.add(buffer.toString());
            information+=buffer.toString()+System.lineSeparator();
        }

        //adapter = new android.widget.ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
        //lv.setAdapter(adapter);
        return information;
    }

    @Override
    public void onLocationChanged(Location location) {
//        text.setText(location.getLatitude()+","+location.getLongitude());
        String lang=String.valueOf(location.getLatitude());
        String longt=String.valueOf(location.getLongitude());
        //TextView textt=findViewById(R.id.cords);
        //textt.setText(pr.getString("ElephantX","default")+","+lang.substring(0, Math.min(lang.length(), 6))+"|"+pr.getString("ElephantY","defaultt")+","+longt.substring(0, Math.min(lang.length(),6)));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        java.util.Date time =Calendar.getInstance().getTime();

        //Toast.makeText(this,lang.substring(0, Math.min(lang.length(), 8))+","+longt.substring(0, Math.min(longt.length(), 8)),Toast.LENGTH_LONG).show();
        //Toast.makeText(this,pr.getString("ElephantX","default")+","+longt.substring(0, Math.min(lang.length(), 8))+"|"+pr.getString("ElephantY","defaultt")+","+lang.substring(0, Math.min(lang.length(), 8)),Toast.LENGTH_LONG).show();
        String timestamp= sdf.format(time);
        if(lang.substring(0, Math.min(lang.length(), 7)).equals(pr.getString("ElephantX","default"))&&longt.substring(0, Math.min(longt.length(),7)).equals(pr.getString("ElephantY","defaultt"))&&elephant==true){
            elephant=false;
            database.execSQL("INSERT INTO Routes VALUES('"+user+"','Elephant','"+timestamp+"');");
            Toast.makeText(this,"Elephants!",Toast.LENGTH_LONG).show();
        }else if(lang.substring(0, Math.min(lang.length(), 7)).equals(pr.getString("PenguinX","default1"))&&longt.substring(0, Math.min(longt.length(), 7)).equals(pr.getString("PenguinY","defaultt1"))&&rhino==true){
            penguin=false;
            database.execSQL("INSERT INTO Routes VALUES('"+user+"','Penguin','"+timestamp+"');");
            Toast.makeText(this,"Penguins!",Toast.LENGTH_LONG).show();
        }else if(lang.substring(0, Math.min(lang.length(), 7)).equals(pr.getString("RhinoX","default1"))&&longt.substring(0, Math.min(longt.length(), 7)).equals(pr.getString("RhinoY","defaultt1"))&&rhino==true) {
            rhino = false;
            database.execSQL("INSERT INTO Routes VALUES('" + user + "','Rhinos','" + timestamp + "');");
            Toast.makeText(this, "Rhinos!", Toast.LENGTH_LONG).show();
        }

        if(lang.substring(0, Math.min(lang.length(), 6)).equals("37.979")&&longt.substring(0, Math.min(lang.length(), 6)).equals("23.908")){
          //   Toast.makeText(this,"Hey2",Toast.LENGTH_LONG).show();
        }




    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
