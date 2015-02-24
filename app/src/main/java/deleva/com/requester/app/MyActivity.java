package deleva.com.requester.app;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import deleva.com.requester.R;
import deleva.com.requester.model.FusedLocationService;

public class MyActivity extends Activity {

    private static final String TAG = "MyActivity";
    Button btnFusedLocation;
    TextView tvLocation;
    FusedLocationService fusedLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        fusedLocationService = new FusedLocationService(this);

        setContentView(R.layout.activity_my);
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        btnFusedLocation = (Button) findViewById(R.id.btnGPSShowLocation);
        btnFusedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Location location = fusedLocationService.getLocation();
                String locationResult = "";
                if (null != location) {
                    Log.i(TAG, location.toString());
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    float accuracy = location.getAccuracy();
                    /*double elapsedTimeSecs = (double) location.getElapsedRealtimeNanos()
                            / 1000000000.0;*/
                    String provider = location.getProvider();
                    double altitude = location.getAltitude();
                    locationResult = "Latitude: " + latitude + "\n" +
                            "Longitude: " + longitude + "\n" +
                            "Altitude: " + altitude + "\n" +
                            "Accuracy: " + accuracy + "\n" +
/*
                            "Elapsed Time: " + elapsedTimeSecs + " secs" + "\n" +
*/
                            "Provider: " + provider + "\n";
                } else {
                    locationResult = "Location Not Available!";
                }
                tvLocation.setText(locationResult);
            }
        });
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
}