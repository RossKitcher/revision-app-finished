package com.example.revision_app_two;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class RemoveTopic extends AppCompatActivity {
    Spinner topicSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_topic);



        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        setTitle("Remove Topic");
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<String> topics = MainActivity.getTopics(this);

        topicSpinner = (Spinner) findViewById(R.id.topicSpinner);
        ArrayAdapter<String> topicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, topics);

        topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        topicSpinner.setAdapter(topicAdapter);
    }

    public void onRemove(View view) {
        if (topicSpinner.getSelectedItem() != null) {
            String selected = topicSpinner.getSelectedItem().toString();
            String id = MainActivity.getSelectedTopID(this, selected);
            MainActivity.removeTopicFromTopID(this, id);

            Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();

            finish();
            startActivity(getIntent());

        } else {
            Toast.makeText(this, "Nothing Selected", Toast.LENGTH_SHORT).show();

        }
    }
}
