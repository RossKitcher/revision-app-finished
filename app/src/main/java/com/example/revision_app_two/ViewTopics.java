package com.example.revision_app_two;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewTopics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_topics);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getSharedPreferences("Pref",0);
        setTitle(pref.getString("currentMod", "") + " Topics");



    }
    @Override
    public void onResume() {
        super.onResume();

        List<String> topicList = new ArrayList<String>();

        String URL = "content://com.example.revision-app-two.NotesProvider/topics";
        Uri notes = Uri.parse(URL);

        String modID = MainActivity.getCurrentModuleId(this);

        Cursor c = getContentResolver().query(notes, null, "modID=?", new String[]{modID}, "");

        if (c.moveToFirst()) {
            do {
                topicList.add(c.getString(c.getColumnIndex("topName")));
            } while (c.moveToNext());
        }

        ListView listView = (ListView) findViewById(R.id.topicListView);

        StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, topicList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                SharedPreferences pref = getSharedPreferences("Pref",0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("currentTopic", item);
                editor.commit();

                Intent intent = new Intent(getBaseContext(), ViewNotes.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_bar_delete, menu);
        getMenuInflater().inflate(R.menu.app_bar_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddTopic.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
