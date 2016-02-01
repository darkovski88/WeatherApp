package darko.com.weatherapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Petkovski on 01.02.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    public static final String DB_NAME = "weatherApp.db";
    public static final int DB_VERS = 1;
    public static final String TABLE = "cities";
    public static final boolean Debug = true;
    private final String CITY_ID = "cityID";
    private final String POSITION = "position";
    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERS);
        this.context = context;
    }

    public Cursor query(SQLiteDatabase db, String query) {
        Cursor cursor = db.rawQuery(query, null);
        if (Debug) {
            Log.d(TAG, "Executing Query: " + query);
        }
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* Create table Logic, once the Application has ran for the first time. */
        String sql = String.format("CREATE TABLE %s (cityID integer primary key, position integer)", TABLE);
        db.execSQL(sql);
        if (Debug) {
            Log.d(TAG, "onCreate Called.");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE));
        if (Debug) {
            Log.d(TAG, "Upgrade: Dropping Table and Calling onCreate");
        }
        this.onCreate(db);

    }

    /**
     * Add a city to DB
     *
     * @param cityId
     * @param position
     */
    public void addCity(int cityId, int position) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        int cityID = cityId;
        values.put(CITY_ID, cityID);
        values.put(POSITION, position);
        db.insert(DBHelper.TABLE, null, values);
        db.close();
        dbHelper.close();
    }

    /**
     * Get all cityIds from the DB
     *
     * @return ArrayList of city ids
     */
    public ArrayList<Integer> getCityIds() {
        ArrayList<Integer> results = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = dbHelper.query(db, "SELECT * from " + TABLE + " ORDER BY " + POSITION + " ASC");
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                results.add(cursor.getInt(cursor.getColumnIndex(CITY_ID)));
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        dbHelper.close();
        return results;
    }

    /**
     * check if a city is added to the db by its ID
     *
     * @param cityId
     * @return true if its added, false otherwise
     */
    public boolean checkIfCityAdded(Integer cityId) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String Query = "Select * from " + TABLE + " where " + CITY_ID + " = " + cityId;
        Cursor cursor = db.rawQuery(Query, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        dbHelper.close();
        return exists;

    }

    /**
     * Remove a city by its id from the DB
     * @param cityId
     */
    public void removeCityFromDb(Integer cityId) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE, CITY_ID + "=" + cityId, null);
        Log.d(TAG, "item removed");
        db.close();
        dbHelper.close();
    }
}
