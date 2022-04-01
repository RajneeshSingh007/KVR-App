package com.kvr.user.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class AppPref(private val context: Context) {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREF_NAME)

    suspend fun putString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.datastore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    suspend fun getString(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences: Preferences = context.datastore.data.first()
        return preferences[dataStoreKey]
    }
}
