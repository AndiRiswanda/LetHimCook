package com.example.lethimcook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lethimcook.R;

public class IngredientDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ingredients.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "ingredients";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_IMAGE_RES = "image_res";
    public static final String COLUMN_IMAGE_URL = "image_url"; // Add this for remote images

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESC + " TEXT, " +
                    COLUMN_IMAGE_RES + " INTEGER, " +
                    COLUMN_IMAGE_URL + " TEXT);";

    private Context context;

    public IngredientDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        populateInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void populateInitialData(SQLiteDatabase db) {
        // Add common spices with their resource IDs (if available)
        addIngredient(db, "Ginger", "A flowering plant whose rhizome is widely used as a spice.", R.drawable.ginger);
        addIngredient(db, "Cumin", "A flowering plant in the family Apiaceae, native to the region from the eastern Mediterranean to East India.", 0);
        addIngredient(db, "Turmeric", "A rhizomatous herbaceous perennial plant of the ginger family.", 0);
        addIngredient(db, "Cardamom", "A spice made from the seeds of several plants in the genera Elettaria and Amomum.", 0);
        addIngredient(db, "Cinnamon", "A spice obtained from the inner bark of several tree species.", 0);
        // Add more spices
    }

    private void addIngredient(SQLiteDatabase db, String name, String description, int imageRes) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESC, description);
        values.put(COLUMN_IMAGE_RES, imageRes);

        // Set the URL for remote images when we don't have a local resource
        if (imageRes == 0) {
            String formattedName = name.replace(" ", "%20");
            String imageUrl = "https://www.themealdb.com/images/ingredients/" + formattedName + "-Small.png";
            values.put(COLUMN_IMAGE_URL, imageUrl);
        }

        db.insert(TABLE_NAME, null, values);
    }
}