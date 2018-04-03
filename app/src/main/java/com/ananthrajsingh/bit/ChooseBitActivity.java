package com.ananthrajsingh.bit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChooseBitActivity extends AppCompatActivity {
    //This will hold reference to Good Bit button
    private Button mGoodButton;
    //This will hold reference to Bad Bit button
    private Button mBadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bit);

        //Getting references from button
        mGoodButton = (Button) findViewById(R.id.good_bit_button);
        mBadButton =(Button) findViewById(R.id.bad_bit_button);

        mGoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 //         TODO 2 - send intent to activity which creates bit
            }
        });

        mBadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
 //         TODO 3 - send intent to activity which creates Bit
            }
        });
    }
}
