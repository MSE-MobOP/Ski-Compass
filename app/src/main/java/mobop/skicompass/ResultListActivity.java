package mobop.skicompass;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.List;

import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by artanpapaj on 25.10.17.
 */

public class ResultListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<SkiResort> skiResortList;
    private GeoLocation location;
    private SortPriority sortPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        if (skiResortList == null){
            skiResortList = new ArrayList<>();
        }
        setContentView(R.layout.list_layout);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("Latitude", 0.0);
        double longitude = intent.getDoubleExtra("Longitude", 0.0);
        sortPriority = (SortPriority)intent.getSerializableExtra("SortPriority");
        location = new GeoLocation(latitude, longitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkiResortArrayAdapter adapter = new SkiResortArrayAdapter(this, skiResortList);
        listView.setAdapter(adapter);
        if (adapter.getCount() == 0) {
            SkiResortManager.getInstance().loadNearestResorts(location, adapter, sortPriority);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // Toast.makeText(getApplicationContext(), "Position :" + position, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("selectedItem", skiResortList.get(position));
        startActivity(intent);
    }

    public void disableLoadingSymbol() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}
