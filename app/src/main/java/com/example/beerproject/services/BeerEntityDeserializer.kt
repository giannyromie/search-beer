package com.example.beerproject.services

import com.example.beerproject.model.Beer
import com.google.gson.*
import java.lang.reflect.Type

/**
 * Deserializer to transform a json object
 */

class BeerEntityDeserializer : JsonDeserializer<Beer?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        paramJsonElement: JsonElement, paramType: Type?,
        paramJsonDeserializationContext: JsonDeserializationContext?
    ): Beer {
        return Gson().fromJson(paramJsonElement.asJsonObject, Beer::class.java)
    }
}