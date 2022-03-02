package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.googlemap.Models.ModelApi;
import com.example.googlemap.url.ApiUrl;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.example.googlemap.databinding.ActivityMapsBinding;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsActivity extends FragmentActivity implements OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    MapsActivity instance;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//               ApiThread api=new ApiThread(latLng.latitude, latLng.longitude,instance);
//                api.execute();
                ApiUrl url=new ApiUrl();
                Retrofit retrofit=url.getConnect();
                ApiCall apiCall = retrofit.create(ApiCall.class);
                Call<ModelApi> call = apiCall.getData(latLng.latitude,latLng.longitude);
                call.enqueue(new Callback<ModelApi>(){
                    @Override
                    public void onResponse(Call<ModelApi> call, Response<ModelApi> response) {
                        Log.i("testApi", response.toString());
                        if(response.code()!=200){
                            Log.i("testApi", "checkConnection");
                            return;
                        }
                        if (response.isSuccessful()) {
                            ArrayList<String> url=new ArrayList<>();
                            Intent intent = new Intent(getApplicationContext(),slider.class);
                            Bundle bundle = new Bundle();
                            Log.i("testApi",  ""+response.body().getPhotos().getPhoto().size());
                            if(response.body().getPhotos().getPhoto().size()>10){
                            for(int i=0;i<10;i++){
                                String server_id=response.body().getPhotos().getPhoto().get(i).getServer();
                                String id=response.body().getPhotos().getPhoto().get(i).getId();
                                String secret=response.body().getPhotos().getPhoto().get(i).getSecret();
                                url.add("https://live.staticflickr.com/"+server_id+"/"+id+"_"+secret+".jpg");
                            }}else{
                                for(int i=0;i<response.body().getPhotos().getPhoto().size();i++){
                                    String server_id=response.body().getPhotos().getPhoto().get(i).getServer();
                                    String id=response.body().getPhotos().getPhoto().get(i).getId();
                                    String secret=response.body().getPhotos().getPhoto().get(i).getSecret();
                                    url.add("https://live.staticflickr.com/"+server_id+"/"+id+"_"+secret+".jpg");
                                }
                            }
                            bundle.putStringArrayList("url", url);
                            intent.putExtra("url",bundle);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onFailure(Call<ModelApi> call, Throwable t) {
                        Log.i("testApi", "failed");
                    }
                });




            }
        });
        enableMyLocation();
    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {

        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.

            permissionDenied = false;
        }
    }


}