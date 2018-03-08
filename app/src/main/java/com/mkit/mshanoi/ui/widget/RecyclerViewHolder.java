package com.mkit.mshanoi.ui.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkit.mshanoi.R;


/**
 * Created by MinhDN on 26/1/2018.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
    // View holder for gridview recycler view as we used in listview
    public TextView title;
    public ImageView imageview;




    public RecyclerViewHolder(View view) {
        super(view);
        // Find all views ids

        this.title = (TextView) view.findViewById(R.id.txtNhaNghi);
        this.imageview = (ImageView) view.findViewById(R.id.imgNhaNghi);

    }



}