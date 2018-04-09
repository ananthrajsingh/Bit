package com.ananthrajsingh.bit;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToMainTable;
import static com.ananthrajsingh.bit.utilities.DatabaseUtils.makeContentValuesToInsert;

public class CreateBit extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextMaxCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bit);
        Intent intent = getIntent();

        /*bitId represents whether user wants to insert a bad bit or good bit */
        final int bitId = intent.getIntExtra(getString(R.string.bit_id_extra), 0);

        /*Extracting information that the user entered and hit FAB*/

        FloatingActionButton fabInsertHabit = (FloatingActionButton) findViewById(R.id.fabAddBit);

        /* When fab is clicked, this must happen */
        fabInsertHabit.setOnClickListener(new View.OnClickListener() {
            /* We will get the information in EditText fields and pass to database*/
            @Override
            public void onClick(View v) {
                editTextName = (EditText) findViewById(R.id.editTextName);
                String nameOfHabit = editTextName.getText().toString();
                editTextMaxCount = (EditText) findViewById(R.id.editTextFrequency);
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
                ContentValues values = makeContentValuesToInsert(nameOfHabit, bitId, maximumFrequency);
                Uri uriToMainTable = buildUriToMainTable();
                Uri returnedUri = getContentResolver().insert(uriToMainTable, values);
                //Check is insertion was successful
                if (returnedUri != null)
                    Toast.makeText(getBaseContext(), "Inserted - " + returnedUri, Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(getBaseContext(), "Not inserted! ", Toast.LENGTH_LONG).show();
                    Log.e("CreateBit.java", "Uri sent to db- " + uriToMainTable);
                }
            }
        });



    }


}
