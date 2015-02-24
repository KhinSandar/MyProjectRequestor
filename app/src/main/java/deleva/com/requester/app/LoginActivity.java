package deleva.com.requester.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import deleva.com.requester.Config;
import deleva.com.requester.R;
import deleva.com.requester.api.RegisterApi;
import deleva.com.requester.api.RegisterService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by khinsandar on 2/2/15.
 */
public class LoginActivity extends ActionBarActivity {

    Button btn_login;
    TextView txt_register;
    EditText username, password;

    //Facebook Login
    private UiLifecycleHelper uiHelper;
    private static final String TAG_LOG = "FacebookLogin";

    private List<GraphUser> tags;
    public LoginButton loginButton;

    String name, userId, email;
    private GraphUser graph_user;

    private final List<String> permissions;

    public LoginActivity() {
        permissions = Arrays.asList("public_profile", "email", "user_friends");
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        btn_login = (Button)findViewById(R.id.login_button);
        txt_register = (TextView)findViewById(R.id.login_register);
        username = (EditText)findViewById(R.id.login_username);
        password = (EditText)findViewById(R.id.login_password);
        loginButton = (LoginButton)findViewById(R.id.fb_login_button);


        //For Facebook Login
        //printHashKey();
        uiHelper = new UiLifecycleHelper(LoginActivity.this, callback);
        uiHelper.onCreate(savedInstanceState);

        ((CheckBox) findViewById(R.id.login_showpwd)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                password.setInputType(!isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                password.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
            }
        });
        ((CheckBox) findViewById(R.id.login_rememberpwd)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences sPref = getApplicationContext().getSharedPreferences(Config.PWD_REMEMBER_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sPref.edit();
                //String username = username.getText().toString();
                editor.putString(Config.PWD_REMEMBER_USER, username.getText().toString());
                editor.putString(Config.PWD_REMEMBER, password.getText().toString());
                editor.commit();


                //password.setInputType(!isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                //password.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uniquekey = Build.SERIAL + android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);

                JSONObject obj = new JSONObject();
                try {
                    obj.put("email", username.getText().toString());
                    obj.put("password", password.getText().toString());
                    obj.put("uuid", uniquekey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String json = obj.toString();

                Log.i("JsonOBj", json);
                try {
                    TypedInput in = new TypedByteArray("application/json", json.getBytes("UTF-8"));

                    RegisterApi.getInstance().getService().getToken(in ,new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {

                            try{

                                JSONObject data = new JSONObject(s);
                                if(data.getString("token") != null){
                                    SharedPreferences sPref = getApplicationContext().getSharedPreferences(Config.TOKEN_PREF, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sPref.edit();
                                    String token = data.getString("token");
                                    editor.putString(Config.TOKEN, token);
                                    editor.commit();
                                    Log.i("TOKEN", token);
                                }else{
                                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Something went wrong!")
                                            .show();
                                }

                            }catch(JSONException exp){
                                exp.printStackTrace();
                            }
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });

                }catch (UnsupportedEncodingException exp1){
                    exp1.printStackTrace();
                }

                }

        });


        loginButton.setReadPermissions(permissions);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {

            public void onUserInfoFetched(GraphUser user) {
                //Toast.makeText(getApplicationContext(), "Login Onclick", 1).show();

                LoginActivity.this.graph_user = user;


            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.GONE);
            }
        });
        //loginButton.setFragment(this);


        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivtiy.class));
                finish();
            }
        });

        //Direct Go to Main if Login session

        SharedPreferences sPref = getApplicationContext().getSharedPreferences(Config.TOKEN_PREF, MODE_PRIVATE);
        if(sPref.getString(Config.TOKEN, null) != null){
            startActivity(new Intent(this, MainActivity.class));
        }




    }
    //For Facebook app hash tab key
    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("deleva.com.requester", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("TEMPTAGHASH KEY:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("TEMPTAGHASH KEY:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
    }


    //For Facebook login Session Control
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {

            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, com.facebook.Response response) {
                    if (user != null) {

                        graph_user = user;
                        Toast.makeText(LoginActivity.this, "User Info at Login " + graph_user.getFirstName() + " /" + graph_user.getLastName() + graph_user.getProperty("email").toString() + graph_user.getId(), Toast.LENGTH_SHORT).show();
                        //sharePrefUtils.createLoginSession(graph_user.getFirstName() + " " + graph_user.getLastName(), graph_user.getProperty("email").toString(), graph_user.getId());
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();


                    }
                }
            });

            Log.i(TAG_LOG, "Logged in...");

        } else if (state.isClosed()) {
            Log.i(TAG_LOG, "Logged out...");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

    }
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }


}
