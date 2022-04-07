package com.example.slambook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Activity5_AddEntry extends AppCompatActivity {

    ImageView profilePicture;
    DatePickerDialog picker;
    Context context = this;
    TextView hobbiesError, genderError;
    EditText lastname, firstname, middlename, remark, birthday, otherGender, address, contact, hobbies, otherInfo;
    RadioGroup radioGender;
    Button goBack, addEntry, cancel;

    Bitmap profile;
    String lastNameInput, firstNameInput, middleNameInput, remarkInput, birthInput, genderInput, othGenderInput, addressInput, contactInput, hobbiesInput, othInfoInput;
    String empty = "Field cannot be empty.";

    final int PICK_IMAGE = 1;
    final int REQUEST_CODE_CAMERA = 2;
    final int REQUEST_CODE_PERMISSION = 3;
    Uri imageUri;
    SQLiteDBHelper myDB;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addentry_layout);

        myDB = new SQLiteDBHelper(context);
        init();
        regList();
    }

    /*
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                profile = imageBitmap;
                profilePicture.setImageBitmap(imageBitmap);
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity5_AddEntry.this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(Activity5_AddEntry.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(Activity5_AddEntry.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Grant Permission");
                builder.setMessage("Camera and Read Storage");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Activity5_AddEntry.this, new String[] {
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
                ActivityCompat.requestPermissions(Activity5_AddEntry.this, new String[] {
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
        profilePicture = findViewById(R.id.profilePicture_entry);
        lastname = findViewById(R.id.lastname_entry);
        firstname = findViewById(R.id.firstname_entry);
        middlename = findViewById(R.id.middlename_entry);
        remark = findViewById(R.id.remark_entry);
        birthday = findViewById(R.id.birthday_entry);
        radioGender = findViewById(R.id.radioGender_entry);
        otherGender = findViewById(R.id.others_entry);
        address = findViewById(R.id.address_entry);
        contact = findViewById(R.id.contactnumber_entry);
        hobbiesError = findViewById(R.id.hobbiesError_entry);
        genderError = findViewById(R.id.genderError_entry);
        hobbies = findViewById(R.id.hobbies_entry);
        otherInfo = findViewById(R.id.otherInfoAnswer_entry);
        goBack = findViewById(R.id.goBack);
        addEntry = findViewById(R.id.addEntry_entry);
        cancel = findViewById(R.id.cancel_entry);
    }

    private void regList() {

        //This is the date picker dialog for birthday.
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
                                        break;
                                    }
                                    case 2:{
                                        birthday.setText("February" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 3:{
                                        birthday.setText("March" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 4:{
                                        birthday.setText("April" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 5:{
                                        birthday.setText("May" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 6:{
                                        birthday.setText("June" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 7:{
                                        birthday.setText("July" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 8:{
                                        birthday.setText("August" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 9:{
                                        birthday.setText("September" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 10:{
                                        birthday.setText("October" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 11:{
                                        birthday.setText("November" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                    case 12:{
                                        birthday.setText("December" + " " + dayOfMonth + ", " + year);
                                        break;
                                    }
                                }
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //This is for the gender.
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radioOther_entry){
                    otherGender.setVisibility(View.VISIBLE);
                }
                else {
                    int selectedId = checkedId;
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    genderInput = (String) radioButton.getText();
                    otherGender.setVisibility(View.INVISIBLE);
                    genderError.setError(null);
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity3_EntryList.entryData.clear();
                Activity5_AddEntry.super.onBackPressed();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity3_EntryList.entryData.clear();
                Activity5_AddEntry.super.onBackPressed();
            }
        });

        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastNameInput = lastname.getText().toString();
                firstNameInput = firstname.getText().toString();
                middleNameInput = middlename.getText().toString();
                remarkInput = remark.getText().toString();
                birthInput = birthday.getText().toString();
                othGenderInput = otherGender.getText().toString();
                addressInput = address.getText().toString();
                contactInput = contact.getText().toString();
                hobbiesInput = hobbies.getText().toString();
                othInfoInput = otherInfo.getText().toString();

                //This is to check the field of name if empty for error.
                if (lastNameInput.isEmpty()){
                    lastname.setError(empty);
                    lastname.requestFocus();
                } else {
                    lastname.setError(null);

                }

                if (firstNameInput.isEmpty()){
                    firstname.setError(empty);
                    firstname.requestFocus();
                } else {
                    firstname.setError(null);

                }

                if (middleNameInput.isEmpty()){
                    middlename.setError(empty);
                    middlename.requestFocus();
                } else {
                    middlename.setError(null);

                }

                //This is to check the field of remark if empty for error.
                if (remarkInput.isEmpty()){
                    remark.setError(empty);
                    remark.requestFocus();
                } else {
                    remark.setError(null);
                }

                //This is to check the field of birthday if empty for error.
                if(!birthInput.isEmpty()){
                    birthday.setError(null);

                } else {
                    birthday.setError(empty);
                    birthday.requestFocus();
                }

                //This is to check the field of "other gender to be specified" and gender if empty for error.
                if(othGenderInput.isEmpty()){
                    otherGender.setError(empty);
                    otherGender.requestFocus();
                } else {
                    otherGender.setError(null);
                }

                int isSelected = radioGender.getCheckedRadioButtonId();
                if (isSelected == -1){
                    genderError.setError(empty);
                    genderError.requestFocus();
                }
                else if (isSelected == R.id.radioOther_entry){
                    genderInput = othGenderInput;
                }
                else {
                    genderError.setError(null);
                }

                //This is to check the field of hobbies if empty for error.
                if(hobbiesInput.isEmpty()){
                    hobbies.setError(empty);
                    hobbies.requestFocus();
                }
                else {
                    hobbies.setError(null);
                }

                /*
                if (profile == null){
                    Drawable drawable = context.getResources().getDrawable(R.drawable.add_profile);
                    Bitmap icon = convertToBitmap(drawable, 110, 110);
                    profile = icon;
                }
                 */

                if (!lastNameInput.isEmpty() && !firstNameInput.isEmpty() && !middleNameInput.isEmpty() && !remarkInput.isEmpty()
                        && !birthInput.isEmpty() && !genderInput.isEmpty() && !hobbiesInput.isEmpty()) {
                    //TODO:
                    //Activity3_EntryList.entryData.add(new ForIndividualEntry_Data(profile, lastNameInput, remarkInput, birthInput, genderInput, addressInput, contactInput, hobbiesInput, othInfoInput));
                    saveToGallery();
                    //selectAllEntry();
                    if(myDB.insertEntry(Activity3_EntryList.id, imagePath, lastNameInput, firstNameInput,
                            middleNameInput, remarkInput, birthInput, genderInput, addressInput, contactInput, hobbiesInput, othInfoInput)){
                        Toast.makeText(context, "New Entry Added.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Insert failed.", Toast.LENGTH_SHORT).show();
                    }

                    Activity3_EntryList.entryData.clear();
                    //Activity3_EntryList.recyclerviewAdapter.notifyDataSetChanged();
                    Activity5_AddEntry.super.onBackPressed();
                }
            }
        });

    }



}