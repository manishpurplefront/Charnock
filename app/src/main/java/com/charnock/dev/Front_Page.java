package com.charnock.dev;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.charnock.dev.model.Database;

import java.util.List;


public class Front_Page extends Fragment {

    View rootView;
    List<Database> database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.front_page, container, false);
        setHasOptionsMenu(true);

        try {
            getActivity().getActionBar().removeAllTabs();
            getActivity().invalidateOptionsMenu();
            getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            getActivity().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + getString(R.string.app_name_home) + "</font>")));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        try {
            dbhelp.DatabaseHelper2 entry = new dbhelp.DatabaseHelper2(getActivity());
            entry.close();
            database = entry.getdatabase();
        } catch (Exception e) {
            Log.d("Exception : ", "" + e);
            Log.d("exception", "user does not exist");
        }

        Button b = (Button) rootView.findViewById(R.id.catalog);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeCategory.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        Button b2 = (Button) rootView.findViewById(R.id.service_request);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (database == null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("redirection", "Login");
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("redirection", "Service Create");
                    getActivity().finish();
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }

            }
        });
        return rootView;
    }
}
