package com.example.data

import android.content.Context
import org.json.JSONException
import org.json.JSONObject

class Property(
    val id: Int,
    val rooms: Int,
    val area: Int,
    val price: Int,
    val last_updated: String,
    val address: String,
    val owner: String,
    val image: String) {

    companion object {

        fun getRecipesFromFile(filename: String, context: Context): ArrayList<Property> {
            val recipeList = ArrayList<Property>()

            try {
                // Load data
                val jsonString = loadJsonFromAsset(filename, context)
                val json = JSONObject(jsonString)
                val recipes = json.getJSONArray("recipes")

                // Get Recipe objects from data
                (0 until recipes.length()).mapTo(recipeList) {
                    Property(recipes.getJSONObject(it).getInt("id"),
                        recipes.getJSONObject(it).getInt("rooms"),
                        recipes.getJSONObject(it).getInt("area"),
                        recipes.getJSONObject(it).getInt("price"),
                        recipes.getJSONObject(it).getString("last_updated"),
                        recipes.getJSONObject(it).getString("address"),
                        recipes.getJSONObject(it).getString("owner"),
                        recipes.getJSONObject(it).getString("image"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return recipeList
        }

        private fun loadJsonFromAsset(filename: String, context: Context): String? {
            var json: String? = null

            try {
                val inputStream = context.assets.open(filename)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: java.io.IOException) {
                ex.printStackTrace()
                return null
            }

            return json
        }
    }
}