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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        selectedResort = (SkiResort) getIntent().getSerializableExtra("selectedItem");

        setTitle(selectedResort.getName());
        setWeatherDescription();

        checkWebButton();
        setWeatherIcon();
        setStatusIcon();

        // TextView detailText = (TextView) findViewById(R.id.detailText);

        // detailText.setText(getResources().getText(R.string.detailName) + ": " + selectedResort.getName() + System.lineSeparator());
        // detailText.append(getResources().getText(R.string.detailOperatingStatus) + ": " + selectedResort.getOperatingStatus() + System.lineSeparator());

        // detailText.append(getResources().getText(R.string.detailWeather) + ": " +  System.lineSeparator());//getResources().getText(test) +
        // detailText.append("ID: " + selectedResort.getId()); // debug only

    }

    private void setWeatherDescription() {
        TextView weatherDescription = (TextView)findViewById(R.id.WeatherDescription);
        switch (Locale.getDefault().getLanguage()){
            case "de":
                weatherDescription.setText(selectedResort.getWeatherData().getWeather().get(0).getDescriptionDE());
                break;
            case "fr":
                weatherDescription.setText(selectedResort.getWeatherData().getWeather().get(0).getDescriptionFR());
                break;
            default:
                weatherDescription.setText(selectedResort.getWeatherData().getWeather().get(0).getDescription());
        }
    }

    private void setWeatherIcon() {
        String weatherIconName = selectedResort.getWeatherData().getWeather().get(0).getIcon();
        if (weatherIconName.isEmpty() || weatherIconName.equals(""))
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.detailErrorWeatherIcon), Toast.LENGTH_LONG).show();
        ImageView weatherView = (ImageView) findViewById(R.id.detailWeatherImage);
        int id = getResources().getIdentifier("weather_" + weatherIconName,"drawable",getPackageName());
        weatherView.setImageResource(id);
        //Picasso.with(this).load("http://openweathermap.org/img/w/"+weatherIconName+".png").fit().into(weatherView);
    }

    private void setStatusIcon() {
        OperatingStatus status = selectedResort.getOperatingStatus();
        ImageView statusImage = (ImageView) findViewById(R.id.detailStatusImage);
        switch(status) {
            case Operating:
                statusImage.setImageResource(R.drawable.open);
                break;
            case Closed:
                statusImage.setImageResource(R.drawable.closed);
                break;
            default:
                statusImage.setImageResource(R.drawable.unknown);
                break;
        }

    }

    private void checkWebButton() {
        if (selectedResort.getOfficialWebsite() == null) {
            // ImageButton imgButton = (ImageButton) findViewById(R.id.detailWebButton);
            // imgButton.setEnabled(false);
            // imgButton.setBackgroundResource(R.mipmap.detail_web_deactivated);

            // Button imgButton2 = (Button) findViewById(R.id.detailWebButton);
            // imgButton2.setEnabled(false);

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
