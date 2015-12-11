package com.charnock.dev;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.charnock.dev.model.Database;
import com.charnock.dev.service_engineer.Service_Request_View_service_engineer;

import java.util.List;


public class MainActivity extends FragmentActivity {

    List<Database> database;
    String status;
    Boolean get_this = true;
    ObjectDrawerItem[] drawerItem;
    String redirection = "", permissionId = "";
    private String[] mPlanetTitles;
    private String[] mPlanetTitles1;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //        Log.d("planet","point 1");
//        mTitle = mDrawerTitle = getTitle();
//        mPlanetTitles = getResources().getStringArray(R.array.planets_array);

        mTitle = mDrawerTitle = getTitle();

        try {
            dbhelp.DatabaseHelper2 db = new dbhelp.DatabaseHelper2(this);
            database = db.getdatabase();
            Log.d("id", database.get(0).getId());
            Log.d("name", database.get(0).getName());
            Log.d("email", database.get(0).getEmail());
            Log.d("password", database.get(0).getPassword());
            permissionId = database.get(0).getPermission_id();
            Log.v("permissionId", permissionId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        try {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (database == null) {
            drawerItem = new ObjectDrawerItem[3];
            drawerItem[0] = new ObjectDrawerItem(R.drawable.home, "Home");
            drawerItem[1] = new ObjectDrawerItem(R.drawable.home, "Catalog");
            drawerItem[2] = new ObjectDrawerItem(R.drawable.profile, "Service Request");
        } else {
            drawerItem = new ObjectDrawerItem[5];
            drawerItem[0] = new ObjectDrawerItem(R.drawable.home, "Home");
            drawerItem[1] = new ObjectDrawerItem(R.drawable.home, "Catalog");
            drawerItem[2] = new ObjectDrawerItem(R.drawable.profile, "Service Request");
            drawerItem[3] = new ObjectDrawerItem(R.drawable.exit_icon1, "Settings");
            drawerItem[4] = new ObjectDrawerItem(R.drawable.exit_icon1, "Exit");
        }

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

//        Log.d("planet","point 5");

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
//        Log.d("planet", "point 6");
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        selectItem(0);
//        Log.d("planet","point 7");

        try {
            redirection = getIntent().getExtras().getString("redirection");
            if (redirection != null) {
                if (redirection.equals("Service Create")) {

                    Log.d("permissin_id", database.get(0).getPermission_id());
                    Log.d("role_id", database.get(0).getRole_id());

                    if (database == null) {
                        Fragment fragment = new Login();
                        Bundle args = new Bundle();
                        fragment.setArguments(args);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                        transaction.replace(R.id.content_frame, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        if (database.get(0).getPermission_id().equals("4") && database.get(0).getRole_id().equals("4")) {
                            Toast.makeText(MainActivity.this, "This feature is currently in cunstruction", Toast.LENGTH_LONG).show();
                        } else if (database.get(0).getPermission_id().equals("2") && database.get(0).getRole_id().equals("3")) {
                            Toast.makeText(MainActivity.this, "This feature is currently in cunstruction", Toast.LENGTH_LONG).show();
                        } else if (database.get(0).getPermission_id().equals("3") && database.get(0).getRole_id().equals("5")) {

                            Fragment fragment = new Service_Request_View_service_engineer();
                            Bundle args = new Bundle();
                            fragment.setArguments(args);

                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                            transaction.replace(R.id.content_frame, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                            Fragment fragment = new Service_Request_View();
                            Bundle args = new Bundle();
                            fragment.setArguments(args);

                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                            transaction.replace(R.id.content_frame, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }

                } else if (redirection.equals("Login")) {

                    Fragment fragment = new Login();
                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (redirection.equals("Settings")) {

                    Fragment fragment = new Settings();
                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
        /*case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectItem(int position) {

        Fragment fragment = new Front_Page();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String planet = drawerItem[position].name;
            mDrawerLayout.closeDrawer(mDrawerList);
//            String planet = getResources().getStringArray(R.array.planets_array1)[position];
//            if(get_this) {
//                planet = getResources().getStringArray(R.array.planets_array)[position];
//                mDrawerLayout.closeDrawer(mDrawerList);
//            } else {
//                planet = getResources().getStringArray(R.array.planets_array_not_logged_in)[position];
//                mDrawerLayout.closeDrawer(mDrawerList);
//            }
            switch (planet) {
                case "Home": {
                    selectItem(0);
                    break;
                }
                case "Catalog": {
                    Intent intent = new Intent(MainActivity.this, HomeCategory.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    break;
                }
                case "Service Request": {
                    if (database == null) {
                        Fragment fragment = new Login();
                        Bundle args = new Bundle();
                        fragment.setArguments(args);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                        transaction.replace(R.id.content_frame, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {

                        try {
                            Log.d("permissin_id", database.get(0).getPermission_id());
                            Log.d("role_id", database.get(0).getRole_id());

                            if (database.get(0).getPermission_id().equals("4") && database.get(0).getRole_id().equals("4")) {
                                Toast.makeText(MainActivity.this, "This feature is currently in cunstruction", Toast.LENGTH_LONG).show();
                            } else if (database.get(0).getPermission_id().equals("2") && database.get(0).getRole_id().equals("3")) {
                                Toast.makeText(MainActivity.this, "This feature is currently in cunstruction", Toast.LENGTH_LONG).show();
                            } else if (database.get(0).getPermission_id().equals("3") && database.get(0).getRole_id().equals("5")) {
                                Fragment fragment = new Service_Request_View_service_engineer();
                                Bundle args = new Bundle();
                                fragment.setArguments(args);

                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                                transaction.replace(R.id.content_frame, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            } else {

                                Fragment fragment = new Service_Request_View();
                                Bundle args = new Bundle();
                                fragment.setArguments(args);

                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                                transaction.replace(R.id.content_frame, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    break;
                }
                case "Settings": {
                    if (database == null) {

                        Fragment fragment = new Login();
                        Bundle args = new Bundle();
                        fragment.setArguments(args);

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                        transaction.replace(R.id.content_frame, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    } else {
                        Fragment fragment = new Settings();
                        Bundle args = new Bundle();
                        fragment.setArguments(args);

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                        transaction.replace(R.id.content_frame, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    break;
                }
                case "Change Password": {
                    finish();
                    Intent intent = new Intent(MainActivity.this, Change_Password.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    break;
                }
                case "LogOut": {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false)
                            .setMessage(getResources().getString(R.string.logout_warning))
                            .setNegativeButton(getResources().getString(R.string.logout), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbhelp db = new dbhelp(MainActivity.this);
                                    db.open();
                                    db.logoout();
                                    db.close();
                                    finish();
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    startActivity(intent);
                                    MainActivity.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }

                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                }
                case "Exit": {
                    finish();
                    System.exit(0);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;
                }
                default:
                    System.exit(0);
                    android.os.Process.killProcess(android.os.Process.myPid());
//                    Toast.makeText(MainActivity.this,"Unkown error found",Toast.LENGTH_LONG).show();
//                    System.exit(0);
//                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;
            }
        }
    }
}
