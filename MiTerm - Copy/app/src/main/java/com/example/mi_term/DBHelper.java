package com.example.mi_term;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){super(context,"Userdata.db",null,1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table UserDetails (accountnumber TEXT primary key, password TEXT,housetype TEXT,username TEXT unique,phonenumber TEXT)");
        db.execSQL("create Table bills(amount INT,date TEXT,username TEXT,type TEXT,bill_id INTEGER primary key autoincrement, FOREIGN KEY (username) REFERENCES UserDteails(username))");
        db.execSQL("create Table session(username TEXT primary key)");
//bill_id
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists UserDetails");
        db.execSQL("drop Table if exists bills");
        db.execSQL("drop Table if exists session");

    }
    //accountnumber(housenumber)
    //housetype(apartment or maisonette)
    //email
    public Boolean insertUserdata(String accountnumber, String password,String housetype,String username,String Phonenumber){

        System.out.println("Here we are");

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Sign up and log in
        contentValues.put("accountnumber", accountnumber);
        contentValues.put("password", password);
        contentValues.put("housetype", housetype);
        contentValues.put("username", username);
        contentValues.put("phonenumber",Phonenumber);
        long result = DB.insert("UserDetails", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addSession(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("username", username);

        long result = db.delete("session",null, null);

        if(result == -1){
            Log.d("addSession1", "addSession: Nope no work");
        }
        else{
            long success = db.insert("session", null,cv);
            if (success == -1){
                Log.d("addSession2", "addSession: Nope insert failed");
                db.close();
            }
            else{
                db.close();
                return true;
            }
        }
        return false;
    }

    public String getSession(){
        SQLiteDatabase db= this.getReadableDatabase();
        String session = "";

        Cursor c = db.rawQuery("SELECT * FROM session", null);


        if (c.moveToFirst()){
            do {

                session = c.getString(0);

            }while (c.moveToNext());
        }

        Log.d("pys", "getSession: " + session);

        c.close();
        db.close();

        return session;
    }



    public Boolean checkpassword(String username, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from UserDetails where username=? and password=?",
                new String[] {username,password});
        if (cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean updateuserdata(String username, String password)
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        Cursor cursor=DB.rawQuery("Select * from UserDetails where username=?", new String[] {username});
        if(cursor.getCount()>0) {
            long result = DB.update("UserDetails", contentValues, "name=?", new String[]{username});

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }

    public Boolean deletedata(String username) {
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from UserDetails where username=?", new String[] {username});
        if(cursor.getCount()>0) {
            long result = DB.delete("UserDetails", "name=?", new String[]{username});

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }

    public Boolean insertBills (int wateramount, String waterdate,int electricityamount,String electricitydate, String username){
        Log.d("insertBills", "insertBills: Made it here 1");
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues electricty = new ContentValues();
        ContentValues water = new ContentValues();
        //instert
        electricty.put("amount",electricityamount);
        electricty.put("date",electricitydate);
        electricty.put("username",username);
        electricty.put("type","E");

        long result = DB.insert("bills", null, electricty);

        if (result == -1) {
            return false;
        } else {
            water.put("amount",wateramount);
            water.put("date",waterdate);
            water.put("username",username);
            water.put("type","W");

            long result2 = DB.insert("bills", null, water);
            if(result2 == -1){
                return false;
            }
            else {
                return true;
            }

        }
    }

    public Cursor obtainbill(String bill_id)
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from Apartment where water_previous_reading=?",new String[]{bill_id});
        return cursor;
    }
    public Cursor obtainmaisonettedetails(String number)
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from Maisonnette where water_previous_reading=?",new String[]{number});
        return cursor;
    }

    public ArrayList<String> getUserHistory(String username, String typeValue){
        Log.d("222", "getUserHistory: nice");
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("ArrayList1", "getUserHistory: Works fine");
        Cursor cursor = db.rawQuery("SELECT * FROM bills WHERE username=? AND type=?", new String [] {username, typeValue});

        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex("date");
            do {
                String data = cursor.getString(columnIndex);
                arrayList.add(data);
            } while (cursor.moveToNext());
        }
        if (arrayList.isEmpty()) {
            arrayList.add("No data");
        }
        cursor.close();
        db.close();
        return arrayList;
    }
    public String getPhoneNo(String username){
        String data = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("ArrayList1", "getUserHistory: Works fine");
        Cursor cursor = db.rawQuery("SELECT * FROM UserDetails WHERE username=? ", new String [] {username});
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex("phonenumber");
            Log.d("Karim", "getPhoneNo: " + columnIndex);
            do {
                data = cursor.getString(columnIndex);
                Log.d("Karim", "getPhoneNo: " + data);
            } while (cursor.moveToNext());
        }
        if(data == null){
            Log.d("Karim", "getPhoneNo: No work");
        }
        cursor.close();
        db.close();
        return data;
    }

}


