package com.example.tugasppb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.none:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button go = (Button) findViewById(R.id.btnGo);
        go.setOnClickListener(op);
        Button cari = (Button) findViewById(R.id.btnCari);
        cari.setOnClickListener(op);

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

        // Add a marker in Sydney and move the camera
        LatLng ITS = new LatLng(-7.2819705, 112.795323);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(newLatLngZoom(ITS, 15));
    }


    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnGo:
                    sembunyikanKeyboard(view);
                    gotoLokasi();
                    break;
                case R.id.btnCari:
                    goCari();
                    break;


            }
        }
    };


    private void gotoLokasi() {
        EditText lat = (EditText) findViewById(R.id.lat);
        EditText lng = (EditText) findViewById(R.id.lng);
        EditText zoom = (EditText) findViewById(R.id.zoom);

        Double dbllat = Double.parseDouble(lat.getText().toString());
        Double dbllng = Double.parseDouble(lng.getText().toString());
        Float dblzoom = Float.parseFloat(zoom.getText().toString());
        Toast.makeText(this, "Move to Lat:" + dbllat + " Long:" + dbllng, Toast.LENGTH_LONG).show();
        gotoPeta(dbllat, dbllng, dblzoom);
    }

    private void sembunyikanKeyboard(View view) {
        InputMethodManager a = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void gotoPeta(Double lat, Double lng, float z) {

        LatLng Lokasibaru = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().
                position(Lokasibaru).
                title("Marker in  " + lat + ":" + lng));
        mMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(Lokasibaru, z));
    }

    private void goCari() {
        EditText tempat = (EditText) findViewById(R.id.idCari);
        EditText zoom = (EditText) findViewById(R.id.zoom);
        float dblzoom = Float.parseFloat(zoom.getText().toString());
        Geocoder g = new Geocoder(getBaseContext());
        List<Address> daftar = null;
        try {
            daftar = g.getFromLocationName(tempat.getText().toString(), 1);
            Address alamat = daftar.get(0);

            String nemuAlamat = alamat.getAddressLine(0);
            Double lintang = alamat.getLatitude();
            Double bujur = alamat.getLongitude();

            Toast.makeText(getBaseContext(), "Ketemu" + nemuAlamat, Toast.LENGTH_LONG).show();

            Toast.makeText(this, "Move to " + nemuAlamat + " Lat:" +
                    lintang + " Long:" + bujur, Toast.LENGTH_LONG).show();
            gotoPeta(lintang, bujur, dblzoom);

            EditText lat = (EditText) findViewById(R.id.lat);
            EditText lng = (EditText) findViewById(R.id.lng);
            lat.setText(lintang.toString());
            lng.setText(bujur.toString());

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}

