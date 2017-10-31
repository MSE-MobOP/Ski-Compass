/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package DataArchitecture;

/**
 * Reflects the root of resort data from database
 */
public class SkiResort {

    private int id;
    private String name;
    private WeatherData weatherData;
    private String official_website;
    private String operating_status;
    private String night_skiing;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public String getOfficialWebsite() {
        return official_website;
    }

    public String getOperatingStatus() {
        return operating_status;
    }

    public String getNightSkiing() {
        return night_skiing;
    }
}
