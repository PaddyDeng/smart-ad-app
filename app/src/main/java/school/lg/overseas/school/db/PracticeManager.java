package school.lg.overseas.school.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.MyApplication;

/**
 * Created by Administrator on 2018/1/16.
 */

public class PracticeManager {
    private DBHelper dbHelper = null;
    private static PracticeManager instance;
    private static SQLiteDatabase writableDatabase;
    private static SQLiteDatabase readableDatabase;
    public static PracticeManager getInstance() {
        if (null == instance) {
            synchronized (PracticeManager.class) {
                if (null == instance) {
                    instance = new PracticeManager();
                }
            }
        }
        return instance;
    }
    private PracticeManager() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(MyApplication.getInstance());
            writableDatabase = dbHelper.getWritableDatabase();
            readableDatabase = dbHelper.getReadableDatabase();
        }
    }

    public List<Reading> queryAll(int tag){
        List<Reading> readings =new ArrayList<>();
        Cursor cursor = readableDatabase.query(Field.TABLE_NAME, null, Field.TAG + "=?", new String[]{tag + ""}, null, null, null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                Reading reading =new Reading();
                reading.setId(cursor.getString(cursor.getColumnIndex(Field.UID)));
                reading.setTag(tag);
                reading.setTime(cursor.getString(cursor.getColumnIndex(Field.TIME)));
                reading.setTitle(cursor.getString(cursor.getColumnIndex(Field.TITLE)));
                reading.setType(cursor.getInt(cursor.getColumnIndex(Field.TYPE)));
                reading.setName(cursor.getString(cursor.getColumnIndex(Field.SCHOOL)));
                reading.setS(cursor.getString(cursor.getColumnIndex(Field.SCHOOLNET)));
                reading.setEnMajprName(cursor.getString(cursor.getColumnIndex(Field.ENNAME)));
                readings.add(reading);
            }
        }
        cursor.close();
        return readings;
    }

    public List<Reading> queryForId(int tag,int type,String id){
        List<Reading> readings =new ArrayList<>();
        Cursor cursor = readableDatabase.query(Field.TABLE_NAME, null, Field.TAG + "=? and "+Field.TYPE+"=? and "+Field.UID+"=?", new String[]{tag + "",type+"",id}, null, null, null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                Reading reading =new Reading();
                reading.setId(cursor.getString(cursor.getColumnIndex(Field.UID)));
                reading.setTag(tag);
                reading.setTime(cursor.getString(cursor.getColumnIndex(Field.TIME)));
                reading.setTitle(cursor.getString(cursor.getColumnIndex(Field.TITLE)));
                reading.setType(cursor.getInt(cursor.getColumnIndex(Field.TYPE)));
                reading.setName(cursor.getString(cursor.getColumnIndex(Field.SCHOOL)));
                reading.setS(cursor.getString(cursor.getColumnIndex(Field.SCHOOLNET)));
                reading.setEnMajprName(cursor.getString(cursor.getColumnIndex(Field.ENNAME)));
                readings.add(reading);
            }
        }
        cursor.close();
        return readings;
    }

    public List<Reading> queryForType(int tag, int type){
        List<Reading> readings =new ArrayList<>();
        Cursor cursor = readableDatabase.query(Field.TABLE_NAME, null, Field.TAG + "=? and "+Field.TYPE+"=?", new String[]{tag + "",type+""}, null, null, null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                Reading reading =new Reading();
                reading.setId(cursor.getString(cursor.getColumnIndex(Field.UID)));
                reading.setTag(tag);
                reading.setTime(cursor.getString(cursor.getColumnIndex(Field.TIME)));
                reading.setTitle(cursor.getString(cursor.getColumnIndex(Field.TITLE)));
                reading.setType(cursor.getInt(cursor.getColumnIndex(Field.TYPE)));
                reading.setName(cursor.getString(cursor.getColumnIndex(Field.SCHOOL)));
                reading.setS(cursor.getString(cursor.getColumnIndex(Field.SCHOOLNET)));
                reading.setEnMajprName(cursor.getString(cursor.getColumnIndex(Field.ENNAME)));
                readings.add(reading);
            }
        }
        cursor.close();
        return readings;
    }
    public void insert(Reading reading){
        ContentValues values = new ContentValues();
        values.put(Field.TIME,System.currentTimeMillis()+"");
        values.put(Field.UID, reading.getId());
        values.put(Field.TITLE, reading.getTitle());
        values.put(Field.TYPE, reading.getType());
        values.put(Field.TAG, reading.getTag());
        values.put(Field.SCHOOL,reading.getName());
        values.put(Field.SCHOOLNET,reading.getS());
        values.put(Field.ENNAME,reading.getEnMajprName());
        int update = writableDatabase.update(Field.TABLE_NAME, values, Field.UID + "=? and " + Field.TYPE + "=? and "+Field.TAG+"=?",
                new String[]{reading.getId(), String.valueOf(reading.getType()),String.valueOf(reading.getTag())});
        if (update != 1) {
            writableDatabase.insert(Field.TABLE_NAME, null, values);
        }
    }
    public void deleteAllForTag(int tag){
        writableDatabase.delete(Field.TABLE_NAME,Field.TAG+"=?",new String[]{String.valueOf(tag)});
    }

    public void deleteOne(int tag,int type,String id){
        writableDatabase.delete(Field.TABLE_NAME,Field.TAG+"=? and "+Field.TYPE+"=? and "+Field.UID+"=?",new String[]{String.valueOf(tag),String.valueOf(type),id});
    }
}
