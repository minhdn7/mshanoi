package com.mkit.mshanoi.ui.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseActivity;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;
import com.mkit.mshanoi.ui.event.ListMsEvent;
import com.mkit.mshanoi.ui.fragment.DanhSachMsFragment;
import com.mkit.mshanoi.ui.fragment.ForumsFragment;
import com.mkit.mshanoi.ui.fragment.MapFragment;
import com.mkit.mshanoi.ui.fragment.MenuFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.bottomBar) BottomBar bottomBar;
    @BindView(R.id.contentContainer) FrameLayout contentContainer;
    private MapFragment mapFragment;
    private DanhSachMsFragment danhSachMsFragment;
    private ForumsFragment forumsFragment;
    private MenuFragment menuFragment;
    private FirebaseDatabase mFireBaseDataBase;
    private DatabaseReference mDatabaseReference;
    public List<DiaDiemMsResponse> danhSachDiemMs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        getDataFireBase();
        addControls();
    }

    private void addControls() {
        createFragments();
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_home:
                        commitFragment(mapFragment);
                        break;
                    case R.id.tab_list_ms:
                        commitFragment(danhSachMsFragment);
                        break;
                    case R.id.tab_forums:
                        commitFragment(forumsFragment);
                        break;
                    case R.id.tab_menu:
                        commitFragment(menuFragment);
                        break;
                }
            }

        });

        try {
            danhSachDiemMs = EventBus.getDefault().getStickyEvent(ListMsEvent.class).getListMs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFragments() {
        mapFragment = new MapFragment();
        danhSachMsFragment = new DanhSachMsFragment();
        menuFragment = new MenuFragment();
        forumsFragment = new ForumsFragment();
    }


    private void commitFragment(Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentContainer, fragment);
        fragmentTransaction.commit();
    }

    public void getDataFireBase() {
        Firebase.setAndroidContext(this);
        Firebase firebaseRef = new Firebase("https://ms-ha-noi.firebaseio.com/ms ha noi");
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
                        if(danhSachMs.child("điểm") != null && danhSachMs.child("điểm").getValue() != null) {
                            diaDiemMsResponse.setPoint(danhSachMs.child("điểm").getValue().toString());
                        }
                        if(danhSachMs.child("long") != null && danhSachMs.child("long").getValue() != null) {
                            diaDiemMsResponse.setLongTitule(danhSachMs.child("long").getValue().toString());
                        }
                        if(danhSachMs.child("lat") != null && danhSachMs.child("lat").getValue() != null) {
                            diaDiemMsResponse.setLatTitule(danhSachMs.child("lat").getValue().toString());
                        }
                        danhSachDiemMs.add(diaDiemMsResponse);


                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(HomeActivity.this, "errorr", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
