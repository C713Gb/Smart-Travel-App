package com.example.smarttravel.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smarttravel.Fragments.SetRouteFragment;
import com.example.smarttravel.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MapActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, PermissionsListener,
        MapboxMap.OnFlingListener, MapboxMap.OnMoveListener, MapboxMap.OnCameraMoveListener {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    @SuppressLint("StaticFieldLeak")
    private static MapView mapView;
    public MapboxMap mapboxMap;
    PermissionsManager permissionsManager;
    public int count123 = 0;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static Boolean isvis = false;
    public Location location;
    public String placeName = "", lat = "", lng = "";
    public Point origin = null, destination = null;
    ProgressDialog progressDialog;
    DirectionsRoute currentRoute;
    public BottomSheetBehavior bottomSheetBehavior;
    public StringBuilder str_date;
    public Calendar calendar;

    private final LocationChangeListeningActivityLocationCallback callback =
            new LocationChangeListeningActivityLocationCallback(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        ImageView back = findViewById(R.id.back_btn);
        back.setOnClickListener(view -> onBackPressed());

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        calendar = Calendar.getInstance();

        ImageButton zoom = findViewById(R.id.zoom_btn);
        zoom.setOnClickListener(view -> {
            if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) {
                com.mapbox.mapboxsdk.geometry.LatLng abc = new com.mapbox.mapboxsdk.geometry.LatLng();
                abc.setLatitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
                abc.setLongitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude());
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(abc)
                        .zoom(14f)
                        .bearing(0)
                        .padding(0, 0, 0, 500)
                        .build()), 500);

            } else {
                Toast.makeText(getApplicationContext(), "Please Turn on Location...", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom,
                new SetRouteFragment()).commit();

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MapActivity.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/uchiha-itachi/ckgotitik0d7819lata9c7x82")
                , style -> {

                    enableLocationComponent(style);
                    resetcamera();
                    initSource(style);
                    initLayers(style);

                    mapboxMap.addOnCameraMoveListener(MapActivity.this);
                    mapboxMap.addOnFlingListener(MapActivity.this);
                    mapboxMap.addOnMoveListener(MapActivity.this);
                }
        );
    }

    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource("ROUTE_SOURCE_ID"));
    }

    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer("ROUTE_LAYER_ID", "ROUTE_SOURCE_ID");

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(getResources().getColor(R.color.purple_500))
        );
        loadedMapStyle.addLayer(routeLayer);

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MapActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int @NotNull [] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void resetcamera()
    {
        Handler h = new Handler();

        h.postDelayed(() -> {
            count123++;
            try {

                if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) {
                    try {

                        if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) {
                            com.mapbox.mapboxsdk.geometry.LatLng abc = new com.mapbox.mapboxsdk.geometry.LatLng();
                            abc.setLatitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
                            abc.setLongitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude());
                            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                    .target(abc)
                                    .zoom(14f)
                                    .bearing(0)
                                    .padding(0, 0, 0, 500)
                                    .build()), 500);

                        } else {
                            Toast.makeText(getApplicationContext(), "Please Turn on Location...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Please Turn on Location...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (count123 <= 10) {
                        resetcamera();
                    }
                }
            } catch (Exception ignored) {

            }
        }, 750);


    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style style) {

        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            try {
                // Get an instance of the component
                LocationComponent locationComponent = mapboxMap.getLocationComponent();

                // Activate with a built LocationComponentActivationOptions object
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build());

                // Enable to make component visible
                locationComponent.setLocationComponentEnabled(true);

                // Set the component's camera mode
                locationComponent.setCameraMode(CameraMode.TRACKING);

                // Set the component's render mode
                locationComponent.setRenderMode(RenderMode.COMPASS);

                initLocationEngine();
            } catch (Exception e) {
                Toast.makeText(this, "Please Turn on Location...", Toast.LENGTH_SHORT).show();
            }

        } else {

            permissionsManager = new PermissionsManager(this);

            permissionsManager.requestLocationPermissions(this);

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
    protected void onSaveInstanceState(@NotNull Bundle outState) {
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
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(this::enableLocationComponent);
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    public void searchLocation() {

        Point myPoint;
        if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) {
            com.mapbox.mapboxsdk.geometry.LatLng abc = new com.mapbox.mapboxsdk.geometry.LatLng();
            abc.setLatitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
            abc.setLongitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude());
            myPoint = Point.fromLngLat(abc.getLongitude(), abc.getLatitude());

            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(Color.parseColor("#EEEEEE"))
                            .toolbarColor(getResources().getColor(R.color.purple_500))
                            .limit(10)
                            .proximity(myPoint)
                            .country("IN")
                            .build(PlaceOptions.MODE_CARDS))
                    .build(MapActivity.this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        }
        else{
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(Color.parseColor("#EEEEEE"))
                            .toolbarColor(getResources().getColor(R.color.purple_500))
                            .limit(10)
                            .country("IN")
                            .build(PlaceOptions.MODE_CARDS))
                    .build(MapActivity.this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        }


    }

    public void showDirection(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading Route...");
        progressDialog.show();

        if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) {
            com.mapbox.mapboxsdk.geometry.LatLng abc = new com.mapbox.mapboxsdk.geometry.LatLng();
            abc.setLatitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
            abc.setLongitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude());
            origin = Point.fromLngLat(abc.getLongitude(), abc.getLatitude());

            routeSource();
        }
    }

    private void routeSource() {

        MapboxDirections client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();


        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                progressDialog.dismiss();
                Timber.d("onResponse: Response code %s", response.code());
                if (response.body() == null) {
                    Timber.d("No routes found, make sure you set the right user and access token.");
                    return;
                }
                // Get the directions route
                currentRoute = response.body().routes().get(0);

                // Make a toast which displays the route's distance
//                Toast.makeText(MapActivity.this, ""+currentRoute.distance(), Toast.LENGTH_SHORT).show();

                if (mapboxMap != null) {
                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                            // Retrieve and update the source designated for showing the directions route
                            GeoJsonSource source = style.getSourceAs("ROUTE_SOURCE_ID");

                            // Create a LineString with the directions route's geometry and
                            // reset the GeoJSON source for the route LineLayer source
                            if (source != null) {
                                source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                                LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                        .include(new LatLng(origin.latitude(), origin.longitude()))
                                        .include(new LatLng(destination.latitude(), destination.longitude()))
                                        .build();
                                int[] padding = {40,10,40,1000};
                                CameraPosition cameraPosition = mapboxMap.getCameraForLatLngBounds
                                        (latLngBounds,padding);
                                mapboxMap.easeCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition),
                                        1000);
                            }
                        }
                    });
                }

            }

            @Override public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {

                progressDialog.dismiss();
                Timber.d("Error: %s", throwable.getMessage());

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            mapboxMap.clear();
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            Timber.d(selectedCarmenFeature.placeName());

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    placeName = selectedCarmenFeature.placeName();

                    LatLng loc = new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                            ((Point) selectedCarmenFeature.geometry()).longitude());

                    lat = Double.toString(loc.getLatitude());
                    lng = Double.toString(loc.getLongitude());

                    destination = Point.fromLngLat(loc.getLongitude(), loc.getLatitude());

                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                            .title(placeName));

                    showDirection();

                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(2000)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(2000)
                .setFastestInterval(100)
                .build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onFling() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onMoveBegin(@NonNull MoveGestureDetector detector) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onMove(@NonNull MoveGestureDetector detector) {

    }

    @Override
    public void onMoveEnd(@NonNull MoveGestureDetector detector) {

    }

    @Override
    public void onCameraMove() {

    }

    public void goToHome() {
        Intent intent = new Intent(MapActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private static class LocationChangeListeningActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MapActivity> activityWeakReference;

        LocationChangeListeningActivityLocationCallback(MapActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MapActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                // Create a Toast which displays the new location's coordinates
                Timber.d(result.getLastLocation().getLatitude() + " " + result.getLastLocation().getLongitude());


                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                    activity.location =result.getLastLocation();
                    // Log.d("getlegs",String.valueOf(result.getLastLocation().getBearing()));
                    try {
                        if (!isvis) {

                            mapView.setVisibility(View.INVISIBLE);

                            Handler h = new Handler();

                            h.postDelayed(() -> {
                                mapView.setVisibility(View.VISIBLE);
                                isvis = true;
                            }, 750);

                        }
                    } catch (Exception e) {
                        Timber.d(e);
                    }
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Timber.d(exception.getLocalizedMessage());
            MapActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    calendar.set(Calendar.YEAR, arg3);
                    calendar.set(Calendar.MONTH, arg2+1);
                    calendar.set(Calendar.DAY_OF_MONTH, arg1);
                    showDate(arg1, arg2+1, arg3);
                }
            };

    public void showDate(int year, int month, int day) {
        str_date = new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year);

        SetRouteFragment fragment = (SetRouteFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container_bottom);

        assert fragment != null;
        fragment.updateDate(str_date);
    }
}