package mobop.skicompass;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Comparator;
import mobop.skicompass.dataarchitecture.SkiResort;
import static android.content.ContentValues.TAG;

/**
 * Created by Martin on 27.10.2017.
 */

public class SkiResortManager {

    private static final int MAX_RESULTS = 10;
    private static SkiResortManager instance;
    private ArrayAdapter<SkiResort> adapter;
    private GeoLocation location;

    private int numberOfResorts = 0;
    GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference("GeoFire"));

    private SkiResortManager() {
    }

    public static SkiResortManager getInstance() {
        if (instance == null) {
            instance = new SkiResortManager();
        }
        return instance;
    }

    public void loadNearestResorts(GeoLocation location, final ArrayAdapter<SkiResort> adapter, final SortPriority sortPriority) {
        this.adapter = adapter;
        this.location = location;
        numberOfResorts = 0;
        final GeoQuery query = geoFire.queryAtLocation(location, 10);
        query.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, final GeoLocation location) {
                numberOfResorts++;
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("skiresorts").child(key);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange ValueEvent: " + dataSnapshot);
                        SkiResort resort = dataSnapshot.getValue(SkiResort.class);
                        adapter.add(resort);
                        sortingList(sortPriority);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled: " + databaseError);
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
                // do nothing
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                // do nothing
            }

            @Override
            public void onGeoQueryReady() {
                if (numberOfResorts < MAX_RESULTS) {
                    query.setRadius(query.getRadius() + 1);
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e(TAG, "onGeoQueryError: " + error);
            }
        });
    }

    public void sortingList(SortPriority sortPriority) {
        Comparator<SkiResort> comparator;
        switch (sortPriority) {
            case LOCATION:
                comparator = new DistanceComparator(location);
                break;
            case WEATHER:
                comparator = new WeatherComparator();
                break;
            case OPERATING:
                comparator = new OperatingComparator();
                break;
            case OPENED_LIFTS:
                comparator = new OpenedLiftsComparator();
                break;
            case OPENED_SLOPS:
                comparator = new OpenedSlopsComparator();
                break;
            default:
                comparator = new DistanceComparator(location);
        }
        adapter.sort(comparator);
    }

    private class DistanceComparator implements Comparator<SkiResort> {

        private GeoLocation location;

        DistanceComparator(GeoLocation location) {
            this.location = location;
        }

        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            double dist1 = distFrom(location.latitude, location.longitude, skiResort.getLatitude(), skiResort.getLongitude());
            double dist2 = distFrom(location.latitude, location.longitude, t1.getLatitude(), t1.getLongitude());
            return Double.compare(dist1, dist2);
        }

        /**
         * Calculates distance between two coordinates given with lat and long
         * @param lat1
         * @param lng1
         * @param lat2
         * @param lng2
         * @return
         */
        private double distFrom(double lat1, double lng1, double lat2, double lng2) {
            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(lat2-lat1);
            double dLng = Math.toRadians(lng2-lng1);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            return earthRadius * c;
        }
    }

    private class WeatherComparator implements Comparator<SkiResort> {
        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            return Integer.compare(skiResort.getWeatherData().getClouds().getAll(),
                    t1.getWeatherData().getClouds().getAll());
        }
    }

    private class OperatingComparator implements  Comparator<SkiResort>{

        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            return Integer.compare(skiResort.getOperatingStatus().getValue(), t1.getOperatingStatus().getValue());
        }
    }

    private class OpenedLiftsComparator implements  Comparator<SkiResort>{

        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            // Changed -> More is better
            return Integer.compare(t1.getOpenedLifts(), skiResort.getOpenedLifts());
        }
    }

    private class OpenedSlopsComparator implements  Comparator<SkiResort>{

        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            // More is Better
            return Double.compare(t1.getOpenedSlops(), skiResort.getOpenedSlops());
        }
    }
}

