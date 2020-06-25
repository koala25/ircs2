package com.example.ircsa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class NewsFragment extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_fragment);



    }


    public class User{

        public String email;
        public String name;
        public String fname;
        public String number;
        public String nd;
        public String address;
        public String state;
        public String city;
        public String bloodgrp;

        public User(String name, String fname, String number, String email, String nd,String address,String state,String city,String bloodgrp){
            this.email = email;
            this.name = name;
            this.fname = fname;
            this.number = number;
            this.nd = nd;
            this.address = address;
            this.state = state;
            this.city = city;
            this.bloodgrp = bloodgrp;
        }

    };

    public void writeNewUser(String name, String fname, String number, String email, String nd,String address,String state,String city,String bloodgrp) {
        User user = new User(name, fname, number, email, nd, address, state,city,bloodgrp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("BloodBank/DonorList");
        myRef.child(number).child("Name").setValue(user.name);
        myRef.child(number).child("Father's Name").setValue(user.fname);
        myRef.child(number).child("Number").setValue(user.number);
        myRef.child(number).child("Email").setValue(user.email);
        myRef.child(number).child("Number of Donations").setValue(user.nd);
        myRef.child(number).child("Address").setValue(user.address);
        myRef.child(number).child("City").setValue(user.city);
        myRef.child(number).child("Blood Group").setValue(user.bloodgrp);;
        EditText nameField = (EditText) findViewById((R.id.input_name));
        nameField.setText("");

        EditText nameField1 = (EditText) findViewById(R.id.input_fname);
        nameField1.setText("");

        EditText nameField2 = (EditText) findViewById(R.id.input_mn);
        nameField2.setText("");

        EditText nameField3 = (EditText) findViewById(R.id.input_email);
        nameField3.setText("");

        EditText nameField4 = (EditText) findViewById(R.id.input_nd);
        nameField4.setText("");

        EditText nameField5 = (EditText) findViewById(R.id.input_add);
        nameField5.setText("");

        Spinner feedbackSpinner = (Spinner) findViewById(R.id.statespinner);
        feedbackSpinner.setSelection(0);

        Spinner feedbackSpinner1 = (Spinner) findViewById(R.id.disspinner);
        feedbackSpinner1.setSelection(0);

        Spinner feedbackSpinner2 = (Spinner) findViewById(R.id.bloodgrp);
        feedbackSpinner2.setSelection((0));

        Toast.makeText(getApplicationContext(),"Donor Registered Successfully",Toast.LENGTH_SHORT).show();
    }
    public void displayToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void signUp(View view) {
        final EditText nameField = (EditText) findViewById(R.id.input_name);
        String name = nameField.getText().toString();
        if(name.isEmpty()){
            displayToast("Enter name");
            return;
        }
        final EditText nameField1 = (EditText) findViewById(R.id.input_fname);
        String name1 = nameField1.getText().toString();
        if(name1.isEmpty()){
            displayToast("Enter Father's name");
            return;
        }

        final EditText nameField2 = (EditText) findViewById(R.id.input_mn);
        String name2 = nameField2.getText().toString();
        if(name2.isEmpty()){
            displayToast("Enter mobile number");
            return;
        }
        else if(name2.length()!=10){
            displayToast("Enter a valid phone number");
            return;
        }

        final EditText nameField3 = (EditText) findViewById(R.id.input_email);
        String name3 = nameField3.getText().toString();
        if(name3.isEmpty()){
            displayToast("Enter email ID");
            return;
        }
        else if(!isValidEmailId(name3.trim())){
            displayToast("Enter a valid email ID");
            return;
        }

        final EditText nameField4 = (EditText) findViewById(R.id.input_nd);
        String name4 = nameField4.getText().toString();
        if(name4.isEmpty()){
            displayToast("Enter the number of donations");
            return;
        }

        final EditText nameField5 = (EditText) findViewById(R.id.input_add);
        String name5 = nameField5.getText().toString();
        if(name5.isEmpty()){
            displayToast("Enter the address");
            return;
        }

        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.statespinner);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();

        final Spinner feedbackSpinner1 = (Spinner) findViewById(R.id.disspinner);
        String feedbackType1 = feedbackSpinner1.getSelectedItem().toString();

        final Spinner feedbackSpinner2 = (Spinner) findViewById(R.id.bloodgrp);
        String feedbackType2 = feedbackSpinner2.getSelectedItem().toString();


        writeNewUser(name, name1, name2, name3, name4, name5, feedbackType, feedbackType1, feedbackType2);
    }




}