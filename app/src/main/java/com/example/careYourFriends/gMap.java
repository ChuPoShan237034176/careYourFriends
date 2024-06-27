package com.example.careYourFriends;

import static android.content.ContentValues.TAG;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class gMap extends AppCompatActivity {

    private apicall api;
    private apicall.HKO_Data Data;

    TextView wText;

    MapView mView;

    GoogleMap map;

    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gmap);

        wText =findViewById(R.id.wTextView);

        api=new apicall();

        api.HKO_Data_Req(new Callback<ResponseBody>()  {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.body()!=null){
                    try {
                        Data=api.HKO_Data_Res(response.body().string());
                        wText.setText(Data.forecastPeriod + "\n" + Data.generalSituation + "\n" + Data.outlook);
                        Log.i(TAG,Data.toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "get HKO Error!");
            }
        });


        mView = (MapView) findViewById(R.id.mView);
        mView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                if (!(ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                }
            }
        });

        if (!(ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    lm.removeUpdates(this);


                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());


                    CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(latLng, 17.0F);

                    map.animateCamera(myLocation);

                }
            }); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

        }

    }
}