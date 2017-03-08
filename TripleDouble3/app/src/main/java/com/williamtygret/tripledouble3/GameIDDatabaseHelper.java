package com.williamtygret.tripledouble3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by williamtygret on 1/27/17.
 */
public class GameIDDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "GameIDLibrary.db";
    public static final String GAMEID_TABLE_NAME = "GAMEID_LIBRARY";

    public static final String COL_ID = "_id";
    public static final String COL_DATE = "DATE";
    public static final String COL_GAMEID = "GAMEID";
    public static final String COL_HOME_AWAY = "HOMEAWAY";



    private Context mHelperContext;

    public GameIDDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mHelperContext = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    public static final String [] GAMEID_COLUMNS = {COL_ID,COL_DATE,COL_GAMEID,COL_HOME_AWAY};

    //creating tables
    private static final String CREATE_GAMEID_TABLE =
            "CREATE TABLE"+GAMEID_TABLE_NAME+
                    "("+
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COL_DATE+ " TEXT, "+
                    COL_GAMEID + " TEXT, " +
                    COL_HOME_AWAY + " TEXT, )";

    //make db accessible in a singleton
    private static GameIDDatabaseHelper instance;

    public static GameIDDatabaseHelper getInstance(Context context){
        if(instance == null){
            instance = new GameIDDatabaseHelper(context);
        }
        return instance;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GAMEID_TABLE);
        Log.d("GameIDDatabaseHelper", "SQL"+CREATE_GAMEID_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+GAMEID_TABLE_NAME);
        onCreate(db);
    }

    public Cursor getGameIDList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GAMEID_TABLE_NAME,// a. table
                GAMEID_COLUMNS, // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        return cursor;
    }

    //method to insert gameid data
    public void insertGameID(int id, String date, String gameID, String homeaway){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_id", id);
        values.put("DATE", date);
        values.put("GAME_ID", gameID);
        values.put("HOMEAWAY", homeaway);

        db.insert("GAMEID_LIBRARY", null, values);
        Log.d("PokemonDatabaseHelper","insert being called from insertPokemon");
    }

    //here is where all the info goes
    public void insertAllGameID(){
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(1,"29-Jan-2017","f07ecf10-a55a-4972-a372-488b754996de","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(2,"31-Jan-2017","fb60c3d8-3feb-4ca7-8cd5-045f2e6a4c22","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(3,"01-Feb-2017","a9e3a626-bcc0-4943-ba12-179274c053fb","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(4,"03-Feb-2017","d568fa4d-5afd-417f-b150-ae5a84d4bce8","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(5,"05-Feb-2017","f6d5c066-6107-4f38-850a-8b24778c72e0","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(6,"06-Feb-2017","3c89469a-14b8-48d2-ac5c-4f7abdf34b1a","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(7,"09-Feb-2017","7990de27-5bd1-427c-9ee4-c79b49b7fd35","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(8,"11-Feb-2017","ac902735-f9c9-470a-aebd-9927d2883e94","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(9,"13-Feb-2017","0db34af3-6032-4657-9ad7-4e56846966f9","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(10,"15-Feb-2017","4b776ea3-1725-4f66-a79b-6095f2fd2567","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(11,"24-Feb-2017","bfe8bc7f-93bd-4873-9876-47cbdfb869c7","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(12,"26-Feb-2017","83090e04-9e61-4ead-8be9-2d42cb1e80bd","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(13,"28-Feb-2017","d1329579-910c-41c5-a207-bb82e179da80","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(14,"02-Mar-2017","77cf8953-e49b-4e5e-b292-8fc5df88da24","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(15,"03-Mar-2017","bbb15824-fcb9-430a-9273-8c59fbcdf487","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(16,"05-Mar-2017","63561b89-5357-4b39-975a-120654810905","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(17,"07-Mar-2017","88528057-11ab-4ae9-9c89-12e0e30d3ac9","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(18,"09-Mar-2017","c5d68224-d044-46c3-97c4-1220396b7e62","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(19,"11-Mar-2017","ecefc021-ce8a-474d-aebd-3f73639ac28e","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(20,"14-Mar-2017","82e93bf4-e61f-4047-9366-a91682a52ead","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(21,"16-Mar-2017","bf233e82-d1f8-4e99-8c41-c3fc68fe2ae1","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(22,"18-Mar-2017","09e8bcfb-e88e-4af4-817c-626d6a96cba9","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(23,"20-Mar-2017","f7d82d03-c7e5-4a70-ba03-25b06cf86d29","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(24,"22-Mar-2017","7896f54b-8027-495f-b49a-565361366052","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(25,"26-Mar-2017","d4fb86ff-416d-4db5-9486-a7a92213f7a5","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(26,"27-Mar-2017","3df84225-9c20-41b6-879f-d09ea8d80f7f","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(27,"29-Mar-2017","3eb9c54a-8155-43b8-adc1-3c07ec657d1a","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(28,"31-Mar-2017","a28cca52-0dbd-4952-8f42-f48891414fdc","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(29,"02-Apr-2017","4db687e5-558d-47ae-b7a8-3d5e86257f36","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(30,"04-Apr-2017","b4b3d924-41fb-42f5-a7de-4997f277f657","home");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(31,"05-Apr-2017","41ce352a-8546-44a3-b4c8-714c5a1d2e07","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(32,"07-Apr-2017","1c4ba5e9-1899-43c6-9ae1-eb09ea785e7f","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(33,"09-Apr-2017","5e77bf4d-c249-4828-9f95-e5089cdcaebf","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(34,"11-Apr-2017","4203d831-84c8-44b2-aec6-24b2fed2874a","away");
        GameIDDatabaseHelper.getInstance(mHelperContext).insertGameID(35,"12-Apr-2017","4e55dde9-0d75-4760-a036-b3a119519b57","home");


    }

    //method to search pokemon
    public Cursor searchDate(String query){

        Log.d("searchplaces", "did we get here?");
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("searchplaces", "did we get here also?");

        Cursor cursor = db.query(GAMEID_TABLE_NAME, // a. table
                GAMEID_COLUMNS, // b. column names
                COL_DATE+" LIKE ?", // c. selections
                new String[]{"%"+query+"%"}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        return cursor;
    }

    public String getGameID(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(GAMEID_TABLE_NAME, // a. table
                new String[] {COL_GAMEID}, // b. column names
                COL_ID + "= ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if(cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COL_GAMEID));
        }else{
            return "No Game Today";
        }
    }

    public String getHomeAway(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(GAMEID_TABLE_NAME, // a. table
                new String[] {COL_HOME_AWAY}, // b. column names
                COL_ID + "= ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if(cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COL_HOME_AWAY));
        }else{
            return "No Game Today";
        }
    }


}
