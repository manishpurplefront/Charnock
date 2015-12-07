package com.charnock.dev;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.charnock.dev.model.Database;

import java.util.ArrayList;
import java.util.List;

public class dbhelp {

    static final String DATABASE_NAME = "Charnock";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_TABLE = "user_master";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PROFILE = "prifile_id";
    private static final String KEY_BUSINESS_ID = "business_id";
    private static final String KEY_ROLE_ID = "role_id";
    private static final String KEY_NODE_ID = "node_id";
    private static final String KEY_PERMISSION_ID = "permission_id";
    private static final String KEY_LEVEL_ID = "level_id";
    private final Context ourctx;
    private SQLiteDatabase ourdb;
    private DatabaseHelper2 ourhelper;


    public dbhelp(Context ctx) {
        ourctx = ctx;
    }

    public dbhelp open() throws SQLException {
        ourhelper = new DatabaseHelper2(ourctx);
        ourdb = ourhelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourhelper.close();
    }

    public void logoout() {
        ourdb.delete(DATABASE_TABLE, null, null);
    }

    public void updateeuser(String id, String name, String email, String address, String city, String pincode) {
        ContentValues cv4 = new ContentValues();
        cv4.put(KEY_NAME, name);
        cv4.put(KEY_EMAIL, email);
        cv4.put(KEY_BUSINESS_ID, pincode);
        ourdb.update(DATABASE_TABLE, cv4, KEY_ID + "=" + id, null);
    }

    public void passwordupdate(String id, String newpassword) {
        ContentValues cv5 = new ContentValues();
        cv5.put(KEY_PASSWORD, newpassword);
        ourdb.update(DATABASE_TABLE, cv5, KEY_ID + "=" + id, null);
    }

    public void createuser(String id, String name, String email, String password, String phone,
                           String business_id, String profile_id, String node_id, String role_id, String permission_id, String level_id) {
        ourdb.delete(DATABASE_TABLE, null, null);
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, id);
        cv.put(KEY_NAME, name);
        cv.put(KEY_EMAIL, email);
        cv.put(KEY_PASSWORD, password);
        cv.put(KEY_PHONE, phone);
        cv.put(KEY_PROFILE, profile_id);
        cv.put(KEY_BUSINESS_ID, business_id);
        if (node_id.equals("null") || node_id.equals("")) {
            cv.put(KEY_NODE_ID, "");
        } else {
            cv.put(KEY_NODE_ID, node_id);
        }
        if (role_id.equals("null") || role_id.equals("")) {
            cv.put(KEY_ROLE_ID, "");
        } else {
            cv.put(KEY_ROLE_ID, role_id);
        }
        if (permission_id.equals("null") || permission_id.equals("")) {
            cv.put(KEY_PERMISSION_ID, "");
        } else {
            cv.put(KEY_PERMISSION_ID, permission_id);
        }
        cv.put(KEY_LEVEL_ID, "");

//        if (level_id.equals("null") || level_id.equals("")) {
//            cv.put(KEY_LEVEL_ID,"");
//        } else {
//            cv.put(KEY_LEVEL_ID,level_id);
//        }
        ourdb.insert(DATABASE_TABLE, null, cv);
    }

    public void updatepassword(String id, String password) {
        ContentValues cv4 = new ContentValues();
        cv4.put(KEY_PASSWORD, password);
        ourdb.update(DATABASE_TABLE, cv4, KEY_ID + "=" + id, null);

    }

    public static class DatabaseHelper2 extends SQLiteOpenHelper {

        public DatabaseHelper2(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ID + " INTEGER NOT NULL, " +
                    KEY_NAME + " TEXT NOT NULL, " +
                    KEY_EMAIL + " TEXT NOT NULL, " +
                    KEY_PASSWORD + " TEXT NOT NULL, " +
                    KEY_PHONE + " TEXT NOT NULL, " +
                    KEY_PROFILE + " TEXT NOT NULL, " +
                    KEY_BUSINESS_ID + " TEXT NOT NULL, " +
                    KEY_ROLE_ID + " TEXT NOT NULL, " +
                    KEY_NODE_ID + " TEXT NOT NULL, " +
                    KEY_PERMISSION_ID + " TEXT NOT NULL, " +
                    KEY_LEVEL_ID + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

        public List<Database> getdatabase() {
            List<Database> databases = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            String query2 = "Select * from " + DATABASE_TABLE;
            String query = "Select * from user_master";
            Log.d("query2", query2);
            Log.d("query", query);
            Cursor c = db.rawQuery(query, null);

            if (c != null) {
                c.moveToFirst();
                Database td = new Database();
                Log.d("id", c.getString(c.getColumnIndex("id")));
                td.setId(c.getString(c.getColumnIndex(KEY_ID)));
                Log.d("id", c.getString(c.getColumnIndex(KEY_ID)));
                td.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                td.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                td.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
                td.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
                td.setProfile_id(c.getString(c.getColumnIndex(KEY_PROFILE)));
                td.setBusiness_id(c.getString(c.getColumnIndex(KEY_BUSINESS_ID)));
                td.setRole_id(c.getString(c.getColumnIndex(KEY_ROLE_ID)));
                td.setNode_id(c.getString(c.getColumnIndex(KEY_NODE_ID)));
                td.setPermission_id(c.getString(c.getColumnIndex(KEY_PERMISSION_ID)));
                td.setLevel_id(c.getString(c.getColumnIndex(KEY_LEVEL_ID)));
                databases.add(td);
                c.close();
            }
            return databases;
        }

    }

}