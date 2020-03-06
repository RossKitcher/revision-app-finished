package com.example.revision_app_two;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class NotesProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.revision-app-two.NotesProvider";
    static final String SUBJECTS_URL = "content://" + PROVIDER_NAME + "/subjects";
    static final String MODULES_URL = "content://" + PROVIDER_NAME + "/modules";
    static final String TOPICS_URL = "content://" + PROVIDER_NAME + "/topics";
    static final String CONTENT_URL = "content://" + PROVIDER_NAME + "/content";

    static final Uri SUBJECT_CONTENT_URI = Uri.parse(SUBJECTS_URL);
    static final Uri MODULE_CONTENT_URI = Uri.parse(MODULES_URL);
    static final Uri TOPIC_CONTENT_URI = Uri.parse(TOPICS_URL);
    static final Uri CONTENT_CONTENT_URI = Uri.parse(CONTENT_URL);

    static final String COL_ID = "_id";
    static final String COL_SUBJECT_NAME = "subName";
    static final String COL_MODULE_NAME = "modName";
    static final String COL_SUBJECT_ID = "subID";
    static final String COL_TOPIC_NAME = "topName";
    static final String COL_MODULE_ID = "modID";
    static final String COL_NOTE = "note";
    static final String COL_TOPIC_ID = "topID";

    private static HashMap<String, String> SUBJECTS_PROJECTION_MAP;
    private static HashMap<String, String> MODULES_PROJECTION_MAP;
    private static HashMap<String, String> TOPICS_PROJECTION_MAP;
    private static HashMap<String, String> CONTENT_PROJECTION_MAP;

    static final int SUBJECTS = 1;
    static final int SUBJECT_ID = 2;
    static final int MODULES = 3;
    static final int MODULE_ID = 4;
    static final int TOPICS = 5;
    static final int TOPIC_ID = 6;
    static final int CONTENT = 7;
    static final int CONTENT_ID = 8;

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "subjects", SUBJECTS);
        uriMatcher.addURI(PROVIDER_NAME, "subject/#", SUBJECT_ID);
        uriMatcher.addURI(PROVIDER_NAME, "modules", MODULES);
        uriMatcher.addURI(PROVIDER_NAME, "module/#", MODULE_ID);
        uriMatcher.addURI(PROVIDER_NAME, "topics", TOPICS);
        uriMatcher.addURI(PROVIDER_NAME, "topic/#", TOPIC_ID);
        uriMatcher.addURI(PROVIDER_NAME, "content", CONTENT);
        uriMatcher.addURI(PROVIDER_NAME, "content/#", CONTENT_ID);
    }

    /**
     * Database specific constant declarations
     */

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Notes";
    static final String SUBJECTS_TABLE_NAME = "subjects";
    static final String MODULES_TABLE_NAME = "modules";
    static final String TOPICS_TABLE_NAME = "topics";
    static final String CONTENT_TABLE_NAME = "content";
    static final int DATABASE_VERSION = 7;
    static final String CREATE_SUBJECTS_TABLE =
            " create table " + SUBJECTS_TABLE_NAME +
                    " (" + COL_ID + " integer primary key autoincrement, " +
                    COL_SUBJECT_NAME + " text not null);";
    static final String CREATE_MODULES_TABLE =
            " create table " + MODULES_TABLE_NAME +
                    " (" + COL_ID + " integer primary key autoincrement, " +
                    COL_MODULE_NAME + " text not null, " +
                    COL_SUBJECT_ID + " integer not null, " +
                    " foreign  key(" + COL_SUBJECT_ID + ") references " + SUBJECTS_TABLE_NAME + "(" + COL_ID + "));";
    static final String CREATE_TOPICS_TABLE =
            " create table " + TOPICS_TABLE_NAME +
                    " (" + COL_ID + " integer primary key autoincrement, " +
                    COL_TOPIC_NAME + " text not null, " +
                    COL_MODULE_ID + " integer not null, " +
                    " foreign  key(" + COL_MODULE_ID + ") references " + MODULES_TABLE_NAME + "(" + COL_ID + "));";
    static final String CREATE_CONTENT_TABLE =
            " create table " + CONTENT_TABLE_NAME +
                    " (" + COL_ID + " integer primary key autoincrement, " +
                    COL_NOTE + " text not null, " +
                    COL_TOPIC_ID + " integer not null, " +
                    " foreign  key(" + COL_TOPIC_ID + ") references " + TOPICS_TABLE_NAME + "(" + COL_ID + "));";

    /**
     * Helper class that creates and manages
     * the providers underlying data repository
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);}

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SUBJECTS_TABLE);
            db.execSQL(CREATE_MODULES_TABLE);
            db.execSQL(CREATE_TOPICS_TABLE);
            db.execSQL(CREATE_CONTENT_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + CONTENT_TABLE_NAME);
            db.execSQL("drop table if exists " + TOPICS_TABLE_NAME);
            db.execSQL("drop table if exists " + MODULES_TABLE_NAME);
            db.execSQL("drop table if exists " + SUBJECTS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write-able database which will trigger its
         * creation if it doesn't already exist
         */

        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID;
        Uri _uri;

        switch(uriMatcher.match(uri)) {
            case SUBJECTS:
                rowID = db.insert(SUBJECTS_TABLE_NAME, "", values);
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(SUBJECT_CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
            case MODULES:
                rowID = db.insert(MODULES_TABLE_NAME, "", values);
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(MODULE_CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
            case TOPICS:
                rowID = db.insert(TOPICS_TABLE_NAME, "", values);
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(TOPIC_CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
            case CONTENT:
                rowID = db.insert(CONTENT_TABLE_NAME, "", values);
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
            default:
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)) {
            case SUBJECTS:
                qb.setTables(SUBJECTS_TABLE_NAME);
                qb.setProjectionMap(SUBJECTS_PROJECTION_MAP);
                break;
            case SUBJECT_ID:
                qb.setTables(SUBJECTS_TABLE_NAME);
                qb.appendWhere(COL_ID + "=" + uri.getPathSegments().get(1));
                break;
            case MODULES:
                qb.setTables(MODULES_TABLE_NAME);
                qb.setProjectionMap(MODULES_PROJECTION_MAP);
                break;
            case MODULE_ID:
                qb.setTables(MODULES_TABLE_NAME);
                qb.appendWhere(COL_ID + "=" + uri.getPathSegments().get(1));
                break;
            case TOPICS:
                qb.setTables(TOPICS_TABLE_NAME);
                qb.setProjectionMap(TOPICS_PROJECTION_MAP);
                break;
            case TOPIC_ID:
                qb.setTables(TOPICS_TABLE_NAME);
                qb.appendWhere(COL_ID + "=" + uri.getPathSegments().get(1));
                break;
            case CONTENT:
                qb.setTables(CONTENT_TABLE_NAME);
                qb.setProjectionMap(CONTENT_PROJECTION_MAP);
                break;
            case CONTENT_ID:
                qb.setTables(CONTENT_TABLE_NAME);
                qb.appendWhere(COL_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = "_id";

        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(),uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        String id;
        switch (uriMatcher.match(uri)) {
            case SUBJECTS:
                count = db.delete(SUBJECTS_TABLE_NAME, selection, selectionArgs);
                break;
            case SUBJECT_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(SUBJECTS_TABLE_NAME, COL_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            case MODULES:
                count = db.delete(MODULES_TABLE_NAME, selection, selectionArgs);
                break;
            case MODULE_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(MODULES_TABLE_NAME, COL_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            case TOPICS:
                count = db.delete(TOPICS_TABLE_NAME, selection, selectionArgs);
                break;
            case TOPIC_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(TOPICS_TABLE_NAME, COL_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            case CONTENT:
                count = db.delete(CONTENT_TABLE_NAME, selection, selectionArgs);
                break;
            case CONTENT_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(CONTENT_TABLE_NAME, COL_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch(uriMatcher.match(uri)) {
            case SUBJECTS:
                count = db.update(SUBJECTS_TABLE_NAME, values, selection, selectionArgs);
                break;
            case SUBJECT_ID:
                count = db.update(SUBJECTS_TABLE_NAME, values, COL_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            case MODULES:
                count = db.update(MODULES_TABLE_NAME, values, selection, selectionArgs);
                break;
            case MODULE_ID:
                count = db.update(MODULES_TABLE_NAME, values, COL_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            case TOPICS:
                count = db.update(TOPICS_TABLE_NAME, values, selection, selectionArgs);
                break;
            case TOPIC_ID:
                count = db.update(TOPICS_TABLE_NAME, values, COL_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            case CONTENT:
                count = db.update(CONTENT_TABLE_NAME, values, selection, selectionArgs);
                break;
            case CONTENT_ID:
                count = db.update(CONTENT_TABLE_NAME, values, COL_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case SUBJECTS:
                return "vnd.android.cursor.dir/vnd.example.subjects";
            case MODULES:
                return "vnd.android.cursor.dir/vnd.example.modules";
            case TOPICS:
                return "vnd.android.cursor.dir/vnd.example.topics";
            case CONTENT:
                return "vnd.android.cursor.dir/vnd.example.content";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}
