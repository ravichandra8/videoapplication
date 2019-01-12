package com.mobiotics.videoapplication.modal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MydatabaseAdapter {
    String result;
    sqlhelper sqlhelper;

    static class sqlhelper extends SQLiteOpenHelper {

        private static final String videoTable ="CREATE TABLE videoInfo (id VARCHAR(255),playbackposition INTEGER,videostate BOOLEAN);";

        private static final String name = "mobiotics";
        private static final int version = 1;

        private Context context;

        public sqlhelper(Context context) {
            super(context, name, null, version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {

                db.execSQL(videoTable);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS videoInfo");

            onCreate(db);

        }
    }

    public MydatabaseAdapter(Context context) {
        this.sqlhelper = new sqlhelper(context);
    }

    public void insertVideoPosition(String id,long videoDuration,boolean videoState) {
        SQLiteDatabase sd = this.sqlhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("playbackposition", videoDuration);
        values.put("videostate", videoState);
        if (sd.query("videoInfo", new String[]{"id"}, "id=?", new String[]{id}, null, null, null).getCount() > 0) {
            sd.update("videoInfo", values, "id=?", new String[]{id});
        }
        else{
         sd.insert("videoInfo", null, values);
        }
    }

    public Cursor getParticularVideoPosition(String id)
    {
        SQLiteDatabase sd = this.sqlhelper.getWritableDatabase();
        return sd.query("videoInfo",new String[]{"id","playbackposition","videostate"},"id=?", new String[]{id},null,null,null);

    }

    public void deleteParticularVideo(String id) {
        SQLiteDatabase sd = this.sqlhelper.getWritableDatabase();
        sd.delete("videoInfo", "id LIKE ? ", new String[]{id});
        sd.close();
    }
}
