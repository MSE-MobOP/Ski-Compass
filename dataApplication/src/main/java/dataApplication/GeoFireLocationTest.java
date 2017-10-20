package dataApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeoFireLocationTest {

	static int numberOfResults = 10;
	static GeoQuery geoQuery;
	static int radius = 1;

	public static void main(String[] args) throws FileNotFoundException {
		// Initialize Firebase Database Connection
		FileInputStream serviceAccount = new FileInputStream("ski-compass-firebase-adminsdk-9ojmj-a8522c42f9.json");
		FirebaseOptions options = new FirebaseOptions.Builder().setServiceAccount(serviceAccount)
				.setDatabaseUrl("https://ski-compass.firebaseio.com/").build();
		FirebaseApp.initializeApp(options);
		FirebaseDatabase database = FirebaseDatabase.getInstance();

		// Get Reference to Skiresorts and geoFire Locations
		DatabaseReference skiResorts = database.getReference("skiresorts-test");
		GeoFire geoFire = new GeoFire(database.getReference("GeoFire"));

		geoQuery = geoFire.queryAtLocation(new GeoLocation(47.4503667, 9.15917), radius);
		geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

			public void onKeyMoved(String arg0, GeoLocation arg1) {
				System.out.println("On Key Moved");

			}

			public void onKeyExited(String arg0) {
				System.out.println("ON Key Exited");

			}

			public void onKeyEntered(String key, GeoLocation location) {
				numberOfResults--;
				if (numberOfResults >= 0) {
					System.out.println(numberOfResults + " Location found: " + key + " Location: " + location.longitude
							+ " " + location.latitude);
				} else {
					geoQuery.removeAllListeners();
					System.out.println("Foudn enought Locations");
					
				}
			}

			public void onGeoQueryReady() {
				//System.out.println("Ready with Radius " + radius);
				geoQuery.setRadius(radius++);
			}

			public void onGeoQueryError(DatabaseError arg0) {
				System.out.println("On DB Error");

			}
		});

		// geoFire.setLocation("home", new GeoLocation(47.4503667, 9.15917));

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
