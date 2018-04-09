package com.ananthrajsingh.bit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CreateBit extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bit);
        Intent intent = getIntent();

        /*bitId represents whether user wants to insert a bad bit or good bit */
        int bitId = intent.getIntExtra(getString(R.string.bit_id_extra), 0);


    }
}
