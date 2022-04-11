package com.example.gpsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gpsapp.User.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gpsapp.databinding.ActivityMapsBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private User user;
    private final CollectionReference user_data = new Database().getUser_data();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent mapsActivity = getIntent();
        user = (User) mapsActivity.getSerializableExtra("User");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //read from db and create marker
        user_data.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String username = documentSnapshot.getId();
                User user_ = documentSnapshot.toObject(User.class);
                LatLng user_position = new LatLng(user_.getLattitude(), user_.getLongitude());

                if (user_.getAcceleration() > 30 || user_.getLast_speed() > 120 || user_.getAcceleration() < -19) {
                    mMap.addMarker(new MarkerOptions().position(user_position).title("User:" + username).snippet("Acceleration: " + user_.getAcceleration() + "\n Speed:" + user_.getLast_speed() + "\n Time:" + user_.getTimestamp()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                } else {
                    mMap.addMarker(new MarkerOptions().position(user_position).title("User:" + username).snippet("Acceleration: " + user_.getAcceleration() + "\n Speed:" + user_.getLast_speed() + "\n Time:" + user_.getTimestamp()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                }

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @NonNull
                    @Override
                    public View getInfoContents(Marker marker) {
                        Context context = getApplicationContext();
                        LinearLayout info = new LinearLayout(context);
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(context);
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(context);
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());
                        return info;
                    }

                    @Nullable
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {
                        return null;
                    }

                });

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_position, 16.0f));
            }
        })
            .addOnFailureListener(e -> Log.d("Maps error: ", e.getMessage()));
    }
}


