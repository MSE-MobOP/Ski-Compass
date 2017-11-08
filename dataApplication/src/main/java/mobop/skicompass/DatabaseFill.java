package mobop.skicompass;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import mobop.skicompass.dataarchitecture.OperatingStatus;
import mobop.skicompass.dataarchitecture.SkiResort;
import mobop.skicompass.dataarchitecture.Weather;
import mobop.skicompass.dataarchitecture.WeatherData;

public class DatabaseFill {

	private static Random random = new Random();
	private static int[] resortsIDs = {
			// 383 Graub�nden
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

	public static void main(String[] args) throws Exception {
		// Initialize Firebase Database Connection
		File dir = new File(".");
		File admin_sdk_key_file = dir.listFiles(
				(directory, name) -> name.startsWith("ski-compass-firebase-adminsdk") && name.endsWith(".json"))[0];
		FileInputStream serviceAccount = new FileInputStream(admin_sdk_key_file.getAbsolutePath());
		FirebaseOptions options = new FirebaseOptions.Builder().setServiceAccount(serviceAccount)
				.setDatabaseUrl("https://ski-compass.firebaseio.com").build();
		FirebaseApp.initializeApp(options);
		FirebaseDatabase database = FirebaseDatabase.getInstance();

		// Get Reference to Skiresorts and geoFire Locations
		DatabaseReference skiResorts = database.getReference("skiresorts");
		GeoFire geoFire = new GeoFire(database.getReference("GeoFire"));

		Gson gson = new Gson();

		for (int id : resortsIDs) {

			// Get Data form API: Resorts
			System.out.println("Get Resort: " + id);
			JsonNode jsonResponse = Unirest.get("https://skimap.org/SkiAreas/view/" + id + ".json").asJson().getBody();

			// Read out Location from API Response
			double latitude = jsonResponse.getObject().getDouble("latitude");
			double longitude = jsonResponse.getObject().getDouble("longitude");

			// Set Location on GeoFire
			geoFire.setLocation(Integer.toString(id), new GeoLocation(latitude, longitude));

			SkiResort skiResort = gson.fromJson(jsonResponse.toString(), SkiResort.class);

			// get weather data
			jsonResponse = Unirest.get("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="
					+ longitude + "&APPID=ae4954f6aa99cd585f31703b2247b4e6&lang=en").asJson().getBody();
			WeatherData weather = gson.fromJson(jsonResponse.toString(), WeatherData.class);
			jsonResponse = Unirest.get("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="
					+ longitude + "&APPID=ae4954f6aa99cd585f31703b2247b4e6&lang=fr").asJson().getBody();
			WeatherData frenchWeather = gson.fromJson(jsonResponse.toString(), WeatherData.class);
			jsonResponse = Unirest.get("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="
					+ longitude + "&APPID=ae4954f6aa99cd585f31703b2247b4e6&lang=de").asJson().getBody();
			WeatherData germanWeather = gson.fromJson(jsonResponse.toString(), WeatherData.class);
			
			Weather w = weather.getWeather().get(0);
			w.setDescriptionDE(germanWeather.getWeather().get(0).getDescription());
			w.setDescriptionFR(frenchWeather.getWeather().get(0).getDescription());
			
			skiResort.setWeatherData(weather);
			
			
			generateRandomSkiResortData(skiResort);

			// Put resort data to firebase database
			skiResorts.child(Integer.toString(id)).setValue(skiResort);

			// save one json to file for testing
			System.out.println("ID: " + skiResort.getId());
		}

	}

	private static void generateRandomSkiResortData(SkiResort skiResort) {
		switch (random.nextInt(2)) {			// Set this to 3 to get Resorts with Unknown Operating Status
		case 0:
			skiResort.setOperatingStatus(OperatingStatus.Operating);
			break;
		case 1:
			skiResort.setOperatingStatus(OperatingStatus.Closed);
			break;
		case 2:
			skiResort.setOperatingStatus(OperatingStatus.Unknown);
		}

		skiResort.setTotalLifts(random.nextInt(20));
		skiResort.setTotalSlops(Math.round(random.nextDouble() * 1500) / 10);

		if (skiResort.getOperatingStatus() == OperatingStatus.Operating) {
			skiResort.setOpenedLifts((int) (skiResort.getTotalLifts() * random.nextDouble()));
			skiResort.setOpenedSlops(Math.round(skiResort.getTotalSlops() * random.nextDouble() * 10) / 10);
		}

		// test for malfunction
		if (skiResort.getOpenedLifts() > skiResort.getTotalLifts()) {
			throw new RuntimeException("More lifts opened then total lifts!!");
		}

		if (skiResort.getOpenedSlops() > skiResort.getTotalSlops()) {
			throw new RuntimeException("More opened slopes then total slopes!!!");
		}
	}
}
