package com.example.lethimcook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.lethimcook.R;

public class IngredientDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ingredients.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "ingredient";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_IMAGE_RES = "image_res";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESC + " TEXT, " +
                    COLUMN_IMAGE_RES + " INTEGER);";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public IngredientDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

// Pre‐populate with some ingredients (id, name, desc, imageRes)
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (1, 'Turmeric', 'Yellow root spice, used in curries', " +
                R.drawable.turmeric + ");");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (2, 'Ginger', 'Aromatic root, used fresh or dried', " +
                R.drawable.ginger + ");");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (3, 'Cumin', 'Ground seeds used in many cuisines', " +
                R.drawable.cumin + ");");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (4, 'Paprika', 'Red pepper spice', " +
                R.drawable.paprika + ");");
// … add more if you like
        // … add more if you like
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
}
