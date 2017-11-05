package mobop.skicompass;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by artanpapaj on 26.10.17.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView detailText;
    SkiResort selectedResort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        addButtonListener();

        detailText = (TextView) findViewById(R.id.detailText);
        int selectedItemNr = getIntent().getIntExtra("position", -1);

        if (selectedItemNr == -1) {
            // Error
        }

        selectedResort = ResultListActivity.skiResortList.get(selectedItemNr);

        detailText.setText(getResources().getText(R.string.detailName) + ": " + selectedResort.getName() + "\n");
        detailText.append(getResources().getText(R.string.detailOperatingStatus) + ": " + selectedResort.getOperatingStatus() + "\n");
        detailText.append(getResources().getText(R.string.detailWeather) + ": " + selectedResort.getWeatherData().getWeather().get(0).getDescription());


    }

    private void addButtonListener() {
        Button navButton = (Button) findViewById(R.id.detailNavButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMapIntent();
            }
        });
    }


    private void createMapIntent() {
        String latLong = selectedResort.getLatitude() + "," + selectedResort.getLongitude();
        Uri mapsIntentUri = Uri.parse("geo:" + latLong + "?z=10&q=" + latLong + "(" + selectedResort.getName() + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.detailErrorNav), Toast.LENGTH_LONG).show();
        }
    }
}
