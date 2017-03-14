package com.example.android.pets.Data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.pets.Data.PetContract.PetEntry;//import this to use constants


public class PetDbHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME ="shelter.db";
    private final static int DATABASE_VERSION = 1;

    public PetDbHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);//null is cursur factory which set to null for default values
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PETS_TABLE = "CREATE TABLE "+PetEntry.TABLE_NAME+"("+PetEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                PetEntry.COLUMN_PET_NAME +" TEXT NOT NULL, "+
                PetEntry.COLUMN_PET_BREED +" TEXT, " +
                PetEntry.COLUMN_PET_GENDER +" INTEGER NOT NULL, " +
                PetEntry.COLUMN_PET_WEIGHT +" INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL();
//        onCreate(sqLiteDatabase);
    }
}
