package com.example.slambook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Activity2_Register extends AppCompatActivity {

    EditText username_xml, password_xml, confirmpassword_xml;
    EditText lastname_xml, firstname_xml, middlename_xml, email_xml, contactnumber_xml, birthday_xml, others_xml;
    EditText street_xml, housenumber_xml;
    DatePickerDialog picker;
    AutoCompleteTextView barangay_xml, municipality_xml, province_xml;
    RadioGroup radioGender;
    Button register_xml, login_xml, goBack;
    Context context = this;
    String inputUN, inputP, inputCP;
    String inputLm, inputFm, inputMm, inputEm, inputCon, inputBir, inputOth;
    String inputStr, inputHn, inputBar, inputMun, inputProv;
    String empty = "Field cannot be empty.";
    String format = "Enter a valid input";
    String match = "Password does not match.";
    String duplicate = "Security Question must be unique.";
    ForRegister_InfoHolder infoHolder = new ForRegister_InfoHolder();
    ForRegister_ErrorHandler errorHandler = new ForRegister_ErrorHandler();
    CheckBox cbMusic, cbMovies, cbSwim, cbFish, cbDance, cbRead, cbSew, cbCook, cbPaint, cbWrite;
    TextView genderError, hobbiesError, securityError;
    Spinner security1_xml, security2_xml, security3_xml;
    EditText answer1_xml, answer2_xml, answer3_xml;
    String answer1, answer2, answer3;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ImageView profilePicture;
    final int PICK_IMAGE = 1;
    final int REQUEST_CODE_CAMERA = 2;
    final int REQUEST_CODE_PERMISSION = 3;
    Uri imageUri;
    SQLiteDBHelper myDB;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        myDB = new SQLiteDBHelper(context);
        init();
        regList();
    }

    private void init() {
        username_xml = (EditText) findViewById(R.id.username_xml);
        password_xml = (EditText) findViewById(R.id.password_xml);
        confirmpassword_xml = (EditText) findViewById(R.id.confirmpassword_xml);
        lastname_xml = (EditText) findViewById(R.id.lastname_xml);
        firstname_xml = (EditText) findViewById(R.id.firstname_xml);
        middlename_xml = (EditText) findViewById(R.id.middlename_xml);
        email_xml = (EditText) findViewById(R.id.email_xml);
        contactnumber_xml = (EditText) findViewById(R.id.contactnumber_xml);
        street_xml = (EditText) findViewById(R.id.street_xml);
        housenumber_xml = (EditText) findViewById(R.id.housenumber_xml);
        birthday_xml = (EditText) findViewById(R.id.birthday_xml);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        others_xml = (EditText) findViewById(R.id.others_xml);
        barangay_xml= (AutoCompleteTextView) findViewById(R.id.barangay_xml);
        municipality_xml= (AutoCompleteTextView) findViewById(R.id.municipality_xml);
        province_xml= (AutoCompleteTextView) findViewById(R.id.province_xml);
        register_xml = (Button) findViewById(R.id.register_xml);
        login_xml = (Button) findViewById(R.id.login_xml);
        goBack =findViewById(R.id.goBack);
        genderError = (TextView) findViewById(R.id.genderError);
        hobbiesError = (TextView) findViewById(R.id.hobbiesError);
        securityError = (TextView) findViewById(R.id.securityError);
        cbMusic = (CheckBox) findViewById(R.id.cbMusic);
        cbMovies = (CheckBox) findViewById(R.id.cbMovies);
        cbSwim = (CheckBox) findViewById(R.id.cbSwim);
        cbFish = (CheckBox) findViewById(R.id.cbFish);
        cbDance = (CheckBox) findViewById(R.id.cbDance);
        cbRead = (CheckBox) findViewById(R.id.cbRead);
        cbSew = (CheckBox) findViewById(R.id.cbSew);
        cbCook = (CheckBox) findViewById(R.id.cbCook);
        cbPaint = (CheckBox) findViewById(R.id.cbPaint);
        cbWrite = (CheckBox) findViewById(R.id.cbWrite);
        security1_xml = (Spinner) findViewById(R.id.security1_xml);
        security2_xml = (Spinner) findViewById(R.id.security2_xml);
        security3_xml = (Spinner) findViewById(R.id.security3_xml);
        answer1_xml = (EditText) findViewById(R.id.answer1_xml);
        answer2_xml = (EditText) findViewById(R.id.answer2_xml);
        answer3_xml = (EditText) findViewById(R.id.answer3_xml);
        profilePicture = findViewById(R.id.profilePicture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            infoHolder.personPicture = imageBitmap;
            profilePicture.setImageBitmap(imageBitmap);
        }

        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imagePath = imageUri.toString();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                infoHolder.personPicture = bitmap;
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity2_Register.this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(Activity2_Register.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(Activity2_Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Grant Permission");
                builder.setMessage("Camera and Read Storage");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Activity2_Register.this, new String[] {
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
                ActivityCompat.requestPermissions(Activity2_Register.this, new String[] {
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

    //This is for the checkbox in hobbies.
    public void selectItem(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId())
        {
            case R.id.cbMusic:{
                if (checked){
                    infoHolder.hobbiesResult.add("Listen to Music");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Listen to Music");
                }
                break;
            }
            case R.id.cbMovies:{
                if (checked){
                    infoHolder.hobbiesResult.add("Watch Movies");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Watch Movies");
                }
                break;
            }
            case R.id.cbSwim:{
                if (checked){
                    infoHolder.hobbiesResult.add("Swimming");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Swimming");
                }
                break;
            }
            case R.id.cbFish:{
                if (checked){
                    infoHolder.hobbiesResult.add("Fishing");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Fishing");
                }
                break;
            }
            case R.id.cbDance:{
                if (checked){
                    infoHolder.hobbiesResult.add("Dancing");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Dancing");
                }
                break;
            }
            case R.id.cbRead:{
                if (checked){
                    infoHolder.hobbiesResult.add("Reading");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Reading");
                }
                break;
            }
            case R.id.cbSew:{
                if (checked){
                    infoHolder.hobbiesResult.add("Sewing");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Sewing");
                }
                break;
            }
            case R.id.cbCook:{
                if (checked){
                    infoHolder.hobbiesResult.add("Cooking");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Cooking");
                }
                break;
            }
            case R.id.cbPaint:{
                if (checked){
                    infoHolder.hobbiesResult.add("Painting");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Painting");
                }
                break;
            }
            case R.id.cbWrite:{
                if (checked){
                    infoHolder.hobbiesResult.add("Writing");
                    hobbiesError.setError(null);
                }
                else {
                    infoHolder.hobbiesResult.remove("Writing");
                }
                break;
            }
        }
    }

    private void regList() {

        register_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //The inputs in every field was passed into the variable.
                //The declaration of every variable is above of onCreate.
                inputUN = username_xml.getText().toString();
                inputP = password_xml.getText().toString();
                inputCP = confirmpassword_xml.getText().toString();
                inputLm = lastname_xml.getText().toString();
                inputFm = firstname_xml.getText().toString();
                inputMm = middlename_xml.getText().toString();
                inputEm = email_xml.getText().toString();
                inputCon = contactnumber_xml.getText().toString();
                inputStr = street_xml.getText().toString();
                inputHn = housenumber_xml.getText().toString();
                inputBar = barangay_xml.getText().toString();
                inputMun = municipality_xml.getText().toString();
                inputProv = province_xml.getText().toString();
                inputOth = others_xml.getText().toString();
                inputBir = birthday_xml.getText().toString();
                answer1 = answer1_xml.getText().toString();
                answer2 = answer2_xml.getText().toString();
                answer3 = answer3_xml.getText().toString();

                //This is to check the field of username if empty for error.
                if (inputUN.isEmpty()){
                    username_xml.setError(empty);
                    username_xml.requestFocus();
                    errorHandler.errorResult.add("Username is empty.");
                } else {
                    infoHolder.username = inputUN;
                    errorHandler.errorResult.remove("Username is empty.");
                }

                //This is to check the field of password if empty for error.
                if (inputP.isEmpty()){
                    password_xml.setError(empty);
                    password_xml.requestFocus();
                    errorHandler.errorResult.add("Password is empty.");
                }
                else {
                    errorHandler.errorResult.remove("Password is empty.");
                }

                //This is to check the field of confirm password if empty for error.
                if (inputCP.isEmpty()){
                    confirmpassword_xml.setError(empty);
                    confirmpassword_xml.requestFocus();
                    errorHandler.errorResult.add("Confirm Password is empty.");
                } else {
                    infoHolder.confirmPassword = inputCP;
                    errorHandler.errorResult.remove("Confirm Password is empty.");
                }

                //This is to check if password and confirm password is matched and for error if not.
                if (!inputCP.equals(inputP)){
                    confirmpassword_xml.setError(match);
                    confirmpassword_xml.requestFocus();
                    errorHandler.errorResult.add("Password and Confirm Password does not match.");
                }
                else {
                    errorHandler.errorResult.remove("Password and Confirm Password does not match.");
                }

                //This is to check the field of last name if empty for error.
                if (inputLm.isEmpty()){
                    lastname_xml.setError(empty);
                    lastname_xml.requestFocus();
                    errorHandler.errorResult.add("Last Name is empty.");
                } else {
                    infoHolder.lastName = inputLm;
                    errorHandler.errorResult.remove("Last Name is empty.");
                }

                //This is to check the field of first name if empty for error.
                if (inputFm.isEmpty()){
                    firstname_xml.setError(empty);
                    firstname_xml.requestFocus();
                    errorHandler.errorResult.add("First Name is empty.");
                }
                else {
                    infoHolder.firstName = inputFm;
                    errorHandler.errorResult.remove("First Name is empty.");
                }

                //This is to check if the user input in the field of middle name.
                if (!inputMm.isEmpty()){
                    infoHolder.middleName = inputMm;
                }

                //This is to check the field of email if empty for error and for the proper format of email.
                if (inputEm.isEmpty()){
                    email_xml.setError(empty);
                    email_xml.requestFocus();
                    errorHandler.errorResult.add("Email is empty.");
                }
                else {
                    errorHandler.errorResult.remove("Email is empty.");
                    if (email_xml.getText().toString().trim().matches(emailPattern)){
                        infoHolder.email = inputEm;
                        errorHandler.errorResult.remove("Email should be in proper format.");

                    }
                    else {
                        email_xml.setError(format);
                        email_xml.requestFocus();
                        errorHandler.errorResult.add("Email should be in proper format.");
                    }
                }

                //This is to check the field of contact number if empty and 11 digits for PH number for error.
                if (inputCon.isEmpty()){
                    contactnumber_xml.setError(empty);
                    contactnumber_xml.requestFocus();
                    errorHandler.errorResult.add("Contact Number is empty.");
                }
                else {
                    errorHandler.errorResult.remove("Contact Number is empty.");
                    if (inputCon.length()<11){
                        contactnumber_xml.setError(format);
                        contactnumber_xml.requestFocus();
                        errorHandler.errorResult.add("Contact Number must be 11 digits.");
                    }
                    else {
                        contactnumber_xml.setError(null);
                        infoHolder.contactNumber = inputCon;
                        errorHandler.errorResult.remove("Contact Number must be 11 digits.");
                    }
                }

                //This is to check the field of house number if empty for error.
                if (inputHn.isEmpty()){
                    housenumber_xml.setError(empty);
                    housenumber_xml.requestFocus();
                    errorHandler.errorResult.add("House Number is empty.");
                } else {
                    infoHolder.houseNumber = inputHn;
                    errorHandler.errorResult.remove("House Number is empty.");
                }

                //This is to check the field of street if empty for error.
                if (inputStr.isEmpty()){
                    street_xml.setError(empty);
                    street_xml.requestFocus();
                    errorHandler.errorResult.add("Street is empty.");
                } else {
                    infoHolder.street = inputStr;
                    errorHandler.errorResult.remove("Street is empty.");
                }

                //This is to check the field of barangay if empty for error.
                if (inputBar.isEmpty()){
                    barangay_xml.setError(empty);
                    barangay_xml.requestFocus();
                    errorHandler.errorResult.add("Barangay is empty.");
                } else {
                    infoHolder.barangay = inputBar;
                    errorHandler.errorResult.remove("Barangay is empty.");
                }

                //This is to check the field of municipality if empty for error.
                if (!inputMun.isEmpty()){
                    infoHolder.municipality = inputMun;
                    errorHandler.errorResult.remove("Municipality is empty.");
                } else {
                    municipality_xml.setError(empty);
                    municipality_xml.requestFocus();
                    errorHandler.errorResult.add("Municipality is empty.");
                }

                //This is to check the field of province if empty for error.
                if (inputProv.isEmpty()){
                    province_xml.setError(empty);
                    province_xml.requestFocus();
                    errorHandler.errorResult.add("Province is empty.");
                } else {
                    infoHolder.province = inputProv;
                    errorHandler.errorResult.remove("Province is empty.");
                }

                //This is to check the field of "other gender to be specified" and gender if empty for error.
                if(inputOth.isEmpty()){
                    others_xml.setError(empty);
                    others_xml.requestFocus();
                } else {
                    infoHolder.gender = inputOth;
                }

                if (others_xml.getVisibility() == View.VISIBLE && inputOth.isEmpty()){
                    errorHandler.errorResult.add("Please specify your gender.");
                }
                else {
                    errorHandler.errorResult.remove("Please specify your gender.");
                }

                int isSelected = radioGender.getCheckedRadioButtonId();
                if (isSelected == -1){
                    genderError.setError(empty);
                    genderError.requestFocus();
                    errorHandler.errorResult.add("Gender is empty.");
                }
                else {
                    genderError.setError(null);
                    errorHandler.errorResult.remove("Gender is empty.");
                }

                //This is to check the field of birthday if empty for error.
                if(!inputBir.isEmpty()){
                    infoHolder.birthday = inputBir;
                    birthday_xml.setError(null);
                    errorHandler.errorResult.remove("Birthday is empty.");

                } else {
                    birthday_xml.setError(empty);
                    birthday_xml.requestFocus();
                    errorHandler.errorResult.add("Birthday is empty.");
                }

                //This is to check the field of hobby if empty for error.
                if (!cbMusic.isChecked() && !cbMovies.isChecked() && !cbSwim.isChecked() && !cbFish.isChecked() && !cbDance.isChecked() &&
                    !cbRead.isChecked() && !cbSew.isChecked() && !cbCook.isChecked() && !cbPaint.isChecked() && !cbWrite.isChecked()){
                    hobbiesError.setError(empty);
                    hobbiesError.requestFocus();
                    errorHandler.errorResult.add("Hobbies is empty.");
                }
                else {
                    hobbiesError.setError(null);
                    errorHandler.errorResult.remove("Hobbies is empty.");
                }

                //This is the part to check the field of security question and answer if empty for error.
                if (security1_xml.getSelectedItem()==security2_xml.getSelectedItem()) {
                    securityError.setError(duplicate);
                }
                else {
                    securityError.setError(null);
                }

                if (security1_xml.getSelectedItem()==security3_xml.getSelectedItem()) {
                    securityError.setError(duplicate);
                }
                else {
                    securityError.setError(null);
                }

                if (security2_xml.getSelectedItem()==security3_xml.getSelectedItem()) {
                    securityError.setError(duplicate);
                }
                else {
                    securityError.setError(null);
                }

                if (security1_xml.getSelectedItem().equals("Choose one in the question:")){
                    errorHandler.errorResult.add("Security Question 1 is empty.");
                }
                else {
                    errorHandler.errorResult.remove("Security Question 1 is empty.");
                    if (security1_xml.getSelectedItem().toString() == security2_xml.getSelectedItem().toString()){
                        errorHandler.errorResult.add("Duplication in Security Question.");
                        securityError.setError(duplicate);
                    }
                    else if (security1_xml.getSelectedItem().toString() == security3_xml.getSelectedItem().toString()){
                        errorHandler.errorResult.add("Duplication in Security Question.");
                        securityError.setError(duplicate);
                    }
                }

                if (answer1.isEmpty()){
                    answer1_xml.setError(empty);
                    answer1_xml.requestFocus();
                    errorHandler.errorResult.add("Answer 1 is empty.");
                }
                else {
                    answer1_xml.setError(null);
                    errorHandler.errorResult.remove("Answer 1 is empty.");
                    infoHolder.answer1 = answer1;
                }

                if (security2_xml.getSelectedItem().equals("Choose one in the question:")){
                    errorHandler.errorResult.add("Security Question 2 is empty.");
                }
                else {
                    errorHandler.errorResult.remove("Security Question 2 is empty.");
                    if (security2_xml.getSelectedItem().toString() == security3_xml.getSelectedItem().toString()){
                        errorHandler.errorResult.add("Duplication in Security Question.");
                        securityError.setError(duplicate);
                    }
                }

                if (answer2.isEmpty()){
                    answer2_xml.setError(empty);
                    answer2_xml.requestFocus();
                    errorHandler.errorResult.add("Answer 2 is empty.");
                }
                else {
                    answer2_xml.setError(null);
                    errorHandler.errorResult.remove("Answer 2 is empty.");
                    infoHolder.answer2 = answer2;
                }

                if (security3_xml.getSelectedItem().equals("Choose one in the question:")){
                    errorHandler.errorResult.add("Security Question 3 is empty.");
                }
                else {
                    errorHandler.errorResult.remove("Security Question 3 is empty.");
                }

                if (answer3.isEmpty()){
                    answer3_xml.setError(empty);
                    answer3_xml.requestFocus();
                    errorHandler.errorResult.add("Answer 3 is empty.");
                }
                else {
                    answer3_xml.setError(null);
                    errorHandler.errorResult.remove("Answer 3 is empty.");
                    infoHolder.answer3 = answer3;
                }

                // This is the condition before to print output if there is middle name.
                // MAIN CONDITION
                if (!inputUN.isEmpty() && !inputP.isEmpty() && !inputCP.isEmpty() && inputP.equals(inputCP) && !inputLm.isEmpty()
                    && !inputFm.isEmpty() && email_xml.getText().toString().trim().matches(emailPattern)
                    && !inputBir.isEmpty() && isSelected>-1 && !inputHn.isEmpty() && !inputStr.isEmpty() && !inputBar.isEmpty()
                    && !inputMun.isEmpty() && !inputProv.isEmpty() && !inputCon.isEmpty() && infoHolder.hobbiesResult.size()>-1
                    && !answer1.isEmpty() && !answer2.isEmpty() && !answer3.isEmpty() && ((security1_xml.getSelectedItem()!=security3_xml.getSelectedItem())
                    && (security2_xml.getSelectedItem()!=security3_xml.getSelectedItem())&& (security1_xml.getSelectedItem()!=security2_xml.getSelectedItem()))) {
                        //infoSummaryWithMN();
                        //Toast.makeText(context,"Registration is Successful!",Toast.LENGTH_LONG).show();
                        saveToGallery();

                    if(myDB.insertUser(imagePath,inputUN, inputP,inputLm, inputFm, inputMm, inputBir, infoHolder.getGender(), inputHn, inputStr,
                            inputBar, inputMun, inputProv, inputCon, infoHolder.getHobbies(), security1_xml.getSelectedItem().toString(), answer1,
                            security2_xml.getSelectedItem().toString(), answer2, security3_xml.getSelectedItem().toString(), answer3)){
                        Toast.makeText(context, "New User Added.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Insert failed.", Toast.LENGTH_SHORT).show();
                    }

                    Intent i = new Intent(context, Activity1_LogIn.class);
                    startActivity(i);

                }
                /*
                // This is the condition before to print output without the middle name.
                else if (!inputUN.isEmpty() && !inputP.isEmpty() && !inputCP.isEmpty() && !inputLm.isEmpty()
                        && !inputFm.isEmpty() && email_xml.getText().toString().trim().matches(emailPattern)
                        && !inputBir.isEmpty() && isSelected>-1 && !inputHn.isEmpty() && !inputStr.isEmpty() && !inputBar.isEmpty()
                        && !inputMun.isEmpty() && !inputProv.isEmpty() && !inputCon.isEmpty() && infoHolder.hobbiesResult.size()>-1
                        && !answer1.isEmpty() && !answer2.isEmpty() && !answer3.isEmpty() && ((security1_xml.getSelectedItem()!=security3_xml.getSelectedItem())
                        && (security2_xml.getSelectedItem()!=security3_xml.getSelectedItem())&& (security1_xml.getSelectedItem()!=security2_xml.getSelectedItem()))) {
                            infoSummaryWithoutMN();
                            saveToGallery();
                            Toast.makeText(context,"Registration is Successful!",Toast.LENGTH_LONG).show();
                }

                 */
                //This contains the error.
                else {
                    errorResult();
                }
            }
        });

        //This is the date picker dialog for birthday.
        birthday_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthday_xml.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                birthday_xml.setError(null);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //This is for the gender.
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radioOther){
                    others_xml.setVisibility(View.VISIBLE);
                }
                else {
                    int selectedId = checkedId;
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    infoHolder.gender = (String) radioButton.getText();
                    others_xml.setVisibility(View.INVISIBLE);
                    genderError.setError(null);
                }
            }
        });

        login_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Activity1_LogIn.class);
                startActivity(i);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Activity1_LogIn.class);
                startActivity(i);
            }
        });

        //The following below is the spinner for security question.
        //The content of spinner comes from array of strings declared in strings.xml
        //Populate through xml in the layout.
        security1_xml.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem.equals("Choose one in the question:")){

                }
                else {
                    if(selectedItem.equals(security2_xml.getSelectedItem()))
                    {
                        alert();
                    }
                    else if (selectedItem.equals(security3_xml.getSelectedItem()))
                    {
                        alert();
                    }
                    else {
                        securityError.setError(null);
                        infoHolder.securityQuestion1 = security1_xml.getSelectedItem().toString();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        security2_xml.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem.equals("Choose one in the question:")){

                }
                else {
                    if(selectedItem.equals(security1_xml.getSelectedItem()))
                    {
                        alert();
                    }
                    else if (selectedItem.equals(security3_xml.getSelectedItem()))
                    {
                        alert();
                    }
                    else {
                        securityError.setError(null);
                        infoHolder.securityQuestion2 = security2_xml.getSelectedItem().toString();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        security3_xml.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem.equals("Choose one in the question:")){

                }
                else {
                    if(selectedItem.equals(security1_xml.getSelectedItem()))
                    {
                        alert();
                    }
                    else if (selectedItem.equals(security2_xml.getSelectedItem()))
                    {
                        alert();
                    }
                    else {
                        securityError.setError(null);
                        infoHolder.securityQuestion3 = security3_xml.getSelectedItem().toString();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Autocomplete for barangay, municipality and province using ArrayAdapter to populate.
        //The content of barangay comes from array of strings declared in strings.xml
        String[] barangay = getResources().getStringArray(R.array.barangay_array);
        ArrayAdapter<String> adapterB = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, barangay);
        barangay_xml.setAdapter(adapterB);

        String[] municipality = getResources().getStringArray(R.array.municipality_array);
        ArrayAdapter<String> adapterM = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, municipality);
        municipality_xml.setAdapter(adapterM);

        String[] province = getResources().getStringArray(R.array.province_array);
        ArrayAdapter<String> adapterP = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, province);
        province_xml.setAdapter(adapterP);
    }

    //Alert Dialog that contains information with Middle Name.
    private void infoSummaryWithMN() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("INFORMATION")
                .setMessage(
                                infoHolder.getPersonPicture() + infoHolder.getUsername() + "\n" + infoHolder.getConfirmPassword() + "\n" +
                                infoHolder.getFullNamewithMN() + "\n" + infoHolder.getEmail() + "\n" + infoHolder.getBirthday() +
                                "\n" +infoHolder.getGender() + "\n" + infoHolder.getFullAddress() + "\n" + infoHolder.getContactNumber() +
                                "\n" + "Hobbies\n" + infoHolder.getHobbies() + "\n" + "Security Question\n" + infoHolder.getSecurityQuestionAnswer1()
                                + infoHolder.getSecurityQuestionAnswer2() + infoHolder.getSecurityQuestionAnswer3()
                )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context, Activity1_LogIn.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    //Alert Dialog that contains information without Middle Name.
    private void infoSummaryWithoutMN() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("INFORMATION")
                .setMessage(
                                infoHolder.getPersonPicture() + infoHolder.getUsername() + "\n" + infoHolder.getConfirmPassword() + "\n" +
                                infoHolder.getFullNamewithoutMN() + "\n" + infoHolder.getEmail() + "\n" + infoHolder.getBirthday() +
                                "\n" +infoHolder.getGender() + "\n" + infoHolder.getFullAddress() + "\n" + infoHolder.getContactNumber() +
                                "\n" + "Hobbies\n" + infoHolder.getHobbies() + "\n" + "Security Question\n" + infoHolder.getSecurityQuestionAnswer1()
                                + infoHolder.getSecurityQuestionAnswer2() + infoHolder.getSecurityQuestionAnswer3()
                )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context, Activity1_LogIn.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    //Alert Dialog that contains information with error.
    private void errorResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ERROR")
                .setMessage(errorHandler.getErrorResult())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        errorHandler.errorResult.clear();
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    //Alert Dialog that displays when the selected is a duplicate.
    private void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("SECURITY QUESTION")
                .setMessage("Please select another question. Duplication is not allowed.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    //Alert Dialog that displays when log in button is clicked.
    private void alertLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("LOG IN")
                .setMessage("Redirecting to Log In...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

}