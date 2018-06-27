package school.lg.overseas.school.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/1/16.
 */

public class DBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME= "record.db";
    private static final int DB_VERSION = 1;
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        newTable(sqLiteDatabase,false);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    private void newTable(SQLiteDatabase db, boolean b){
        String cond = b ? "IF NOT EXISTS " : " ";
        String sql="CREATE TABLE " + cond + Field.TABLE_NAME + " (ID INT PRIMARY KEY,"
                + Field.TIME + " text,"
                + Field.TYPE + " int,"
                + Field.TITLE + " text,"
                + Field.UID + " text,"
                +Field.SCHOOL+" text,"
                +Field.SCHOOLNET+" text,"
                +Field.ENNAME+" text,"
                + Field.TAG + " int"
                + ");";
        db.execSQL(sql);
    }
}
