package com.mkit.mshanoi.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.ui.IconGenerator;
import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseFragment;
import com.mkit.mshanoi.app.utils.PermissionUtils;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;
import com.mkit.mshanoi.domain.repository.MapDemoActivityPermissionsDispatcher;
import com.mkit.mshanoi.ui.activity.DetailActivity;
import com.mkit.mshanoi.ui.activity.HomeActivity;
import com.mkit.mshanoi.ui.event.DiaDiemMsEvent;
import com.mkit.mshanoi.ui.event.ListMsEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;

import static android.content.Context.LOCATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private OnFragmentInteractionListener mListener;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationRequest mLocationRequest;
    private HomeActivity homeActivity;
    Location mCurrentLocation;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    private final static String KEY_LOCATION = "location";
    private int cameraZoomTo = 13;
    private float minValues = 0;
    private float maxValues = 1000000;
    protected LocationManager locationManager;
    private boolean isGPSEnabled = false;

    private double latitude = 21.028511;
    private double longitude = 105.804817;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    private List<DiaDiemMsResponse> danhSachDiaDiem = new ArrayList<>();

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        addControls(view);
        addEvents();
        return view;
    }

    private void addEvents() {


    }

    private void addMarkerEvents(){
        if(map != null){
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Locale.setDefault(Locale.US);
                    IconGenerator iconFactory = new IconGenerator(getContext());
                    iconFactory.setStyle(R.style.iconFactoryClick);
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(marker.getTitle())));
                    String idMassage = danhSachDiaDiem.get(mHashMap.get(marker)).getId();

                    dialogThongTinNhaNghi(danhSachDiaDiem.get(mHashMap.get(marker)));
                    return false;
                }
            });
        }
    }

    private void addControls(View view) {
//        mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        homeActivity = (HomeActivity) getActivity();

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
        danhSachDiaDiem = EventBus.getDefault().getStickyEvent(ListMsEvent.class).getListMs();
    }


    private void addMarker(String title, String desCription, LatLng position, int id) {
        IconGenerator iconFactory = new IconGenerator(getActivity());
        iconFactory.setTextAppearance(R.style.iconGenText);
        Marker marker = map.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(title)))
                .snippet(desCription));
        mHashMap.put(marker, id);

    }

    public void onLocationChanged(Location location) {
        // GPS may be turned off
        if (location == null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(cameraZoomTo);
            map.moveCamera(center);
            map.animateCamera(zoom);
            return;
        }

        // Report to the UI that the location was updated

        mCurrentLocation = location;
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(cameraZoomTo);
        map.moveCamera(center);
        map.animateCamera(zoom);
        // hiển thị pointer
        map.addMarker(new MarkerOptions()
                .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                .title("vị trí của tôi"));
//        addPointer();
        // end
//        hiển thị address
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }



    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map = googleMap;
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(),
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
//            onLocationChanged(homeActivity.mLastLocation);
//            Location location = new Location(latitude, longitude);
            onLocationChanged(homeActivity.mLastLocation);
            addDiaDiem();
            addMarkerEvents();
        } else {
            Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDiaDiem() {
        if(danhSachDiaDiem.size() > 0){
            for(int i = 0; i < danhSachDiaDiem.size(); i++){
                addMarker(danhSachDiaDiem.get(i).getName(),
                        danhSachDiaDiem.get(i).getTic(),
                        new LatLng(danhSachDiaDiem.get(i).getLatTitule(), danhSachDiaDiem.get(i).getLongTitule()),
                        i);
            }
        }

    }

    private void performSearchLocation(LatLng searchLocation) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(searchLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(cameraZoomTo);
        map.moveCamera(center);
        map.animateCamera(zoom);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), cameraZoomTo));
    }

    private void addPointer() {
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

    // show dialog
//    private void dialogThongTinNhaNghi(String title, String distance, String giaPhong, String shortDescription, String imgUrl) {
    private void dialogThongTinNhaNghi(final DiaDiemMsResponse diaDiemMsResponse) {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nha_nghi);
        LinearLayout viewDialogNhaNghi = (LinearLayout) dialog.findViewById(R.id.viewDialogNhaNghi);
        TextView txtTenNhaNghi = (TextView) dialog.findViewById(R.id.txtTenNhaNghi);
        TextView txtDistance = (TextView) dialog.findViewById(R.id.txtDistance);
        TextView txtGiaPhong = (TextView) dialog.findViewById(R.id.txtGiaPhong);
        TextView txtMieuTa = (TextView) dialog.findViewById(R.id.txtMieuTa);
        ImageView imgNhaNghi = (ImageView) dialog.findViewById(R.id.imgNhaNghi);

        txtTenNhaNghi.setText(diaDiemMsResponse.getName());
        txtDistance.setText(diaDiemMsResponse.getAddress());
        txtGiaPhong.setText(diaDiemMsResponse.getTic());
        txtMieuTa.setText(diaDiemMsResponse.getPoint());

        // chỉnh kích cỡ dialog show
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        height = (int) (height * 0.4);
        lp.width = width;
        lp.height = height;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
//        mBehavior.setPeekHeight(height);
        dialog.show();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                // Right here!
                final BottomSheetBehavior behaviour = BottomSheetBehavior.from(bottomSheet);
                behaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dialog.dismiss();
                        }

                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            ((BottomSheetBehavior) behaviour).setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
            }
        });

        viewDialogNhaNghi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaDiemMsEvent diaDiemMsEvent = new DiaDiemMsEvent(diaDiemMsResponse);
                EventBus.getDefault().postSticky(diaDiemMsEvent);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                startActivity(intent);
            }
        });

    }

}
