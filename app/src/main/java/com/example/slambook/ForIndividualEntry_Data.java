package com.example.slambook;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class ForIndividualEntry_Data {

    private String entryID;
    private String personPicture;
    private String lastname, firstname, middlename, remark, birthday, gender, address, contactNumber, hobbies, otherInfo;

    public String getEntryID() {
        return entryID;
    }

    public void setEntryID(String entryID) {
        this.entryID = entryID;
    }

    public String getPersonPicture() {
        return personPicture;
    }

    public void setPersonPicture(String personPicture) {
        this.personPicture = personPicture;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public ForIndividualEntry_Data(String entryID, String personPicture, String lastname, String firstname, String middlename, String remark, String birthday, String gender, String address, String contactNumber, String hobbies, String otherInfo) {
        this.entryID = entryID;
        this.personPicture = personPicture;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.remark = remark;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.contactNumber = contactNumber;
        this.hobbies = hobbies;
        this.otherInfo = otherInfo;
    }
}
