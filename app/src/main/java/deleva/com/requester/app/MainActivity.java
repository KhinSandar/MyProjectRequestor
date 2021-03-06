package deleva.com.requester.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.pnikosis.materialishprogress.ProgressWheel;

import deleva.com.requester.R;
import deleva.com.requester.fragment.JobListFragment;
import deleva.com.requester.fragment.JobPostFragment;


public class MainActivity extends ActionBarActivity {
    private DrawerLayout drawerLayoutt;
    private ListView listView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Toolbar toolbar;

    private String[] navigationDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationDrawerItems = getResources().getStringArray(R.array.navigation_drawer_items);
        drawerLayoutt = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayoutt.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, navigationDrawerItems));
        listView.setOnItemClickListener(new DrawerItemClickListener());

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutt, toolbar, R.string.app_name, R.string.app_name);
        drawerLayoutt.setDrawerListener(actionBarDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if (savedInstanceState == null) {
            selectItem(0);
        }
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        /*Fragment fragment = new DummyFragment();
        Bundle args = new Bundle();
        args.putInt(DummyFragment.ARG_MENU_INDEX, position);
        fragment.setArguments(args);*/

        JobPostFragment jbPost = new JobPostFragment();

        JobListFragment jbList = new JobListFragment();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.content_frame, jbPost).commit();
                jbPost.setRetainInstance(true);// <-- this is important - otherwise the fragment manager will crash when readding the fragment
                break;

            case 1:
                fragmentManager.beginTransaction().replace(R.id.content_frame, jbList).commit();
                jbList.setRetainInstance(true);
                break;

            case 2:
                            /*fragmentManager.beginTransaction().replace(R.id.content_frame, new Settings()).commit();
                setTitle("Setting");*/
                Intent intent2 = new Intent(this.getApplicationContext(), RegisterActivtiy.class);

                intent2.putExtra("ActivityName", "Register");
                intent2.putExtra("user" ,"");

                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;

        }


        // update selected item and title, then close the drawer
        listView.setItemChecked(position, true);
        setTitle(navigationDrawerItems[position]);
        drawerLayoutt.closeDrawer(listView);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

   /* public static class DummyFragment extends Fragment {
        public static final String ARG_MENU_INDEX = "index";

        ProgressWheel progress_wheel;

        private ImageChooserManager imageChooserManager;

        private String filePath;

        private int chooserType;

        public DummyFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.job_post_fragment, container, false);
            progress_wheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel_job);

            progress_wheel.spin();
            //progress_wheel.setBarColor(Color.RED);
            progress_wheel.setRimColor(Color.LTGRAY);

            progress_wheel.setVisibility(View.GONE);
            //progress_wheel.setProgress(0.5f);
      *//*int index = getArguments().getInt(ARG_MENU_INDEX);
      String text = String.format("Menu at index %s", index);
      ((TextView) rootView.findViewById(R.id.textView)).setText(text);
      getActivity().setTitle(text);*//*
            return rootView;
        }

    }*/
}