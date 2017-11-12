/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package mobop.skicompass.dataarchitecture;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class WeatherData implements Serializable {

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("clouds")
    private Clouds clouds;
    
    @SerializedName("wind")
    private Wind wind;

    public WeatherData() {
        // Needed for GSon and FireBase
    }

    public WeatherData(Main main, List<Weather> weather, Clouds clouds, Wind wind) {
		this.main = main;
		this.weather = weather;
		this.clouds = clouds;
		this.wind = wind;
	}

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Clouds getClouds() {
        return clouds;
    }
    
    public Wind getWind() {
		return wind;
	}
}
