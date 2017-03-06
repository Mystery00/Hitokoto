package com.mystery0.hitokoto;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;

public class SettingsFragment extends PreferenceFragment
{
    private static final String TAG = "SettingsFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perferences);
    }
}
