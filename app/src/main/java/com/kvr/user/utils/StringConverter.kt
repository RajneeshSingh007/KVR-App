package com.kvr.user.utils


import com.google.gson.*
import java.lang.reflect.Type


class StringConverter : JsonSerializer<String?>,
    JsonDeserializer<String?> {
    override fun serialize(
        src: String?, typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return if (src == null  || src == "null") {
            JsonPrimitive("")
        } else {
            JsonPrimitive(src.toString())
        }
    }
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement, typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String {
        val value = json.asJsonPrimitive.asString
        if (value == null  || value == "null") {
            return ""
        }
        return value
    }

}
