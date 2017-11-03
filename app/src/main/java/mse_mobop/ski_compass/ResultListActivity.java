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

import mse_mobop.ski_compass.DataArchitecture.SkiResort;
import mse_mobop.ski_compass.DataArchitecture.SkiResortArrayAdapter;

/**
 * Created by artanpapaj on 25.10.17.
 */

public class ResultListActivity extends ListActivity {

    private List<SkiResort> skiResortList;
    private GeoLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (skiResortList == null){
            skiResortList = new ArrayList<>();
        }
        //setContentView(R.layout.list_layout);

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("Latitude", 0.0);
        double longitude = intent.getDoubleExtra("Longitude", 0.0);
        location = new GeoLocation(latitude, longitude);

//        ArrayAdapter<SkiResort> adapter = new ArrayAdapter<SkiResort>(this, android.R.layout.simple_list_item_1, skiResorts);
//        SkiResortManager.getInstance().loadNearestResorts(location, adapter);
//        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getApplicationContext(), "Position :" + position, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // construct and register the adapter
        SkiResortArrayAdapter adapter = new SkiResortArrayAdapter(this, skiResortList);
        setListAdapter(adapter);
        SkiResortManager.getInstance().loadNearestResorts(location, adapter);
    }

}
