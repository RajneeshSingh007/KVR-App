package com.kvr.user.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> GsonExtention(json: String) = Helpers.getGSON().fromJson<T>(json, object : TypeToken<T>() {}.type)
