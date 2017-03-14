package com.example.android.pets.Data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.content.ContentResolver;

//make it final class because it contains only constants
public final class PetContract {
    private PetContract(){};

    public final static String CONTENT_AUTHORITY ="com.example.android.pets";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public final static String PATH_PETS = "pets";


    public static final class PetEntry implements BaseColumns{

        //URI constant
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);

        public final static String TABLE_NAME = "pets";

        public final static String _ID =BaseColumns._ID;
        public final static String COLUMN_PET_NAME ="name";
        public final static String COLUMN_PET_BREED ="breed";
        public final static String COLUMN_PET_GENDER ="gender";
        public final static String COLUMN_PET_WEIGHT ="weight";

        public final static int GENDER_MALE = 1;
        public final static int GENDER_FEMALE = 2;
        public final static int GENDER_UNKNOWN = 0;

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"+ PATH_PETS;
        public static final String CONTENT_ITEM_TYPE= ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        public static boolean isValidGender(int gender){
            if (gender==GENDER_MALE || gender==GENDER_FEMALE || gender==GENDER_UNKNOWN){
                return true;
            }
            return false;
        }

    }
}
