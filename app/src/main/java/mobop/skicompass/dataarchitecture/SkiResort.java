package mobop.skicompass.dataarchitecture;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Reflects the root of resort data from database
 */
public class SkiResort implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("name")
	private String name;

	@SerializedName("operating_status")
	private OperatingStatus operatingStatus;

	@SerializedName("night_skiing")
	private String nightSkiing;
	
	private int totalLifts;
	
	private int openedLifts;
	
	private double totalSlops;
	
	private double openedSlops;

	@SerializedName("official_website")
	private String officialWebsite;

	@SerializedName("weatherData")
	private WeatherData weatherData;
	
	@SerializedName("longitude")
	private double longitude;

	@SerializedName("latitude")
	private double latitude;

	public SkiResort(){};

	public SkiResort(int id, String name, OperatingStatus operatingStatus, String nightSkiing, int totalLifts, int openedLifts, double totalSlops, double openedSlops, String officialWebsite, WeatherData weatherData, double longitude, double latitude) {
		this.id = id;
		this.name = name;
		this.operatingStatus = operatingStatus;
		this.nightSkiing = nightSkiing;
		this.totalLifts = totalLifts;
		this.openedLifts = openedLifts;
		this.totalSlops = totalSlops;
		this.openedSlops = openedSlops;
		this.officialWebsite = officialWebsite;
		this.weatherData = weatherData;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public OperatingStatus getOperatingStatus() {
		return operatingStatus;
	}

	public void setOperatingStatus(OperatingStatus operatingStatus) {
		this.operatingStatus = operatingStatus;
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
	
	public int getTotalLifts() {
		return totalLifts;
	}
	
	public void setTotalLifts(int totalLifts) {
		this.totalLifts = totalLifts;
	}
	
	public int getOpenedLifts() {
		return openedLifts;
	}
	
	public void setOpenedLifts(int openedLifts) {
		this.openedLifts = openedLifts;
	}
	
	public double getTotalSlops() {
		return totalSlops;
	}
	
	public void setTotalSlops(double totalSlops) {
		this.totalSlops = totalSlops;
	}
	
	public double getOpenedSlops() {
		return openedSlops;
	}
	
	public void setOpenedSlops(double openedSlops) {
		this.openedSlops = openedSlops;
	}

	@Override
	public String toString() {
		return name;
	}
}