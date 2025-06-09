package com.example.lethimcook;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.lethimcook.Util.ThemeHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(ThemeHelper.getThemeMode(this));
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Apply custom styling
        toolbar.setBackground(getDrawable(R.drawable.toolbar_background));
        // Set elevation programmatically (optional)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(4f);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {
            // Toggle theme
            int currentThemeMode = ThemeHelper.getThemeMode(this);
            int newThemeMode = (currentThemeMode == ThemeHelper.MODE_DARK) ?
                    ThemeHelper.MODE_LIGHT : ThemeHelper.MODE_DARK;

            // Save and apply the new theme
            ThemeHelper.saveThemeMode(this, newThemeMode);
            ThemeHelper.applyTheme(newThemeMode);

            // Refresh the options menu to update the icon
            invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showThemeSelectionDialog() {
        final String[] themes = {"System Default", "Light", "Dark"};
        final int[] values = {
                ThemeHelper.MODE_SYSTEM,
                ThemeHelper.MODE_LIGHT,
                ThemeHelper.MODE_DARK
        };

        int currentTheme = ThemeHelper.getThemeMode(this);
        int selectedIndex = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == currentTheme) {
                selectedIndex = i;
                break;
            }
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("Choose Theme")
                .setSingleChoiceItems(themes, selectedIndex, (dialog, which) -> {
                    ThemeHelper.saveThemeMode(this, values[which]);
                    ThemeHelper.applyTheme(values[which]);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        if (themeItem != null) {
            // Get current theme mode
            int currentThemeMode = ThemeHelper.getThemeMode(this);
            // Set appropriate icon based on theme
            if (currentThemeMode == ThemeHelper.MODE_DARK) {
                themeItem.setIcon(R.drawable.moon); // Show sun icon when in dark mode
            } else {
                themeItem.setIcon(R.drawable.sun); // Show moon icon when in light mode
            }
            Log.d("ThemeDebug", "Current theme mode: " + currentThemeMode);
            Log.d("ThemeDebug", "Setting icon: " + (currentThemeMode == ThemeHelper.MODE_DARK ? "sun" : "moon"));
        }
        return super.onPrepareOptionsMenu(menu);
    }
}