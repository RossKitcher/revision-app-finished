package com.example.revision_app_two;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.revision_app_two.ui.maths.MathsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    MathsFragment mathsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        setTitle("Modules");


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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_add:
                intent = new Intent(this, AddModule.class);
                startActivity(intent);
                break;
            case R.id.action_delete:
                intent = new Intent(this, RemoveModule.class);
                startActivity(intent);
                break;

            default:
        }
        return super.onOptionsItemSelected(item);


    }

    public static Map<String,String> getModuleColumnNames() {
        Map<String,String> names = new HashMap<String,String>();
        names.put("COL_MODULE_NAME",NotesProvider.COL_MODULE_NAME);
        names.put("COL_ID", NotesProvider.COL_ID);
        names.put("COL_SUBJECT_ID", NotesProvider.COL_SUBJECT_ID);
        return names;
    }

    public static String getCurrentTopicId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", 0);
        String activeFragment = pref.getString("currentTopic", "");
        String URL = "content://com.example.revision-app-two.NotesProvider/topics";
        Uri subUri = Uri.parse(URL);
        String modId = getCurrentModuleId(context);
        Cursor subCursor = context.getContentResolver().query(subUri, new String[] {"_id"}, "topName=? AND modID=?", new String[] {activeFragment, modId},"");
        subCursor.moveToFirst();
        String topID = subCursor.getString(subCursor.getColumnIndex(NotesProvider.COL_ID));
        return topID;
    }

    public static String getCurrentModuleId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", 0);
        String activeFragment = pref.getString("currentMod", "");
        String URL = "content://com.example.revision-app-two.NotesProvider/modules";
        Uri subUri = Uri.parse(URL);
        String subID = getCurrentSubjectId(context);
        Cursor subCursor = context.getContentResolver().query(subUri, new String[] {"_id"}, "modName=? AND subID=?", new String[] {activeFragment, subID},"");
        subCursor.moveToFirst();
        String modID = subCursor.getString(subCursor.getColumnIndex(NotesProvider.COL_ID));
        return modID;
    }

    public static String getCurrentSubjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", 0);
        String activeFragment = pref.getString("currentSub", "");
        String firstChar = String.valueOf(activeFragment.charAt(0));
        String editedFragment = firstChar.toUpperCase() + activeFragment.substring(1);
        String URL = "content://com.example.revision-app-two.NotesProvider/subjects";
        Uri subUri = Uri.parse(URL);
        Cursor subCursor = context.getContentResolver().query(subUri, new String[] {"_id"}, "subName=?", new String[] {editedFragment},"");
        String subID;
        if (subCursor.getCount() > 0) {
            subCursor.moveToFirst();
            subID = subCursor.getString(subCursor.getColumnIndex(NotesProvider.COL_ID));
        } else {
            subID = "none";
        }

        return subID;
    }

    public static List<String> getModules(Context context) {
        List<String> moduleList = new ArrayList<String>();

        String URL = "content://com.example.revision-app-two.NotesProvider/modules";
        Uri notes = Uri.parse(URL);

        String id = getCurrentSubjectId(context);

        Cursor c = context.getContentResolver().query(notes, null, "subID=?", new String[] {id}, "");
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    moduleList.add(c.getString(c.getColumnIndex("modName")));
                } while (c.moveToNext());
            }
        }


        return moduleList;
    }

    public static List<String> getTopics(Context context) {
        List<String> topicList = new ArrayList<String>();

        String URL = "content://com.example.revision-app-two.NotesProvider/topics";
        Uri notes = Uri.parse(URL);

        String modID = getCurrentModuleId(context);

        Cursor c = context.getContentResolver().query(notes, null, "modID=?", new String[]{modID}, "");

        if (c.moveToFirst()) {
            do {
                topicList.add(c.getString(c.getColumnIndex("topName")));
            } while (c.moveToNext());
        }

        return topicList;
    }

    public static List<String> getNotes(Context context) {
        List<String> notesList = new ArrayList<String>();

        String URL = "content://com.example.revision-app-two.NotesProvider/content";
        Uri notes = Uri.parse(URL);

        String modID = getCurrentTopicId(context);

        Cursor c = context.getContentResolver().query(notes, null, "topID=?", new String[]{modID}, "");

        if (c.moveToFirst()) {
            do {
                notesList.add(c.getString(c.getColumnIndex("note")));
            } while (c.moveToNext());
        }

        return notesList;
    }

    public static String getSelectedModID(Context context, String selected) {

        String URL = "content://com.example.revision-app-two.NotesProvider/modules";
        Uri subUri = Uri.parse(URL);
        Cursor subCursor = context.getContentResolver().query(subUri, new String[] {"_id"}, "modName=?", new String[] {selected},"");
        subCursor.moveToFirst();
        String modID = subCursor.getString(subCursor.getColumnIndex(NotesProvider.COL_ID));
        return modID;
    }
    public static String getSelectedTopID(Context context, String selected) {

        String URL = "content://com.example.revision-app-two.NotesProvider/topics";
        Uri subUri = Uri.parse(URL);
        Cursor subCursor = context.getContentResolver().query(subUri, new String[] {"_id"}, "topName=?", new String[] {selected},"");
        subCursor.moveToFirst();
        String topID = subCursor.getString(subCursor.getColumnIndex(NotesProvider.COL_ID));
        return topID;
    }

    public static void removeTopicFromTopID(Context context, String topID) {

        String notesURL = "content://com.example.revision-app-two.NotesProvider/content";
        Uri notes = Uri.parse(notesURL);

        context.getContentResolver().delete(notes, "topID=?", new String[]{topID});

        String topicURL = "content://com.example.revision-app-two.NotesProvider/topics";
        Uri topics = Uri.parse(topicURL);
        context.getContentResolver().delete(topics, "_id=?", new String[]{topID});
    }

    public static String getTopIDFromModID(Context context, String modID) {
        String URL = "content://com.example.revision-app-two.NotesProvider/topics";
        Uri subUri = Uri.parse(URL);
        Cursor subCursor = context.getContentResolver().query(subUri, new String[] {"_id"}, "modID=?", new String[] {modID},"");
        String topID;
        if (subCursor.getCount() > 0) {
            subCursor.moveToFirst();
            topID = subCursor.getString(subCursor.getColumnIndex(NotesProvider.COL_ID));
        } else {
            topID = "none";
        }


        return topID;
    }

    public static void removeModuleFromModID(Context context, String modID) {



        String id  =getTopIDFromModID(context, modID);

        if (!id.equals("none")) {
            removeTopicFromTopID(context, id);
        }


        String topicURL = "content://com.example.revision-app-two.NotesProvider/modules";
        Uri topics = Uri.parse(topicURL);
        context.getContentResolver().delete(topics, "_id=?", new String[]{modID});
    }

    public static void removeSelectedNote(Context context, String selected) {
        String noteURL = "content://com.example.revision-app-two.NotesProvider/content";
        Uri notes = Uri.parse(noteURL);
        context.getContentResolver().delete(notes, "note=?", new String[]{selected});
    }

    public static void seedDatabase(Context context) {
        ContentValues computing = new ContentValues();
        computing.put(NotesProvider.COL_SUBJECT_NAME, "Computing");

        context.getContentResolver().insert(NotesProvider.SUBJECT_CONTENT_URI, computing);

        ContentValues psychology = new ContentValues();
        psychology.put(NotesProvider.COL_SUBJECT_NAME, "Psychology");

        context.getContentResolver().insert(NotesProvider.SUBJECT_CONTENT_URI, psychology);

        ContentValues maths = new ContentValues();
        maths.put(NotesProvider.COL_SUBJECT_NAME, "Maths");

        context.getContentResolver().insert(NotesProvider.SUBJECT_CONTENT_URI, maths);

    }








}
