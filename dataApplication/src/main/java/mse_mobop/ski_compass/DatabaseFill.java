package mse_mobop.ski_compass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.JSONException;
import org.json.JSONObject;

import mse_mobop.ski_compass.DataArchitecture.*;

public class DatabaseFill {

	private static int[] resortsIDs = {
			// 383 Graubünden
			1012, 1017, 4398, /* 4396, */ 4395, 1018, 3801, 1009, 1014, 1016, 1011, 4397, 3712, 3728, 4981, 5029, 4050,
			1010, 3722, 3721, 3799, 1015, 2405, 630, 3800, 4048, 1019,
			// 385 Berner Oberland
			1002, 12874, 1003, 3719, 5448, 5449, 3785, 3788, 1000, 4187, 1001, 3786, 3787,
			// 384 Zentralschweiz
			1006, /* 3724, */ 1004, 5453, 1005, 5454, 1008, 12392, /* 3825, */ 3720, 3789, 5456, 2968, /* 3790, */ 1007,
			3038,
			// 382 Ostschweiz
			3725, 3828, 1023, 3839, 4392, 1020, 1022, 4394, 1021, 3792,
			// 515 Tessin
			3169, 3683, 3715, 3705, 12774, 3713, 3709,
			// 387 Wallis
			998, 3782, 5677, 4388, 2387, 4185, 3714, 991, 3784, 3723, 2291, 4186, 4601, 4184, 12872, 3841, 1037, 5413,
			989, 5670, 5412, 4387, 4835, 3727, 988, 996, 994, 997, 3112, 5676, 3783, 3781, 3718, 2893, 2890, 993, 3711,
			3708, 990, 4390, 4600, 5427, 5428, 992, 999, 995, 4046, 987 };

	public static void main(String[] args) throws UnirestException, FileNotFoundException {
		// Initialize Firebase Database Connection
		FileInputStream serviceAccount = new FileInputStream("ski-compass-firebase-adminsdk-9ojmj-a8522c42f9.json");
		FirebaseOptions options = new FirebaseOptions.Builder().setServiceAccount(serviceAccount)
				.setDatabaseUrl("https://ski-compass.firebaseio.com/").build();
		FirebaseApp.initializeApp(options);
		FirebaseDatabase database = FirebaseDatabase.getInstance();

		// Get Reference to Skiresorts and geoFire Locations
		DatabaseReference skiResorts = database.getReference("skiresorts");
		GeoFire geoFire = new GeoFire(database.getReference("GeoFire"));

		Gson gson = new Gson();

		for (int id : resortsIDs) {
			// int id = 1012;

			// Get Data form API: Resorts
			System.out.println("Get Resort: " + id);
			HttpResponse<JsonNode> jsonResponse = Unirest.get("https://skimap.org/SkiAreas/view/" + id + ".json")
					.asJson();

			// Read out Location from API Response
			double latitude = jsonResponse.getBody().getObject().getDouble("latitude");
			double longitude = jsonResponse.getBody().getObject().getDouble("longitude");

			// Set Location on GeoFire
			geoFire.setLocation(Integer.toString(id), new GeoLocation(latitude, longitude));

			// Save skiresortdata
			String skiResortData = jsonResponse.getBody().toString();

			// get weather data
			jsonResponse = Unirest.get("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="
					+ longitude + "&APPID=ae4954f6aa99cd585f31703b2247b4e6").asJson();
			String weather = jsonResponse.getBody().toString();

			// combine skiresortdata and weather
			skiResortData = addWeather(skiResortData, weather);

			SkiResort skiResort = gson.fromJson(skiResortData, SkiResort.class);
			skiResorts.child(Integer.toString(id)).setValue(skiResort);
			// Put resort data to firebase database
			// Unirest.put("https://ski-compass.firebaseio.com/skiresorts/" + id +
			// ".json").body(skiResortData).asJson();

			// Unirest.put("https://ski-compass.firebaseio.com/skiresorts/" + id +
			// ".json").body(jsonResponse.getBody())
			// .asJson();

			// save one json to file for testing
			if (id == 1012) {
				System.out.println("ID: " + skiResort.getId());
				System.out.println("NAME: " + skiResort.getName());
				System.out.println("WEATHER: " + skiResort.getWeatherData().getWeather().description);
				System.out.println("TEMP: " + skiResort.getWeatherData().getMain().getTemp());
			}
		}

	}

	/**
	 * Merges two JSON Object-String to one by simply remove leading/trailing
	 * braket...
	 *
	 * @param json1
	 *            JSONObject1 String
	 * @param json2
	 *            JSONObject2 String
	 * @return
	 */
	public static String mergeJSON(String json1, String json2) {
		return json1.trim().substring(0, json1.length() - 1) + ", " + json2.trim().substring(1);
	}

	/**
	 * Adds string "weather" as nested object to "json"
	 *
	 * @param json1
	 *            Json-String to add weather-object to
	 * @param weather
	 *            Json-String containing the weather
	 * @return
	 */
	public static String addWeather(String json1, String weather) {
		return json1.trim().substring(0, json1.length() - 1) + ", \"weatherData\" : " + weather.trim() + "}";
	}

}
