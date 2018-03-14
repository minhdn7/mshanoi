package com.mkit.mshanoi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkit.mshanoi.R;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;
import com.mkit.mshanoi.ui.activity.DetailActivity;
import com.mkit.mshanoi.ui.event.DiaDiemMsEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by PC on 2/19/2018.
 */

public class DanhSachMsAdapter extends ArrayAdapter<DiaDiemMsResponse> {
    private Context context;
    private int resource;
    private List<DiaDiemMsResponse> objects;

    public DanhSachMsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DiaDiemMsResponse> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects != null ? objects.size() : 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DanhSachMsAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent,false);
            holder = new DanhSachMsAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DanhSachMsAdapter.ViewHolder) convertView.getTag();
        }
        try {
            holder.txtTenQuanMS.setText(getItem(position).getName());

            holder.txtDanhGia.setText(getItem(position).getPoint());

            holder.viewDiaChiMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DiaDiemMsEvent diaDiemMsEvent = new DiaDiemMsEvent(getItem(position));
                    EventBus.getDefault().postSticky(diaDiemMsEvent);
                    Intent intent = new Intent(context, DetailActivity.class);
                    context.startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txtTenQuanMS, txtDistance, txtDanhGia, txtGiaVe;
        LinearLayout viewDiaChiMS;

        public ViewHolder(View view) {
            txtTenQuanMS = (TextView) view.findViewById(R.id.txtTenQuanMS);
            txtDistance = (TextView) view.findViewById(R.id.txtDistance);
            txtDanhGia = (TextView) view.findViewById(R.id.txtDanhGia);
            txtGiaVe = (TextView) view.findViewById(R.id.txtGiaVe);
            viewDiaChiMS = (LinearLayout) view.findViewById(R.id.viewDiaChiMS);


        }


    }
}
