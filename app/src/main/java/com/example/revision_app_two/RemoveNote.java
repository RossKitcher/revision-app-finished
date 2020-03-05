package com.example.revision_app_two;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class RemoveNote extends AppCompatActivity {
    Spinner noteSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_note);



        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        setTitle("Remove Note");
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<String> notes = MainActivity.getNotes(this);

        noteSpinner = (Spinner) findViewById(R.id.noteSpinner);
        ArrayAdapter<String> noteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, notes);

        noteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        noteSpinner.setAdapter(noteAdapter);
    }

    public void onRemove(View view) {
        if (noteSpinner.getSelectedItem() != null) {
            String selected = noteSpinner.getSelectedItem().toString();
            MainActivity.removeSelectedNote(this, selected);
            Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();

            finish();
            startActivity(getIntent());
        } else {
            Toast.makeText(this, "Nothing Selected", Toast.LENGTH_SHORT).show();

        }
    }
}
