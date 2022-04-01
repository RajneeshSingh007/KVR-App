package com.kvr.user.network

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.kvr.user.utils.Helpers

class DeSerializer<T : Any> (val javaclassname: Class<T>) : ResponseDeserializable<T> {
    override fun deserialize(content: String) = Helpers.getGSON().fromJson(content, javaclassname)
}