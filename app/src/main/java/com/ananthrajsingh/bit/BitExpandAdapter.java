package com.ananthrajsingh.bit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ananthrajsingh on 13/01/19
 *
 * This adapter is used for recycler view of the expanded form of BitDetail i.e. BitDetailExapand
 * activity. This will basically inflate expand_list_item and fill values in it using our underlying
 * SQLite database. For generic explanation of functions, refer class BitAdapter.
 */

public class BitExpandAdapter {


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
