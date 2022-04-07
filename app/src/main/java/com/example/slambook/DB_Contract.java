package com.example.slambook;

import android.provider.BaseColumns;

public class DB_Contract {

    public static class User implements BaseColumns {
        //Table
        public static String USER_TABLE = "table_user";
        //Columns
        public static String ID = "user_id";
        public static String PROFILE = "user_profile";
        public static String USERNAME = "user_username";
        public static String PASSWORD = "user_password";
        public static String LAST_NAME = "user_last_name";
        public static String FIRST_NAME = "user_first_name";
        public static String MIDDLE_NAME = "user_middle_name";
        public static String BIRTHDAY = "user_birthday";
        public static String GENDER = "user_gender";
        public static String HOUSE_NUMBER = "user_house_number";
        public static String STREET = "user_street";
        public static String BARANGAY = "user_barangay";
        public static String MUNICIPALITY = "user_municipality";
        public static String PROVINCE = "user_province";
        public static String CONTACT = "user_contact";
        public static String HOBBIES = "user_hobbies";
        public static String QUESTION_ONE = "user_question_one";
        public static String ANSWER_ONE = "user_answer_one";
        public static String QUESTION_TWO = "user_question_two";
        public static String ANSWER_TWO = "user_answer_two";
        public static String QUESTION_THREE = "user_question_three";
        public static String ANSWER_THREE = "user_answer_three";
    }

    public static class Entry implements BaseColumns {
        //Table
        public static String ENTRY_TABLE = "table_entry";
        //Columns
        public static String ID = "entry_id";
        public static String USER_ID = "entry_user_id";
        public static String PROFILE = "entry_profile";
        public static String LAST_NAME = "entry_last_name";
        public static String FIRST_NAME = "entry_first_name";
        public static String MIDDLE_NAME = "entry_middle_name";
        public static String REMARK = "entry_remark";
        public static String BIRTHDAY = "entry_birthday";
        public static String GENDER = "entry_gender";
        public static String ADDRESS = "entry_address";
        public static String CONTACT = "entry_contact";
        public static String HOBBIES = "entry_hobbies";
        public static String OTHER_INFO = "entry_other_info";
    }


}
