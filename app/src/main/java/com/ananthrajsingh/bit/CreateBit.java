package com.ananthrajsingh.bit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.ananthrajsingh.bit.ChooseBitActivity.GOOD_BIT_ID;

public class CreateBit extends AppCompatActivity {

    private TextView tempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bit);
        Intent intent = getIntent();
        int bitId = intent.getIntExtra(getString(R.string.bit_id_extra), 0);

        tempTextView = (TextView) findViewById(R.id.textView_temporary);

        if (bitId == GOOD_BIT_ID){
            tempTextView.setText("You selected Good Bit");
        }
        else {
            tempTextView.setText("You selected Bad Bit");
        }

    }
}
