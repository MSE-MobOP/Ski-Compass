package mobop.skicompass;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by artanpapaj on 26.10.17.
 */

public class DetailActivity extends AppCompatActivity {

    private SkiResort selectedResort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        selectedResort = (SkiResort) getIntent().getSerializableExtra("selectedItem");

        addButtonListeners();

        TextView detailText = (TextView) findViewById(R.id.detailText);
        ImageView weatherImage = (ImageView) findViewById(R.id.detailWeatherImage);

        checkWebButton();

        detailText.setText(getResources().getText(R.string.detailName) + ": " + selectedResort.getName() + System.lineSeparator());
        detailText.append(getResources().getText(R.string.detailOperatingStatus) + ": " + selectedResort.getOperatingStatus() + System.lineSeparator());

        String weatherDescription = selectedResort.getWeatherData().getWeather().get(0).getDescription().replace(' ', '_');
        int test = Utils.getStringResourceByName(this, weatherDescription);

        weatherImage.setImageResource(WeatherManager.getImageIdFromDescription(weatherDescription));

        detailText.append(getResources().getText(R.string.detailWeather) + ": " +  System.lineSeparator());//getResources().getText(test) +
        detailText.append("ID: " + selectedResort.getId()); // debug only

    }

    private void checkWebButton() {
        if (selectedResort.getOfficialWebsite() == null)
            findViewById(R.id.detailWebButton).setEnabled(false);
    }

    private void addButtonListeners() {
        Button navButton = (Button) findViewById(R.id.detailNavButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMapIntent();
            }
        });

        Button webButton = (Button) findViewById(R.id.detailWebButton);
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWebIntent();
            }
        });
    }

    private void createWebIntent() {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(selectedResort.getOfficialWebsite()));
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(webIntent);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.detailErrorWeb), Toast.LENGTH_LONG).show();
        }
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
