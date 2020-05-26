package com.example.ircsa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class OtherActivitiesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_other_activities_fragment, container, false);
        ImageView btmFragment = (ImageView) myView.findViewById(R.id.disaster_response_button);
        ImageView serv_button=(ImageView)myView.findViewById(R.id.serv_button) ;
        ImageView disaster_management_button=(ImageView)myView.findViewById(R.id.disaster_management_button) ;
        ImageView blood_donation_button=(ImageView)myView.findViewById(R.id.blood_donation_button) ;
        ImageView youth_redCross_button=(ImageView)myView.findViewById(R.id.youth_redcross_button) ;
        ImageView junior_redCross_button=(ImageView)myView.findViewById(R.id.junior_redcross_button) ;
        ImageView urbanWelfare_button=(ImageView)myView.findViewById(R.id.urban_welfare_button) ;
        ImageView tb_button=(ImageView)myView.findViewById(R.id.tuberculosis_button) ;
        ImageView optical_button=(ImageView)myView.findViewById(R.id.opticalcenter_button) ;
        ImageView internship_button=(ImageView)myView.findViewById(R.id.internship_button) ;
        ImageView boca_button=(ImageView)myView.findViewById(R.id.boca_button) ;
        ImageView first_aid=(ImageView)myView.findViewById(R.id.first_aid_button) ;
        btmFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new disaster_response_fragment());
                fr.addToBackStack(null);
                fr.commit();

                

            }
        });
        serv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new serv_fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        disaster_management_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new disaster_management_fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        blood_donation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new blood_donation_fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        youth_redCross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new youth_redCross_Fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        junior_redCross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new juniorRedCross_Fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        urbanWelfare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new urban_Welfare_fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });

        tb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new tb_fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        optical_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new optical_center_fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });

        internship_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new internship_fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        boca_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main, new boca_fragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        first_aid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl ("https://play.google.com/store/apps/details?id=org.indianredcross.firstaid");


            }
        });



        return myView;

    }




    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


}
