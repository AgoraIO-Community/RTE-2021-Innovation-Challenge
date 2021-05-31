package com.dong.circlelive.base
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class MapDeserializer : JsonDeserializer<Map<String, String>> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        element: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Map<String, String> {
        val parentJsonObject = element.asJsonObject
        val childMap = HashMap<String, String>()
        for (entry in parentJsonObject.entrySet()) {
//            childMap = HashMap()
            for (entry1 in entry.value.asJsonObject.entrySet()) {
                childMap[entry1.key] = entry1.value.toString()
            }
        }
        return childMap
    }
}