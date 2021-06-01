package com.dong.circlelive.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.dong.circlelive.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}