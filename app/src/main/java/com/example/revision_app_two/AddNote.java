package com.example.revision_app_two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

    }

    public void onClickAddNote(View view) {
        ContentValues values = new ContentValues();
        values.put(NotesProvider.COL_NOTE, ((EditText)findViewById(R.id.addNote)).getText().toString());
        values.put(NotesProvider.COL_TOPIC_ID, MainActivity.getCurrentTopicId(this));

        Uri uri = getContentResolver().insert(NotesProvider.CONTENT_CONTENT_URI, values);

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }
    public void onClickRetrieveNotes(View view) {
        String URL = "content://com.example.revision-app-two.NotesProvider/content";
        Uri notes = Uri.parse(URL);
        Cursor c = getContentResolver().query(notes, null, null, null, "");
        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(NotesProvider.COL_ID)) +
                                ", " + c.getString(c.getColumnIndex(NotesProvider.COL_NOTE)) +
                                ", " + c.getString(c.getColumnIndex(NotesProvider.COL_TOPIC_ID)),
                        Toast.LENGTH_SHORT
                ).show();
            } while (c.moveToNext());
        }
    }



}
