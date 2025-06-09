package com.example.lethimcook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lethimcook.Model.Meal;

import java.util.ArrayList;
import java.util.List;

public class FavoritesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 2; // Increased version for schema update

    public static final String TABLE_NAME = "favorites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MEAL_ID = "meal_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_INSTRUCTIONS = "instructions";
    public static final String COLUMN_THUMB_URL = "thumb_url";
    public static final String COLUMN_YOUTUBE_URL = "youtube_url";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_MEASURES = "measures";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MEAL_ID + " TEXT UNIQUE, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_AREA + " TEXT, " +
                    COLUMN_INSTRUCTIONS + " TEXT, " +
                    COLUMN_THUMB_URL + " TEXT, " +
                    COLUMN_YOUTUBE_URL + " TEXT, " +
                    COLUMN_INGREDIENTS + " TEXT, " +
                    COLUMN_MEASURES + " TEXT);";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    // Add a meal to favorites with all its details
    public long addFavorite(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_ID, meal.getIdMeal());
        values.put(COLUMN_NAME, meal.getName());
        values.put(COLUMN_CATEGORY, meal.getCategory());
        values.put(COLUMN_AREA, meal.getArea());
        values.put(COLUMN_INSTRUCTIONS, meal.getInstructions());
        values.put(COLUMN_THUMB_URL, meal.getThumbnailUrl());
        values.put(COLUMN_YOUTUBE_URL, meal.getYoutubeUrl());

        // Store ingredients and measures as JSON strings
        StringBuilder ingredientsBuilder = new StringBuilder();
        StringBuilder measuresBuilder = new StringBuilder();

        for (int i = 1; i <= 20; i++) {
            String ingredient = meal.getIngredient(i);
            String measure = meal.getMeasure(i);

            if (ingredient != null && !ingredient.trim().isEmpty() && !ingredient.equals("null")) {
                if (ingredientsBuilder.length() > 0) {
                    ingredientsBuilder.append("|||");
                    measuresBuilder.append("|||");
                }
                ingredientsBuilder.append(ingredient);
                measuresBuilder.append(measure != null ? measure : "");
            }
        }

        values.put(COLUMN_INGREDIENTS, ingredientsBuilder.toString());
        values.put(COLUMN_MEASURES, measuresBuilder.toString());

        long newRowId = db.insertWithOnConflict(
                TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        return newRowId;
    }

    // Check if a meal is in favorites
    public boolean isFavorite(String mealId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { COLUMN_MEAL_ID };
        String selection = COLUMN_MEAL_ID + " = ?";
        String[] selectionArgs = { mealId };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return isFavorite;
    }

    // Remove a meal from favorites
    public boolean removeFavorite(String mealId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = COLUMN_MEAL_ID + " = ?";
        String[] selectionArgs = { mealId };

        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();

        return deletedRows > 0;
    }

    // Get all favorite meals with complete details
    public List<Meal> getAllFavorites() {
        List<Meal> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_MEAL_ID,
                COLUMN_NAME,
                COLUMN_CATEGORY,
                COLUMN_AREA,
                COLUMN_INSTRUCTIONS,
                COLUMN_THUMB_URL,
                COLUMN_YOUTUBE_URL,
                COLUMN_INGREDIENTS,
                COLUMN_MEASURES
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC");

        while (cursor.moveToNext()) {
            Meal meal = new Meal();
            meal.setIdMeal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEAL_ID)));
            meal.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            meal.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
            meal.setArea(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AREA)));
            meal.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)));
            meal.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMB_URL)));
            meal.setYoutubeUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_YOUTUBE_URL)));

            // Parse ingredients and measures
            String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS));
            String measures = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEASURES));

            if (ingredients != null && measures != null) {
                String[] ingredientArray = ingredients.split("\\|\\|\\|");
                String[] measureArray = measures.split("\\|\\|\\|");

                for (int i = 0; i < ingredientArray.length && i < 20; i++) {
                    meal.setIngredient(i + 1, ingredientArray[i]);
                    meal.setMeasure(i + 1, i < measureArray.length ? measureArray[i] : "");
                }
            }

            favorites.add(meal);
        }

        cursor.close();
        db.close();

        return favorites;
    }

    // Get a specific favorite meal by ID with full details
    public Meal getFavoriteMealById(String mealId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_MEAL_ID,
                COLUMN_NAME,
                COLUMN_CATEGORY,
                COLUMN_AREA,
                COLUMN_INSTRUCTIONS,
                COLUMN_THUMB_URL,
                COLUMN_YOUTUBE_URL,
                COLUMN_INGREDIENTS,
                COLUMN_MEASURES
        };

        String selection = COLUMN_MEAL_ID + " = ?";
        String[] selectionArgs = { mealId };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Meal meal = null;

        if (cursor.moveToFirst()) {
            meal = new Meal();
            meal.setIdMeal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEAL_ID)));
            meal.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            meal.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
            meal.setArea(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AREA)));
            meal.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)));
            meal.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMB_URL)));
            meal.setYoutubeUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_YOUTUBE_URL)));

            // Parse ingredients and measures
            String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS));
            String measures = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEASURES));

            if (ingredients != null && measures != null) {
                String[] ingredientArray = ingredients.split("\\|\\|\\|");
                String[] measureArray = measures.split("\\|\\|\\|");

                for (int i = 0; i < ingredientArray.length && i < 20; i++) {
                    meal.setIngredient(i + 1, ingredientArray[i]);
                    meal.setMeasure(i + 1, i < measureArray.length ? measureArray[i] : "");
                }
            }
        }

        cursor.close();
        db.close();

        return meal;
    }
}