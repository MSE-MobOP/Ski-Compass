package mobop.skicompass;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
        checkWebButton();
        setWeatherIcon();


        TextView detailText = (TextView) findViewById(R.id.detailText);

        detailText.setText(getResources().getText(R.string.detailName) + ": " + selectedResort.getName() + System.lineSeparator());
        detailText.append(getResources().getText(R.string.detailOperatingStatus) + ": " + selectedResort.getOperatingStatus() + System.lineSeparator());

        detailText.append(getResources().getText(R.string.detailWeather) + ": " +  System.lineSeparator());//getResources().getText(test) +
        detailText.append("ID: " + selectedResort.getId()); // debug only

    }

    private void setWeatherIcon() {
        String weatherIconName = selectedResort.getWeatherData().getWeather().get(0).getIcon();
        if (weatherIconName.isEmpty() || weatherIconName.equals(""))
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.detailErrorWeatherIcon), Toast.LENGTH_LONG).show();

        System.out.println("*******************************http://openweathermap.org/img/w/" + weatherIconName + ".png");
        Drawable weatherIcon = Utils.getDrawableFromUrl("http://openweathermap.org/img/w/" + weatherIconName + ".png");
        ((ImageView) findViewById(R.id.detailWeatherImage)).setImageDrawable(weatherIcon);
    }

    private void checkWebButton() {
        if (selectedResort.getOfficialWebsite() == null) {
            ImageButton imgButton = (ImageButton) findViewById(R.id.detailWebButton);
            imgButton.setEnabled(false);
            imgButton.setBackground(getResources().getDrawable(R.mipmap.detail_web_deactivated));
        }
    }

    private void addButtonListeners() {
        ImageButton navButton = (ImageButton) findViewById(R.id.detailNavButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMapIntent();
            }
        });

        ImageButton webButton = (ImageButton) findViewById(R.id.detailWebButton);
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
