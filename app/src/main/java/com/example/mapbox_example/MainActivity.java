package com.example.mapbox_example;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private EditText editText_search;
    String query;
    MapboxGeocoding mapboxGeocoding;
    Button btn_Geo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_main);
        btn_Geo = findViewById(R.id.geoCode_btn);

        editText_search = findViewById(R.id.edittext_search);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mapboxGeocoding.cancelCall();

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(Style style) {

geoCode();
            }
        });


    }

    private void geoCode() {
        btn_Geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = editText_search.getText().toString();
                mapboxGeocoding = MapboxGeocoding.builder()
                        .accessToken("pk.eyJ1IjoiYW1lbmUta2hhbGlsaSIsImEiOiJja3NiY3d5azMwNWo1MnVtbHphdnE5c2p0In0.MCefEplSacw44sHZ9XM6Jw")
                        .query(query)

                        .build();
                mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                        List<CarmenFeature> results = response.body().features();

                        if (results.size() > 0) {

                            // Log the first results Point.
                            Point firstResultPoint = results.get(0).center();

                            Log.d(TAG, "onResponse: " + firstResultPoint.latitude() + "****" + firstResultPoint.longitude());

                            //     Toast.makeText(MainActivity.this, firstResultPoint.toString(), Toast.LENGTH_SHORT).show();


                        } else {

                            // No result for your request were found.
                            Log.d(TAG, "onResponse: No result found");

                        }


                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });


    }


}