package com.ananthrajsingh.bit;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static com.ananthrajsingh.bit.MainActivity.BAD_BIT_ID;
import static com.ananthrajsingh.bit.MainActivity.GOOD_BIT_ID;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToMainTable;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.makeContentValuesToInsert;

public class CreateBit extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextMaxCount;
    /*declaring here so we can cancel if previous toast is not null*/
    public Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bit);


        /*Extracting information that the user entered and hit FAB*/

        FloatingActionButton fabInsertHabit = (FloatingActionButton) findViewById(R.id.fabAddBit);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final RadioButton radioButtonGood = (RadioButton) findViewById(R.id.goodRadio);
        RadioButton radioButtonBad = (RadioButton) findViewById(R.id.badRadio);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextMaxCount = (EditText) findViewById(R.id.editTextFrequency);



        /* When fab is clicked, this must happen */
        fabInsertHabit.setOnClickListener(new View.OnClickListener() {
            /* We will get the information in EditText fields and pass to database*/
            @Override
            public void onClick(View v) {
                int bitIdRadio;
                /*
                 * Check if user hasn't left any field empty. That exception will be caught.
                 */
                try {
                     /* Checking which radio button is checked */
                    if (radioButtonGood.isChecked()){
                        bitIdRadio = GOOD_BIT_ID;
                    }
                    else{
                        bitIdRadio = BAD_BIT_ID;
                    }
                    String nameOfHabit = editTextName.getText().toString();
                    /*
                     * This below code will be useful if name was left open and frequency was given.
                     * Without this, habit was created with no name.
                     */
                    if (nameOfHabit.equals("")){
                        throw new IllegalArgumentException();
                    }
                    String maximumFrequencyString = editTextMaxCount.getText().toString();
                    int maximumFrequency = Integer.parseInt(maximumFrequencyString);

                /*
                ---------------------------------------------------------------------
                Database insertion process going on below
                ---------------------------------------------------------------------
                 */
                /*
                 * We are inserting new row into the main table
                 */
                    ContentValues values = makeContentValuesToInsert(nameOfHabit, bitIdRadio, maximumFrequency);
                    Uri uriToMainTable = buildUriToMainTable();
                    Uri returnedUri = getContentResolver().insert(uriToMainTable, values);
                    //Check is insertion was successful
                    if (returnedUri != null)
                        Toast.makeText(getBaseContext(), "Inserted - " + returnedUri, Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(getBaseContext(), "Not inserted! ", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);

                }catch (Exception e){
                    /* Will execute if any one field is left empty */
                    if(toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getBaseContext(), "Invalid entries", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });



    }


}
