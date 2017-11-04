package mobop.skicompass;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private boolean hasLocationPermission = false;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // test

        checkLocationPermission();
        if (hasLocationPermission) {
            location = getLocation();
        }
    }


    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Do nothing
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            hasLocationPermission = false;
        } else {
            hasLocationPermission = true;
        }
    }

    public Location getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            for (String provider : providers) {
                Location lastLocation = locationManager.getLastKnownLocation(provider);
                if (lastLocation != null) {
                    return lastLocation;
                }
            }
        }
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    hasLocationPermission = true;
                }
            } else {
                hasLocationPermission = false;
            }
            return;
        }
    }

    public void testList() {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "No Location found. Is your GPS active?", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, ResultListActivity.class);
        intent.putExtra("Latitude", location.getLatitude());
        intent.putExtra("Longitude", location.getLongitude());
        startActivity(intent);
    }

    public void testDetail() {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

}
