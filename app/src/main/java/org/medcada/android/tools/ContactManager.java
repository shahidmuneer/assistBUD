package org.medcada.android.tools;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;


import org.medcada.android.object.Contacts;

import java.util.ArrayList;

public class ContactManager {
    ArrayList<Contacts> alContacts;
    Context context;

    public ContactManager(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public ArrayList<Contacts> getContacts() {
        this.alContacts = new ArrayList();
        ContentResolver cr = this.context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("has_phone_number"))) > 0) {
                    Cursor pCur = cr.query(Phone.CONTENT_URI, null, "contact_id = ?", new String[]{id}, null);
                    if (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex("data1"));
                        String contactName = pCur.getString(pCur.getColumnIndex("display_name"));
                        Log.i("====", "getContacts: Name: " + contactName + " NUM: " + contactNumber);
                        this.alContacts.add(new Contacts(contactName, contactNumber));
                    }
                    pCur.close();
                }
            } while (cursor.moveToNext());
        }
        return  this.alContacts;
    }

//    @TargetApi(Build.VERSION_CODES.ECLAIR)
//    public String DisplayName(String number) {
//        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
//        String name = "?";
//        Cursor contactLookup = this.context.getContentResolver().query(uri, new String[]{"_id", "display_name"}, null, null, null);
//        if (contactLookup != null) {
//            try {
//                if (contactLookup.getCount() > 0) {
//                    contactLookup.moveToNext();
//                    name = contactLookup.getString(contactLookup.getColumnIndex("display_name"));
//                }
//            } catch (Throwable th) {
//                if (contactLookup != null) {
//                    contactLookup.close();
//                }
//            }
//        }
//        if (contactLookup != null) {
//            contactLookup.close();
//        }
//        return name;
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public String getContactName(String contact) {
//        ContentResolver cr = this.context.getContentResolver();
//        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "display_name='" + contact + "'", null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                String id = cursor.getString(cursor.getColumnIndex("_id"));
//                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("has_phone_number"))) > 0) {
//                    Cursor pCur = cr.query(Phone.CONTENT_URI, null, "contact_id = ?", new String[]{id}, null);
//                    while (pCur.moveToNext()) {
//                        String cont;
//                        String contactNumber = pCur.getString(pCur.getColumnIndex("data1"));
//                        if (contactNumber.startsWith("+92")) {
//                            cont = contactNumber.replace("+92", "0");
//                        } else {
//                            cont = contactNumber;
//                        }
//                        String contactName = pCur.getString(pCur.getColumnIndex("display_name"));
//                        if (cont.trim().equalsIgnoreCase(contact.trim())) {
//                            return contactName;
//                        }
//                    }
//                    pCur.close();
//                }
//            } while (cursor.moveToNext());
//        }
//        return contact;
//    }
}
