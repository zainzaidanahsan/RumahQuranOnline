package com.zain.rumahquranonline.model.modelAyat

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class SuratSelanjutnyaDeserializer : JsonDeserializer<SuratSelanjutnya?> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SuratSelanjutnya? {
        return if (json.isJsonObject) {
            Gson().fromJson(json, SuratSelanjutnya::class.java)
        } else {
            null // atau nilai default lain jika boolean
        }
    }
}
