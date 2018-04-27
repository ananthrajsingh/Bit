package com.ananthrajsingh.bit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ananthrajsingh.bit.data.BitContract;

import static com.ananthrajsingh.bit.utilities.DatabaseUtils.buildUriToMainTable;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private BitAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private static long idToRemoveFromRv;
    private int loaderId;

    public static final int BAD_BIT_ID = 1;
    public static final int GOOD_BIT_ID = 2;

    public static final int DATABASE_LOADER_ID = 18;
    public static final int DELETED_ROW_DATABASE_ID = 28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);
        getSupportActionBar().collapseActionView();

        Intent intent = getIntent();
        int rvPosition = intent.getIntExtra(getString(R.string.rv_position_extra), -1);
        idToRemoveFromRv = intent.getLongExtra(getString(R.string.id_of_habit_extra), -1);

        /* Is this activity called after selecting delete from BitDetail?
         * If it would be then, rvPosition and idToRemoveFromRv won't be -1
         * Else, we came here by SplashActivity
         */
        if (rvPosition != -1 && idToRemoveFromRv != -1){
            loaderId = DELETED_ROW_DATABASE_ID;
            makeSnackBar();
        }
        else{
            loaderId = DATABASE_LOADER_ID;
        }

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
                Intent intent = new Intent(getBaseContext(), CreateBit.class);
                startActivity(intent);

            }
        });
        getSupportLoaderManager().initLoader(loaderId, null, this);
    }

    /*
    -----------------------------------------------------------------------------
    MainActivity needs to display the main table contents. Therefore we need to
    query the table. Query should not be done on main thread. Thus to display
    main table contents we need to use CursorLoader. That is the reason we are
    implementing LoaderManager in this class.
    Use loader manager to do hefty tasks, else your activity will become
    unresponsive.
    -----------------------------------------------------------------------------
     */

    /**
     * This is one of three callback methods from LoaderManager.LoaderCallback interface.
     * This is called when the system needs a new Loader to be created. In this we
     * will query the Main table using a CursorLoader.
     * We are using only one loader therefore we dont need to check loader id. This
     * is is used to distinguish between loaders. Still, it is good practise to check
     * for id.
     *
     * @param loaderId id of the current loader that we are going to deal with
     * @param args any arguments supplied by the caller
     * @return a new loader instance which is ready to do loading
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        /*This will get built uri from helper class*/
        Uri uri = buildUriToMainTable();
        switch(loaderId){
            case DATABASE_LOADER_ID:
                /*We want all columns and all rows, therefore we are passing null.
                 *Also, we need no sort order, thus last argument is also null.
                 */
                return new CursorLoader(this,
                        uri,
                        null,
                        null,
                        null,
                        null);
            case DELETED_ROW_DATABASE_ID:
                String selection = BitContract.MainTableEntry._ID + " != ?";
                String[] selectionArgs = {Long.toString(idToRemoveFromRv)};
                /*
                 * We don't want a particular row which is selected to be deleted (but not yet deleted)
                 * Thus we will remove it from the query.
                 */
                return new CursorLoader(this,
                        uri,
                        null,
                        selection,
                        selectionArgs,
                        null
                        );
            default:
                throw new IllegalArgumentException("This loader is currently not implemented :( loaderId - " + loaderId);
        }
    }

    /**
     * This function is called when the loading is finished.
     *
     * This function will transfer the loaded data to BitAdapter object. BitAdapter will
     * create and bind views with the data that cursor represents. BitAdapter knows where
     * to layout the data.
     *
     * @param loader the loader that has finished loading
     * @param data data which was loaded by loader
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

//    public void removeHabitFromRv(int position, int idOfHabit){
//        idToRemoveFromRv = idOfHabit;
//        getSupportLoaderManager().initLoader(DELETED_ROW_DATABASE_ID, null, this);
//    }
    private void makeSnackBar(){
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.habit_deleted_snackbar), Snackbar.LENGTH_LONG )
                .setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSupportLoaderManager().initLoader(DATABASE_LOADER_ID, null, MainActivity.this);

                    }
                }).addCallback( new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int dismissType) {
                        super.onDismissed(transientBottomBar, dismissType);
                        if (dismissType != DISMISS_EVENT_ACTION){
                            Uri uri = buildUriToMainTable(idToRemoveFromRv);

                            getContentResolver().delete(uri, null, null);
                        }
                    }
                });
        snackbar.show();
    }
}
