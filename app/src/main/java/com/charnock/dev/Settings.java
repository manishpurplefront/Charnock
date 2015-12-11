package com.charnock.dev;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Settings extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.settings, container, false);
        setHasOptionsMenu(true);

        try {
            getActivity().getActionBar().removeAllTabs();
            getActivity().invalidateOptionsMenu();
            getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            getActivity().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + getString(R.string.app_name_home) + "</font>")));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        Button b = (Button) rootView.findViewById(R.id.profile);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewProfile.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        Button b2 = (Button) rootView.findViewById(R.id.password);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Change_Password.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        Button b3 = (Button) rootView.findViewById(R.id.logout);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.logout))
                        .setCancelable(true)
                        .setMessage(getResources().getString(R.string.logout_warning))
                        .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbhelp entry = new dbhelp(getActivity());
                                entry.open();
                                entry.logoout();
                                entry.close();

                                Intent intent2 = new Intent(getActivity(), MainActivity.class);
                                getActivity().finish();
                                startActivity(intent2);
                                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            }

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return rootView;
    }
}
