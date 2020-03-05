package com.example.revision_app_two;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class RemoveModule extends AppCompatActivity {
    Spinner moduleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_module);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        setTitle("Remove Module");
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<String> modules = MainActivity.getModules(this);

        moduleSpinner = (Spinner) findViewById(R.id.moduleSpinner);
        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, modules);

        moduleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        moduleSpinner.setAdapter(moduleAdapter);
    }

    public void onRemove(View view) {
        if (moduleSpinner.getSelectedItem() != null) {
            String selected = moduleSpinner.getSelectedItem().toString();

            String id = MainActivity.getSelectedModID(this, selected);
            MainActivity.removeModuleFromModID(this, id);
            Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();

            finish();
            startActivity(getIntent());
        } else {
            Toast.makeText(this, "Nothing Selected", Toast.LENGTH_SHORT).show();

        }

    }
}
