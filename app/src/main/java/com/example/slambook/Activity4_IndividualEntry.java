package com.example.slambook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Activity4_IndividualEntry extends AppCompatActivity {

    ImageView picture;
    TextView lastname, firstname, middlename, remark, birthday, otherGender, address, contact, hobby, info;
    RadioGroup gender;
    RadioButton f, m, o;
    Button goBack, goBackEntry;

    int noteID;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individualentry_layout);

        init();
        regList();
    }

    private void init() {
        picture = findViewById(R.id.displayProfilePicture);
        lastname = findViewById(R.id.displayLastName_xml);
        firstname = findViewById(R.id.displayFirstName_xml);
        middlename = findViewById(R.id.displayMiddleName_xml);
        remark = findViewById(R.id.displayRemark_xml);
        birthday = findViewById(R.id.displayBirthday_xml);
        gender = findViewById(R.id.displayRadioGender);
        f = findViewById(R.id.displayRadioFemale);
        m = findViewById(R.id.displayRadioMale);
        o = findViewById(R.id.displayRadioOther);
        otherGender = findViewById(R.id.displayOthers_xml);
        address = findViewById(R.id.displayAddress_xml);
        contact = findViewById(R.id.displayContactnumber_xml);
        hobby = findViewById(R.id.displayHobbies_xml);
        info = findViewById(R.id.displayOtherInfoAnswer_xml);
        goBack = findViewById(R.id.goBack);
        goBackEntry = findViewById(R.id.goBackEntry);
    }

    private void regList() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            noteID = bundle.getInt("NoteID");

            String profile = bundle.getString("Picture");
            picture.setImageURI(Uri.parse(profile));

            String ln = bundle.getString("LastName");
            lastname.setText(ln);

            String fn = bundle.getString("FirstName");
            firstname.setText(fn);

            String mn = bundle.getString("MiddleName");
            middlename.setText(mn);

            String completeRemark = bundle.getString("Remark");
            remark.setText(completeRemark);

            String completeBirthday = bundle.getString("Birthday");
            birthday.setText(completeBirthday);

            String completeGender = bundle.getString("Gender");
            if (completeGender.equals("Female")){
                gender.check(R.id.displayRadioFemale);
                m.setClickable(false);
                o.setClickable(false);
            }
            else if (completeGender.equals("Male")){
                gender.check(R.id.displayRadioMale);
                f.setClickable(false);
                o.setClickable(false);
            }
            else {
                gender.check(R.id.displayRadioOther);
                otherGender.setVisibility(View.VISIBLE);
                otherGender.setText(completeGender);
                f.setClickable(false);
                m.setClickable(false);
            }

            String completeAddress = bundle.getString("Address");
            address.setText(completeAddress);

            String completeContactNumber = bundle.getString("Contact");
            contact.setText(completeContactNumber);

            String completeHobbies = bundle.getString("Hobby");
            hobby.setText(completeHobbies);

            String completeInfo = bundle.getString("Info");
            info.setText(completeInfo);

        }

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity3_EntryList.entryData.clear();
                Activity4_IndividualEntry.super.onBackPressed();
            }
        });

        goBackEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity3_EntryList.entryData.clear();
                Activity4_IndividualEntry.super.onBackPressed();
            }
        });
    }
}