package com.mkit.mshanoi.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseFragment;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;
import com.mkit.mshanoi.ui.activity.HomeActivity;
import com.mkit.mshanoi.ui.adapter.DanhSachMsAdapter;
import com.mkit.mshanoi.ui.event.ListMsEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DanhSachMsFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.lvDanhSachMS) ListView lvDanhSachMS;
    private List<DiaDiemMsResponse> danhSachDiaDiem = new ArrayList<>();
    private HomeActivity homeActivity = (HomeActivity) getActivity();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_danh_sach_ms, container, false);
        ButterKnife.bind(this, view);
        addControls();
        return view;
    }

    private void addControls() {
        try {
                danhSachDiaDiem = EventBus.getDefault().getStickyEvent(ListMsEvent.class).getListMs();
                if(danhSachDiaDiem != null && danhSachDiaDiem.size() > 0){
                    DanhSachMsAdapter danhSachMsAdapter = new DanhSachMsAdapter(getActivity(), R.layout.item_danh_sach_ms, danhSachDiaDiem);
                    lvDanhSachMS.setAdapter(danhSachMsAdapter);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
