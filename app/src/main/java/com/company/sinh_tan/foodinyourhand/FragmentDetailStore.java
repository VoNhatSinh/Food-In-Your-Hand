package com.company.sinh_tan.foodinyourhand;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.company.sinh_tan.bus.HistorySearchBUS;
import com.company.sinh_tan.bus.StoreBUS;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sinh on 6/15/2015.
 */
public class FragmentDetailStore extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener{
    private String idStore;
    private StoreBUS storeBUS;
    private StoreDTO storeDTO;

    private HistorySearchBUS historySearchBUS;

    private ProgressDialog myProgress;
    private FrameLayout frameStoreImage;
    private FrameLayout frameStoreDetail;
    private FrameLayout frameStoreMap;

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private GoogleDirection googleDirection;

    private Location lastLocation;
    private LatLng myLatLng;
    private LatLng storeLatLng;

    private Button btnDirecion;
    private Button btnBackToDetail;
    private ImageView imageViewMap;

    private ImageView imageViewStore;
    private TextView txtStoreName;
    private TextView txtLike;
    private TextView txtStoreAddress;
    private TextView txtStoreDescription;
    private RatingBar ratingBar;
    private TextView txtOpenTime;
    private TextView txtStorePrice;
    private TextView txtDetailInfor;

    private boolean isLike;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        idStore = bundle.getString(Constant.ID_STORE);
        historySearchBUS = new HistorySearchBUS(getActivity());
        storeBUS = new StoreBUS(getActivity());
        storeDTO = storeBUS.getStoreById(idStore);
        isLike = storeDTO.getLike();
        Log.d("105", storeDTO.getLongitude() + " " + storeDTO.getLatitude());
        //


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_store, container, false);
        buildGoogleApiClient();
        addControlsDirections();
        setUpMapIfNeeded();

        addControls(view);
        addControlsShowInfor(view);

        return view;
    }

    private void addControlsShowInfor(View view) {
        imageViewStore = (ImageView) view.findViewById(R.id.imageViewStore);
        txtStoreName = (TextView) view.findViewById(R.id.txtStoreName);
        txtLike = (TextView) view.findViewById(R.id.txtLike);
        txtStoreAddress = (TextView) view.findViewById(R.id.txtStoreAddress);
        txtStoreDescription = (TextView) view.findViewById(R.id.txtStoreDescription);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        txtOpenTime = (TextView) view.findViewById(R.id.txtOpenTime);
        txtStorePrice = (TextView) view.findViewById(R.id.txtStorePrice);
        txtDetailInfor = (TextView) view.findViewById(R.id.txtDetailInfor);
        InputStream inImage = null;
        try {
            inImage = getActivity().getAssets().open(storeDTO.getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmapResult = BitmapFactory.decodeStream(inImage);
        bitmapResult.getScaledWidth(imageViewStore.getWidth());
        bitmapResult.getScaledHeight(imageViewStore.getHeight());
        imageViewStore.setImageBitmap(bitmapResult);
        txtLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLike == false)
                {
                    Drawable drawable = getResources().getDrawable(android.R.drawable.btn_star_big_on);
                    txtLike.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    isLike = true;
                }
                else
                {
                    Drawable drawable = getResources().getDrawable(android.R.drawable.btn_star_big_off);
                    txtLike.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    isLike = false;
                }
                storeDTO.setLike(isLike);


            }
        });
        ratingBar.setRating(storeDTO.getRating());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                storeDTO.setRating(v);
            }
        });
        txtStoreName.setText(storeDTO.getName());
        Log.d("106", "like of storeDTO " + storeDTO.getLike());
        if(storeDTO.getLike() == true)
        {

            Drawable drawable = getResources().getDrawable(android.R.drawable.btn_star_big_on);
            txtLike.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
        txtStoreAddress.setText(storeDTO.getAddress());
        txtOpenTime.setText(storeDTO.getTime());
        String price = storeDTO.getLowest_price() + " - " + storeDTO.getHighest_price();
        txtStorePrice.setText(price);

    }
    private void addControls(View view) {
        frameStoreDetail = (FrameLayout) view.findViewById(R.id.frameStoreDetail);
        frameStoreImage = (FrameLayout) view.findViewById(R.id.frameStoreImage);
        frameStoreMap = (FrameLayout) view.findViewById(R.id.frameStoreMap);

        btnDirecion = (Button) view.findViewById(R.id.btnDirection);
        btnBackToDetail = (Button) view.findViewById(R.id.btnBackToDetail);
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
                frameStoreDetail.setVisibility(View.VISIBLE);
                frameStoreImage.setVisibility(View.VISIBLE);
                frameStoreMap.setVisibility(View.GONE);
            }
        });
        imageViewMap = (ImageView) view.findViewById(R.id.imageViewMap);
        imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameStoreDetail.setVisibility(View.GONE);
                frameStoreImage.setVisibility(View.GONE);
                frameStoreMap.setVisibility(View.VISIBLE);
            }
        });
        mMap = ((MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.mapStore)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        myProgress = new ProgressDialog(getActivity());
        myProgress.setProgressStyle(myProgress.STYLE_SPINNER);
        myProgress.setCancelable(true);

    }
    private void addControlsDirections() {
        googleDirection = new GoogleDirection(getActivity());
        googleDirection.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
            @Override
            public void onResponse(String status, Document doc, GoogleDirection gd) {
                mMap.addPolyline(googleDirection.getPolyline(doc, 3, Color.RED));
                mMap.addMarker(new MarkerOptions().position(myLatLng).snippet("Vị trí của bạn")
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // Toast.makeText(getActivity(), "onDetach", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // Toast.makeText(getActivity(), "onDestroyView", Toast.LENGTH_SHORT).show();

    }
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
      //  Toast.makeText(getActivity(), "onDestroy", Toast.LENGTH_SHORT).show();
        android.app.FragmentManager fm = getActivity().getFragmentManager();
        android.app.Fragment fragment = (fm.findFragmentById(R.id.mapStore));
        android.app.FragmentTransaction ft = fm.beginTransaction();
        if(fragment != null)
        {
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        historySearchBUS.insertIdStore(storeDTO);
        storeBUS.updateStoreById(storeDTO);

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapStore))
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
    private void setStoreLocation(StoreDTO storeDTO) {
        storeLatLng  = new LatLng(Double.valueOf(storeDTO.getLatitude()),Double.valueOf(storeDTO.getLongitude()));
        Toast.makeText(getActivity(), storeLatLng.longitude + " " + storeLatLng.latitude , Toast.LENGTH_SHORT).show();
        CameraUpdate storeLocation = CameraUpdateFactory.newLatLngZoom(storeLatLng, 13);
        mMap.animateCamera(storeLocation);
        //get camera position
        CameraPosition cameraPosition = new CameraPosition.Builder().target(storeLatLng)
                .zoom(15).bearing(90).tilt(40).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //set option
        MarkerOptions options = new MarkerOptions();
        options.title(storeDTO.getName());
        options.snippet(storeDTO.getAddress());
        options.position(storeLatLng);
        Marker marker = mMap.addMarker(options);
        marker.showInfoWindow();
    }
    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(lastLocation != null)
        {
            myLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
           // Toast.makeText(getActivity(), lastLocation.getLatitude() + " " + lastLocation.getLongitude(), Toast.LENGTH_LONG).show();
            setStoreLocation(storeDTO);
        }
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
