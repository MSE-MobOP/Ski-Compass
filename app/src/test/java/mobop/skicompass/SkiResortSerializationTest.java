package mobop.skicompass;

/**
 * Created by Martin on 09.11.2017.
 */

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import mobop.skicompass.dataarchitecture.Clouds;
import mobop.skicompass.dataarchitecture.Main;
import mobop.skicompass.dataarchitecture.OperatingStatus;
import mobop.skicompass.dataarchitecture.SkiResort;
import mobop.skicompass.dataarchitecture.Weather;
import mobop.skicompass.dataarchitecture.WeatherData;
import mobop.skicompass.dataarchitecture.Wind;

import static org.junit.Assert.*;

public class SkiResortSerializationTest {

    private OperatingStatus operatingStatus = OperatingStatus.Operating;
    private int id = 1;
    private String nightSkiing = "noNightSkiing";
    private int totalLifts = 25;
    private int openedLifts = 20;
    private double totalSlops = 100;
    private double openedSlops = 80;
    private String officialWebsite = "www.example.com";
    private double longitude = 12349.98;
    private double latitude = 212034.23;
    private double temp = 10.5;
    private double tempMin = 8.8;
    private double tempMax = 15.9;
    private double pressure = 1005.8;
    private int humidity = 85;
    private String icon = "TestIconPath";
    private String description = "Test weather is always beautiful ;-)";
    private String descriptionDE = "schön";
    private String descriptionFR = "bon";
    private int cloudiness = 25;
    private double windDirection = 350;
    private double windSpeed = 25;

    @Test
    public void SerializationTest() {
        Main main = new Main(temp, tempMin, tempMax, pressure, humidity);
        Weather weatherElement = new Weather(icon, description, descriptionDE, descriptionFR, description, id);
        List<Weather> weather = new ArrayList<>();
        weather.add(weatherElement);
        Clouds clouds = new Clouds(cloudiness);
        Wind wind = new Wind(windDirection, windSpeed);
        WeatherData weatherData = new WeatherData(main, weather, clouds, wind);
        String name = "testResort";
        final SkiResort resort = new SkiResort(id, name, operatingStatus, nightSkiing, totalLifts,
                openedLifts, totalSlops, openedSlops, officialWebsite, weatherData, longitude,
                latitude);


        Gson gson = new Gson();

        String jsonString = gson.toJson(resort);
        System.out.print(jsonString);


        SkiResort readedResort = gson.fromJson(jsonString, SkiResort.class);
        WeatherData readedWeatherData = readedResort.getWeatherData();
        Clouds readedClouds = readedWeatherData.getClouds();
        Weather readedWeather = readedWeatherData.getWeather().get(0);
        Main readedMain = readedWeatherData.getMain();

        assertEquals(id, readedResort.getId());
        assertEquals(name, readedResort.getName());
        assertEquals(operatingStatus, readedResort.getOperatingStatus());
        assertEquals(nightSkiing, readedResort.getNightSkiing());
        assertEquals(totalLifts, readedResort.getTotalLifts());
        assertEquals(openedLifts, readedResort.getOpenedLifts());
        assertEquals(totalSlops, readedResort.getTotalSlops(), 0);
        assertEquals(openedSlops, readedResort.getOpenedSlops(), 0);
        assertEquals(officialWebsite, readedResort.getOfficialWebsite());
        assertEquals(longitude, readedResort.getLongitude(), 0);
        assertEquals(latitude, readedResort.getLatitude(), 0);
        assertEquals(temp, readedMain.getTemp(), 0);
        assertEquals(tempMax, readedMain.getTempMax(), 0);
        assertEquals(tempMin, readedMain.getTempMin(), 0);
        assertEquals(pressure, readedMain.getPressure(), 0);
        assertEquals(humidity, readedMain.getHumidity(), 0);
        assertEquals(icon, readedWeather.getIcon());
        assertEquals(description, readedWeather.getDescription());
        assertEquals(descriptionDE, readedWeather.getDescriptionDE());
        assertEquals(descriptionFR, readedWeather.getDescriptionFR());
        assertEquals(cloudiness, readedClouds.getAll());
        assertEquals(windDirection, readedWeatherData.getWind().getDeg(), 0.0);
        assertEquals(windSpeed, readedWeatherData.getWind().getSpeed(), 0.0);
    }
}
