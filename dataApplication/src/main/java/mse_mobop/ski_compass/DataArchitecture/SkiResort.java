package mse_mobop.ski_compass.DataArchitecture;

/**
 * Reflects the root of resort data from database
 */
public class SkiResort {

    private int id;
    private String name;
    private WeatherData weatherData;
    private String officialWebsite;
    private String operatingStatus;
    private String nightSkiing;

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
        return officialWebsite;
    }

    public String getOperatingStatus() {
        return operatingStatus;
    }

    public String getNightSkiing() {
        return nightSkiing;
    }

    @Override
    public String toString() {
        return getName();
    }
}
