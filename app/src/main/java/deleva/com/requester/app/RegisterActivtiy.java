package deleva.com.requester.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

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

    TextView register;
    EditText name, email, password, phone, address , business_type,business_address, business_address_2;
    ProgressWheel progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        register = (TextView) findViewById(R.id.register_button);

        name = (EditText) findViewById(R.id.register_name);
        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        phone = (EditText) findViewById(R.id.register_phone);
        address = (EditText) findViewById(R.id.register_address);

        business_type = (EditText) findViewById(R.id.register_business_type);
        business_address = (EditText) findViewById(R.id.register_business_add);
        business_address_2 = (EditText) findViewById(R.id.register_business_add_2);




        progress = (ProgressWheel) findViewById(R.id.register_progress_wheel);

        register.setOnClickListener(this);




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
        progress.setVisibility(View.VISIBLE);
        RegisterApi.getInstance().getService().requestorRegister(nam, mail, pwd, phone.getText().toString(), address.getText().toString(), business_type.getText().toString(), business_address.getText().toString(),"96.159190,16.775926" ,new Callback<String>() {
            @Override
            public void success(String s, Response response) {

                Toast.makeText(getApplicationContext(),"Register Success" + s,Toast.LENGTH_SHORT).show();
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

    }
}
