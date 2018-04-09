package com.ananthrajsingh.bit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BitAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public static final int BAD_BIT_ID = 1;
    public static final int GOOD_BIT_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab =(FloatingActionButton) findViewById(R.id.fab);

        /* Get hold of the recycler view defined in xml */
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);

        /*
         * We know that list item size doesn't change when values change.
         * therefore this setting. This will improve performance.
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * LinearLayoutManager is responsible for measuring and positioning views items
         * within the RecyclerView. It can produce vertical or horizontal list depending
         * on the passed argument.
         * The last parameter (shouldReverseLayout) is true if you want to reverse your
         * layout. It is generally true in horizontal lists.
         */
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* associate create loader manager with our recycler view*/
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new BitAdapter(this);

        /* Attach the adapter to the recycler view in activity_main */
         mRecyclerView.setAdapter(mAdapter);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          COMPLETED 1 - send an intent to activity to choose good or bad bit
                Intent intent = new Intent(getBaseContext(), ChooseBitActivity.class);
                startActivity(intent);

            }
        });
    }
}
