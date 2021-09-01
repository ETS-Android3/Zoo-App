package com.example.ar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_activity);
    }
   public void Chat(View view ){
       Intent intent2 =new Intent(getApplicationContext(), Information_chat.class);
       startActivity(intent2);
   }
}
