package com.ananthrajsingh.bit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.ananthrajsingh.bit.MainActivity.BAD_BIT_ID;
import static com.ananthrajsingh.bit.MainActivity.GOOD_BIT_ID;

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
 //         COMPLETED 2 - send intent to activity which creates bit
                Intent intent = new Intent(getBaseContext(), CreateBit.class);
                intent.putExtra(getString(R.string.bit_id_extra), GOOD_BIT_ID);
                startActivity(intent);
            }
        });

        mBadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
 //         COMPLETED 3 - send intent to activity which creates Bit
                Intent intent = new Intent(getBaseContext(), CreateBit.class);
                intent.putExtra(getString(R.string.bit_id_extra), BAD_BIT_ID);
                startActivity(intent);
            }
        });
    }
}
