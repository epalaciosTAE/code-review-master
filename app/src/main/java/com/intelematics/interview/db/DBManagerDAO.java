package com.intelematics.interview.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.intelematics.interview.R;
import com.intelematics.interview.models.Song;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Eduardo on 24/12/2015.
 */
public class DBManagerDAO implements ManagerDAO {

    private static DBManagerDAO instance;
    private Context context;
    private DBManager dbHelper;

    private DBManagerDAO (Context context) {
        this.context = context;
        openDataBase(context);
    }

    public static DBManagerDAO getInstance(Context context) {
        if (instance == null) {
            instance = new DBManagerDAO(context);
        }
        return instance;
    }

    private void openDataBase(Context context) {
        dbHelper = new DBManager(context);
    }

    @Override
    public synchronized ArrayList<Song> getSongsList() {
        ArrayList<Song> list = null;// TODO create the list with capacity

//        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();  // TODO do we need this?
//        builder.setTables(context.getString(R.string.sql_table_song));

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String[] values = {};
            Cursor cursor = db.rawQuery(context.getString(R.string.sql_select_all_songs), values);
            cursor.moveToFirst();
//            int coverIndex = cursor.getColumnIndex(context.getString(R.string.sql_column_song_cover)); // TODO remove this
            list = new ArrayList<>(cursor.getCount());
            do {
                Song song = new Song();
                song.setId(cursor.getLong(cursor.getColumnIndex(context.getString(R.string.sql_column_song_idsong))));
                song.setTitle(cursor.getString(cursor.getColumnIndex(context.getString(R.string.sql_column_song_title))));
                song.setArtist(cursor.getString(cursor.getColumnIndex(context.getString(R.string.sql_column_song_artist))));
                song.setPrice(cursor.getDouble(cursor.getColumnIndex(context.getString(R.string.sql_column_song_price))));
                song.setCoverURL(cursor.getString(cursor.getColumnIndex(context.getString(R.string.sql_column_song_coverURL))));
//            byte[] imageByteArray = cursor.getBlob(coverIndex); // FIXME imageByteArray somentimes is null
                // TODO posible bug here, DB schema doesnt match blob with images --> CREATE TABLE IF NOT EXISTS song (_id INT PRIMARY KEY, idSong INT UNIQUE, title TEXT, artist TEXT, price FLOAT, coverURL TEXT, cover TEXT);

//            Bitmap image = null;
//            if(imageByteArray != null){ // TODO this is not needed because we can load the images with picasso retrieving just the URL
//                ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray); //this never happends since imageByteArray is NUll
//                image = BitmapFactory.decodeStream(imageStream);
//                imageStream = null;
//            }
//            song.setCover(image); //TODO Here is stting the bitmap from the data base in Song, sometimes is null

                list.add(song);
            } while (cursor.moveToNext());

            cursor.close();
        } catch (SQLiteException e) {
            Log.e(context.getString(R.string.db_manager_dao), "Error getting songs: ", e);
        } finally {
            if (db.isOpen()){
                db.close();
            }
        }

        if (list == null || list.size() == 0) {
            return (ArrayList<Song>) Collections.EMPTY_LIST;
        }
        return list;
    }

    @Override
    public synchronized void deleteSongsList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            //        String[] values = {}; // TODO this is empty but works with null??
            db.delete(context.getString(R.string.sql_table_song), null, null); // "", values
        } catch (SQLiteException e) {
            Log.e(context.getString(R.string.db_manager_dao), "Error delete list songs: ", e);
        } finally {
            if (db.isOpen()){
                db.close();
            }
        }

    }

    @Override
    public void saveSongsList(ArrayList<Song> list) {
//        deleteSongsList(); // TODO do we need to delete every time the list?
        for(Song song : list){
            saveSong(song);
        }
    }

    @Override
    public void saveSong(Song song) {
        Log.i(context.getString(R.string.db_manager_dao), "saveSong: Song id = " + song.getId());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long insert;
//        if(db == null){
//            return;
//        }
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(context.getString(R.string.sql_column_song_idsong), song.getId());
        dataToInsert.put(context.getString(R.string.sql_column_song_title), song.getTitle());
        dataToInsert.put(context.getString(R.string.sql_column_song_artist), song.getArtist());
        dataToInsert.put(context.getString(R.string.sql_column_song_price), song.getPrice());
        dataToInsert.put(context.getString(R.string.sql_column_song_coverURL), song.getCoverURL());


//        if(!db.isOpen()){
//            dbHelper.openDB(db);
//            db = dbHelper.getWritableDatabase();
//        }
//        if(db == null){
//            return;
//        }
        try {

            insert = db.insertWithOnConflict(context.getString(R.string.sql_table_song), null, dataToInsert,
                    SQLiteDatabase.CONFLICT_IGNORE);
            if (insert < 0) {
                Log.e(context.getString(R.string.db_manager_dao), "saveSong: Insert fail" + song.getTitle());
            }
        } catch (SQLiteException e) {
            Log.e(context.getString(R.string.db_manager_dao), "Error saveSong: ", e);
        } finally {
            if (db.isOpen()){
                db.close();
            }
        }
    }
}
