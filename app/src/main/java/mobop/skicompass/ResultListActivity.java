package mobop.skicompass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.firebase.geofire.GeoLocation;
import java.util.ArrayList;
import java.util.List;
import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by artanpapaj on 25.10.17.
 */

public class ResultListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private ListView listView;
    private List<SkiResort> skiResortList;
    private GeoLocation location;
    private SortPriority sortPriority;
    private final SortPriority[] sortPriorityValues = SortPriority.values();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        toolbar = (Toolbar) findViewById(R.id.listActivityToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupSpinner(sortPriority);
    }

    public void disableLoadingSymbol() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void setupSpinner(SortPriority defaultSortPriority) {
        Spinner spinner = toolbar.findViewById(R.id.spinnerSearchCriteria);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerSortItems, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(defaultSortPriority.getValue());
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * On activity resume (activity lifecycle)
     */
    @Override
    protected void onResume() {
        super.onResume();
        SkiResortArrayAdapter adapter = new SkiResortArrayAdapter(this, skiResortList);
        listView.setAdapter(adapter);
        if (adapter.getCount() == 0) {
            SkiResortManager.getInstance().loadNearestResorts(location, adapter, sortPriority);
        }
    }

    /**
     * On list item clicked
     * @param adapterView
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("selectedItem", skiResortList.get(position));
        startActivity(intent);
    }

    /**
     * Spinner item selected
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SkiResortManager.getInstance().sortingList(sortPriorityValues[i]);
    }

    /**
     * Nothing on spinner selected
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }
}
