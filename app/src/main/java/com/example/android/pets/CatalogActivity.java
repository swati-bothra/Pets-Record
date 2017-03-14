package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.Data.PetContract;
import com.example.android.pets.Data.PetDbHelper;
import com.example.android.pets.Data.PetContract.PetEntry;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private PetDbHelper mDbHelper;

    private final static int PET_LOADER = 0;
    PetCursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new PetDbHelper(this);
//        displayDatabaseInfo();


        ListView listView = (ListView)findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mCursorAdapter = new PetCursorAdapter(this,null);
        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
                Uri contentUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);
                intent.setData(contentUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PET_LOADER,null,this);
    }


//
//    private void displayDatabaseInfo() {
//        // To access our database, we instantiate our subclass of SQLiteOpenHelper
//        // and pass the context, which is the current activity.
//        //PetDbHelper mDbHelper = new PetDbHelper(this);
//
//        // Create and/or open a database to read from it
//       // SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Perform this raw SQL query "SELECT * FROM pets"
//        // to get a Cursor that contains all rows from the pets table.
////        Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME,null);
//
//        String[] projection = new String[]{
//                PetEntry._ID,
//                PetEntry.COLUMN_PET_NAME,
//                PetEntry.COLUMN_PET_BREED,
//                PetEntry.COLUMN_PET_GENDER,
//                PetEntry.COLUMN_PET_WEIGHT};
//        //String selection = PetEntry.COLUMN_PET_NAME + "=?";
//        //String[] selectionArgs = {PetEntry.COLUMN_PET_NAME};
//
//        //Cursor cursor = db.query(PetEntry.TABLE_NAME,projection,null,null,null,null,null);
//
//        Cursor cursor = getContentResolver().query(
//                PetEntry.CONTENT_URI,
//                projection,
//                null,
//                null,
//                null);
//
//
//        ListView listView = (ListView)findViewById(R.id.list);
//        View emptyView = findViewById(R.id.empty_view);
//        listView.setEmptyView(emptyView);
//
//        PetCursorAdapter petCursorAdapter = new PetCursorAdapter(this,cursor);
//        listView.setAdapter(petCursorAdapter);
//
//
//
////        try {
////            // Display the number of rows in the Cursor (which reflects the number of rows in the
////            // pets table in the database).
////            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
////            displayView.setText("Number of rows in pets database table: " + cursor.getCount()+"\n\n");
////            displayView.append(PetEntry._ID + " - " + PetEntry.COLUMN_PET_NAME + " - " + PetEntry.COLUMN_PET_BREED + " - " + PetEntry.COLUMN_PET_GENDER + " - " + PetEntry.COLUMN_PET_WEIGHT + "\n" );
////
///            while (cursor.moveToNext()){
////                int idIndex = cursor.getColumnIndex(PetEntry._ID);
////                int nameIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
////                int breedIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
////                int genderIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
////                int weightIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
////
////                String id = cursor.getString(idIndex);
////                String name = cursor.getString(nameIndex);
////                String breed = cursor.getString(breedIndex);
////                String gender = cursor.getString(genderIndex);
////                String weight = cursor.getString(weightIndex);
////
////                displayView.append(id + " - " + name + " - " + breed + " - " + gender + " - " + weight + "\n");
////            }
////        } finally {
////            // Always close the cursor when you're done reading from it. This releases all its
////            // resources and makes it invalid.
////            cursor.close();
////        }
//    }


//    @Override
//    protected void onStart() {//when control return from editor it restarts n display
//        super.onStart();
////        displayDatabaseInfo();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    private void insertPet(){
        //SQLiteDatabase db =  mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME,"Toto");
        values.put(PetEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER,PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT,7);

       // long newRowId = db.insert(PetEntry.TABLE_NAME,null,values);
        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI,values);
        Log.v("CatalogActivity","new row id");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
//                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPets(){
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI,null,null);
        Log.v("CatalogActivity",rowsDeleted+" rows deleted");
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {PetEntry._ID,PetEntry.COLUMN_PET_NAME,PetEntry.COLUMN_PET_BREED};

        return new CursorLoader(this,PetEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
