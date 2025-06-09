package com.example.lethimcook.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import com.example.lethimcook.Model.Ingredient;

public class IngredientRepository {
    private IngredientDbHelper dbHelper;
    private ExecutorService executor;
    private Handler mainHandler;

    public interface LoadIngredientsCallback {
        void onIngredientsLoaded(List<Ingredient> ingredientList);
    }

    public IngredientRepository(Context context) {
        dbHelper = new IngredientDbHelper(context);
        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getAllIngredients(final LoadIngredientsCallback callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<Ingredient> list = new ArrayList<>();

            String[] projection = {
                    IngredientDbHelper.COLUMN_ID,
                    IngredientDbHelper.COLUMN_NAME,
                    IngredientDbHelper.COLUMN_DESC,
                    IngredientDbHelper.COLUMN_IMAGE_RES
            };
            Cursor cursor = db.query(
                    IngredientDbHelper.TABLE_NAME,
                    projection,
                    null, null, null, null, null
            );

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(IngredientDbHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(IngredientDbHelper.COLUMN_NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(IngredientDbHelper.COLUMN_DESC));
                int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(IngredientDbHelper.COLUMN_IMAGE_RES));
                Ingredient ing = new Ingredient(id, name, desc, imageRes);
                list.add(ing);
            }
            cursor.close();
            db.close();

            mainHandler.post(() -> callback.onIngredientsLoaded(list));
        });
    }
}
