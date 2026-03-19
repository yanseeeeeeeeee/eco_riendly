package com.example.ecofriendly.view;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import com.example.ecofriendly.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Класс для работы нижнего навигационного меню, загрузка фрагментов
 */

public class FragmentContainer extends BaseActivity {
    BottomNavigationView bottomMenu;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_container;
    }

    @Override
    protected int getRootViewId() {
        return R.id.root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        bottomMenu = findViewById(R.id.bottomNavigationView);

        //загружаем дефолтный фрагмент в контейнер
        if (savedInstanceState == null) {
            LoadFragment(new Home());
        }

        // обработка нажатий по  меню
        bottomMenu.setOnItemSelectedListener(menuItem -> {
            Fragment fragment = null;
            int id = menuItem.getItemId(); // берем элемент менюшки по айдишнику

            if (id == R.id.home) {
                fragment = new Home();
            } else if (id == R.id.notes) {
                fragment = new Progress();
            } else if (id == R.id.profile) {
                fragment = new Profile();
            }
            LoadFragment(fragment);
            return true;
        });
    }

    /**
     * Метод для подгрузки экранов
     * @param fragment
     */
    private void LoadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
