package mse_mobop.ski_compass.DataArchitecture;

/**
 * Reflects the root of resort data from database
 */
public class SkiResort {

	private int id;
	private String name;
	private OperatingStatus operating_status;
	private String night_skiing;
	private String official_website;
	private WeatherData weatherData;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public OperatingStatus getOperating_status() {
		return operating_status;
	}

	public String getNight_skiing() {
		return night_skiing;
	}

	public String getOfficial_website() {
		return official_website;
	}

	public WeatherData getWeatherData() {
		return weatherData;
	}

	public void setWeatherData(WeatherData weatherData) {
		this.weatherData = weatherData;
	}

	@Override
	public String toString() {
		return name;
	}
}

enum OperatingStatus{
	Operating,
	Unknown,
	Closed,
}
