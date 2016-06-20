package com.egs.vahan.personinfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

public class PersonFragment extends Fragment {

    private static final String ARG_PERSON_ID = "personId_id";

    private Person mPerson;
    private TextView mNameField;
    private TextView mLastNameField;
    private TextView mEmailField;
    private TextView mAgeField;
    private TextView mAddressField;
    private TextView mPhoneField;

    public static PersonFragment newInstance(UUID personId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON_ID, personId);

        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID personId = (UUID) getArguments().getSerializable(ARG_PERSON_ID);
        mPerson = People.get(getActivity()).getPerson(personId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container, false);

        mNameField = (TextView) v.findViewById(R.id.person_name);
        mNameField.setText(mPerson.getFirstname());

        mLastNameField = (TextView) v.findViewById(R.id.person_lastname);
        mLastNameField.setText(mPerson.getLastName());

        mEmailField = (TextView) v.findViewById(R.id.person_email);
        mEmailField.setText(mPerson.getEmail());

        mAddressField = (TextView) v.findViewById(R.id.person_address);
        mAddressField.setText(mPerson.getAdress());

        mAgeField = (TextView) v.findViewById(R.id.person_age);
       // mAgeField.setText(Integer.toString(mPerson.getAge()));

        mPhoneField = (TextView) v.findViewById(R.id.person_phone_number);
        mPhoneField.setText(mPerson.getPhone());

        return v;
    }
}
