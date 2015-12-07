package com.charnock.dev;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.charnock.dev.model.Database;

import java.util.List;


public class ViewProfile extends Fragment {

    String name = "", email = "", phone = "", address = "", city = "";
    String id = "";
    String business_id = "";
    ProgressDialog progress;
    TextView nametv, emailtv, phonetv, addresstv, citytv;
    List<Database> database;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.view_profile, container, false);
        setHasOptionsMenu(true);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActivity().getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + "Profile" + "</font>")));
        getActivity().getActionBar().setHomeButtonEnabled(true);
        getActivity().getActionBar().setIcon(R.drawable.pf);

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        nametv = (TextView) rootView.findViewById(R.id.viewedit_name);
        emailtv = (TextView) rootView.findViewById(R.id.viewedit_email);
        phonetv = (TextView) rootView.findViewById(R.id.viewedit_phone);
        addresstv = (TextView) rootView.findViewById(R.id.viewedit_address);
        citytv = (TextView) rootView.findViewById(R.id.viewedit_city);

        try {
            dbhelp.DatabaseHelper2 entry = new dbhelp.DatabaseHelper2(getActivity());
            entry.close();
            database = entry.getdatabase();
            if (database != null) {
                for (final Database flower : database) {
                    id = flower.getId();
                    name = flower.getName();
                    email = flower.getEmail();
                    phone = flower.getPhone();
                    address = flower.getRole_id();
                    city = flower.getNode_id();
                    business_id = flower.getBusiness_id();
                }
            }
        } catch (Exception e) {
            Log.d("Exception : ", "" + e);
            Log.d("exception", "user does not exist");
        }

        nametv.setText(name);
        emailtv.setText(email);
        phonetv.setText(phone);
        addresstv.setText(address);
        citytv.setText(city);

        return rootView;
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.viewedit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                progress.show();
                if (isonline()) {
                    Intent intent = new Intent(getActivity(), Editprofile.class);
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    progress.hide();
                    Toast.makeText(getActivity(), R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return true;
        }
    }

}