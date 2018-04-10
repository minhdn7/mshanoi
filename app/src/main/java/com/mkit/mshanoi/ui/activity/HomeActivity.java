package com.mkit.mshanoi.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseActivity;
import com.mkit.mshanoi.app.utils.PermissionUtils;
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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback{
    @BindView(R.id.bottomBar) BottomBar bottomBar;
    @BindView(R.id.contentContainer) FrameLayout contentContainer;
    private MapFragment mapFragment;
    private DanhSachMsFragment danhSachMsFragment;
    private ForumsFragment forumsFragment;
    private MenuFragment menuFragment;
    private FirebaseDatabase mFireBaseDataBase;
    private DatabaseReference mDatabaseReference;
    public List<DiaDiemMsResponse> danhSachDiemMs = new ArrayList<>();

    // permission config
    private static final String TAG = "MyLocationUsingHelper";

    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;

    public Location mLastLocation;

    // Google client to interact with Google API



    private double latitude = 21.028511;
    private double longitude = 105.804817;

    boolean isPermissionGranted;
    // list of permissions
    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;

    // Google client to interact with Google API

    private GoogleApiClient mGoogleApiClient;
    // end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Massage Hà Nội");
        addControls();
        addGooglePermission();
//        initAds();
    }

    private void initAds() {
        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(ADS_BANNER);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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

    /**
     * Creating google api client object
     */

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }


    private void addGooglePermission() {
        permissionUtils = new PermissionUtils(this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions, "Need GPS permission for getting your location", 1);

        // check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }

    private void getLocation() {

        if (isPermissionGranted) {
            try {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

            } catch (SecurityException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
        isPermissionGranted = true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }


    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
    }

    /**
     * Method to verify google play services on the device
     */

    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                this.finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
