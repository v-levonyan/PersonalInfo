package com.egs.vahan.personinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;


public class PersonActivity extends SingleFragmentActivity {

    private static final String EXTRA_PERSON_ID = "com.bignerdranch.android.personinfo.person_id";

    public static Intent newIntent(Context packageContext, int personId) {
        Intent intent = new Intent(packageContext, PersonActivity.class);
        intent.putExtra(EXTRA_PERSON_ID, personId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int personId = (int) getIntent().getSerializableExtra(EXTRA_PERSON_ID);
        return PersonFragment.newInstance(personId);
    }
}
