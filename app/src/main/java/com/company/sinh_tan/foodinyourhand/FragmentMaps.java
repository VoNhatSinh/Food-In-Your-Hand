package com.company.sinh_tan.foodinyourhand;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.company.sinh_tan.common.constant.Constant;
import com.company.sinh_tan.common.google_direction.GoogleDirection;
import com.company.sinh_tan.dto.StoreDTO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Document;

/**
 * Created by Sinh on 6/20/2015.
 */
public class FragmentMaps extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleDirection googleDirection;
    private Document document;
    private StoreDTO storeDTO;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;


    private LatLng myLatLng;
    private LatLng storeLatLng;

    private String requestCode;
    private Button btnDirecion;
    private Button btnBackToDetail;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();
        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            storeDTO = (StoreDTO) bundle.getSerializable(Constant.STORE_DTO);
            storeLatLng = new LatLng(Double.valueOf(storeDTO.getLatitude()), Double.valueOf(storeDTO.getLongitude()));
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        setUpMapIfNeeded();
        addControls(view);
        addControlsDirections();
        return view;
    }

    private void addControls(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //  mapFragment.getMapAsync(this);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                progressDialog.dismiss();
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(getActivity(), "location click", Toast.LENGTH_LONG).show();

                if (myLatLng != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(myLatLng)      // Sets the center of the map to location user
                            .zoom(15)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    MarkerOptions option = new MarkerOptions();
                    option.title("Vị trí hiện tại của bạn");
                    option.snippet("");
                    option.position(myLatLng);
                    Marker currentMarker = mMap.addMarker(option);
                    currentMarker.showInfoWindow();
                }


                return false;
            }
        });

        btnDirecion = (Button) view.findViewById(R.id.btnDirection);
        btnBackToDetail = (Button) view.findViewById(R.id.btnBackToDetail);
        btnDirecion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try
               {
                   if(myLatLng != null && storeLatLng != null)
                   {
                       googleDirection.setLogging(true);
                       googleDirection.request(myLatLng, storeLatLng, GoogleDirection.MODE_DRIVING);
                   }

               }
               catch (Exception e)
               {
                   Toast.makeText(getActivity(), "Vui lòng kiểm tra kết nối internet của bạn", Toast.LENGTH_LONG).show();
               }

            }
        });
        btnBackToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapShowPath))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    private void addControlsDirections() {
        googleDirection = new GoogleDirection(getActivity());
        googleDirection.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
            @Override
            public void onResponse(String status, Document doc, GoogleDirection gd) {
                document = doc;
                mMap.addPolyline(googleDirection.getPolyline(doc, 3, Color.RED));
                mMap.addMarker(new MarkerOptions().position(myLatLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN)));

                mMap.addMarker(new MarkerOptions().position(storeLatLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN)));
            }
        });
    }
    private void setStoreLocation(StoreDTO storeDTO) {
        LatLng latLng  = new LatLng(Double.valueOf(storeDTO.getLatitude()),Double.valueOf(storeDTO.getLongitude()));
        CameraUpdate storeLocation = CameraUpdateFactory.newLatLngZoom(latLng, 13);
        mMap.animateCamera(storeLocation);
        //get camera position
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng)
                .zoom(15).bearing(90).tilt(40).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //set option
        MarkerOptions options = new MarkerOptions();
        options.title(storeDTO.getName());
        options.snippet(storeDTO.getAddress());
        options.position(latLng);
        Marker marker = mMap.addMarker(options);
        marker.showInfoWindow();
    }
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }


    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        android.app.FragmentManager fm = getActivity().getFragmentManager();
        android.app.Fragment fragment = (fm.findFragmentById(R.id.mapShowPath));
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(lastLocation != null)
        {
            myLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            Toast.makeText(getActivity(), lastLocation.getLatitude() + " " + lastLocation.getLongitude(), Toast.LENGTH_LONG).show();
            setStoreLocation(storeDTO);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
