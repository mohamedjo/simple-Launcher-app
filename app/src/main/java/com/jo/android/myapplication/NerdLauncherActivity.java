package com.jo.android.myapplication;

import androidx.fragment.app.Fragment;

public class NerdLauncherActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
