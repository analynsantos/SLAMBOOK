package com.example.slambook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Activity3_EntryList extends AppCompatActivity {

    ImageView accountProfile;
    TextView displayName;
    ImageButton exitButton;
    RecyclerView container;
    Button addEntry;
    Context context = this;

    static String id;
    SQLiteDBHelper myDB;
    static ArrayList<ForIndividualEntry_Data> entryData = new ArrayList<>();
    static RecyclerviewAdapter recyclerviewAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrylist_layout);

        myDB = new SQLiteDBHelper(context);
        //selectByUser();
        init();
        regList();
    }

    public void selectByUser() {
        Cursor result = myDB.selectAllEntry();
            while (result.moveToNext()){
                if (result.getString(1).equals(id)) {
                    entryData.add(new ForIndividualEntry_Data(result.getString(0),
                            result.getString(2), result.getString(3), result.getString(4), result.getString(5),
                            result.getString(6), result.getString(7), result.getString(8), result.getString(9),
                            result.getString(10), result.getString(11), result.getString(12)));
                    recyclerviewAdapter.notifyDataSetChanged();
                }

                if (entryData.size() != -1) {
                    Toast.makeText(context, "New ArrayList has input.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "ArrayList is empty.", Toast.LENGTH_SHORT).show();
                }
            }
    }



    //TODO
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    private void init() {
        accountProfile = findViewById(R.id.accountProfile);
        displayName = findViewById(R.id.displayName);
        exitButton = findViewById(R.id.exitButton);
        container = findViewById(R.id.recycleviewContainer);
        addEntry = findViewById(R.id.addEntry);
    }

    private void regList() {

        recyclerviewAdapter = new RecyclerviewAdapter(context, R.layout.recyclerviewtab_layout, entryData);
        container.setAdapter(recyclerviewAdapter);
        layoutManager = new LinearLayoutManager(context);
        container.setLayoutManager(layoutManager);
        layoutManager.scrollToPosition(entryData.size());



        /*
        Drawable drawable = this.getResources().getDrawable(R.drawable.add_profile);
        Bitmap icon = convertToBitmap(drawable, 110, 110);

        entryData.add(new ForIndividualEntry_Data( icon, "Grant Gustin", "Brother",
        "October 24, 1954", "Male", "New Jersey", "09675466731", "Swimming", "Quiet"));

        entryData.add(new ForIndividualEntry_Data( icon, " Gwen Silvers", "Bestfriend",
                "January 1, 1899", "Female", "Atlanta", "09675466731", "Make-up", "Fashionable"));

        entryData.add(new ForIndividualEntry_Data( icon, "John Villafuerte", "Cousin",
                "May 4, 1986", "Male", "Toronto", "09492267859", "Basketball", "Athlete"));

        entryData.add(new ForIndividualEntry_Data( icon, "Carmen Felizidad", "Mother",
                "July 2, 1890", "Female", "Ohio", "09683526672", "Painting", "Scared of Heights"));

        entryData.add(new ForIndividualEntry_Data( icon, "Michael Castillo", "Classmate's Brother",
                "October 29, 1999", "Male", "Madagascar", "09984758406", "Dancing", "Competitive"));

        entryData.add(new ForIndividualEntry_Data( icon, "Adelline Payne", "Sister",
                "August 9, 2007", "Female", "New York", "09221584709", "Cycling", "Talkative"));

        entryData.add(new ForIndividualEntry_Data( icon, "Miguel Cruz", "Brother",
                "September 14, 1995", "Male", "Manila", "09228567099", "Reading ", "Vegetarian"));

        entryData.add(new ForIndividualEntry_Data( icon, "Crystal Braun", "Highschool Classmate",
                "March 6, 1894", "Female", "Manchester", "09982568905", "Taking Pictures", "Love to travel"));

        entryData.add(new ForIndividualEntry_Data( icon, "Toshi Hayaki", "Bestfriend",
                "June 4, 2000", "Male", "Tokyo", "09275467721", "Singing", "Love to party"));

        entryData.add(new ForIndividualEntry_Data( icon, "Charlie Damion", "Friend",
                "November 26, 2005", "Female", "California", "09498247502", "Listening to Music", "Allergic to Flowers"));
         */

        recyclerviewAdapter.setOnConvertViewClickListener(new RecyclerviewAdapter.OnConvertViewClickListener() {
            @Override
            public void ConvertViewOnClick(int position) {
                Intent i = new Intent(context, Activity4_IndividualEntry.class);
                //TODO
                i.putExtra("NoteID", position);
                i.putExtra("Picture", entryData.get(position).getPersonPicture());
                i.putExtra("LastName", entryData.get(position).getLastname());
                i.putExtra("FirstName", entryData.get(position).getFirstname());
                i.putExtra("MiddleName", entryData.get(position).getMiddlename());
                i.putExtra("Remark", entryData.get(position).getRemark());
                i.putExtra("Birthday", entryData.get(position).getBirthday());
                i.putExtra("Gender", entryData.get(position).getGender());
                i.putExtra("Address", entryData.get(position).getAddress());
                i.putExtra("Contact", entryData.get(position).getContactNumber());
                i.putExtra("Hobby", entryData.get(position).getHobbies());
                i.putExtra("Info", entryData.get(position).getOtherInfo());
                startActivity(i);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            id = bundle.getString("ID");

            String picture = bundle.getString("Picture");
            accountProfile.setImageURI(Uri.parse(picture));

            String name = bundle.getString("Name");
            displayName.setText(name);
        }

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("LOG OUT")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(context, Activity1_LogIn.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                entryData.clear();
                                startActivity(i);
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                builder.show();
            }
        });

        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Activity5_AddEntry.class);
                startActivity(i);


            }
        });
    }

    public void onStart() {
        super.onStart();
        Cursor result = myDB.selectAllEntry();
        while (result.moveToNext()){
            if (result.getString(1).equals(Activity3_EntryList.id)) {
                Activity3_EntryList.entryData.add(new ForIndividualEntry_Data(result.getString(0),
                        result.getString(2), result.getString(3), result.getString(4), result.getString(5),
                        result.getString(6), result.getString(7), result.getString(8), result.getString(9),
                        result.getString(10), result.getString(11), result.getString(12)));
                Activity3_EntryList.recyclerviewAdapter.notifyDataSetChanged();
            }
        }

    }
}