package org.medcada.android.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onegravity.contactpicker.contact.Contact;

import org.medcada.android.object.Contacts;
import org.medcada.android.object.ProfileDataBean;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Preferences {
    static final String LOCALE_CLEANED = "PREF_LOCALE_CLEANED";

    private final SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isLocaleCleaned() {
        return sharedPreferences.getBoolean(LOCALE_CLEANED, false);
    }

    public void saveLocaleCleaned() {
        sharedPreferences.edit().putBoolean(LOCALE_CLEANED, true).apply();
    }

    public void setProfileData(String json) {
        sharedPreferences.edit().putString("profiledata", json).apply();
    }

    public ProfileDataBean getProfiledata() {
        ProfileDataBean bean = new Gson().fromJson(sharedPreferences.getString("profiledata", ""), ProfileDataBean.class);
        if (bean != null) {
            return bean;
        } else return new ProfileDataBean();
    }

    public void setEmergencyContacts(ArrayList<Contacts> list) {
        Log.i("=====", "setEmergencyContacts: "+ new Gson().toJson(list));
        sharedPreferences.edit().putString("EmergencyContacts", new Gson().toJson(list)).apply();
    }

    public ArrayList<Contacts> getEmergencyContacts() {

        String json = sharedPreferences.getString("EmergencyContacts","");
        Log.i("=====", "getEmergencyContacts: "+ json);
        Type listType = new TypeToken<ArrayList<Contacts>>() {
        }.getType();
        if (json.isEmpty()) {
            return new ArrayList<>();
        } else {
            try {
                return new Gson().fromJson(json, listType);
            }catch (Exception e){
                e.printStackTrace();
                return new ArrayList<>();
            }

        }



    }
    public void setPoliceContact(String policeContact){
        sharedPreferences.edit().putString("policeContact", policeContact).apply();
    }
    public String getPoliceContact(){
        return sharedPreferences.getString("policeContact", "");
    }
    public void setEmergencyMessage(String emergencyMessage){
        sharedPreferences.edit().putString("emergencyMessage", emergencyMessage).apply();
    }
    public String getEmergencyMessage(){
        return sharedPreferences.getString("emergencyMessage", "I am  in Emergency please approach me as soon as possible");
    }

    public void setShouldIncludeLocation(boolean shouldIncludeLocation){
        sharedPreferences.edit().putBoolean("shouldIncludeLocation", shouldIncludeLocation).apply();
    }
    public boolean getShouldIncludeLocation(){
        return sharedPreferences.getBoolean("shouldIncludeLocation", true);
    }

    public void setIsPolicyAccepted(boolean isAccpted){
        sharedPreferences.edit().putBoolean("isPolicyAccepted", isAccpted).apply();
    }

    public boolean getIsPolicyAccepted(){
        return sharedPreferences.getBoolean("isPolicyAccepted", false);
    }
}
