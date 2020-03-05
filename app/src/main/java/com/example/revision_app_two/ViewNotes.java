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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewNotes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getSharedPreferences("Pref",0);
        setTitle(pref.getString("currentTopic", "") + " Notes");
    }

    @Override
    public void onResume() {
        super.onResume();

        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.list_layout);

        Map<String,String> colNames = MainActivity.getModuleColumnNames();

        String URL = "content://com.example.revision-app-two.NotesProvider/content";
        Uri notes = Uri.parse(URL);
        String topID = MainActivity.getCurrentTopicId(this);
        int idCounter = 0;
        Cursor c = getBaseContext().getContentResolver().query(notes, null, "topID=?", new String[]{topID}, "");
        if (c != null ? c.moveToFirst() : false) {
            do {
                String content = c.getString(c.getColumnIndex("note"));
                idCounter += 1;
                TextView tv = new TextView(this);
                tv.setText(content);
                tv.setId(idCounter);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(16);
                tv.setPadding(0,10,0,10);
                mLinearLayout.addView(tv);


            } while (c.moveToNext());
        }

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
                Intent intent = new Intent(this, AddNote.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
