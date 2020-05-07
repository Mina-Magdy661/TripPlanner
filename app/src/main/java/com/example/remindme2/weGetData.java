package com.example.remindme2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.remindme2.R;

public class weGetData extends AppCompatActivity {


    Button okBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_get_data);

        okBtn=findViewById(R.id.okBtn);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i =new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("FROMGETDATA",1);
                startActivity(i);

                finish();
            }
        });
    }
}
