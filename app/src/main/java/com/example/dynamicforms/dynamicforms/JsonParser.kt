package com.example.dynamicforms

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonParser {
    fun parseJson(jsonString: String): FormType {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val adapter: JsonAdapter<JsonData> = moshi.adapter(JsonData::class.java)

        // Parse JSON string into JsonData object
        val jsonData = adapter.fromJson(jsonString)
            ?: throw IllegalArgumentException("Invalid JSON or JSON doesn't match expected structure")

        // Return the formType property from JsonData
        return jsonData.data.formType
    }
}
