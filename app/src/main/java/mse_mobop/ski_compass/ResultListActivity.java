package mse_mobop.ski_compass;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artanpapaj on 25.10.17.
 */

public class ResultListActivity extends ListActivity {

    private List<String> skiResorts = new ArrayList<String>();
    private GeoLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("Latitude", 0.0);
        double longitude = intent.getDoubleExtra("Longitude", 0.0);
        location = new GeoLocation(latitude, longitude);

      //  results.add("Location: " + location.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, skiResorts);
        SkiResortManager.getInstance().loadNearestResorts(location, adapter);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getApplicationContext(), "Position :" + position, Toast.LENGTH_LONG).show();
    }

}
