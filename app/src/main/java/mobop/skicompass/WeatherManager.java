package mobop.skicompass;

import mobop.skicompass.R;

/**
 * Created by Christian on 05.11.2017.
 */

public class WeatherManager {

    private WeatherManager(){}

    public static int getImageIdFromDescription(String description) {
        switch(description) {
            default:
                return R.drawable.weather_unknown;
        }
    }

}
