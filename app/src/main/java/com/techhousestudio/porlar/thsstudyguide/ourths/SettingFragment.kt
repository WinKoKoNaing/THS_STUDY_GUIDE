package com.techhousestudio.porlar.thsstudyguide.ourths


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.techhousestudio.porlar.thsstudyguide.R
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_setting, rootKey)
        val theme = findPreference<ListPreference>("theme")
        theme?.setOnPreferenceChangeListener { _, newValue ->
            Timber.i("$newValue Value")
            when (newValue) {
                "1" -> setDefaultNightMode(MODE_NIGHT_NO)
                "2" -> setDefaultNightMode(MODE_NIGHT_YES)
                "3" -> setDefaultNightMode(MODE_NIGHT_AUTO_BATTERY)
            }
            return@setOnPreferenceChangeListener true
        }
        val contact = findPreference<Preference>("contact")
        contact?.setOnPreferenceClickListener {

            return@setOnPreferenceClickListener true
        }

    }


}
