package com.mkit.mshanoi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseActivity;
import com.mkit.mshanoi.app.LineApplication;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;
import com.mkit.mshanoi.ui.event.ListMsEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends BaseActivity {
    public List<DiaDiemMsResponse> danhSachDiemMs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if(isConnectedNetwork()){
            showProgressBar();
            getDataFireBase();
        }else{
            showDialogCheckInternet("Không thể kết nối được đến hệ thống, xin quý đồng dâm kiểm tra lại kết nối internet(wifi, 3G), và thử lại");
        }
    }

    public void getDataFireBase() {
        Firebase.setAndroidContext(this);
        Firebase firebaseRef = new Firebase(LineApplication.apiFireBaseUrl);
        firebaseRef.child("danh sach").addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null){
                    for(DataSnapshot danhSachMs : dataSnapshot.getChildren()){
                        DiaDiemMsResponse diaDiemMsResponse = new DiaDiemMsResponse();
                        if(danhSachMs.child("id") != null && danhSachMs.child("id").getValue() != null) {
                            diaDiemMsResponse.setId(danhSachMs.child("id").getValue().toString());
                        }
                        if(danhSachMs.child("name") != null && danhSachMs.child("name").getValue() != null) {
                            diaDiemMsResponse.setName(danhSachMs.child("name").getValue().toString());
                        }
                        if(danhSachMs.child("linkTruyen") != null && danhSachMs.child("linkTruyen").getValue() != null) {
                            diaDiemMsResponse.setLinkTruyen(danhSachMs.child("linkTruyen").getValue().toString());
                        }
                        if(danhSachMs.child("linkAnh") != null && danhSachMs.child("linkAnh").getValue() != null) {
                            diaDiemMsResponse.setLinkAnh(danhSachMs.child("linkAnh").getValue().toString());
                        }
                        if(danhSachMs.child("point") != null && danhSachMs.child("point").getValue() != null) {
                            diaDiemMsResponse.setPoint(danhSachMs.child("point").getValue().toString());
                        }
                        if(danhSachMs.child("long") != null && danhSachMs.child("long").getValue() != null) {
                            diaDiemMsResponse.setLongTitule((Double) danhSachMs.child("long").getValue());
                        }
                        if(danhSachMs.child("lat") != null && danhSachMs.child("lat").getValue() != null) {
                            diaDiemMsResponse.setLatTitule((Double) danhSachMs.child("lat").getValue());
                        }
                        if(danhSachMs.child("address") != null && danhSachMs.child("address").getValue() != null) {
                            diaDiemMsResponse.setAddress(danhSachMs.child("address").getValue().toString());
                        }
                        if(danhSachMs.child("rate") != null && danhSachMs.child("rate").getValue() != null) {
                            diaDiemMsResponse.setRate(danhSachMs.child("rate").getValue().toString());
                        }
                        if(danhSachMs.child("tic") != null && danhSachMs.child("tic").getValue() != null) {
                            diaDiemMsResponse.setTic(danhSachMs.child("tic").getValue().toString());
                        }
                        if(danhSachMs.child("phone") != null && danhSachMs.child("phone").getValue() != null) {
                            diaDiemMsResponse.setPhone(danhSachMs.child("phone").getValue().toString());
                        }
                        danhSachDiemMs.add(diaDiemMsResponse);
                    }
                    Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                    ListMsEvent listMsEvent = new ListMsEvent(danhSachDiemMs);
                    EventBus.getDefault().postSticky(listMsEvent);
//                    intent.putExtra("LIST_MS", (Serializable) danhSachDiemMs);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(StartActivity.this, "errorr", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void showDialogCheckInternet(String contentDialog) {
        MaterialDialog dialog = new MaterialDialog.Builder(this).title(R.string.str_thong_bao)
                .content(contentDialog)
                .positiveText(R.string.str_reload)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override public void onDismiss(DialogInterface dialogInterface) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .show();
    }
}
