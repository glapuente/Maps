package com.example.guillermo.maps;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class DisplayMessageActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        //Get GoogleMaps running
        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get data as String from the other activity
        Bundle bundle=getIntent().getExtras();
        String from=bundle.getString("from");
        String to=bundle.getString("to");

        //Get TextViews from this activity and update with incoming strings
        TextView textFrom=(TextView) findViewById(R.id.from);
        TextView textTo=(TextView) findViewById(R.id.to);
        textFrom.setText(from);
        textTo.setText(to);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Get from the other activity data as Strings
        Bundle bundle=getIntent().getExtras();
        String from=bundle.getString("from");
        String to=bundle.getString("to");

        //Geocoder will "translate" the string to Address
        Geocoder geocoder;
        geocoder = new Geocoder(this);

        List<Address> listFrom = null;
        List<Address> listTo = null;

        try {
            listFrom = geocoder.getFromLocationName(from,1);
            listTo = geocoder.getFromLocationName(to,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address addressFrom=listFrom.get(0);
        Address addressTo=listTo.get(0);

        //Calculate LatLng with the String from
        LatLng latFrom=new LatLng(addressFrom.getLatitude(),addressFrom.getLongitude());
        //'from' marker added
        googleMap.addMarker(new MarkerOptions().position(latFrom).title(from));

        //Calculate LatLng with the String to
        LatLng latTo=new LatLng(addressTo.getLatitude(),addressTo.getLongitude());
        //'to' marker added
        googleMap.addMarker(new MarkerOptions().position(latTo).title(to));


        //Focus the camera on the markers
        CameraUpdate cu=CameraUpdateFactory.newLatLngZoom(latFrom,15);
        googleMap.moveCamera(cu);
        googleMap.animateCamera(cu);
    }
}
