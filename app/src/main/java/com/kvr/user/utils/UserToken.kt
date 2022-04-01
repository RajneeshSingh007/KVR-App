package com.kvr.user.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class UserToken(val context: Context) {

    val Context.datastore: DataStore<Preferences> by preferencesDataStore("pref")

    companion object {
        val TOKEN = stringPreferencesKey("USER_TOKEN")
    }

    suspend fun storeUserToken(token: String){
        context.datastore.edit {
            it[TOKEN]= token
        }
    }

    fun getUserToken() = context.datastore.data.map {
        it[TOKEN]?: ""
    }
}