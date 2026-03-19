package com.example.ecofriendly.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecofriendly.system.InsetsHelper;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract @LayoutRes int getLayoutId();
    protected abstract @IdRes int getRootViewId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        View root = findViewById(getRootViewId());
        if (root != null) {
            InsetsHelper.applySystemBarsInsets(root);
        }
    }
}