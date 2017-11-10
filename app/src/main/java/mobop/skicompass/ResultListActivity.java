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

@java.lang.SuppressWarnings("squid:MaximumInheritanceDepth") // AppCompatActivity has already too much parents... Would be kind of a lot work to make it better
public class ResultListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private List<SkiResort> skiResortList;
    private final SortPriority[] sortPriorityValues = SortPriority.values();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (skiResortList == null){
            skiResortList = new ArrayList<>();
        }
        setContentView(R.layout.list_layout);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("Latitude", 0.0);
        double longitude = intent.getDoubleExtra("Longitude", 0.0);
        SortPriority sortPriority = (SortPriority)intent.getSerializableExtra("SortPriority");
        GeoLocation location = new GeoLocation(latitude, longitude);

        SkiResortArrayAdapter skiResortArrayAdapter = new SkiResortArrayAdapter(this, skiResortList);
        listView.setAdapter(skiResortArrayAdapter);
        SkiResortManager.getInstance().loadNearestResorts(location, skiResortArrayAdapter, sortPriority);

        toolbar = (Toolbar) findViewById(R.id.listActivityToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        setupSpinner(sortPriority);
    }

    /**
     * This hides the loading symbol. Should be called after list has at least one element.
     */
    public void disableLoadingSymbol() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    /**
     * This shows the loading symbol. Call if list to display is empty.
     */
    public void enableLoadingSymbol() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    /**
     * Sets the spinner to choose the sorting algorithm
     * @param defaultSortPriority
     */
    private void setupSpinner(SortPriority defaultSortPriority) {
        Spinner spinner = toolbar.findViewById(R.id.spinnerSearchCriteria);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerSortItems, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(defaultSortPriority.getValue());
        spinner.setOnItemSelectedListener(this);
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
