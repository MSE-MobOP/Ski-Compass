package mobop.skicompass;

import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import mobop.skicompass.dataarchitecture.OperatingStatus;
import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by artanpapaj on 26.10.17.
 */

public class DetailActivity extends AppCompatActivity {

    private SkiResort selectedResort;
    private ListView detailList;
    private DetailRowData[] rowData;
    private final int numRows = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        selectedResort = (SkiResort) getIntent().getSerializableExtra("selectedItem");

        detailList = (ListView) findViewById(R.id.detailList);
        detailList.setEnabled(false);
        rowData = new DetailRowData[numRows];

        setTitle(selectedResort.getName());

        setWeatherIcon();
        setWeatherDescription();
        setStatusDescription();

        DetailArrayAdapter detailArrayAdapter = new DetailArrayAdapter(this, rowData);
        detailList.setAdapter(detailArrayAdapter);

        checkWebButton();
    }

    private void setWeatherDescription() {
        String weatherDescriptionText;

        switch (Locale.getDefault().getLanguage()){
            case "de":
                weatherDescriptionText = selectedResort.getWeatherData().getWeather().get(0).getDescriptionDE();
                break;
            case "fr":
                weatherDescriptionText = selectedResort.getWeatherData().getWeather().get(0).getDescriptionFR();
                break;
            default:
                weatherDescriptionText = selectedResort.getWeatherData().getWeather().get(0).getDescription();
        }

        // fill detailList
        rowData[0] = new DetailRowData(getResources().getString(R.string.detailWeather), weatherDescriptionText);
    }

    private void setWeatherIcon() {
        String weatherIconName = selectedResort.getWeatherData().getWeather().get(0).getIcon();
        if (weatherIconName.isEmpty() || weatherIconName.equals(""))
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.detailErrorWeatherIcon), Toast.LENGTH_LONG).show();
        ImageView weatherView = (ImageView) findViewById(R.id.detailWeatherImage);
        int id = getResources().getIdentifier("weather_" + weatherIconName,"drawable",getPackageName());
        weatherView.setImageResource(id);
        // Picasso.with(this).load("http://openweathermap.org/img/w/"+weatherIconName+".png").fit().into(weatherView);
    }

    private void setStatusDescription() {
        OperatingStatus status = selectedResort.getOperatingStatus();
        String statusDescription;

        switch (status) {
            case Operating:
                statusDescription = "open"; // todo: use DE EN and FR with stings.xml
                break;
            case Closed:
                statusDescription = "closed";
                break;
            default:
                statusDescription = "unknown";
                break;
        }

        // fill detailList
        rowData[1] = new DetailRowData(getResources().getString(R.string.detailStatus), statusDescription);
    }

    private void checkWebButton() {
        if (selectedResort.getOfficialWebsite() == null) {
            Button webButton = (Button) findViewById(R.id.detailWebButton);
            webButton.setEnabled(false);
        }
    }

    public void createWebIntent(View v) {
        if (selectedResort.getOfficialWebsite() != null) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse(selectedResort.getOfficialWebsite()));
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.detailErrorWeb), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void createMapIntent(View v) {
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
