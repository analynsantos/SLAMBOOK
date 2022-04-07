package com.example.slambook;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ForRegister_InfoHolder {
    public Bitmap personPicture;
    public String username, confirmPassword, lastName, firstName, middleName;
    public String email, contactNumber, birthday, gender;
    public String street, houseNumber, barangay, municipality, province;
    public ArrayList<String> hobbiesResult = new ArrayList<String>();
    public String securityQuestion1, answer1;
    public String securityQuestion2, answer2;
    public String securityQuestion3, answer3;

    public Bitmap getPersonPicture() {
        return personPicture;
    }

    public String getUsername(){
        return "Username\n" + username + "\n";
    }

    public String getConfirmPassword() {
        return "Password\n" + confirmPassword + "\n";
    }

    public String getFullNamewithoutMN(){
        return "Name\n" + lastName + ", " + firstName + "\n";
    }

    public String getFullNamewithMN(){
        return "Name\n" + lastName + ", " + firstName + " " + middleName + "\n";
    }

    public String getEmail() {
        return "Email\n" + email + "\n";
    }

    public String getBirthday() {
        return "Birthday\n" + birthday + "\n";
    }

    public String getGender() {
        return gender;
    }

    public String getFullAddress() {
        return "Address\n" + houseNumber + " " + street + " " + barangay + ", " +municipality + ", " + province + "\n";
    }

    public String getContactNumber() {
        return "Contact Number\n" + contactNumber + "\n";
    }

    public String getHobbies() {
        String output = "";
        for (String s : hobbiesResult){
            output = output + s + ", ";
        }

        return output.substring(0, output.length()-2);
    }

    public String getSecurityQuestionAnswer1() {
        return securityQuestion1 + "\n" + answer1 + "\n";
    }

    public String getSecurityQuestionAnswer2() {
        return securityQuestion2 + "\n" + answer2 + "\n";
    }

    public String getSecurityQuestionAnswer3() {
        return securityQuestion3 + "\n" + answer3 + "\n";
    }

}

