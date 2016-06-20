package com.egs.vahan.personinfo;

import android.support.v4.app.Fragment;

/**
 * Created by vahan on 6/19/16.
 */
public class PersonListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PersonListFragment();
    }
}
