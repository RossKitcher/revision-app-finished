package com.example.revision_app_two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddSubject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
    }

    public void onClickAddSubject(View view) {
        ContentValues values = new ContentValues();
        values.put(NotesProvider.COL_TOPIC_NAME, ((EditText)findViewById(R.id.addNote)).getText().toString());

        Uri uri = getContentResolver().insert(NotesProvider.TOPIC_CONTENT_URI, values);

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }
    public void onClickRetrieveSubject(View view) {
        String URL = "content://com.example.revision-app-test.StudentsProvider/subjects";
        Uri notes = Uri.parse(URL);
        Cursor c = getContentResolver().query(notes, null, null, null, "");
        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        "Added: " + c.getString(c.getColumnIndex(NotesProvider.COL_SUBJECT_NAME)),
                        Toast.LENGTH_SHORT
                ).show();
            } while (c.moveToNext());
        }
    }
}
