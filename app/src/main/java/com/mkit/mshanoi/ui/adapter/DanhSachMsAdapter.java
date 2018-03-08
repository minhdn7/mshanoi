package com.mkit.mshanoi.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mkit.mshanoi.R;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
//            holder.txtUser.setText(getItem(position).getAccount());
//            holder.txtTieuDe.setText(getItem(position).getTieuDe());
//            holder.txtNoiDung.setText(getItem(position).getNoiDung());
//            holder.txtThoiGian.setText(getItem(position).getThoiGian());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txtTenQuanMS, txtDistance, txtDanhGia, txtGiaVe;

        public ViewHolder(View view) {
            txtTenQuanMS = (TextView) view.findViewById(R.id.txtTenQuanMS);
            txtDistance = (TextView) view.findViewById(R.id.txtDistance);
            txtDanhGia = (TextView) view.findViewById(R.id.txtDanhGia);
            txtGiaVe = (TextView) view.findViewById(R.id.txtGiaVe);


        }


    }
}
