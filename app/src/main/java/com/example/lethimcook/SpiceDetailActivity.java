package com.example.lethimcook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lethimcook.db.IngredientDbHelper;

public class SpiceDetailActivity extends AppCompatActivity {

    private ImageView imgDetail;
    private TextView tvNameDetail, tvDescDetail;
    private IngredientDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spice_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgDetail = findViewById(R.id.imgDetail);
        tvNameDetail = findViewById(R.id.tvNameDetail);
        tvDescDetail = findViewById(R.id.tvDescDetail);

        dbHelper = new IngredientDbHelper(this);

        int ingId = getIntent().getIntExtra("ING_ID", -1);
        if (ingId != -1) {
            loadIngredientDetail(ingId);
        }

    }
    private void loadIngredientDetail(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                IngredientDbHelper.COLUMN_NAME,
                IngredientDbHelper.COLUMN_DESC,
                IngredientDbHelper.COLUMN_IMAGE_RES
        };
        String selection = IngredientDbHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(IngredientDbHelper.TABLE_NAME,
                projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(IngredientDbHelper.COLUMN_NAME));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(IngredientDbHelper.COLUMN_DESC));
            int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(IngredientDbHelper.COLUMN_IMAGE_RES));

            tvNameDetail.setText(name);
            tvDescDetail.setText(desc);
            imgDetail.setImageResource(imageRes);
            setTitle(name);
        }
        cursor.close();
        db.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}