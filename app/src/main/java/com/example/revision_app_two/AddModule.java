package com.example.revision_app_two;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddModule extends AppCompatActivity {
    SharedPreferences pref;
    String activeFragment;
    String editedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

        pref = getBaseContext().getSharedPreferences("Pref", 0);
        activeFragment = pref.getString("currentSub", "");
        String firstChar = String.valueOf(activeFragment.charAt(0));
        editedFragment = firstChar.toUpperCase() + activeFragment.substring(1);
        setTitle("Add Module To " + editedFragment);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void onClickAddModule(View view) {

        String URL = "content://com.example.revision-app-two.NotesProvider/subjects";
        Uri subUri = Uri.parse(URL);
        Cursor subCursor = getContentResolver().query(subUri, new String[] {"_id"}, "subName=?", new String[] {editedFragment},"");

        subCursor.moveToFirst();

        String subID = subCursor.getString(subCursor.getColumnIndex(NotesProvider.COL_ID));


        ContentValues values = new ContentValues();
        values.put(NotesProvider.COL_MODULE_NAME, ((EditText)findViewById(R.id.addModule)).getText().toString());
        values.put(NotesProvider.COL_SUBJECT_ID, Integer.parseInt(subID));

        Uri uri = getContentResolver().insert(NotesProvider.MODULE_CONTENT_URI, values);

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }
    public void onClickRetrieveModules(View view) {
        String URL = "content://com.example.revision-app-two.NotesProvider/modules";
        Uri notes = Uri.parse(URL);
        Cursor c = getContentResolver().query(notes, null, null, null, "");
        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(NotesProvider.COL_ID)) +
                                ", " + c.getString(c.getColumnIndex(NotesProvider.COL_MODULE_NAME)) +
                                ", " + c.getString(c.getColumnIndex(NotesProvider.COL_SUBJECT_ID)),
                        Toast.LENGTH_SHORT
                ).show();
            } while (c.moveToNext());
        }
    }
}
