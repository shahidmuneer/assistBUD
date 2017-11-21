package org.medcada.android.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.picture.ContactPictureType;

import org.medcada.android.R;
import org.medcada.android.adapter.ContactsAdapter;
import org.medcada.android.adapter.EmergencyContactsAdapter;
import org.medcada.android.object.Contacts;
import org.medcada.android.tools.Preferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EmergencyCircleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;
    @BindView(R.id.lv_contacts)
    ListView lvContacts;
    @BindView(R.id.fab_add_contacts)
    FloatingActionButton fabAddContacts;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public EmergencyCircleFragment() {
        // Required empty public constructor
    }

    Preferences preferences;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmergencyCircleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmergencyCircleFragment newInstance(String param1, String param2) {
        EmergencyCircleFragment fragment = new EmergencyCircleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ArrayList<Contacts> toSaveContact;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency_circle, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = new Preferences(getActivity());
        toSaveContact = new ArrayList<>();

        toSaveContact = preferences.getEmergencyContacts();
        if (toSaveContact != null) {
            ContactsAdapter adapter = new ContactsAdapter(getActivity(), toSaveContact, true);
            lvContacts.setAdapter(adapter);
        } else {
            toSaveContact = new ArrayList<>();
        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static final int GET_PHONE_NUMBER = 3007;
    public static int REQUEST_CODE = 123;
    @OnClick(R.id.fab_add_contacts)
    public void onViewClicked() {

        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE
            );
            return;
        }

        Intent intent = new Intent(getActivity(), ContactPickerActivity.class)
                // .putExtra(ContactPickerActivity.EXTRA_THEME, mDarkTheme ? R.style.Theme_Dark : R.style.Theme_Light)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, false)
                .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT, 10)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
        startActivityForResult(intent, GET_PHONE_NUMBER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // See which child activity is calling us back.
        switch (requestCode) {
            case GET_PHONE_NUMBER:
                if (requestCode == GET_PHONE_NUMBER && resultCode == getActivity().RESULT_OK &&
                        data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {

                    // we got a result from the contact picker

                    // process contacts
                    ArrayList<Contact> contacts = (ArrayList<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);

                    for (Contact contact : contacts) {
                        Contacts cont = new Contacts(contact.getDisplayName(), contact.getPhone(0).toString());

                            if (!isContactContained(toSaveContact,cont)) {
                                toSaveContact.add(cont);
                            }


                    }
                    ContactsAdapter adapter = new ContactsAdapter(getActivity(), toSaveContact, true);
                    lvContacts.setAdapter(adapter);
                    preferences.setEmergencyContacts(toSaveContact);
//                    for (Contact contact : contacts) {
//
//                    }

                    // process groups

                }
            default:
                break;
        }
    }

    private boolean isContactContained(ArrayList<Contacts> list, Contacts bean) {
        for (Contacts cont : list) {
            if (cont.getName().equals(bean.getName()) ||  cont.getNumber().equals(bean.getNumber())) {
                return true;
            }
        }
        return false;
    }
}
