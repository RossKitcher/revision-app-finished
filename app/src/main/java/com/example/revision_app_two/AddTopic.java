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

public class AddTopic extends AppCompatActivity {
    SharedPreferences pref;
    String activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);

        pref = getBaseContext().getSharedPreferences("Pref", 0);
        activeFragment = pref.getString("currentMod", "");
        setTitle("Add Module To " + activeFragment);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void onClickAddTopic(View view) {



        ContentValues values = new ContentValues();
        values.put(NotesProvider.COL_MODULE_ID, MainActivity.getCurrentModuleId(this));
        values.put(NotesProvider.COL_TOPIC_NAME, ((EditText)findViewById(R.id.addNote)).getText().toString());

        Uri uri = getContentResolver().insert(NotesProvider.TOPIC_CONTENT_URI, values);

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }
    public void onClickRetrieveTopic(View view) {
        String URL = "content://com.example.revision-app-two.NotesProvider/topics";
        Uri notes = Uri.parse(URL);
        Cursor c = getContentResolver().query(notes, null, null, null, "");
        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(NotesProvider.COL_ID)) +
                                ", " + c.getString(c.getColumnIndex(NotesProvider.COL_TOPIC_NAME)) +
                                ", " + c.getString(c.getColumnIndex(NotesProvider.COL_MODULE_ID)),
                        Toast.LENGTH_SHORT
                ).show();
            } while (c.moveToNext());
        }
    }
}
