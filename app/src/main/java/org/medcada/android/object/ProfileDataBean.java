package org.medcada.android.object;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by DeLL on 19-May-17.
 */

public class ProfileDataBean {

    private String firstName;
    private String lastName;
    private String StreetAddress1;
    private String StreetAddress2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String bloodType;

    public String getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(String myLocation) {
        this.myLocation = myLocation;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    private String myLocation;
    private String latlng;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    private String pinCode;
    private ArrayList<String> medContion;

    public byte[] getImageBytes() {
        return imageBytes;
    }

    private byte[] imageBytes;
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetAddress1() {
        return StreetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        StreetAddress1 = streetAddress1;
    }

    public String getStreetAddress2() {
        return StreetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        StreetAddress2 = streetAddress2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public ArrayList<String> getMedContion() {
        return medContion;
    }

    public void setMedContion(ArrayList<String> medContion) {
        this.medContion = medContion;
    }

    public boolean isPinEnabled() {
        return isPinEnabled;
    }

    public void setPinEnabled(boolean pinEnabled) {
        isPinEnabled = pinEnabled;
    }

    private boolean isPinEnabled;

    public String toJson(){
        return new Gson().toJson(this);
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}
