package ca.apocrypher.webhelpdesk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

// JSONArray extensions

val JSONArray.size: Int
	get() = length()

val JSONArray.indices: IntRange
	get() = (0 until size)

operator fun JSONArray.iterator(): Iterator<JSONObject> = indices.map { i -> getJSONObject(i) }.iterator()
fun JSONArray.withIndex(): Iterator<IndexedValue<JSONObject>> = indices.map { i -> IndexedValue(i, getJSONObject(i)) }.iterator()

// AppCompatActivity extensions

fun AppCompatActivity.startActivity(cls: Class<*>) = startActivity(Intent(this, cls))

// Bundle extensions

operator fun Bundle.set(key: String, value: String) = putString(key, value)
operator fun Bundle.set(key: String, value: Bundle) = putBundle(key, value)