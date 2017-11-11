package mobop.skicompass;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

@java.lang.SuppressWarnings("squid:MaximumInheritanceDepth") // AppCompatActivity has already too much parents... Would be kind of a lot work to make it better
public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final String WRONG_BUTTON = "Wrong binding of Button";

    private Location location;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startLocationListener();
    }

    public void startLocationListener() {
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
        } else {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            startLocationListener();
        }
    }

    public void listByDistance(View v) {
        if (v.getId() != R.id.listDistanceButton) {
            throw new IllegalArgumentException(WRONG_BUTTON);
        }
        list(SortPriority.LOCATION);
    }

    public void listByWeather(View v) {
        if (v.getId() != R.id.listWeatherButton) {
            throw new IllegalArgumentException(WRONG_BUTTON);
        }
        list(SortPriority.WEATHER);
    }

    public void listByOperatingStatus(View v) {
        if (v.getId() != R.id.listOperatingStatusButton) {
            throw new IllegalArgumentException(WRONG_BUTTON);
        }
        list(SortPriority.OPERATING);
    }

    public void listByOpenedLifts(View v){
        if (v.getId() != R.id.listLiftsButton) {
            throw new IllegalArgumentException(WRONG_BUTTON);
        }
        list(SortPriority.OPENED_LIFTS);
    }

    public void listByOpenedSlops(View v){
        if (v.getId() != R.id.listSlopsButton) {
            throw new IllegalArgumentException(WRONG_BUTTON);
        }
        list(SortPriority.OPENED_SLOPS);
    }

    private void list(SortPriority sortPriority) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), R.string.noGPS, Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, ResultListActivity.class);
        intent.putExtra("Latitude", location.getLatitude());
        intent.putExtra("Longitude", location.getLongitude());
        intent.putExtra("SortPriority", sortPriority);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Called when new Location is found
        if (this.location == null) {
            this.location = location;
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(this);
        } else if (location != null && location.getAccuracy() < this.location.getAccuracy()) {
            this.location = location;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // Do nothing
    }

    @Override
    public void onProviderEnabled(String s) {
        // Do nothing
    }

    @Override
    public void onProviderDisabled(String s) {
        // Do nothing
    }
}
