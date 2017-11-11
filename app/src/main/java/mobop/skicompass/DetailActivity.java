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
import java.util.Locale;
import mobop.skicompass.dataarchitecture.OperatingStatus;
import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by artanpapaj on 26.10.17.
 */
@java.lang.SuppressWarnings("squid:MaximumInheritanceDepth") // AppCompatActivity has already too much parents... Would be kind of a lot work to make it better
public class DetailActivity extends AppCompatActivity {

    private SkiResort selectedResort;
    private DetailRowData[] rowData;
    private static final int NUM_ROWS = 2;
    private static final String WRONG_BUTTON = "Wrong binding of Button";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        selectedResort = (SkiResort) getIntent().getSerializableExtra("selectedItem");

        ListView detailList = (ListView) findViewById(R.id.detailList);
        detailList.setEnabled(false);
        rowData = new DetailRowData[NUM_ROWS];

        setTitle(selectedResort.getName());

        setWeather();
        setStatusDescription();

        DetailArrayAdapter detailArrayAdapter = new DetailArrayAdapter(this, rowData);
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
    }

    /**
     * Adds a brief description of the current weather to the detail-list
     */
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

    /**
     * Get the temp of ski-resort (abs value), calculate celsius and displays on screen.
     */
    private void setTemperature() {
        TextView textView = (TextView) findViewById(R.id.temperatureTextView);
        double tempAbs = selectedResort.getWeatherData().getMain().getTemp();
        double temp = tempAbs - 273.0;
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
        int id = getResources().getIdentifier("weather_" + weatherIconName,"drawable", getPackageName());
        weatherImageView.setImageResource(id);
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
        rowData[1] = new DetailRowData(getResources().getString(R.string.detailStatus), statusDescription);
    }

    /**
     * Checks if chosen resort has a website. If not, the web button is disabled
     */
    private void checkWebButton() {
        if (selectedResort.getOfficialWebsite() == null) {
            Button webButton = (Button) findViewById(R.id.detailWebButton);
            webButton.setEnabled(false);
        }
    }

    /**
     * Creates a web-browser intent, checks if it can be handled and fires it
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
     * Resizes the list to fit all items in height
     * @param detailList
     */
    private void blowDetailListToFullHeight(ListView detailList) {
        ListAdapter detailListAdapter = detailList.getAdapter();
        if (detailListAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(detailList.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < detailListAdapter.getCount(); i++) {
            view = detailListAdapter.getView(i, view, detailList);
            if (i == 0) view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = detailList.getLayoutParams();
        params.height = totalHeight + (detailList.getDividerHeight() * (detailListAdapter.getCount() - 1));
        detailList.setLayoutParams(params);
    }

}
