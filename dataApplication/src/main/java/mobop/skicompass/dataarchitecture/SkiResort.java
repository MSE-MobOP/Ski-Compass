package mobop.skicompass.dataarchitecture;

import com.google.gson.annotations.SerializedName;

/**
 * Reflects the root of resort data from database
 */
public class SkiResort {

	@SerializedName("id")
	private int id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("operating_status")
	private OperatingStatus operatingStatus;
	
	@SerializedName("night_skiing")
	private String nightSkiing;
	
	@SerializedName("official_website")
	private String officialWebsite;
	
	@SerializedName("weatherData")
	private WeatherData weatherData;
        
        @SerializedName("longitude")
        private double longitude;
        
        @SerializedName("latitude")
        private double latitude;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public OperatingStatus getOperatingStatus() {
		return operatingStatus;
	}

	public String getNightSkiing() {
		return nightSkiing;
	}

	public String getOfficialWebsite() {
		return officialWebsite;
	}

	public WeatherData getWeatherData() {
		return weatherData;
	}

	public void setWeatherData(WeatherData weatherData) {
		this.weatherData = weatherData;
	}
        
        public double getLongitude() {
            return longitude;
        }
        
        public double getLatitude() {
            return latitude;
        }

	@Override
	public String toString() {
		return name;
	}
}