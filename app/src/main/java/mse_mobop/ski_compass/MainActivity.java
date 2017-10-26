package mse_mobop.ski_compass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // test

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // tv.setText("Hi there");

                // Prepare
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("GeoFire");
                GeoFire geoFire = new GeoFire(dbRef);

                // Write a location to db
                geoFire.setLocation("firebase-hq", new GeoLocation(5, 5));

                // Search around a location
                GeoQuery query = geoFire.queryAtLocation(new GeoLocation(5, 5), 10);
                query.addGeoQueryEventListener(new GeoQueryEventListener() {

                    // This method is called every time a location gets into the radius...
                    // should save the list once and then deregister...
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        Toast.makeText(MainActivity.this, "Something is coming... Key: " + key, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onKeyExited(String key) {

                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {

                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });


            }
        });
    }

    public void testList(View v) {
        Intent intent = new Intent(this, ResultListActivity.class);
        startActivity(intent);
    }

    public void testDetail(View v) {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

}
