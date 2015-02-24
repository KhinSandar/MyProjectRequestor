package deleva.com.requester.app;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import deleva.com.requester.R;
import deleva.com.requester.api.RegisterApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by khinsandar on 2/2/15.
 */
public class RegisterActivtiy extends ActionBarActivity implements View.OnClickListener{

    Button register;
    FormEditText name, email, password, phone, address , business_type,business_address, business_address_2;
    ProgressWheel progress;
    private double coordinates[] = new double[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        register = (Button) findViewById(R.id.register_button);

        name = (FormEditText) findViewById(R.id.register_name);
        email = (FormEditText) findViewById(R.id.register_email);
        password = (FormEditText) findViewById(R.id.register_password);
        phone = (FormEditText) findViewById(R.id.register_phone);
        address = (FormEditText) findViewById(R.id.register_address);

        business_type = (FormEditText) findViewById(R.id.register_business_type);
        business_address = (FormEditText) findViewById(R.id.register_business_add);
        business_address_2 = (FormEditText) findViewById(R.id.register_business_add_2);




        progress = (ProgressWheel) findViewById(R.id.register_progress_wheel);

        register.setOnClickListener(this);

        coordinates = getGPS();//coordinates[0] is now the lat, [1] is the long.






    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_page, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        String nam = name.getText().toString();
        final String mail = email.getText().toString();
        final String pwd = password.getText().toString();


        final  FormEditText[] allFields    = { name, email, password, phone,address,business_type ,business_address };



        boolean allValid = true;
        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }

        if (allValid) {
            progress.setVisibility(View.VISIBLE);

            RegisterApi.getInstance().getService().requestorRegister(nam, mail, pwd, phone.getText().toString(), address.getText().toString(), business_type.getText().toString(), business_address.getText().toString(), String.valueOf(coordinates[1])+ ", "+ String.valueOf(coordinates[0]), new Callback<String>() {
                @Override
                public void success(String s, Response response) {

                    Toast.makeText(getApplicationContext(), "Register Success" + s, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    //getToken(mail, pwd);
                    progress.setVisibility(View.INVISIBLE);


                }

                @Override
                public void failure(RetrofitError error) {
                    new SweetAlertDialog(RegisterActivtiy.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(error.toString())
                            .show();

                }
            });
        } else {
            // EditText are going to appear with an exclamation mark and an explicative message.
        }

    }

    private double[] getGPS() {
        LocationManager lm = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }

}
