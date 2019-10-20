package com.wNagiesEducationalCenterj_9905.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.wNagiesEducationalCenterj_9905.BuildConfig
import com.wNagiesEducationalCenterj_9905.R
import kotlinx.android.synthetic.main.settings_activity.*



class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(toolbar)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_settings_socket, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onDestroy() {
            super.onDestroy()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String) {
            val pref = findPreference<Preference>(key)
            if (null != pref) {
                val value = sharedPreferences?.getString(pref.key, "")
                setPreferenceSummary(pref, value)
            }

        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val sharedPreferences = preferenceScreen.sharedPreferences
            val preferenceScreen = preferenceScreen
            val count = preferenceScreen.preferenceCount

            for (i in 0 until count) {
                val pref = preferenceScreen.getPreference(i)
                val value = sharedPreferences.getString(pref.key, "")
                setPreferenceSummary(pref, value)
            }
            val listPref = findPreference<ListPreference>(getString(R.string.pref_fetch_option_key))
            val prefAppText = findPreference<Preference>(getString(R.string.pref_app_version_key))
            prefAppText?.title = BuildConfig.VERSION_NAME
            listPref?.summary = listPref?.entry
        }

        private fun setPreferenceSummary(pref: Preference?, value: String?) {
            if (pref is ListPreference) {
                val listPreference: ListPreference = pref
                val prefIndex = listPreference.findIndexOfValue(value)
                if (prefIndex >=0) {
                    listPreference.summary = listPreference.entries[prefIndex]
                }
            }

        }


    }
}