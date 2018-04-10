package com.ananthrajsingh.bit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ananthrajsingh.bit.data.BitContract;

/**
 * Created by Ananth on 4/8/2018.
 *
 * This is the Adapter for our RecyclerView shown in main Activity. This will do what a
 * RecyclerView adapter does. When finished, our item will have name of habit and a color
 * showing current status of habit. See documentation to understand better what this code
 * will do.
 */

public class BitAdapter extends RecyclerView.Adapter<BitAdapter.BitAdapterViewHolder> {


    private final Context mContext;

    private Cursor mCursor;


    /**
     * RecyclerView recycles views. A ViewHolder is a required part of the pattern
     * for RecyclerViews. findViewById() calls are quiet expensive. Therefore
     * ViewHolder objects are created. These are recycled with new values without
     * findVIewById() calls. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class BitAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* Currently we have only one textview in our list_item */
        TextView mainTextView;

        public BitAdapterViewHolder(View itemView) {
            super(itemView);
            mainTextView = (TextView) itemView.findViewById(R.id.list_item_textview);

            /* we have to pass in a OnClickListner object, since this class extends from it,
             we pass "this"
             */
            itemView.setOnClickListener(this);
        }

        /**
         * This method is called when we click on a View when our views are displayed.
         * We will have to open a new activity. That new activity will display detailed
         * view of that habit. Therefore we will need the _ID of that habit. Since we are
         * not sorting our output list, getting adapter position will be used to move
         * cursor to desired row/
         *
         * @param v view which was clicked
         */
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Item touched at position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            /*
             * This tag was set in onBindViewHolder(..). This will be passed to BitDetail
             * activity as we want to get access to the row of touched item in Main table
             */
            Long idOfItem = (Long) v.getTag();
            Intent intent = new Intent(v.getContext(), BitDetail.class);
            intent.putExtra(v.getContext().getString(R.string.item_id_extra), idOfItem);
            v.getContext().startActivity(intent);

        }

    }

    /**
     * Will create a new BitAdapter
     *
     * @param context to get access to app UI and app resources
     */
    public BitAdapter(@NonNull Context context) {
        mContext = context;
    }

    /**
     * This method creates a new view. This is called only if there are no views to recycle.
     * If views are present to recycle, there is no need to create a new view. Therefore, it is
     * called when the recycler view is laid out. Enough views are created to fill the screen
     * and few more to ease scrolling.
     *
     * @param viewGroup The ViewGroup in which these views are contained within
     * @param viewType when there are more than one view type in a recycler view
     *                 then codes are used to differentiate between views. then
     *                 this is used. In our case, we have all views similar,
     *                 hence no need to care about it.
     * @return a ViewHolder containing all list items
     */
    @Override
    public BitAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false);

        view.setFocusable(true);

        return new BitAdapterViewHolder(view);
    }

    /**
     * This is called to display data at a particular position. This is NOT called when item
     * positions change. Here we will extract information fron database using cursor, and
     * attach it to current view. We are going to get position of adapter and then get
     * corresponding item from Main table and attach the information.
     *
     * @param holder the view holder which is to updated with information of the position
     * @param position position of view in the current data set.
     */
    @Override
    public void onBindViewHolder(BitAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String name = mCursor.getString(mCursor.getColumnIndex(BitContract.MainTableEntry.COLUMN_NAME));
        /*
         * We will need _ID in BitDetail activity to display the details of clicked activity
         * Now, we cannot pass getAdapterPosition() to BitDetail activity, that will work until
         * an item is deleted, then position and id will mismatch. To solve this we will add
         * a tag to every item of its corresponding id represented in the Main table
         */
        Long id = mCursor.getLong(mCursor.getColumnIndex(BitContract.MainTableEntry._ID));
        holder.mainTextView.setText(name);
        holder.mainTextView.setTag(id);
    }

    /**
     * This method will take new cursor from main activity. This will help lay out all the data
     * initially on the screen. We will also use this when new item is added to the database.
     * In future though, we want to use notifyItemIntserted(position) to add a new item. But that
     * functionality will be added later. When this method is called, we assume we have a
     * completely new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCursor cursor with new items
     */
    public void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
        //TODO : Add method addNewItem() to insert a new habit and call notifyItemInserted()
        //Currently we are calling this same fumction even when a single item is added.
    }

    /**
     * This method simply returns the number of items to display. This function is called behind
     * the scenes to help in layout the views and in animations
     *
     * @return number of items available in the main table
     */
    @Override
    public int getItemCount() {

        if (mCursor == null) return 0;
        return mCursor.getCount();
    }
}
