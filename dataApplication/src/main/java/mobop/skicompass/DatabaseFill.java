package mobop.skicompass;

import java.io.FileInputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import mobop.skicompass.dataarchitecture.SkiResort;
import mobop.skicompass.dataarchitecture.WeatherData;

public class DatabaseFill {

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
		FileInputStream serviceAccount = new FileInputStream("ski-compass-firebase-adminsdk-9ojmj-a8522c42f9.json");
		FirebaseOptions options = new FirebaseOptions.Builder().setServiceAccount(serviceAccount)
				.setDatabaseUrl("https://ski-compass.firebaseio.com/").build();
		FirebaseApp.initializeApp(options);
		FirebaseDatabase database = FirebaseDatabase.getInstance();

		// Get Reference to Skiresorts and geoFire Locations
		DatabaseReference skiResorts = database.getReference("skiresorts");
		GeoFire geoFire = new GeoFire(database.getReference("GeoFire"));

		Gson gson = new Gson();

		// Start Workaround with outdated SSL Certificate from skimap.org
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} }, new SecureRandom());

			SSLContext.setDefault(ctx);
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			Unirest.setHttpClient(httpclient);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// End Workaround

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
					+ longitude + "&APPID=ae4954f6aa99cd585f31703b2247b4e6").asJson().getBody();
			String weatherJson = jsonResponse.toString();
			skiResort.setWeatherData(gson.fromJson(weatherJson, WeatherData.class));

			// Put resort data to firebase database
			skiResorts.child(Integer.toString(id)).setValue(skiResort);

			// save one json to file for testing
			if (id == 1012) {
				System.out.println("ID: " + skiResort.getId());
				System.out.println("NAME: " + skiResort.getName());
				System.out.println("LAT: " + skiResort.getLatitude());
				System.out.println("TEMP: " + skiResort.getWeatherData().getMain().getTemp());
			}
		}

	}
}
