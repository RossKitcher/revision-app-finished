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

public class AddNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getBaseContext().getSharedPreferences("Pref", 0);
        String activeFragment = pref.getString("currentTopic", "");
        setTitle("Add Note To " + activeFragment);

    }

    public void onClickAddNote(View view) {
        ContentValues values = new ContentValues();
        values.put(NotesProvider.COL_NOTE, ((EditText)findViewById(R.id.addNote)).getText().toString());
        values.put(NotesProvider.COL_TOPIC_ID, MainActivity.getCurrentTopicId(this));

        Uri uri = getContentResolver().insert(NotesProvider.CONTENT_CONTENT_URI, values);

        Toast.makeText(getBaseContext(), "Added Note", Toast.LENGTH_LONG).show();
    }
    public void onClickRetrieveNotes(View view) {
        String URL = "content://com.example.revision-app-two.NotesProvider/content";
        Uri notes = Uri.parse(URL);
        Cursor c = getContentResolver().query(notes, null, null, null, "");
        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        "Added Note",
                        Toast.LENGTH_SHORT
                ).show();
            } while (c.moveToNext());
        }
    }



}
