package org.medcada.android.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.medcada.android.R;
import org.medcada.android.tools.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallToPoliceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallToPoliceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.etContact)
    EditText etContact;

    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CallToPoliceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallToPoliceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallToPoliceFragment newInstance(String param1, String param2) {
        CallToPoliceFragment fragment = new CallToPoliceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
Preferences preferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_to_police, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = new Preferences(getActivity());
        etContact.setText(preferences.getPoliceContact());
        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    preferences.setPoliceContact(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
//
//    @OnClick(R.id.btn_call)
//    public void onViewClicked() {
////        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
////
////        if (permission != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(
////                    getActivity(),
////                    new String[]{Manifest.permission.READ_CONTACTS},
////                    REQUEST_CODE
////            );
////            return;
////        }
//        String contact = etContact.getText().toString();
//        if (!contact.isEmpty()){
//            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact));
//            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//
//
//                    ActivityCompat.requestPermissions(
//                            getActivity(),
//                            new String[]{Manifest.permission.CALL_PHONE},
//                            123
//                    );
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            startActivity(intent);
//        }else{
//            Toast.makeText(getActivity(), "Contact can not be empty!", Toast.LENGTH_SHORT).show();
//        }
//    }
}
