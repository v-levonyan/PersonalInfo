package com.egs.vahan.personinfo.database;

/**
 * Created by vahanl on 6/24/16.
 */
public class PersonDbSchema {
    public static final class PersonTable {
        public static final String NAME = "persons";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String USERNAME = "username";
            public static final String ADDRESS = "address";
            public static final String EMAIL = "email";
            public static final String PHONE = "phone";
        }
    }
}
