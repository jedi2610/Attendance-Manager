package com.attendancemanager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import github.com.st235.lib_expandablebottombar.ExpandableBottomBar;
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ExpandableBottomBar bottomBar;

    private Handler handler;
    private Runnable hideBottomBar = new Runnable() {
        @Override
        public void run() {
            bottomBar.hide();
        }
    };

    private static final String TAG = "MainActivity";

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TimeTableFragment();
                case 1:
                    return new HomeFragment();
                case 2:
                    return new SettingsFragment();
            }
            throw new IllegalStateException("Unexpected Position" + position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            Context context = MainActivity.this;
            switch (position) {
                case 0:
                    return context.getString(R.string.time_table);
                case 1:
                    return context.getString(R.string.home);
                case 2:
                    return context.getString(R.string.settings);
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Find views */
        bottomBar = findViewById(R.id.bottom_bar);
        viewPager = findViewById(R.id.view_pager);

        initialSetup();
        setupViewPager();
    }

    private void initialSetup() {
        /* All initializing stuff */

        handler = new Handler();
        handler.postDelayed(hideBottomBar, 3000);

        Window window = getWindow();
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    private void setupViewPager() {
        /* Initializes the view pager */

        /* https://proandroiddev.com/the-seven-actually-10-cardinal-sins-of-android-development-491d2f64c8e0
        3rd point */
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter(viewPagerAdapter);
        bottomBar.select(R.id.homeFragment);
        viewPager.setCurrentItem(1);

        /* Listen for page changes and select change the bottom bar accordingly */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomBar.select(R.id.timeTableFragment);
                        break;

                    case 1:
                        bottomBar.select(R.id.homeFragment);
                        break;

                    case 2:
                        bottomBar.select(R.id.settingsFragment);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /* Listen for item selections and change the view pager accordingly */
        bottomBar.setOnItemSelectedListener((view, item) -> {
            Log.d(TAG, "onCreate: " + item.getItemId());
            switch (item.getItemId()) {
                case R.id.timeTableFragment:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.homeFragment:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.settingsFragment:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return null;
        });
    }

    @Override
    public void onUserInteraction() {
        /* Hide the bottom bar after 5 seconds of inactivity */

        super.onUserInteraction();
        handler.removeCallbacks(hideBottomBar);
        bottomBar.show();
        handler.postDelayed(hideBottomBar, 3000);
    }
}