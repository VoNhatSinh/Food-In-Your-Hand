package com.company.sinh_tan.foodinyourhand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.company.sinh_tan.bus.StoreBUS;
import com.company.sinh_tan.common.constant.Constant;
import com.company.sinh_tan.dto.StoreDTO;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class DetailStoreActivity extends FragmentActivity {

    private String idStore;
    private StoreBUS storeBUS;
    private StoreDTO storeDTO;
    private GoogleMap map;
    ProgressDialog myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_store);
        Intent intent = getIntent();
        idStore = intent.getStringExtra(Constant.ID_STORE);
        storeBUS = new StoreBUS(this);
        storeDTO = storeBUS.getStoreById(idStore);
        Log.d("105", storeDTO.getLongitude() + " " + storeDTO.getLatitude());
        //
        addControls();
        setStoreLocationOnMap(storeDTO);



    }

    private void addControls() {
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.mapStore)).getMap();
        myProgress = new ProgressDialog(this);
        myProgress.setProgressStyle(myProgress.STYLE_SPINNER);
        myProgress.setCancelable(true);
        myProgress.show();
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                myProgress.dismiss();
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(DetailStoreActivity.this, MapsActivity.class);
                    intent.putExtra(Constant.STORE_DTO, storeDTO);
                    startActivity(intent);
            }
        });
    }

    private void setStoreLocationOnMap(StoreDTO storeDTO)
    {
        //get latlng
        LatLng latLng  = new LatLng(Double.valueOf(storeDTO.getLatitude()),Double.valueOf(storeDTO.getLongitude()));
        CameraUpdate storeLocation = CameraUpdateFactory.newLatLngZoom(latLng, 13);
        map.animateCamera(storeLocation);
        //get camera position
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng)
                .zoom(15).bearing(90).tilt(40).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //set option
        MarkerOptions options = new MarkerOptions();
        options.title(storeDTO.getName());
        options.snippet(storeDTO.getAddress());
        options.position(latLng);
        Marker marker = map.addMarker(options);
        marker.showInfoWindow();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
