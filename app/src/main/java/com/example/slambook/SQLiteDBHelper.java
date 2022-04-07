package com.example.slambook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "mydatabase.db";
    private static int VERSION = 1;
    Context context;

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_USER_TABLE = "CREATE TABLE '"+ DB_Contract.User.USER_TABLE +"' ("+" '"+ DB_Contract.User.ID +"' INTEGER PRIMARY KEY, "+
        " '"+ DB_Contract.User.PROFILE +"' TEXT, "+" '"+ DB_Contract.User.USERNAME +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.PASSWORD +"' TEXT NOT NULL, "+
        " '"+ DB_Contract.User.LAST_NAME +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.FIRST_NAME +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.MIDDLE_NAME +"' TEXT, "+
        " '"+ DB_Contract.User.BIRTHDAY +"' DATE NOT NULL, "+" '"+ DB_Contract.User.GENDER +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.HOUSE_NUMBER +"' INTEGER NOT NULL,"+
        " '"+ DB_Contract.User.STREET +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.BARANGAY +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.MUNICIPALITY +"' TEXT NOT NULL,"+
        " '"+ DB_Contract.User.PROVINCE +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.CONTACT +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.HOBBIES +"' TEXT NOT NULL, "+
        " '"+ DB_Contract.User.QUESTION_ONE +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.ANSWER_ONE +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.QUESTION_TWO +"' TEXT NOT NULL, "+
        " '"+ DB_Contract.User.ANSWER_TWO +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.QUESTION_THREE +"' TEXT NOT NULL, "+" '"+ DB_Contract.User.ANSWER_THREE +"' TEXT NOT NULL, "+
        " UNIQUE ('"+ DB_Contract.User.ID +"') ON CONFLICT ABORT); ";

        db.execSQL(CREATE_USER_TABLE);

        final String CREATE_ENTRY_TABLE = "CREATE TABLE '"+ DB_Contract.Entry.ENTRY_TABLE +"' ("+" '"+ DB_Contract.Entry.ID +"' INTEGER PRIMARY KEY, "+
        " `"+ DB_Contract.Entry.USER_ID +"` INTEGER NOT NULL, "+" '"+ DB_Contract.Entry.PROFILE +"' TEXT, "+" '"+ DB_Contract.Entry.LAST_NAME +"' TEXT NOT NULL, "+
        " '"+ DB_Contract.Entry.FIRST_NAME +"' TEXT NOT NULL, "+" '"+ DB_Contract.Entry.MIDDLE_NAME +"' TEXT NOT NULL, "+" '"+ DB_Contract.Entry.REMARK +"' TEXT NOT NULL, "+
        " '"+ DB_Contract.Entry.BIRTHDAY +"' DATE NOT NULL, "+" '"+ DB_Contract.Entry.GENDER +"' TEXT NOT NULL, "+" '"+ DB_Contract.Entry.ADDRESS +"' TEXT, "+" '"+ DB_Contract.Entry.CONTACT +"' TEXT, "+
        " '"+ DB_Contract.Entry.HOBBIES +"' TEXT NOT NULL, "+" '"+ DB_Contract.Entry.OTHER_INFO +"' TEXT, "+" FOREIGN KEY (`"+ DB_Contract.Entry.USER_ID +"`) REFERENCES "+" `"+ DB_Contract.User.USER_TABLE +"` (`"+ DB_Contract.User.ID +"`), "+
        " UNIQUE ('"+ DB_Contract.Entry.ID +"') ON CONFLICT ABORT); ";

        db.execSQL(CREATE_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertUser(String profile, String username, String password, String last_name, String first_name, String middle_name, String birthday,
                              String gender, String house_number, String street, String barangay, String municipality, String province, String contact, String hobbies,
                              String question_one, String answer_one, String question_two, String answer_two, String question_three, String answer_three) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_Contract.User.PROFILE,profile);
        values.put(DB_Contract.User.USERNAME,username);
        values.put(DB_Contract.User.PASSWORD,password);
        values.put(DB_Contract.User.LAST_NAME,last_name);
        values.put(DB_Contract.User.FIRST_NAME,first_name);
        values.put(DB_Contract.User.MIDDLE_NAME,middle_name);
        values.put(DB_Contract.User.BIRTHDAY,birthday);
        values.put(DB_Contract.User.GENDER,gender);
        values.put(DB_Contract.User.HOUSE_NUMBER,house_number);
        values.put(DB_Contract.User.STREET,street);
        values.put(DB_Contract.User.BARANGAY,barangay);
        values.put(DB_Contract.User.MUNICIPALITY,municipality);
        values.put(DB_Contract.User.PROVINCE,province);
        values.put(DB_Contract.User.CONTACT,contact);
        values.put(DB_Contract.User.HOBBIES,hobbies);
        values.put(DB_Contract.User.QUESTION_ONE,question_one);
        values.put(DB_Contract.User.ANSWER_ONE,answer_one);
        values.put(DB_Contract.User.QUESTION_TWO,question_two);
        values.put(DB_Contract.User.ANSWER_TWO,answer_two);
        values.put(DB_Contract.User.QUESTION_THREE,question_three);
        values.put(DB_Contract.User.ANSWER_THREE,answer_three);

        long result = db.insert(DB_Contract.User.USER_TABLE,null,values);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertEntry( String userID, String profile, String lastname, String firstname, String middlename,
                                String remark, String birthday, String gender, String address, String contact,
                                String hobbies, String otherInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_Contract.Entry.USER_ID,userID);
        values.put(DB_Contract.Entry.PROFILE,profile);
        values.put(DB_Contract.Entry.LAST_NAME,lastname);
        values.put(DB_Contract.Entry.FIRST_NAME,firstname);
        values.put(DB_Contract.Entry.MIDDLE_NAME,middlename);
        values.put(DB_Contract.Entry.REMARK,remark);
        values.put(DB_Contract.Entry.BIRTHDAY,birthday);
        values.put(DB_Contract.Entry.GENDER,gender);
        values.put(DB_Contract.Entry.ADDRESS,address);
        values.put(DB_Contract.Entry.CONTACT,contact);
        values.put(DB_Contract.Entry.HOBBIES,hobbies);
        values.put(DB_Contract.Entry.OTHER_INFO,otherInfo);

        long result = db.insert(DB_Contract.Entry.ENTRY_TABLE,null,values);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor selectAllUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.query(DB_Contract.User.USER_TABLE,null,null,null,
                null,null,null);
        return result;
    }

    public Cursor selectAllEntry() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.query(DB_Contract.Entry.ENTRY_TABLE,null,null,null,
                null,null,null);
        return result;
    }

    public boolean updateEntry( String entryID, String userID, String profile, String lastname, String firstname, String middlename,
                                String remark, String birthday, String gender, String address, String contact,
                                String hobbies, String otherInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_Contract.Entry.USER_ID,userID);
        values.put(DB_Contract.Entry.PROFILE,profile);
        values.put(DB_Contract.Entry.LAST_NAME,lastname);
        values.put(DB_Contract.Entry.FIRST_NAME,firstname);
        values.put(DB_Contract.Entry.MIDDLE_NAME,middlename);
        values.put(DB_Contract.Entry.REMARK,remark);
        values.put(DB_Contract.Entry.BIRTHDAY,birthday);
        values.put(DB_Contract.Entry.GENDER,gender);
        values.put(DB_Contract.Entry.ADDRESS,address);
        values.put(DB_Contract.Entry.CONTACT,contact);
        values.put(DB_Contract.Entry.HOBBIES,hobbies);
        values.put(DB_Contract.Entry.OTHER_INFO,otherInfo);

        String selection = DB_Contract.Entry.ID + " = ?";
        String[] selectionArgs = {entryID};

        int affected = db.update(DB_Contract.Entry.ENTRY_TABLE,values,selection,selectionArgs);
        if(affected>0){
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteEntry(String ID){
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = DB_Contract.Entry.ID + " = ?";
        String[] selectionArgs = {ID};

        int affected = db.delete(DB_Contract.Entry.ENTRY_TABLE,selection,selectionArgs);
        if(affected>0){
            return true;
        }else{
            return false;
        }
    }


}
