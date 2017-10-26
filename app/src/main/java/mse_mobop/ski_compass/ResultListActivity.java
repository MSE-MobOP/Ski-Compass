package mse_mobop.ski_compass;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artanpapaj on 25.10.17.
 */

public class ResultListActivity extends ListActivity {
    List<String> results = new ArrayList<String>();

    protected void loadTestContent() {
        for (int i = 0; i < 15; i++) {
            results.add("Skigebiet " + String.valueOf(i + 1));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        loadTestContent();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getApplicationContext(), "Position :" + position, Toast.LENGTH_LONG).show();
    }

}
