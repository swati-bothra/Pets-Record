package com.example.android.pets;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.Data.PetContract;
import com.example.android.pets.Data.PetContract.PetEntry;//import till PetEntry
import com.example.android.pets.Data.PetDbHelper;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;
    private final int EXISTING_PET_LOADER = 0;

    //private PetDbHelper mDbHelper;
    private Uri mCurrentPetUri;

    private boolean mPetHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();

        if (mCurrentPetUri==null){
            setTitle(getString(R.string.editor_activity_title_new_pet));
            invalidateOptionsMenu();
        }
        else {
            setTitle(getString(R.string.editor_activity_title_edit_pet));
            getLoaderManager().initLoader(EXISTING_PET_LOADER,null,this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);



        mGenderSpinner.setOnTouchListener(mTouchListener);
        mWeightEditText.setOnTouchListener(mTouchListener);
        mBreedEditText.setOnTouchListener(mTouchListener);
        mNameEditText.setOnTouchListener(mTouchListener);

        setupSpinner();


    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = PetEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }
            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetEntry.GENDER_UNKNOWN; // Unknown
            }
        });
    }

    private void savePet(){

        //Log.v("EditorActivity","inserting pet");
        String nameString =  mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();

        //int s = Integer.parseInt(weightString);


       // mDbHelper = new PetDbHelper(this);
        //SQLiteDatabase db= mDbHelper.getWritableDatabase();

        if (mCurrentPetUri== null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(breedString) &&
                TextUtils.isEmpty(weightString) && mGender == PetEntry.GENDER_UNKNOWN) {return;}

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME,nameString);
        values.put(PetEntry.COLUMN_PET_BREED,breedString);
        values.put(PetEntry.COLUMN_PET_GENDER,mGender);
        int weight = 0;
        if (!TextUtils.isEmpty(weightString)){
            weight = Integer.parseInt(weightString);
        }
        values.put(PetEntry.COLUMN_PET_WEIGHT,weight);


        if (mCurrentPetUri==null){
            //long id = db.insert(PetEntry.TABLE_NAME,null,values);
            Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI,values);

            if (newUri == null){
                Toast.makeText(this,getString(R.string.editor_insert_pet_failed), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,getString(R.string.editor_insert_pet_successful) , Toast.LENGTH_SHORT).show();
            }
        }
        else {
            int rowsAffected = getContentResolver().update(mCurrentPetUri,values,null,null);

            if (rowsAffected==0){
                Toast.makeText(this,"Update Failed",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Update Successful",Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //Log.v("EditorActivity","click");
                savePet();
                //Log.v("EditorActivity","returned");
                finish();//return to parent activity
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!mPetHasChanged){
                    // Navigate back to parent activity (CatalogActivity)
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentPetUri==null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mPetHasChanged){
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardButtonClickListner);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {PetEntry._ID,PetEntry.COLUMN_PET_NAME,PetEntry.COLUMN_PET_BREED,PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT};
        return new CursorLoader(this,mCurrentPetUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount()<1){
            return;
        }

        if (cursor.moveToFirst()){
            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            String name = cursor.getString(nameColumnIndex);
            String breed = cursor.getString(breedColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int weight = cursor.getInt(weightColumnIndex);

            mNameEditText.setText(name);
            mBreedEditText.setText(breed);
            mWeightEditText.setText(Integer.toString(weight));

            switch (gender){
                case PetEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case PetEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.setText("");
        mBreedEditText.setText("");
        mWeightEditText.setText("");
        mGenderSpinner.setSelection(0);

    }
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListner){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your change and quit editing ?");
        builder.setPositiveButton("Discard",discardButtonClickListner);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface!=null){
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Pet ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePet();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface!=null){
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deletePet(){

        if (mCurrentPetUri!=null){
            int rowsDeleted = getContentResolver().delete(mCurrentPetUri,null,null);
        if (rowsDeleted==0) {
            Toast.makeText(this, "Deletion of Pet Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();
        }
             finish();
        }
    }
}