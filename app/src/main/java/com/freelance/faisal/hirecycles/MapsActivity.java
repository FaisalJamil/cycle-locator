package com.freelance.faisal.hirecycles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.freelance.faisal.hirecycles.Helpers.SessionManager;
import com.freelance.faisal.hirecycles.Helpers.WebServiceCall;
import com.freelance.faisal.hirecycles.Model.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SessionManager session;
    private String access_token;
    private HashMap<String, String> user = new HashMap<>();
    private List<Location> location = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize Session Manager to get/store access token for this session
        session = new SessionManager(getApplicationContext());

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
        final Marker marker;

        if (!session.checkLogin()) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }else {
            user = session.getUserDetails();
            try {
                new WebServiceCall(new WebServiceCall.OnResultReceived() {
                    public void onResult(String data) {
                        final String res = data;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        // Parsing Json response
                                        JSONObject temp = new JSONObject(res);
                                        JSONArray results = temp.getJSONArray("results");
                                        for (int i = 0; i < results.length(); i++) {
                                            String id = results.getJSONObject(i).getString("id");
                                            Double lat = results.getJSONObject(i).getJSONObject("location").getDouble("lat");
                                            Double lng = results.getJSONObject(i).getJSONObject("location").getDouble("lng");
                                            String name = results.getJSONObject(i).getString("name");
                                            location.add(new Location(id, name, lat, lng));
                                        }
                                        Iterator<Location> loc = location.iterator();
                                        Location currentLocation;
                                        while(loc.hasNext()){
                                            currentLocation = loc.next();
                                            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLat(), currentLocation.getLng())).title(currentLocation.getName()));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLat(), currentLocation.getLng())));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    }
                }).asyncGet(getString(R.string.server_url) + getString(R.string.places_url), user.get("auth_key"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
