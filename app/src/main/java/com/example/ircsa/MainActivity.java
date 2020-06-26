package com.example.ircsa;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private CircleImageView setupImage;
    ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

         if(currentuser == null){
            Log.i("Output","Not Logged in");
            Intent loginintent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginintent);


            // No user is signed in
        }


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.main,new HomeFragment(),"Home_Fragment").commit();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new HomeFragment(),"Home_Fragment").commit();
        } else if (id == R.id.nav_volunteer) {
            goToUrl( "https://docs.google.com/forms/d/e/1FAIpQLSf5TBcpJGkTq1KIPgg6Gj_mM9aypP6Bzmg1vzoZGrRrY8R2zg/viewform?usp=sf_link/");
        } else if (id == R.id.nav_intern) {
            goToUrl("https://docs.google.com/forms/d/e/1FAIpQLSeHeWZflRnWP5q-8qintDiNAk-uYyF4wZ1ZkY2ic6VCAGij3g/viewform?usp=sf_link");
        } else if (id == R.id.nav_bloodb) {
            openbloodbank();
        } else if (id == R.id.other_activities) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new OtherActivitiesFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_jac) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new JanAushadiFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_au) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new AboutUsFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_dc) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new DistrictBankFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_developer) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new DeveloperFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_bbl) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new BblFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_donate) {
           FragmentManager fm = getSupportFragmentManager();
           fm.beginTransaction().replace(R.id.main,new CharityFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_cu) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new CsFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_emc) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new EmergencyFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_tc) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new TalukaFragment()).addToBackStack("Home_Fragment").commit();
        } else if (id == R.id.nav_logout) {
            FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
            mAuth.signOut();
            Intent login_activityIntent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(login_activityIntent);
            finish();
        }else if(id == R.id.nav_share) {
            ClipData clipdata = ClipData.newPlainText("text","Check out the new IRCS_Karnataka app now available on play store!!! https://play.google.com/store/apps/details?id=com.ircs.ircsa");
            clipboardManager.setPrimaryClip(clipdata);
            Toast.makeText(getApplicationContext(),"Share link copied to Clipboard",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openbloodbank(){
        Intent intent = new Intent(this, NewsFragment.class);
        startActivity(intent);
    };

    public void writeNewUser1(String name, String gender, String dob, String age, String no,String email,String address,String district,String daysavailable,String volunteer) {
        User1 user = new User1(name, gender, dob, age, no, email, address,district, daysavailable, volunteer);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("VolunteerList");
        myRef.child(user.name+" "+user.age).child("Name").setValue(user.name);
        myRef.child(user.name+" "+user.age).child("Gender").setValue(user.gender);
        myRef.child(user.name+" "+user.age).child("DOB").setValue(user.dob);
        myRef.child(user.name+" "+user.age).child("Age").setValue(user.age);
        myRef.child(user.name+" "+user.age).child("Contact Number").setValue(user.no);
        myRef.child(user.name+" "+user.age).child("Email Address").setValue(user.email);
        myRef.child(user.name+" "+user.age).child("Permanent Address").setValue(user.address);
        myRef.child(user.name+" "+user.age).child("District").setValue(user.district);
        myRef.child(user.name+" "+user.age).child("Days Available").setValue(user.dayavailable);
        myRef.child(user.name+" "+user.age).child("Interseted in").setValue(user.volunteer);

    }

    public void register(View view) {
        final EditText nameField = (EditText) findViewById(R.id.input_name);
        String name = nameField.getText().toString();

        final EditText nameField1 = (EditText) findViewById(R.id.dob);
        String name1 = nameField1.getText().toString();

        final EditText nameField2 = (EditText) findViewById(R.id.age);
        String name2 = nameField2.getText().toString();

        final EditText nameField3 = (EditText) findViewById(R.id.input_mn);
        String name3 = nameField3.getText().toString();

        final EditText nameField4 = (EditText) findViewById(R.id.input_email);
        String name4 = nameField4.getText().toString();

        final EditText nameField5 = (EditText) findViewById(R.id.input_pa);
        String name5 = nameField5.getText().toString();

        final EditText nameField6 = (EditText) findViewById(R.id.input_district);
        String name6 = nameField6.getText().toString();

        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.gender);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();

        final Spinner feedbackSpinner1 = (Spinner) findViewById(R.id.dayselect);
        String feedbackType1 = feedbackSpinner1.getSelectedItem().toString();

        final Spinner feedbackSpinner2 = (Spinner) findViewById(R.id.volunteer);
        String feedbackType2 = feedbackSpinner2.getSelectedItem().toString();

        writeNewUser1(name, feedbackType , name1, name2, name3, name4, name5, name6, feedbackType1, feedbackType2);
    }

    public void writeIntern(String fname, String lname, String mn, String amn, String age,String dob,String pa,String email,String district,String q1,String q2,String q3,String q4,String q5) {
        InternUser user = new InternUser(fname,lname,mn , amn,age,dob, pa, email , district, q1, q2, q3, q4, q5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Intern_Application_List");
        myRef.child(user.fname+" "+user.age).child("First Name").setValue(user.fname);
        myRef.child(user.fname+" "+user.age).child("Last Name").setValue(user.lname);
        myRef.child(user.fname+" "+user.age).child("Mobile Number").setValue(user.mn);
        myRef.child(user.fname+" "+user.age).child("Alternate Mobile Number").setValue(user.amn);
        myRef.child(user.fname+" "+user.age).child("Age").setValue(user.age);
        myRef.child(user.fname+" "+user.age).child("Date of Birth").setValue(user.dob);
        myRef.child(user.fname+" "+user.age).child("Permanent Address").setValue(user.address);
        myRef.child(user.fname+" "+user.age).child("Email").setValue(user.email);
        myRef.child(user.fname+" "+user.age).child("District").setValue(user.district);
        myRef.child(user.fname+" "+user.age).child("Question 1").setValue(user.q1);
        myRef.child(user.fname+" "+user.age).child("Question 2").setValue(user.q2);
        myRef.child(user.fname+" "+user.age).child("Question 3").setValue(user.q3);
        myRef.child(user.fname+" "+user.age).child("Question 4").setValue(user.q4);
        myRef.child(user.fname+" "+user.age).child("Question 5").setValue(user.q5);

    };

    private void displayToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    private boolean checkAnswer(String s,int i){
        StringTokenizer ST=new StringTokenizer(s);
        if (s.isEmpty()) {
            displayToast("Enter answer to Question "+i);
            return true;
        }
        else if(ST.countTokens()<50){
            displayToast("Minimum words in answer to Q"+i+" should be 50");
            return true;
        }
        else
            return false;
    }

    public void internRegister(View view) {
        final EditText nameField = (EditText) findViewById(R.id.fname);
        String name = nameField.getText().toString();
        if(name.isEmpty()){
            displayToast("Enter First Name");
            return;
        }

        final EditText nameField1 = (EditText) findViewById(R.id.lname);
        String name1 = nameField1.getText().toString();
        if(name1.isEmpty()){
            displayToast("Enter Last Name");
            return;
        }

        final EditText nameField2 = (EditText) findViewById(R.id.mn);
        String name2 = nameField2.getText().toString();
        if(name2.isEmpty()){
            displayToast("Enter mobile number");
            return;
        }
        else if(name2.length()!=10){
            displayToast("Enter a valid mobile number");
            return;
        }

        final EditText nameField3 = (EditText) findViewById(R.id.amn);
        String name3 = nameField3.getText().toString();
        if(name3.isEmpty()){
            displayToast("Enter alternate mobile number");
            return;
        }
        else if(name3.length()!=10){
            displayToast("Enter a valid alternate mobile number");
            return;
        }

        final EditText nameField4 = (EditText) findViewById(R.id.age);
        String name4 = nameField4.getText().toString();
        if(name4.isEmpty()){
            displayToast("Enter age");
            return;
        }
        else if(Integer.parseInt(name4)>=60){
            displayToast("Not an eligible age for internship");
            return;
        }

        final EditText nameField5 = (EditText) findViewById(R.id.dob);
        String name5 = nameField5.getText().toString();
        if(name5.isEmpty()){
            displayToast("Enter date of birth");
            return;
        }

        final EditText nameField7 = (EditText) findViewById(R.id.pa);
        String name7 = nameField7.getText().toString();
        if(name7.isEmpty()){
            displayToast("Enter permanent address");
            return;
        }

        final EditText nameField8 = (EditText) findViewById(R.id.email);
        String name8 = nameField8.getText().toString();
        if(name8.isEmpty()){
            displayToast("Enter email address");
            return;
        }
        else if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(name8).matches())){
            displayToast("Enter a valid email address");
            return;
        }

        final EditText nameField9 = (EditText) findViewById(R.id.q1);
        String name9 = nameField9.getText().toString();
        if(checkAnswer(name9,1)){
            return;
        }

        final EditText nameField10 = (EditText) findViewById(R.id.q2);
        String name10 = nameField10.getText().toString();
        if(checkAnswer(name10,2)){
            return;
        }

        final EditText nameField11 = (EditText) findViewById(R.id.q3);
        String name11 = nameField11.getText().toString();
        if(checkAnswer(name11,3)){
            return;
        }

        final EditText nameField12 = (EditText) findViewById(R.id.q4);
        String name12 = nameField12.getText().toString();
        if(checkAnswer(name12,4)){
            return;
        }

        final EditText nameField13 = (EditText) findViewById(R.id.q5);
        String name13 = nameField13.getText().toString();
        if(checkAnswer(name13,5)){
            return;
        }

        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.district);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();

        writeIntern(name, name1, name2, name3, name4, name5,name7, name8,feedbackType, name9, name10, name11, name12, name13);

        displayToast("Intern sucessfully registered");

        nameField.setText("");
        nameField1.setText("");
        nameField2.setText("");
        nameField3.setText("");
        nameField4.setText("");
        nameField5.setText("");
        nameField7.setText("");
        nameField8.setText("");
        nameField9.setText("");
        nameField10.setText("");
        nameField11.setText("");
        nameField12.setText("");
        nameField13.setText("");
    }
    public void copyEmailId(View view){
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Email ID", "yrckar@gmail.com");
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), " Email ID copied to clipboard", Toast.LENGTH_LONG).show();
    }

    public void dialUs1(View view){
        Button dialUs=(Button)findViewById(R.id.phoneNumber1Button);
        String s="tel:080-22264205";
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(s));
        startActivity(intent);
    }

    public void dialUs2(View view){
        Button dialUs=(Button)findViewById(R.id.phoneNumber1Button);
        String s="tel:080-22268435";
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(s));
        startActivity(intent);
    }
    //developers Page

    public void goTo_n_insta (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_n_fb (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_n_linkedin (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_k_insta (View view) {
        goToUrl ("https://www.instagram.com/kshitij_ver/?hl=en");
    }
    public void goTo_k_fb (View view) {
        goToUrl ("https://www.facebook.com/kshitij.verma.1422");
    }
    public void goTo_k_linkedin (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_ay_insta (View view) {
        goToUrl ("https://www.instagram.com/ayush__chandak/");
    }
    public void goTo_ay_fb (View view) {
        goToUrl ("https://www.facebook.com/aayushchandakroxx");
    }
    public void goTo_ay_linkedin (View view) {
        goToUrl ("https://www.linkedin.com/in/ayush-chandak-520003160/");
    }
    public void goTo_kar_insta (View view) {
        goToUrl ("https://www.instagram.com/kartikeya_aryan/");
    }
    public void goTo_kar_fb (View view) {
        goToUrl ("https://www.facebook.com/profile.php?id=100009289614261");
    }
    public void goTo_kar_linkedin (View view) {
        goToUrl ("https://www.linkedin.com/in/kartikeya-aryan-agarwal-466869167/");
    }
    public void goTo_kb_insta (View view) {
        goToUrl ("https://www.instagram.com/kbtg25/");
    }
    public void goTo_kb_fb (View view) {
        goToUrl ("https://www.facebook.com/kushal.bakliwal.31");
    }
    public void goTo_kb_linkedin (View view) {
        goToUrl ("https://www.linkedin.com/in/kushal-bakliwal-973828167/");
    }

    public void goTo_sidg_insta (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_sidg_fb (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_sidg_linkedin (View view) {
        goToUrl ("https://www.google.com/");
    }

    public void goTo_swt_insta (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_swt_fb (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_swt_linkedin (View view) {
        goToUrl ("https://www.google.com/");
    }

    public void goTo_hab_insta (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_hab_fb (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_hab_linkedin (View view) {
        goToUrl ("https://www.google.com/");
    }

    public void goTo_hig_insta (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_hig_fb (View view) {
        goToUrl ("https://www.google.com/");
    }
    public void goTo_hig_linkedin (View view) {
        goToUrl ("https://www.google.com/");
    }

    public void goToSu (View view) {
        goToUrl ( "http://superuser.com/");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    //account setup

    public void onClick_AccountSetup(MenuItem menuItem)
    {
        Intent account_setup_Intent=new Intent(MainActivity.this, AccountSetup.class);
        startActivity(account_setup_Intent);
    }



}

