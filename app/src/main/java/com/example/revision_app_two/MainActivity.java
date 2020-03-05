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
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddModule.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }

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
        subCursor.moveToFirst();
        String subID = subCursor.getString(subCursor.getColumnIndex(NotesProvider.COL_ID));
        return subID;
    }

    public static List<String> getModules(Context context) {
        List<String> moduleList = new ArrayList<String>();

        String URL = "content://com.example.revision-app-two.NotesProvider/modules";
        Uri notes = Uri.parse(URL);

        String id = getCurrentSubjectId(context);

        Cursor c = context.getContentResolver().query(notes, null, "subID=?", new String[] {id}, "");

        if (c.moveToFirst()) {
            do {
                moduleList.add(c.getString(c.getColumnIndex("modName")));
            } while (c.moveToNext());
        }

        return moduleList;
    }




}
