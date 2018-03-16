package com.mkit.mshanoi.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseActivity;

import butterknife.OnClick;

public class MapDetailActivity extends BaseActivity {
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private int cameraZoomTo = 13;
    // get tọa độ
    private double latitude = 21.028511;
    private double longitude = 105.804817;
    private String hotelName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);
        addControls();
    }

    private void addControls() {
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        latitude = getIntent().getDoubleExtra("LAT_POSITION", 21.028511);
        longitude = getIntent().getDoubleExtra("LONG_POSITION", 105.804817);
        hotelName = getIntent().getStringExtra("NAME_HOTEL");
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map = googleMap;
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            //Initialize Google Play Services
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    map.setMyLocationEnabled(true);
                } else {
                    //Request Location Permission
                }
            } else {
                map.setMyLocationEnabled(true);
            }

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(cameraZoomTo);
            map.moveCamera(center);
            map.animateCamera(zoom);
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .snippet(hotelName)
                    .title(hotelName));
            // end
        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnDong)
    public void onViewClicked() {
        onBackPressed();
    }
}
