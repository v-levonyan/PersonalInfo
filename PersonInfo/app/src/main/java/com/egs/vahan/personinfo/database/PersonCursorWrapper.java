package com.egs.vahan.personinfo.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.egs.vahan.personinfo.Person;
import com.egs.vahan.personinfo.database.PersonDbSchema.PersonTable;


/**
 * Created by vahanl on 6/24/16.
 */
public class PersonCursorWrapper extends CursorWrapper {
    public PersonCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Person getPerson() {
        String idString = getString(getColumnIndex(PersonTable.Cols.ID));
        String name = getString(getColumnIndex(PersonTable.Cols.NAME));
        String phone = getString(getColumnIndex(PersonTable.Cols.PHONE));
        String address = getString(getColumnIndex(PersonTable.Cols.ADDRESS));
        String email = getString(getColumnIndex(PersonTable.Cols.EMAIL));
        String username = getString(getColumnIndex(PersonTable.Cols.USERNAME));

        Person person = new Person();
        person.setId(Integer.parseInt(idString));
        String normalAddress = address.replaceAll("[{}\"]", " ");
        person.setAdress(normalAddress);
        Log.d("Adress", normalAddress);
        person.setLastName(username);
        person.setFirstname(name);
        person.setPhone(phone);
        person.setEmail(email);
        return person;
    }

}
