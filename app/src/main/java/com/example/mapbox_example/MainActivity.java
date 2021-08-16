package com.example.mapbox_example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {


    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager mPermissionsManager;
    private LocationEngine mLocationEngine;
    private EditText editText_search;
    String query;
    MapboxGeocoding mapboxGeocoding;
    Button btn_Geo;
    List<CarmenFeature> results;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        btn_Geo = findViewById(R.id.geoCode_btn);
        mRecyclerView = findViewById(R.id.recyclerView_search_location);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        editText_search = findViewById(R.id.edittext_search);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }


    private void setUpAdapter(List<CarmenFeature> response) {

        List<Location> mLocation = new ArrayList<>();

        for (int i = 0; i < response.size(); i++) {
            Point resultPoint = results.get(i).center();
            Location location = new Location(resultPoint.toString(), resultPoint.latitude(), resultPoint.longitude());
            mLocation.add(location);
        }

        LocationAdapter adapter = new LocationAdapter(mLocation);
        mRecyclerView.setAdapter(adapter);
        //TODO
    }


    private class LocationHolder extends RecyclerView.ViewHolder {

        private Button mButton;
        private Location mLocation;

        public LocationHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mButton = itemView.findViewById(R.id.btn_location);
        }


        public void binLocation(Location location) {
            mLocation = location;
            mButton.setText(location.address);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "you click to choose a location", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


    private class LocationAdapter extends RecyclerView.Adapter<LocationHolder> {
        private List<Location> mLocations;

        public List<Location> getLocations() {
            return mLocations;
        }

        public void setLocations(List<Location> locations) {
            mLocations = locations;
        }

        public LocationAdapter(List<Location> locations) {
            mLocations = locations;
        }

        @NonNull
        @NotNull
        @Override
        public LocationHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.rec_view_location, parent, false);
            return new LocationHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MainActivity.LocationHolder holder, int position) {

            Location location = mLocations.get(position);
            holder.binLocation(location);


        }

        @Override
        public int getItemCount() {
            return mLocations.size();
        }
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
        map = mapboxMap;


        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(Style style) {
                enableLocationComponent(style);

                geoCode(mapboxMap);
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void enableLocationComponent(Style style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponent locationComponent = map.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this,
                            style).build());

            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            mPermissionsManager = new PermissionsManager(this);
            mPermissionsManager.requestLocationPermissions(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void geoCode(MapboxMap mapboxMap) {

        btn_Geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = editText_search.getText().toString();
                mapboxGeocoding = MapboxGeocoding.builder()
                        .accessToken("pk.eyJ1IjoiYW1lbmUta2hhbGlsaSIsImEiOiJja3NjenJubG8wbG12Mm9vZHprZTc5Nmg4In0.2HaGBe3yPxa3Dy3aZDWMrQ")
                        .query(query)

                        .build();
                mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                        results = response.body().features();
                        if (results.size() > 0) {
                            // Log the first results Point.
                            for (int i = 0; i < results.size(); i++) {
                                Point firstResultPoint = results.get(i).center();
                                Log.d(TAG, "onResponse: " + firstResultPoint.latitude() + "****" + firstResultPoint.longitude());
                                Toast.makeText(MainActivity.this, firstResultPoint.latitude() +
                                        "       ////       " + firstResultPoint.longitude(), Toast.LENGTH_LONG).show();
                                AddMarker(firstResultPoint, mapboxMap);

                                animateCamera(firstResultPoint, mapboxMap);
                                setUpAdapter(results);

                            }
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

    private void AddMarker(Point firstResultPoint, MapboxMap mapboxMap) {
        MarkerOptions options = new MarkerOptions();
        options.title("selected location");
        options.position(new LatLng(firstResultPoint.latitude(), firstResultPoint.longitude()));
        mapboxMap.addMarker(options);
    }

    private void animateCamera(Point firstResultPoint, MapboxMap mapboxMap) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(firstResultPoint.latitude(), firstResultPoint.longitude()))
                .zoom(10)
                .tilt(20)
                .build();

        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }


    //MODEL FOR REC
    public class Location {


        String address;
        double lat;
        double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Location(String address, double lat, double lon) {
            this.address = address;
            this.lat = lat;
            this.lon = lon;
        }

        public Location() {

        }
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.mapbox_plugins_place_picker_user_location_permission_explanation, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            map.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull @NotNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.mapbox_plugins_place_picker_user_location_permission_not_granted, Toast.LENGTH_SHORT).show();
        }

    }
}