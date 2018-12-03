package project.aut.carato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CaratoDB.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cars" +
                "(c_id integer PRIMARY KEY," +
                "c_name text," +
                "c_model text," +
                "c_type text," +
                "c_seats text," +
                "c_doors text," +
                "c_transmission text," +
                "c_rent text," +
                "c_image blob," +
                "c_color text," +
                "c_class text)");

        db.execSQL("CREATE TABLE users" +
                "(u_id integer PRIMARY KEY," +
                "u_name text," +
                "u_uname text," +
                "u_email text," +
                "u_gender text," +
                "u_birth text," +
                "u_pass text," +
                "u_type text)");

        db.execSQL("CREATE TABLE booking" +
                "(bookin_id integer PRIMARY KEY," +
                "c_id integer," +
                "u_id integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cars");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS booking");
        onCreate(db);
    }

    public boolean GetUserCredentials(String uname, String upass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM users WHERE u_uname='" + uname + "'AND u_pass='" + upass + "'", null);
        return res.getCount() > 0;
    }

    public boolean InsertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("u_name", user.getName());
        contentValues.put("u_uname", user.getUname());
        contentValues.put("u_email", user.getEmail());
        contentValues.put("u_gender", user.getGender());
        contentValues.put("u_birth", user.getBirth());
        contentValues.put("u_pass", user.getPass());
        contentValues.put("u_type", user.getType());

        db.insert("users", null, contentValues);

        return true;
    }

    public boolean UsernameAvailability(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE u_uname='" + username + "'",null);

        return cursor.getCount()>0;
    }

    public boolean AdminAccess(String uname){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE u_uname='"+ uname + "'AND u_type = 'admin'",null);

        return cursor.getCount()>0;
    }

    public ArrayList<Car> GetAllCars(){
        ArrayList<Car> cars = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cars",null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            cars.add(new Car(cursor.getString(cursor.getColumnIndex("c_name")),
                    cursor.getString(cursor.getColumnIndex("c_model")),
                    cursor.getString(cursor.getColumnIndex("c_type")),
                    cursor.getString(cursor.getColumnIndex("c_seats")),
                    cursor.getString(cursor.getColumnIndex("c_doors")),
                    cursor.getString(cursor.getColumnIndex("c_transmission")),
                    cursor.getString(cursor.getColumnIndex("c_rent")),
                    cursor.getBlob(cursor.getColumnIndex("c_image")),
                    cursor.getString(cursor.getColumnIndex("c_color")),
                    cursor.getString(cursor.getColumnIndex("c_class"))));

            cursor.moveToNext();
        }

        return cars;
    }
}
