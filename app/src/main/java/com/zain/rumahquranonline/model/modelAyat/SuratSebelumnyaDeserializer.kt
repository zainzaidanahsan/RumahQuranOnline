package com.zain.rumahquranonline.model.modelAyat

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class SuratSebelumnyaDeserializer : JsonDeserializer<SuratSebelumnya?> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SuratSebelumnya? {
        return if (json.isJsonObject) {
            Gson().fromJson(json, SuratSebelumnya::class.java)
        } else if (json.isJsonPrimitive && json.asJsonPrimitive.isBoolean) {
            null // atau menangani sebagai boolean jika diperlukan
        } else {
            throw JsonParseException("Unexpected type for suratSebelumnya field")
        }
    }
}