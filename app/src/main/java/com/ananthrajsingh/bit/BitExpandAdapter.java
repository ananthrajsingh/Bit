package com.ananthrajsingh.bit;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ananthrajsingh on 13/01/19
 *
 * This adapter is used for recycler view of the expanded form of BitDetail i.e. BitDetailExapand
 * activity. This will basically inflate expand_list_item and fill values in it using our underlying
 * SQLite database. For generic explanation of functions, refer class BitAdapter.
 */

public class BitExpandAdapter extends RecyclerView.Adapter<BitExpandAdapter.BitExpandAdapterViewHolder>{

    private Context mContext;
    private boolean mIsGood;
    private Cursor mCursor;

    /**
     * This will help us create a new adapter. We are passing context and a boolean variable.
     * Our adapter should know whether it is dealing with a good habit or bad.
     * @param context
     * @param isGood
     */
    public BitExpandAdapter(Context context, boolean isGood){
        mContext = context;
        mIsGood = isGood;
    }

    /**
     * All expected task here, nothing special
     * @param parent The ViewGroup in which these views are contained within
     * @param viewType when there are more than one view type in a recycler view
     *                 then codes are used to differentiate between views. then
     *                 this is used. In our case, we have all views similar,
     *                 hence no need to care about it.
     * @return a ViewHolder containing all list items
     */
    @Override
    public BitExpandAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.expand_list_item, parent, false);
        return new BitExpandAdapterViewHolder(view);
    }

    /**
     * We will be doing he relevant task here. We have cursor here. We will bind data from cursor
     * to the list item using position argument. We assume here that our cursor has all(and only that)
     * information that we need to show in recycler view.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BitExpandAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * This class is necessary. I have taken a (not so) long break from Android, therefore having
     * a hard time in documenting. Still I'll try to explain what I'm doing. This class basically
     * helps recycler not to make frequent findViewById() calls. So when Recycler is inflating views
     * for the first time on screen, it uses findViewById() calls for the items views that could fit
     * the screen. Now when it comes to recycling a scrolled up/down list item, the cached object
     * of this class is filled with new values.
     */
    class BitExpandAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView bitDayColor;
        TextView date;
        TextView count;

        public BitExpandAdapterViewHolder(View itemView) {
            super(itemView);
            bitDayColor = itemView.findViewById(R.id.expand_list_item_bit_color);
            date = itemView.findViewById(R.id.expand_list_item_date_tv);
            count = itemView.findViewById(R.id.expand_list_item_count_tv);
        }
    }


}
