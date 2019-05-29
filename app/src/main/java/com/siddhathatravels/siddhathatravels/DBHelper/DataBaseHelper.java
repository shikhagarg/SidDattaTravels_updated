package com.siddhathatravels.siddhathatravels.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.siddhathatravels.siddhathatravels.Model.UserProfile;
import com.siddhathatravels.siddhathatravels.Model.UserTable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "user_db";

   public DataBaseHelper(Context context){
       super(context,DATABASE_NAME,null,DATABASE_VERSION);

   }
    @Override
    public void onCreate(SQLiteDatabase db) {

       db.execSQL(UserTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertUser(UserProfile user) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(UserTable.STUDENT_NAME, user.studentName);
        values.put(UserTable.FATHER_NAME, user.fatherName);
        values.put(UserTable.ADDRESS, user.address);
        values.put(UserTable.BATCH, user.batch);
        values.put(UserTable.STREAM, user.stream);
        values.put(UserTable.ROLLNO,user.rollNo);
        values.put(UserTable.ISADMIN, user.isAdmin);
        values.put(UserTable.STUDENTPH, user.studentPh);
        values.put(UserTable.FATHERPH, user.fatherPh);
        values.put(UserTable.MOTHERPH, user.motherPh);
        values.put(UserTable.REGISTEREDPHONE, user.registeredPhone);
        values.put(UserTable.AVAILTRANSPORTDATE, user.availTransportDate);
        values.put(UserTable.ISAVAILINGAC, user.isAvailingAC);
        values.put((UserTable.CREATEDAT),user.createdAt);

        // insert row
        long id = db.insert(UserTable.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();

        // Select All Query
        String selectQuery = String.format("SELECT  * FROM %s ", UserTable.TABLE_NAME);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserTable userTable = new UserTable();

                UserProfile userProfile = new UserProfile();


                if(cursor.getString(cursor.getColumnIndex(UserTable.CREATEDAT))!= null)
                {
                    userProfile.studentName  = cursor.getString(cursor.getColumnIndex(UserTable.STUDENT_NAME));
                    userProfile.fatherName = cursor.getString(cursor.getColumnIndex(UserTable.FATHER_NAME));
                    userProfile.batch  = cursor.getString(cursor.getColumnIndex(UserTable.BATCH));
                    userProfile.stream  = cursor.getString(cursor.getColumnIndex(UserTable.STREAM));
                    userProfile.rollNo  = cursor.getString(cursor.getColumnIndex(UserTable.ROLLNO));
                    userProfile.studentPh = cursor.getString(cursor.getColumnIndex(UserTable.STUDENTPH));
                    userProfile.fatherPh  = cursor.getString(cursor.getColumnIndex(UserTable.FATHERPH));
                    userProfile.motherPh  = cursor.getString(cursor.getColumnIndex(UserTable.MOTHERPH));
                    userProfile.isAdmin  = cursor.getInt(cursor.getColumnIndex(UserTable.ISADMIN)) == 0 ? false : true;
                    userProfile.isAvailingAC  = cursor.getInt(cursor.getColumnIndex(UserTable.ISAVAILINGAC)) == 0 ? false : true;
                    userProfile.registeredPhone  = cursor.getString(cursor.getColumnIndex(UserTable.REGISTEREDPHONE));
                    userTable.setUserProfile(userProfile);

                    users.add(userProfile.studentName);
                }

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list

        return users;
    }

}
