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

public class SkiResortManager implements GeoQueryEventListener{

    private static final int MAX_RESULTS = 10;
    private static SkiResortManager instance;
    private ArrayAdapter<SkiResort> adapter;
    private GeoLocation location;
    private SortPriority sortPriority;
    private GeoQuery query;
    private ComparatorFactory comparatorFactory;

    private int numberOfResorts = 0;
    GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference("GeoFire"));

    private SkiResortManager() {
        comparatorFactory = new ComparatorFactory();
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
        this.sortPriority = sortPriority;
        numberOfResorts = 0;
        query = geoFire.queryAtLocation(location, 10);
        query.addGeoQueryEventListener(this);
    }

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

    public void sortingList(SortPriority sortPriority) {
        Comparator<SkiResort> comparator;
        switch (sortPriority) {
            case LOCATION:
                comparator = comparatorFactory.getDistanceComparator(location);
                break;
            case WEATHER:
                comparator = comparatorFactory.getWeatherComparator();
                break;
            case OPERATING:
                comparator = comparatorFactory.getOperatingComparator();
                break;
            case OPENED_LIFTS:
                comparator = comparatorFactory.getOpenedLiftsComparator();
                break;
            case OPENED_SLOPS:
                comparator = comparatorFactory.getOpenedSlopsComparator();
                break;
            default:
                comparator = comparatorFactory.getDistanceComparator(location);
        }
        adapter.sort(comparator);
    }
}

