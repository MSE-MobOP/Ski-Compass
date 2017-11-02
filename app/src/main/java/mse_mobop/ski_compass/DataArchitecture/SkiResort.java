package mse_mobop.ski_compass.DataArchitecture;

/**
 * Reflects the root of resort data from database
 */
public class SkiResort {

    private int id;
    private String name;
    private String operatingStatus;
    private String nightSkiing;
    private String officialWebsite;
    private WeatherData weatherData;

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public int getId() {
        return id;
    }

    public String getNightSkiing() {
        return nightSkiing;
    }

    public String getOfficialWebsite() {
        return officialWebsite;
    }

    public String getOperatingStatus() {
        return operatingStatus;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
