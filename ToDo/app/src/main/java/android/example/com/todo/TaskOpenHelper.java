package android.example.com.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class TaskOpenHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "task_list";
    private final static int DATABASE_VERSION = 1;

    private final static String TABLE_NAME = "task_entries";
    private final static String KEY_ID = "_id";
    private final static String KEY_HEADING = "title";
    private final static String KEY_DETAIL = "detail";
    private final static String KEY_PENDING = "pending";

    private SQLiteDatabase mReadableDB;
    private SQLiteDatabase mWritableDB;

    public TaskOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
                " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_HEADING + " TEXT, " +
                KEY_DETAIL + " TEXT, " + KEY_PENDING + " INTEGER );";
        db.execSQL(CREATE_TABLE_SQL);
        //fillDatabaseWithData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(DROP_TABLE_SQL);
        onCreate(db);
    }

    public Task queryTask(int position){
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_ID + " ASC LIMIT "
                + position + " , 1";
        Cursor cursor = null;
        Task task = null;
        try{
            if(mReadableDB == null){
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            task = new Task(cursor.getString(cursor.getColumnIndex(KEY_HEADING)),
                    cursor.getString(cursor.getColumnIndex(KEY_DETAIL)),
                    cursor.getInt(cursor.getColumnIndex(KEY_PENDING))!=0);

        }
        catch(Exception e){
            Log.d(TAG, "EXCEPTION! " + e);
        }
        finally{
            if(cursor != null){
                cursor.close();
            }
        }
        return task;
    }

    public long getTaskAtPos(int position){
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_ID + " ASC LIMIT "
                + position + " , 1";
        Cursor cursor = null;
        long id = -1;
        try{
            if(mReadableDB == null){
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        }
        catch(Exception e){
            Log.d(TAG, "EXCEPTION! " + e);
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return id;
    }

    public int deleteTaskAtPos(int position){

        int numOfRowsDeleted = 0;

        long id = getTaskAtPos(position);

        if(id != -1){
            String where = KEY_ID + "=?";
            try{
                if(mWritableDB == null){
                    mWritableDB = getWritableDatabase();
                }
                numOfRowsDeleted = mWritableDB.delete(TABLE_NAME, where, new String[]{Long.toString(id)});
                Log.d(TAG, "DONE! " + id)   ;
            }
            catch (Exception e){
                Log.d(TAG, "EXCEPTION! " + e);
            }
        }
        return numOfRowsDeleted;
    }

    public int editTaskAtPos(int position){

        int numOfRowsUpdated = 0;

        Task task = queryTask(position);
        long id = getTaskAtPos(position);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_HEADING, task.getHeading());
        contentValues.put(KEY_DETAIL, task.getDetail());
        contentValues.put(KEY_PENDING, !task.isPending());

        try{
            if(mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }
            numOfRowsUpdated = mWritableDB.update(TABLE_NAME, contentValues, KEY_ID + " = ?", new String[]{Long.toString(id)});
        }
        catch(Exception e){
            Log.d(TAG, "EXCEPTION! " + e);
        }
        return numOfRowsUpdated;
    }

    public long insertTask(String heading, String detail, int pending){
        long newId = 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_HEADING, heading);
        contentValues.put(KEY_DETAIL, detail);
        contentValues.put(KEY_PENDING, pending);

        try{
            if(mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(TABLE_NAME, null, contentValues);
        }
        catch(Exception e){
            Log.d(TAG, "EXCEPTION! " + e);
        }
        return newId;
    }

    public void fillDatabaseWithData(SQLiteDatabase db){
        Task[] tasks = new Task[20];
        ContentValues contentValues = new ContentValues();
        for(int i=0;i<20;i++){
            tasks[i] = new Task("Heading: " + i, "Detail: " + i, i%2!=0);
            contentValues.put(KEY_HEADING, "Heading: " + i);
            contentValues.put(KEY_DETAIL, "Detail: " + i);
            contentValues.put(KEY_PENDING, i%2!=0);
            db.insert(TABLE_NAME, null, contentValues);
        }
    }

    public int getSize(){
        String query = "SELECT * FROM " + TABLE_NAME + " ;";
        Cursor cursor = null;
        int size = 0;
        try{
            if(mReadableDB == null){
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            size = cursor.getCount();

        }
        catch(Exception e){
            Log.d(TAG, "EXCEPTION! " + e);
        }
        finally{
            if(cursor != null){
                cursor.close();
            }
        }
        return size;
    }
}
