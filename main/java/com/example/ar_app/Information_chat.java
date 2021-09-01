package com.example.ar_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Information_chat extends AppCompatActivity {
    Map<String, Object> hash;
    Map<String, Object> hash2;
    FirebaseAuth mAuth;
    ArrayAdapter adpt;
    String username, code;
    EditText txt;
    Button bt;
    ArrayList<String> arr = new ArrayList<>();
    private DatabaseReference db;
    private DatabaseReference db2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information__chat);
        username="guest";
        mAuth = FirebaseAuth.getInstance();
        ListView lst = findViewById(R.id.listview);
        bt = findViewById(R.id.button);
        txt = findViewById(R.id.editText3);
        adpt = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, arr);
        lst.setAdapter(adpt);

        db = FirebaseDatabase.getInstance().getReference("Online_Chat_Fin999");

       // DatabaseReference db1 = db.child("username");
       //hell DatabaseReference db2 = db.child("message");


        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                method(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                method(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        SendMessage_Bot("Default");
    }


    public void method(DataSnapshot data) {

       String msg, user, message;
        String[] parts = data.getValue().toString().split(",");
        msg = parts[0]; // 004
        user = parts[1];


        user=user.substring(10);
        message= user.substring(0, user.length() - 1)+ ": "+msg.substring(9) ;
        if(message.contains("Elephant")&&(! data.getValue().toString().contains("Zoo expert")))SendMessage_Bot("Elephants");
        else if(message.contains("Penguin")&&(!data.getValue().toString().contains("Zoo expert")))SendMessage_Bot("Penguins");
        else if(message.contains("Rhino")&&(!user.equals("Zoo expert")))SendMessage_Bot("Rhino");
        adpt.insert( message, adpt.getCount());
        adpt.notifyDataSetChanged();
    }




    public void SendMessage_Bot (String x) {
        String msg="";
        String user="Zoo expert";
        if (x == "Penguins") {
            msg = "Penguins hang out in Zoo at 37.9802 23.9094";


        } else if (x == "Elephants") {
            msg = "Elephants hang out in Zoo at 37.9793 23.9087";

        } else if (x == "Rhino") {
             msg = "Penguins hang out in Zoo at 37.9802 23.9073";

        }else if(x=="Default") {
           // msg="Hello fellow animal_lover,\n how may i help you?";
            msg="How may i Help you";
        }
            hash = new HashMap<String, Object>();
            code = db.push().getKey();

            db.updateChildren(hash);
            db2 = db.child(code);
            hash2 = new HashMap<String, Object>();
            hash2.put("message", msg);
            hash2.put("username", user);
            db2.updateChildren(hash2);

            txt.setText("");


        }



    public void SendMessage(View view){
        hash = new HashMap<String, Object>();
        code = db.push().getKey();

        db.updateChildren(hash);
        db2 = db.child(code);
        hash2 = new HashMap<String, Object>();
        hash2.put("message", txt.getText().toString());
        hash2.put("username",Main2Activity.user);
        db2.updateChildren(hash2);

        txt.setText("");


    }
}



