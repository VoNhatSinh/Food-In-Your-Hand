package com.company.sinh_tan.foodinyourhand;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.company.sinh_tan.bus.StoreAddressBUS;
import com.company.sinh_tan.dto.StoreAddress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/16/2015.
 */
public class FragmentFindingPlaceNearMe extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks
                                                                , GoogleApiClient.OnConnectionFailedListener
{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private ArrayList<Marker> markers;
    private FragmentActivity activity;

    private LatLng myLatLng;
    private Location lastLocation;
    private ProgressDialog progressDialog;

    private Spinner spinnerDistance;
    private String[] arrDistance;

    private float distance;
    private boolean isGoogleMapConnected = false;

    private StoreAddressBUS storeAddressBUS;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(activity);
        progressDialog.show();
        buildGoogleApiClient();
        storeAddressBUS = new StoreAddressBUS(getActivity());
        markers = new ArrayList<Marker>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finding_place_near_my, container, false);
        setupSpinnerDistance(view);

        setUpMapIfNeeded();
        setMyLatLng();
        return view;
    }

    private void setupSpinnerDistance(View view)
    {
        spinnerDistance = (Spinner) view.findViewById(R.id.spinnerDistance);
        arrDistance = new String[]{"500 m", "1 km", "2 km", "3 km", "5 km"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrDistance);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistance.setAdapter(adapter);
        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                distance = getDistaceFromString(arrDistance[i]);
                if (isGoogleMapConnected == true) {
                    try {
                        getPlaceNearMe(myLatLng, distance);
                        setCameraZoom(i);
                    }
                    catch (Exception e)
                    {

                    }

                }
                Log.d("105", "distance " + distance);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setCameraZoom(int distance)
    {
        float zoom = 0;
        switch (distance)
        {
            case 0:
                zoom = 15;
                break;
            case 1:
                zoom = 14;
                break;
            case 2:
                zoom = 14;
                break;
            case 3:
                zoom = 13;
                break;
            case 4:
                zoom = 12;
                break;
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, zoom));
    }

    private void getPlaceNearMe(LatLng myLocatin, float distance)
    {
        ArrayList<StoreAddress>storeAddresses =  storeAddressBUS.getListStoreAddressNearMe(myLocatin, distance);
        StoreAddress storeAddress;
        LatLng storeLocation;
        if(markers.size() > 0)
        {
            for(int j = 0, size = markers.size(); j < size; j++)
            {
                markers.get(j).remove();
            }
            markers.clear();
        }
        for(int i = 0, size = storeAddresses.size(); i < size; i++)
        {
            storeAddress = storeAddresses.get(i);
            storeLocation = new LatLng(Double.valueOf(storeAddress.getLatitude()), Double.valueOf(storeAddress.getLongitude()));
            MarkerOptions option = new MarkerOptions();
            option.title(storeAddress.getName());
            option.snippet(storeAddress.getAddress());
            option.position(storeLocation);
            Marker currentMarker = mMap.addMarker(option);
            markers.add(currentMarker);
            markers.get(i).showInfoWindow();
        }

    }

    private float getDistaceFromString(String distance)
    {
        float result = -1;
        String[]temp = distance.split(" ");
        result = Float.valueOf(temp[0]);
        if(result < 100)
        {
            result *= 1000;
        }
        return result;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }
    public void setFragmentActivity(FragmentActivity activity)
    {
        this.activity = activity;
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.mapFindingPlaceNearMy)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void setMyLatLng()
    {
        if(lastLocation != null)
        {
            myLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            Toast.makeText(activity, lastLocation.getLatitude() + " " + lastLocation.getLongitude(), Toast.LENGTH_LONG).show();
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

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        android.app.FragmentManager fm = getActivity().getFragmentManager();
        android.app.Fragment fragment = (fm.findFragmentById(R.id.mapFindingPlaceNearMy));
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
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

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for(int i = 0, size = markers.size(); i < size; i++)
                {
                    if(marker.equals(markers.get(i)))
                    {
                        //show fragment detail store here
                    }
                }
            }
        });
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                setMyLatLng();
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(activity, "onConnected", Toast.LENGTH_SHORT).show();
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        setMyLatLng();
        progressDialog.dismiss();
        isGoogleMapConnected = true;

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
