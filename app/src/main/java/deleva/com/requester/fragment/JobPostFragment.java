package deleva.com.requester.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andreabaccega.widget.FormEditText;
import com.facebook.Settings;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import deleva.com.requester.Config;
import deleva.com.requester.R;
import deleva.com.requester.api.JobPostApi;
import deleva.com.requester.app.MainActivity;
import deleva.com.requester.model.Connection;

import deleva.com.requester.model.FusedLocationService;
import deleva.com.requester.model.ResizableImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by khinsandar on 2/7/15.
 */
public class JobPostFragment extends Fragment implements ImageChooserListener {

    public static final String ARG_MENU_INDEX = "index";

    ProgressWheel progress_wheel;

    private ImageChooserManager imageChooserManager;

    private String filePath;

    private int chooserType;

    private ResizableImageView img_job;

    Context mcontext;

    String encodedImage;

    Button btnSubmit;

    FormEditText et_type, et_address, et_address_ll, et_price, et_receiver_name, et_receiver_contact, et_post_code;

    FusedLocationService fusedLocationService;

    SharedPreferences sPref;

    private static final String TAG = "JOBPOST";


    private double coordinates[] = new double[2];


    public JobPostFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mcontext = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.job_post_fragment, container, false);

        progress_wheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel_job);

        img_job = (ResizableImageView) rootView.findViewById(R.id.img_job);

        btnSubmit = (Button) rootView.findViewById(R.id.btn_job_submit);

        et_type = (FormEditText) rootView.findViewById(R.id.et_itemType);
        et_receiver_name = (FormEditText) rootView.findViewById(R.id.et_recipent_name);
        et_receiver_contact = (FormEditText) rootView.findViewById(R.id.et_ph);
        et_address = (FormEditText) rootView.findViewById(R.id.et_rec_address);
        et_post_code = (FormEditText) rootView.findViewById(R.id.et_postcode);
        et_price = (FormEditText) rootView.findViewById(R.id.et_price);

        sPref = getActivity().getSharedPreferences(Config.TOKEN_PREF, Context.MODE_PRIVATE);


        progress_wheel.spin();
        //progress_wheel.setBarColor(Color.RED);
        progress_wheel.setRimColor(Color.LTGRAY);

        progress_wheel.setVisibility(View.GONE);


        img_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCustomView();
                //Toast.makeText(mcontext,"onClick",Toast.LENGTH_SHORT).show();

            }
        });



        //gpsTracker = new GPSTracker(mcontext);// Instantiate GPSTracker object

        this.coordinates = getGPS();
        String location = "Current Location :" + "lat="
                + this.coordinates[0]+ "&lng="
                + this.coordinates[1] ;
        Toast.makeText(mcontext, "GPS " + location, Toast.LENGTH_LONG).show();

        /*if (gpsTracker.canGetLocation()) {



            this.coordinates = new Double[]
                    {getGPS()[0] , getGPS()[1]

                    };
            String location = "Current Location :" + "lat="
                    + this.coordinates[0].toString() + "&lng="
                    + this.coordinates[1].toString();
            Toast.makeText(mcontext, "GPS Location" + location, Toast.LENGTH_LONG).show();


        } else {
            showSettingsAlert();
        }*/

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
             getActivity().finish();
        }
        fusedLocationService = new FusedLocationService(getActivity());

        final  FormEditText[] allFields    = { et_type, et_address, et_post_code, et_price,et_receiver_contact,et_receiver_name };


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Location location = fusedLocationService.getLocation();
                String locationResult = "";
                if (null != location) {
                    Log.i(TAG, location.toString());
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    float accuracy = location.getAccuracy();
                    //API Level 17 need
                    //double elapsedTimeSecs = (double) location.getElapsedRealtimeNanos()/ 1000000000.0;
                    String provider = location.getProvider();
                    double altitude = location.getAltitude();
                    locationResult = "Latitude: " + latitude + "\n" +
                            "Longitude: " + longitude + "\n" +
                            "Altitude: " + altitude + "\n" +
                            "Accuracy: " + accuracy + "\n" +
                            /*"Elapsed Time: " + elapsedTimeSecs + " secs" + "\n" +*/
                            "Provider: " + provider + "\n";
                } else {
                    locationResult = "Google API Location Not Available!";
                }
                Toast.makeText(mcontext, "ON CLick"  +  locationResult, Toast.LENGTH_LONG).show();


                boolean allValid = true;
                for (FormEditText field: allFields) {
                    allValid = field.testValidity() && allValid;
                }

                if (allValid) {

                    //SharedPreferences sPref =getActivity(). getApplicationContext().getSharedPreferences(Config.TOKEN_PREF, Context.MODE_PRIVATE);
                    String token = sPref.getString(Config.TOKEN, null);

                    //Toast.makeText(mcontext, "ON CLick"  + token + "&&" +Connection.isOnline(getActivity()), Toast.LENGTH_LONG).show();


                    if (token != null && Connection.isOnline(getActivity())) {


                        JobPostApi.getInstance().getService().requestorJobPost(token, et_type.getText().toString(), et_address.getText().toString(), String.valueOf(coordinates[1])+ ", "+ String.valueOf(coordinates[0]), et_price.getText().toString(), et_receiver_name.getText().toString(), et_receiver_contact.getText().toString(), et_post_code.getText().toString(), new Callback<String>() {
                            @Override
                            public void success(String s, Response response) {
                                Toast.makeText(mcontext, "Job Post Success" + s, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {

                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(error.toString())
                                        .show();

                                Toast.makeText(mcontext, "Job Post Errror" + error.toString(), Toast.LENGTH_LONG).show();


                            }
                        });
                    } else {
                        Toast.makeText(mcontext, "Connection Error", Toast.LENGTH_LONG).show();
                    }




                    // YAY
                } else {
                    // EditText are going to appear with an exclamation mark and an explicative message.
                }





            }
        });


        //progress_wheel.setProgress(0.5f);
      /*int index = getArguments().getInt(ARG_MENU_INDEX);
      String text = String.format("Menu at index %s", index);
      ((TextView) rootView.findViewById(R.id.textView)).setText(text);
      getActivity().setTitle(text);*/
        return rootView;
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            progress_wheel.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            progress_wheel.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {
            progress_wheel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        getActivity().
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progress_wheel.setVisibility(View.GONE);
                        if (image != null) {
                            //textViewFile.setText(image.getFilePathOriginal());
                            img_job.setImageURI(Uri.parse(new File(image
                                    .getFileThumbnail()).toString()));
                    /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/


                            encodedImage = Base64.encodeToString(image.toString().getBytes(), Base64.DEFAULT);
                            //Then send this encodedImage as a String and in your server you will need to decode it in order to get the image itself.


                        }
                    }
                });
    }

    @Override
    public void onError(final String reason) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progress_wheel.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        /*if (adView != null) {
            adView.destroy();
        }*/
        super.onDestroy();
    }

    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,
                "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        super.onSaveInstanceState(outState);
    }

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }

            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }*/
    private void showCustomView() {
        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())


                .title(R.string.app_name)
                .customView(R.layout.image_chose_dialog, true)
                .positiveText("ok")
                        //.negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Toast.makeText(getActivity().getApplicationContext(), "Positive ".toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();

        dialog.show();

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Button btn_camera_chose = (Button) dialog.findViewById(R.id.btn_camera_chose);

        btn_camera_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                dialog.dismiss();

            }
        });

        Button btn_img_chose = (Button) dialog.findViewById(R.id.btn_gallery_chose);
        btn_img_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                dialog.dismiss();
            }
        });


        //positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().getApplicationContext().startActivity(
                                intent);
                        // mContext.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    private double[] getGPS() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(mcontext.LOCATION_SERVICE);
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

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }



}
