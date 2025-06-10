package com.example.lethimcook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lethimcook.Model.CookingMistake;
import com.example.lethimcook.R;

import java.util.ArrayList;
import java.util.List;

public class MistakesDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "MistakesDbHelper";
    private static final String DATABASE_NAME = "cooking_mistakes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "mistakes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_IMAGE_RES = "image_resource";
    public static final String COLUMN_IMAGE_URL = "image_url";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESC + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_IMAGE_RES + " INTEGER, " +
                    COLUMN_IMAGE_URL + " TEXT);";

    public MistakesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        populateDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void populateDatabase(SQLiteDatabase db) {
        // Category: Seasoning
        addMistake(db, "Over-salting", "Adding too much salt ruins the dish and can't be easily fixed. Add salt gradually and taste as you go.", "Seasoning", R.drawable.oversalting);
        addMistake(db, "Under-seasoning", "Not using enough seasoning leads to bland food. Season in layers throughout cooking for depth of flavor.", "Seasoning", 0);
        addMistake(db, "Timing of seasoning", "Adding herbs too early can cause them to lose flavor or become bitter. Add delicate herbs toward the end of cooking.", "Seasoning", 0);
        addMistake(db, "Not tasting while cooking", "Failing to taste as you cook prevents adjusting seasoning appropriately. Taste frequently during the cooking process.", "Seasoning", 0);
        addMistake(db, "Relying on single flavor", "Using only salt instead of balancing all flavor elements. Remember to balance sweet, salty, sour, bitter, and umami.", "Seasoning", 0);
        addMistake(db, "Incorrect spice combinations", "Using spices that clash rather than complement each other. Learn which spice combinations work well together.", "Seasoning", 0);
        addMistake(db, "Old spices", "Using stale spices that have lost potency. Replace ground spices every 6-12 months for best flavor.", "Seasoning", 0);
        addMistake(db, "Improper spice storage", "Storing spices near heat or light which degrades them quickly. Store in cool, dark places in airtight containers.", "Seasoning", 0);
        addMistake(db, "Not blooming spices", "Adding ground spices directly without blooming them in fat first. Blooming releases more flavor compounds.", "Seasoning", 0);
        addMistake(db, "Overseasoning to compensate", "Adding more seasoning to fix an already over-seasoned dish. Instead, try adding more base ingredients or acid to balance.", "Seasoning", 0);

        // Category: Cooking Temperature
        addMistake(db, "Cooking too hot", "Using too high heat can burn food exteriors while leaving interiors raw. Adjust heat based on what you're cooking.", "Temperature", 0);
        addMistake(db, "Cooking too cold", "Using too low heat can result in steamed rather than seared food, and longer cooking times. Ensure proper preheating.", "Temperature", 0);
        addMistake(db, "Cold ingredients", "Adding cold ingredients to hot pans causes temperature drops and uneven cooking. Bring ingredients to room temperature first.", "Temperature", 0);
        addMistake(db, "Overcrowded pan", "Adding too many items to a pan lowers temperature and causes steaming instead of browning. Cook in batches if necessary.", "Temperature", 0);
        addMistake(db, "Impatience with preheating", "Not properly preheating pans or ovens leads to inconsistent results. Always preheat to the specified temperature.", "Temperature", 0);
        addMistake(db, "Inconsistent heat", "Constantly adjusting temperature during cooking. Maintain consistent heat for even cooking.", "Temperature", 0);
        addMistake(db, "Ignoring carryover cooking", "Not accounting for food continuing to cook after removing from heat. Remove food slightly before desired doneness.", "Temperature", 0);
        addMistake(db, "Wrong cooking vessel", "Using pans that don't conduct or maintain heat properly for the cooking method. Choose appropriate cookware for the task.", "Temperature", 0);
        addMistake(db, "Not resting meat", "Cutting meat immediately after cooking, losing juices. Allow proper resting time based on size.", "Temperature", 0);
        addMistake(db, "Neglecting thermometers", "Relying on visual cues instead of using a thermometer for accuracy. Invest in a good thermometer for precise cooking.", "Temperature", 0);

        // Category: Knife Skills
        addMistake(db, "Dull knives", "Using dull knives requires more force and leads to accidents. Keep knives properly sharpened.", "Knife Skills", 0);
        addMistake(db, "Inconsistent cutting", "Cutting ingredients in different sizes causes uneven cooking. Practice uniform cutting for consistent results.", "Knife Skills", 0);
        addMistake(db, "Improper knife grip", "Holding the knife incorrectly reduces control and efficiency. Learn proper pinch grip technique.", "Knife Skills", 0);
        addMistake(db, "Unstable cutting surface", "Cutting on unstable or slippery surfaces increases accident risk. Use a stable cutting board with a damp towel underneath.", "Knife Skills", 0);
        addMistake(db, "Wrong knife for task", "Using inappropriate knives for specific tasks. Match the knife to the job for better results.", "Knife Skills", 0);
        addMistake(db, "Improper claw technique", "Not using the 'claw' hand position to protect fingers while cutting. Curl fingers under with knuckles guiding the blade.", "Knife Skills", 0);
        addMistake(db, "Sawing instead of slicing", "Using a sawing motion instead of smooth slicing or chopping. Let the knife do the work with proper technique.", "Knife Skills", 0);
        addMistake(db, "Cutting towards yourself", "Directing knife edge toward your body while cutting. Always cut away from yourself.", "Knife Skills", 0);
        addMistake(db, "Poor knife maintenance", "Not cleaning or storing knives properly. Hand wash and store knives safely in a block or on a magnetic strip.", "Knife Skills", 0);
        addMistake(db, "Rushing knife work", "Trying to cut too quickly before developing proper technique. Focus on precision before speed.", "Knife Skills", 0);

        // Category: Food Preparation
        addMistake(db, "Not mise en place", "Starting to cook without preparing and organizing ingredients first. Set up mise en place to cook efficiently.", "Preparation", 0);
        addMistake(db, "Improper meat thawing", "Thawing meat at room temperature promotes bacterial growth. Thaw in refrigerator or use cold water method.", "Preparation", 0);
        addMistake(db, "Not drying ingredients", "Not patting ingredients dry before cooking, especially for searing. Dry ingredients properly for better browning.", "Preparation", 0);
        addMistake(db, "Skipping resting time", "Not allowing proteins to rest after cooking. Rest meat to redistribute juices for better texture and flavor.", "Preparation", 0);
        addMistake(db, "Neglecting ingredient quality", "Using poor quality ingredients and expecting great results. Quality ingredients make a significant difference.", "Preparation", 0);
        addMistake(db, "Improper vegetable washing", "Not properly washing produce to remove contaminants. Wash all produce thoroughly before use.", "Preparation", 0);
        addMistake(db, "Overcomplicated recipes", "Attempting overly complex recipes without mastering basics. Build skills progressively with appropriate challenges.", "Preparation", 0);
        addMistake(db, "Cross-contamination", "Using the same surfaces for raw meat and ready-to-eat foods. Use separate cutting boards and wash hands frequently.", "Preparation", 0);
        addMistake(db, "Improper oil storage", "Exposing oils to light and air, causing rancidity. Store in cool, dark places and use appropriate oils for different heat levels.", "Preparation", 0);
        addMistake(db, "Not reading recipe thoroughly", "Starting a recipe without reading it completely first. Read through recipes entirely before beginning.", "Preparation", 0);
    }

    private void addMistake(SQLiteDatabase db, String name, String description, String category, int imageRes) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESC, description);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_IMAGE_RES, imageRes);

        // Generate a generic image URL if needed
        String formattedName = name.replace(" ", "-").toLowerCase();
        String imageUrl = "https://example.com/cooking-mistakes/" + formattedName + ".jpg";
        values.put(COLUMN_IMAGE_URL, imageUrl);

        db.insert(TABLE_NAME, null, values);
    }

    public List<CookingMistake> getMistakesByCategory(String category, int limit) {
        List<CookingMistake> mistakeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = category != null ? COLUMN_CATEGORY + " = ?" : null;
        String[] selectionArgs = category != null ? new String[]{category} : null;
        String limitClause = limit > 0 ? String.valueOf(limit) : null;

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_NAME,
                limitClause
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC));
            String cat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
            int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_RES));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL));

            CookingMistake mistake = new CookingMistake(id, name, desc, cat, imageRes, imageUrl);
            mistakeList.add(mistake);
        }

        cursor.close();
        return mistakeList;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                true, // distinct
                TABLE_NAME,
                new String[]{COLUMN_CATEGORY},
                null,
                null,
                COLUMN_CATEGORY,
                null,
                COLUMN_CATEGORY + " ASC",
                null
        );

        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
            categories.add(category);
        }

        cursor.close();
        return categories;
    }
}
