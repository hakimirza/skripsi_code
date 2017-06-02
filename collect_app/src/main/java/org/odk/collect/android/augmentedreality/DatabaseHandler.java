package org.odk.collect.android.augmentedreality;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 3/14/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "skripsi";
    private static final String TABLE_BANGUNAN = "bangunan_sensus";
    private static final String TABLE_DOWNLOAD = "download";

    private static final String ID= "id";
    private static final String NAMA_KRT = "nama_krt";
    private static final String LAT = "gps_lat";
    private static final String LONG = "gps_long";
    private static final String PATH_FOTO = "path_foto";

    private static final String FORM_PATH_FILE = "form_path_file";
    private static final String INSTANCE_PATH_FILE = "instance_path_file";
    private static final String MEDIA_PATH_FILE = "media_path_file";
    private static final String UUID = "uuid";


    private static final String SEND = "send";
    private static final String FALSE = "false";
    public DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_GPS_TABLE = "CREATE TABLE "+ TABLE_BANGUNAN+" ("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +NAMA_KRT+" TEXT,"
                +LAT+" TEXT, "
                +LONG+" TEXT, "
                +PATH_FOTO+" TEXT,"
                +SEND+" TEXT)";

        String CREATE_DOWNLOAD_TABLE = "CREATE TABLE "+ TABLE_DOWNLOAD+" ("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +UUID+" TEXT,"
                +FORM_PATH_FILE+" TEXT,"
                +INSTANCE_PATH_FILE+" TEXT,"
                +MEDIA_PATH_FILE+" TEXT)";
        db.execSQL(CREATE_GPS_TABLE);
        db.execSQL(CREATE_DOWNLOAD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BANGUNAN);
        db.execSQL("DROP TABBE IF EXISTS "+TABLE_DOWNLOAD);
        onCreate(db);
    }

    public void insertTabel(Bangunan bs){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values= new ContentValues();
//            values.put(NAMA_KRT,bs.getNamaKRT());
            values.put(LAT,bs.getLat());
            values.put(LONG,bs.getLon());
            values.put(PATH_FOTO,bs.getPathFoto());
            values.put(SEND,FALSE);
            db.insert(TABLE_BANGUNAN,null,values);
        }catch (Exception e ){
            Log.d("insert_gps",e.toString());
        }
    }
    public ArrayList<Bangunan> getAll(){
        ArrayList<Bangunan> array = new ArrayList<>();
        Bangunan bs;
//        try{
        SQLiteDatabase sql = this.getReadableDatabase();
        String query = "SELECT "+ID+","+NAMA_KRT+","+LAT+","+LONG+","+PATH_FOTO+" FROM "+TABLE_BANGUNAN;
        Cursor c= sql.rawQuery(query,null);

        if(c.moveToFirst()){
            do{
                bs = new Bangunan();
                bs.setId(c.getInt(c.getColumnIndex(ID)));
//                bs.setNamaKRT(c.getString(c.getColumnIndex(NAMA_KRT)));
                bs.setLat(c.getDouble(c.getColumnIndex(LAT)));
                bs.setLon(c.getDouble(c.getColumnIndex(LONG)));
                bs.setPathFoto(c.getString(c.getColumnIndex(PATH_FOTO)));
                array.add(bs);
            }while(c.moveToNext());
        }else{
            Log.d("getGps","not move to first");
            return array;
        }
//        }catch (Exception e){
//            Log.d("error1",e.toString());
//        }
        return array ;
    }

//    public ArrayList<Tabel> getNotSend(){
//        ArrayList<Tabel> array = new ArrayList<>();
//        Tabel tabel;
////        try{
//        SQLiteDatabase sql = this.getReadableDatabase();
//        String query = "SELECT "+ID+","+NAMA_TEMPAT+","+GPS_LAT+","+GPS_LONG+","+NETWORK_LAT+","+NETWORK_LONG+","+BEST_LAT+","+BEST_LONG+" FROM "+TABLE+" WHERE "+SEND+"='"+FALSE+"'";
//        Cursor c= sql.rawQuery(query,null);
//
//        if(c.moveToFirst()){
//            do{
//                tabel = new Tabel();
//                tabel.setId(c.getInt(c.getColumnIndex(ID)));
//                tabel.setNamaTempat(c.getString(c.getColumnIndex(NAMA_TEMPAT)));
//                tabel.setGpsLat(c.getDouble(c.getColumnIndex(GPS_LAT)));
//                tabel.setGpsLong(c.getDouble(c.getColumnIndex(GPS_LONG)));
//                tabel.setNetLat(c.getDouble(c.getColumnIndex(NETWORK_LAT)));
//                tabel.setNetLong(c.getDouble(c.getColumnIndex(NETWORK_LONG)));
//                tabel.setBestLat(c.getDouble(c.getColumnIndex(BEST_LAT)));
//                tabel.setBestLong(c.getDouble(c.getColumnIndex(BEST_LONG)));
//                array.add(tabel);
//            }while(c.moveToNext());
//        }else{
//            Log.d("getGps","not move to first");
//            return array;
//        }
////        }catch (Exception e){
////            Log.d("error1",e.toString());
////        }
//        return array ;
//    }
//
//    public int getJumlahData() {
//        String countQuery = "SELECT  * FROM " + TABLE;
//        int cnt=0;
//        try{
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(countQuery, null);
//            cnt = cursor.getCount();
//            cursor.close();
//        }catch (Exception  e){
//            Log.d("error2",e.toString());
//        }
//        return cnt;
//    }

//    public void insertTabel(Download download){
//        try{
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues values= new ContentValues();
//            values.put(FORM_PATH_FILE,download.getFormPath());
//            v
//            values.put(PATH_FOTO,bs.getPathFoto());
//            values.put(SEND,FALSE);
//            db.insert(TABLE_BANGUNAN,null,values);
//        }catch (Exception e ){
//            Log.d("insert_gps",e.toString());
//        }
//    }
}
