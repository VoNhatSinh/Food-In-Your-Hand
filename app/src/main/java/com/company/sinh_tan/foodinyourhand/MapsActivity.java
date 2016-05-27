package com.company.sinh_tan.foodinyourhand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback
{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleDirection googleDirection;
    private Document document;
    private StoreDTO storeDTO;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private MapFragment mapFragment;

    private LatLng myLatLng;
    private LatLng storeLatLng;

    private String requestCode;
    private Button btnDirecion;
    private Button btnBackToDetail;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        buildGoogleApiClient();
        Intent intent = getIntent();
        if(intent != null)
        {
            requestCode = intent.getStringExtra(Constant.REQUEST_CODE);
            storeDTO = (StoreDTO) intent.getSerializableExtra(Constant.STORE_DTO);
            storeLatLng = new LatLng(Double.valueOf(storeDTO.getLatitude()), Double.valueOf(storeDTO.getLongitude()));
        }

        addControls();
        setStoreLocation(storeDTO);
        addControlsDirections();


    }

    private void addControlsDirections() {
        googleDirection = new GoogleDirection(this);
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

    private void addControls() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
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
                Toast.makeText(MapsActivity.this, "location click", Toast.LENGTH_LONG).show();

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

        btnDirecion = (Button) findViewById(R.id.btnDirection);
        btnBackToDetail = (Button) findViewById(R.id.btnBackToDetail);
        btnDirecion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleDirection.setLogging(true);
                googleDirection.request(myLatLng, storeLatLng, GoogleDirection.MODE_DRIVING);
            }
        });
        btnBackToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(lastLocation != null)
        {
            myLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            Toast.makeText(MapsActivity.this, lastLocation.getLatitude() + " " + lastLocation.getLongitude(), Toast.LENGTH_LONG).show();
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
