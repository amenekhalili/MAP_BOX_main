package com.example.mapbox_example;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;




import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;





public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback  {




    private static String[] READ_PHONE = {
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_PHONE_STATE,
    };

    public static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private CarmenFeature iran;
    private CarmenFeature work;
    private String geoJsonSourceLayerId = "geoJsonSourceLayerId";
    private String symbolIconId = "symbolIconId";

    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager mPermissionsManager;

    private EditText editText_search;
    String query;
    MapboxGeocoding mapboxGeocoding;
    Button btn_Geo;

    private RecyclerView mRecyclerView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);




        //btn_Geo = findViewById(R.id.geoCode_btn);
        //  mRecyclerView = findViewById(R.id.recyclerView_search_location);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // editText_search = findViewById(R.id.edittext_search);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }


   /* private final static int REQUEST_CODE_ASK_PERMISSIONS = 1002;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "READ_PHONE_STATE Denied", Toast.LENGTH_SHORT)
                            .show();
                } else {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void setUpAdapter(List<CarmenFeature> response) {

        List<Location> mLocation = new ArrayList<>();

        for (int i = 0; i < response.size(); i++) {
            Location location = new Location(response.get(i).placeName() + response.get(i).address(),
                    response.get(i).center().latitude(), response.get(i).center().longitude());
            mLocation.add(location);
        }

        LocationAdapter adapter = new LocationAdapter(mLocation);
        mRecyclerView.setAdapter(adapter);
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

                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(mLocation.getLat(), mLocation.getLon()))
                            .zoom(10)
                            .tilt(20)
                            .build();

                    map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                    MarkerOptions options = new MarkerOptions();
                    options.title("selected location");
                    options.position(new LatLng(location.getLat(), location.getLon()));
                    map.addMarker(options);
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

*/
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

                initSearchFab();

                style.addImage(symbolIconId, BitmapFactory.decodeResource(
                        getResources(), R.drawable.mapbox_marker_icon_default
                ));
                setUpSource(style);
                setupLayer(style);

                // geoCode(mapboxMap);
            }
        });
    }

    private void setupLayer(Style style) {
        style.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geoJsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[]{0f, -8f})
        ));
    }

    private void setUpSource(Style style) {
        style.addSource(new GeoJsonSource(geoJsonSourceLayerId));
    }


    private void initSearchFab() {

        findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .country("IR")
                                .bbox(43.673647326134784,23.253854760103223,65.21172669473009,40.257577132816834)
                                .geocodingTypes("locality")
                                .language("fa")
                                .proximity(Point.fromLngLat(53.6830157,32.4207423 ))
                                .build(PlaceOptions.MODE_CARDS))
                        .build(MainActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

// Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

// Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
// Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (map != null) {
                Style style = map.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geoJsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

// Move map camera to the selected location
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
    }

   /* private void geoCode(MapboxMap mapboxMap) {

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
                        List<CarmenFeature> results = response.body().features();
                        if (results.size() > 0) {
                            // Log the first results Point.
                            for (int i = 0; i < results.size(); i++) {
                                Point firstResultPoint = results.get(i).center();
                                Log.d(TAG, "onResponse: " + firstResultPoint.latitude() + "****" + firstResultPoint.longitude());
                            }

                            setUpAdapter(results);

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
*/

 /*   //MODEL FOR REC
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
    }*/

}