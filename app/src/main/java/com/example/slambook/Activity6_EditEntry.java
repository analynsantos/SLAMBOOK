package com.example.slambook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Activity6_EditEntry extends AppCompatActivity {

    EditText lastname, firstname, middlename, remark, birthday, otherGender, address, contact, hobbies, otherInfo;
    RadioGroup radioGender;
    Button goBack, save, cancel;
    DatePickerDialog picker;
    ImageView profilePicture;
    int noteID;
    Context context = this;

    Uri imageUri;
    String imagePath;
    final int PICK_IMAGE = 1;
    final int REQUEST_CODE_CAMERA = 2;
    final int REQUEST_CODE_PERMISSION = 3;
    SQLiteDBHelper myDB;
    String entryID, gen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editentry_layout);

        myDB = new SQLiteDBHelper(context);
        init();
        regList();
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            profilePicture.setImageBitmap(imageBitmap);
            //TODO
            //Activity3_EntryList.entryData.get(noteID).setPersonPicture(imageBitmap);
        }

    }

    public void profilePicture(View view){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,REQUEST_CODE_CAMERA);

    }
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            profilePicture.setImageBitmap(imageBitmap);
        }

        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imagePath = imageUri.toString();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void saveToGallery() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) profilePicture.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/SLAMBOOK");
        dir.mkdir();

        String filename = String.format("%d.png", System.currentTimeMillis());
        File outfile = new File(dir,filename);
        imagePath = outfile.getAbsolutePath();
        try {
            outputStream = new FileOutputStream(outfile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        try {
            outputStream.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void profilePicture(View view){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Permission not Granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity6_EditEntry.this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(Activity6_EditEntry.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(Activity6_EditEntry.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Grant Permission");
                builder.setMessage("Camera and Read Storage");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Activity6_EditEntry.this, new String[] {
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, REQUEST_CODE_PERMISSION);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            else {
                ActivityCompat.requestPermissions(Activity6_EditEntry.this, new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CODE_PERMISSION);
            }
        }

        else {
            //Permission Granted
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture, null);
            builder.setCancelable(false);
            builder.setView(dialogView);

            ImageView imageViewADPPCamera = dialogView.findViewById(R.id.imageViewADPPCamera);
            ImageView imageViewADPPGallery = dialogView.findViewById(R.id.imageViewADPPGallery);

            AlertDialog alertDialogProfile = builder.create();
            alertDialogProfile.show();

            imageViewADPPCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePictureCamera();
                    alertDialogProfile.cancel();
                }
            });

            imageViewADPPGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePictureFromGallery();
                    alertDialogProfile.cancel();
                }
            });
        }
    }

    public void takePictureFromGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }

    public void takePictureCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,REQUEST_CODE_CAMERA);
    }

    private void init() {
        profilePicture = findViewById(R.id.profilePicture_editEntry);
        lastname = findViewById(R.id.lastname_editEntry);
        firstname = findViewById(R.id.firstname_editEntry);
        middlename = findViewById(R.id.middlename_editEntry);
        remark = findViewById(R.id.remark_editEntry);
        birthday = findViewById(R.id.birthday_editEntry);
        radioGender = findViewById(R.id.radioGender_editEntry);
        otherGender = findViewById(R.id.others_editEntry);
        address = findViewById(R.id.address_editEntry);
        contact = findViewById(R.id.contactnumber_editEntry);
        hobbies = findViewById(R.id.hobbies_editEntry);
        otherInfo = findViewById(R.id.otherInfoAnswer_editEntry);
        goBack = findViewById(R.id.goBack_editEntry);
        save = findViewById(R.id.save_editEntry);
        cancel = findViewById(R.id.cancel_editEntry);
    }

    private void regList() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            noteID = bundle.getInt("NoteID");

            entryID = bundle.getString("EntryID");

            String profile = bundle.getString("Picture");
            profilePicture.setImageURI(Uri.parse(profile));

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

            birthday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    birthday.setError(null);
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    switch (monthOfYear + 1){
                                        case 1:{
                                            birthday.setText("January" + " " + dayOfMonth + ", " + year);
                                            String input = "January" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 2:{
                                            birthday.setText("February" + " " + dayOfMonth + ", " + year);
                                            String input = "February" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 3:{
                                            birthday.setText("March" + " " + dayOfMonth + ", " + year);
                                            String input = "March" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 4:{
                                            birthday.setText("April" + " " + dayOfMonth + ", " + year);
                                            String input = "April" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 5:{
                                            birthday.setText("May" + " " + dayOfMonth + ", " + year);
                                            String input = "May" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 6:{
                                            birthday.setText("June" + " " + dayOfMonth + ", " + year);
                                            String input = "June" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 7:{
                                            birthday.setText("July" + " " + dayOfMonth + ", " + year);
                                            String input = "July" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 8:{
                                            birthday.setText("August" + " " + dayOfMonth + ", " + year);
                                            String input = "August" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 9:{
                                            birthday.setText("September" + " " + dayOfMonth + ", " + year);
                                            String input = "September" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 10:{
                                            birthday.setText("October" + " " + dayOfMonth + ", " + year);
                                            String input = "October" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 11:{
                                            birthday.setText("November" + " " + dayOfMonth + ", " + year);
                                            String input = "November" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                        case 12:{
                                            birthday.setText("December" + " " + dayOfMonth + ", " + year);
                                            String input = "December" + " " + dayOfMonth + ", " + year;
                                            Activity3_EntryList.entryData.get(noteID).setBirthday(input);
                                            break;
                                        }
                                    }
                                }
                            }, year, month, day);
                    picker.show();
                }
            });

            String completeGender = bundle.getString("Gender");
            if (completeGender.equals("Female")){
                radioGender.check(R.id.radioFemale_editEntry);
                Activity3_EntryList.entryData.get(noteID).setGender(completeGender);
                otherGender.setVisibility(View.INVISIBLE);
            }
            else if (completeGender.equals("Male")){
                radioGender.check(R.id.radioMale_editEntry);
                Activity3_EntryList.entryData.get(noteID).setGender(completeGender);
                otherGender.setVisibility(View.INVISIBLE);
            }
            else {
                radioGender.check(R.id.radioOther_editEntry);
                Activity3_EntryList.entryData.get(noteID).setGender(completeGender);
                otherGender.setVisibility(View.VISIBLE);
                otherGender.setText(completeGender);
            }

            radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId==R.id.radioOther_editEntry){
                        otherGender.setVisibility(View.VISIBLE);
                        otherGender.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                Activity3_EntryList.entryData.get(noteID).setGender(String.valueOf(s));

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    }
                    else {
                        otherGender.setVisibility(View.INVISIBLE);
                        int selectedId = checkedId;
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        String completeGender = (String) radioButton.getText();
                        Activity3_EntryList.entryData.get(noteID).setGender(completeGender);
                        gen = completeGender;
                    }

                }
            });

            String completeAddress = bundle.getString("Address");
            address.setText(completeAddress);

            String completeContactNumber = bundle.getString("Contact");
            contact.setText(completeContactNumber);

            String completeHobbies = bundle.getString("Hobby");
            hobbies.setText(completeHobbies);

            String completeInfo = bundle.getString("Info");
            otherInfo.setText(completeInfo);
        }

        //TODO
        lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Activity3_EntryList.entryData.get(noteID).setLastname(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Activity3_EntryList.entryData.get(noteID).setFirstname(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        middlename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Activity3_EntryList.entryData.get(noteID).setMiddlename(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Activity3_EntryList.entryData.get(noteID).setRemark(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Activity3_EntryList.entryData.get(noteID).setAddress(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Activity3_EntryList.entryData.get(noteID).setContactNumber(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        hobbies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Activity3_EntryList.entryData.get(noteID).setHobbies(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otherInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Activity3_EntryList.entryData.get(noteID).setOtherInfo(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity3_EntryList.entryData.clear();
                saveToGallery();

                if(myDB.updateEntry(entryID,Activity3_EntryList.id,imagePath,lastname.getText().toString(),firstname.getText().toString(),middlename.getText().toString(),
                        remark.getText().toString(),birthday.getText().toString(),gen,address.getText().toString(),contact.getText().toString(),
                        hobbies.getText().toString(),otherInfo.getText().toString())) {
                    Toast.makeText(context, "New Entry Updated.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Update Failed.", Toast.LENGTH_SHORT).show();
                }
                Activity6_EditEntry.super.onBackPressed();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity3_EntryList.entryData.clear();
                Activity6_EditEntry.super.onBackPressed();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity3_EntryList.entryData.clear();
                Activity6_EditEntry.super.onBackPressed();
            }
        });

        }
}