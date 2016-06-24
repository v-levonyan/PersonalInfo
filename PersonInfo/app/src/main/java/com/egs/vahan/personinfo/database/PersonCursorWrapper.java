package com.egs.vahan.personinfo.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.egs.vahan.personinfo.Person;
import com.egs.vahan.personinfo.database.PersonDbSchema.PersonTable;

import java.util.UUID;

/**
 * Created by vahanl on 6/24/16.
 */
public class PersonCursorWrapper extends CursorWrapper {
    public PersonCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Person getPerson() {
        String uuidString = getString(getColumnIndex(PersonTable.Cols.UUID));
        String name = getString(getColumnIndex(PersonTable.Cols.NAME));
        String phone = getString(getColumnIndex(PersonTable.Cols.PHONE));
        String address = getString(getColumnIndex(PersonTable.Cols.ADDRESS));
        String email = getString(getColumnIndex(PersonTable.Cols.EMAIL));

        Person person = new Person(UUID.fromString(uuidString));
        person.setAdress(address);
        person.setAdress(name);
        person.setAdress(phone);
        person.setAdress(email);
        return null;
    }
}
