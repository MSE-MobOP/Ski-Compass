package mobop.skicompass;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import mobop.skicompass.dataarchitecture.OperatingStatus;
import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by artanpapaj on 26.10.17.
 */
@java.lang.SuppressWarnings("squid:MaximumInheritanceDepth")
// AppCompatActivity has already too much parents... Would be kind of a lot work to make it better
public class DetailActivity extends AppCompatActivity {

    private SkiResort selectedResort;
    private ArrayList<DetailRowData> rowData;
    private static final String WRONG_BUTTON = "Wrong binding of Button";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        selectedResort = (SkiResort) getIntent().getSerializableExtra("selectedItem");

        ListView detailList = (ListView) findViewById(R.id.detailList);
        detailList.setEnabled(false);
        detailList.setFocusable(false);
        rowData = new ArrayList<>();

        setTitle(selectedResort.getName());

        setStatusDescription();
        setLiftsDescription();
        setSlopesDescription();
        setWeather();

        DetailRowData[] rowDataArray = new DetailRowData[rowData.size()];
        rowDataArray = rowData.toArray(rowDataArray);

        DetailArrayAdapter detailArrayAdapter = new DetailArrayAdapter(this, rowDataArray);
        detailList.setAdapter(detailArrayAdapter);

        checkWebButton();

        blowDetailListToFullHeight(detailList);
    }

    /**
     * Do all stuff which corresponds to weather-setup
     */
    private void setWeather() {
        setWeatherIcon();
        setWeatherDescription();
        setTemperature();
        setMinMaxTemperatureDescription();
        setWindDescription();
        setHumidityDescription();
        setPressureDescription();
    }

    /**
     * Adds a brief description of the current weather to the detail-list
     */
    private void setWeatherDescription() {
        String weatherDescriptionText;

        switch (Locale.getDefault().getLanguage()) {
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
        rowData.add(new DetailRowData(getResources().getString(R.string.detailWeather), weatherDescriptionText));
    }

    /**
     * Get the temp of ski-resort (abs value), calculate celsius and displays on screen.
     */
    private void setTemperature() {
        TextView textView = (TextView) findViewById(R.id.temperatureTextView);
        double temp = Utils.absToNormal(selectedResort.getWeatherData().getMain().getTemp());
        int tempInt = (int) temp;
        textView.setText(getResources().getString(R.string.detailTemp, tempInt));
    }

    /**
     * Sets the big weather icon
     */
    private void setWeatherIcon() {
        String weatherIconName = selectedResort.getWeatherData().getWeather().get(0).getIcon();
        if (weatherIconName == null || weatherIconName.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.detailErrorWeatherIcon), Toast.LENGTH_LONG).show();
        }
        ImageView weatherImageView = (ImageView) findViewById(R.id.weatherImageView);
        int id = getResources().getIdentifier("weather_" + weatherIconName, "drawable", getPackageName());
        weatherImageView.setImageResource(id);
    }

    /**
     * Sets the min and max temperature to the detail description list
     */
    private void setMinMaxTemperatureDescription() {
        double min = Utils.absToNormal(selectedResort.getWeatherData().getMain().getTempMin());
        double max = Utils.absToNormal(selectedResort.getWeatherData().getMain().getTempMax());
        String tempMinMax = getResources().getString(R.string.detailTempMinMaxFormat, (int) min, (int) max);
        rowData.add(new DetailRowData(getResources().getString(R.string.detailTempMinMax), tempMinMax));
    }

    /**
     * Adds the status with description to the details-list
     */
    private void setStatusDescription() {
        OperatingStatus status = selectedResort.getOperatingStatus();
        String statusDescription;

        switch (status) {
            case Operating:
                statusDescription = getResources().getString(R.string.detailStatusOpen);
                break;
            case Closed:
                statusDescription = getResources().getString(R.string.detailStatusClosed);
                break;
            default:
                statusDescription = getResources().getString(R.string.detailStatusUnknown);
                break;
        }

        // fill detailList
        rowData.add(new DetailRowData(getResources().getString(R.string.detailStatus), statusDescription));
    }

    private void setWindDescription() {
        double windSpeed = selectedResort.getWeatherData().getWind().getSpeed();
        String windSpeedString = getResources().getString(R.string.detailWindSpeedValue, windSpeed);
        rowData.add(new DetailRowData(getResources().getString(R.string.detailWind), windSpeedString));
    }

    private void setHumidityDescription() {
        int humidity = selectedResort.getWeatherData().getMain().getHumidity();
        String humidityString = getResources().getString(R.string.detailHumidityValue, humidity);
        rowData.add(new DetailRowData(getResources().getString(R.string.detailHumidity), humidityString));
    }

    private void setPressureDescription() {
        double pressure = selectedResort.getWeatherData().getMain().getPressure();
        String pressureString = getResources().getString(R.string.detailPressureValue, pressure);
        rowData.add(new DetailRowData(getResources().getString(R.string.detailPressure), pressureString));
    }

    private void setLiftsDescription() {
        int openLifts = selectedResort.getOpenedLifts();
        rowData.add(new DetailRowData(getResources().getString(R.string.detailOpenLifts), Integer.toString(openLifts)));
    }

    private void setSlopesDescription() {
        double openSlopes = selectedResort.getOpenedSlops();
        String openSlopesString = getResources().getString(R.string.detailOpenSlopesValue, openSlopes);
        rowData.add(new DetailRowData(getResources().getString(R.string.detailOpenSlopes), openSlopesString));
    }

    /**
     * Checks if chosen resort has a website. If not, the web button is disabled
     */
    private void checkWebButton() {
        if (selectedResort.getOfficialWebsite() == null || selectedResort.getOfficialWebsite().isEmpty()) {
            Button webButton = (Button) findViewById(R.id.detailWebButton);
            webButton.setEnabled(false);
            webButton.setAlpha(0.5F);
        }
    }

    /**
     * Creates a web-browser intent, checks if it can be handled and fires it
     *
     * @param v
     */
    public void createWebIntent(View v) {
        if (v.getId() != R.id.detailWebButton) {
            throw new IllegalArgumentException(WRONG_BUTTON);
        }
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

    /**
     * Creates a google maps intent, checks if it can be handled and fires it
     *
     * @param v
     */
    public void createMapIntent(View v) {
        if (v.getId() != R.id.detailNavButton) {
            throw new IllegalArgumentException(WRONG_BUTTON);
        }
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

    /**
     * Resize the list to fit all items in height
     *
     * @param detailList
     */
    private void blowDetailListToFullHeight(ListView detailList) {
        ListAdapter detailListAdapter = detailList.getAdapter();
        if (detailListAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(detailList.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < detailListAdapter.getCount(); i++) {
            view = detailListAdapter.getView(i, view, detailList);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = detailList.getLayoutParams();
        params.height = totalHeight + (detailList.getDividerHeight() * (detailListAdapter.getCount() - 1));
        detailList.setLayoutParams(params);
    }

}
