package deleva.com.requester.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;

import deleva.com.requester.R;
import deleva.com.requester.model.ResizableImageView;

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


    public JobPostFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mcontext = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.job_post_fragment, container, false);

        progress_wheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel_job);

        img_job = (ResizableImageView)rootView.findViewById(R.id.img_job);


        progress_wheel.spin();
        //progress_wheel.setBarColor(Color.RED);
        progress_wheel.setRimColor(Color.LTGRAY);

        progress_wheel.setVisibility(View.GONE);


        img_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                //Toast.makeText(mcontext,"onClick",Toast.LENGTH_SHORT).show();

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
}
