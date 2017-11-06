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
                        sortingList(adapter, sortPriority);
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

    private void sortingList(ArrayAdapter<SkiResort> adapter, SortPriority sortPriority) {
        Comparator<SkiResort> comperator;
        switch (sortPriority) {
            case LOCATION:
                comperator = new DistanceComperator();
                break;
            case WEATHER:
                comperator = new WeatherComperator();
                break;
            default:
                comperator = new DistanceComperator();
        }
        adapter.sort(comperator);
    }

    private class DistanceComperator implements Comparator<SkiResort> {
        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            // nothing to do because list is default Sorted by Distance with Atom-Bomb Search Algorithm
            return 0;
        }
    }

    private class WeatherComperator implements Comparator<SkiResort> {
        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            return Integer.compare(skiResort.getWeatherData().getClouds().getAll(),
                    t1.getWeatherData().getClouds().getAll());
        }
    }
}

