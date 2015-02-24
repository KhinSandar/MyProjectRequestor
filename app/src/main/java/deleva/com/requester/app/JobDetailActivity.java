package deleva.com.requester.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;
import deleva.com.requester.Config;
import deleva.com.requester.R;
import deleva.com.requester.api.JobDetailApi;
import deleva.com.requester.api.JobPostApi;
import deleva.com.requester.model.Connection;
import deleva.com.requester.model.JobItem;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by khinsandar on 2/6/15.
 */
public class JobDetailActivity extends ActionBarActivity implements OnMapReadyCallback {

    private JobItem jobItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobdetail);

        Bundle bundle = getIntent().getExtras();
        jobItem = bundle.getParcelable("JobItem");

        Toast.makeText(getApplicationContext(), "Job " +jobItem.get_status() + jobItem.get_id() ,Toast.LENGTH_SHORT).show();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        getJobDetailData();


    }

    private void getJobDetailData(){
        SharedPreferences sPref =this.getSharedPreferences(Config.TOKEN_PREF, MODE_PRIVATE);
        String token = sPref.getString(Config.TOKEN, null);
        String jobID = jobItem.get_id().toString();



        if(Connection.isOnline(getApplicationContext()) && token != null){

            JobDetailApi.getInstance().getService().getJobDetail(jobID, token, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    Toast.makeText(getApplicationContext(),"JobDetail " + s,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    new SweetAlertDialog(JobDetailActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(error.toString())
                            .show();

                    Toast.makeText(getApplicationContext(), "Job Post Errror" + error.toString(), Toast.LENGTH_LONG).show();

                }
            });

        }else {

        }



    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));


    }
}
