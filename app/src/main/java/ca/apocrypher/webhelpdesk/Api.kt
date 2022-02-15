package ca.apocrypher.webhelpdesk

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.android.volley.Request.Method.GET
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy


typealias Parameter = Pair<String, Any>

object Api {
    private const val apiUrl: String = "https://helpdesk.example.com/helpdesk/WebObjects/Helpdesk.woa/ra"
    private lateinit var sessionKey: String
    private lateinit var queue: RequestQueue
    private lateinit var cookieManager: CookieManager

    fun initialize(context: Context) {
        queue = Volley.newRequestQueue(context)

        cookieManager = CookieManager(PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(cookieManager)
    }

    fun reset(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("sessionKey").apply()
        cookieManager.cookieStore.removeAll()
    }

    fun setSessionKey(key: String) {
        sessionKey = key
    }

    private fun addParams(url: String, vararg params: Parameter): String {
        var newUrl = "$url?"
        params.forEach { newUrl += "${it.first}=${it.second}&"}
        return "${newUrl}sessionKey=$sessionKey"
    }

    private fun addParams(url: String, params: Bundle): String {
        var newUrl = "$url?"
        params.keySet().forEach { newUrl += "${it}=${params[it]}&"}
        return "${newUrl}sessionKey=$sessionKey"
    }

    private fun addParams(url: String, username: String, password: String): String {
        return "${url}?username=${username}&password=${password}"
    }

    fun getSession(resource: String, result: (JSONObject) -> Unit, error: (VolleyError) -> Unit, username: String, password: String) {
        makeSessionRequest(resource, result, error, username, password)
    }

    fun testSession(result: (JSONObject) -> Unit, error: (VolleyError) -> Unit) {
        getResource("Session", result, error)
    }

    private fun makeSessionRequest(resource: String, result: (JSONObject) -> Unit, error: (VolleyError) -> Unit, username: String, password: String) {
        queue.add(JsonObjectRequest(GET, addParams("$apiUrl/$resource", username, password), null, result, error))
    }

    fun getResource(resource: String, result: (JSONObject) -> Unit, error: (VolleyError) -> Unit, vararg params: Parameter) {
        makeObjectRequest(resource, result, error, *params)
    }

    fun getResource(resource: String, result: (JSONObject) -> Unit, error: (VolleyError) -> Unit, params: Bundle) {
        makeObjectRequest(resource, result, error, params)
    }

    fun getResources(resource: String, result: (JSONArray) -> Unit, error: (VolleyError) -> Unit, vararg params: Parameter) {
        makeArrayRequest(resource, result, error, *params)
    }

    fun getResources(resource: String, result: (JSONArray) -> Unit, error: (VolleyError) -> Unit, params: Bundle) {
        makeArrayRequest(resource, result, error, params)
    }

    private fun makeObjectRequest(resource: String, result: (JSONObject) -> Unit, error: (VolleyError) -> Unit, vararg params: Parameter) {
        queue.add(JsonObjectRequest(GET, addParams("$apiUrl/$resource", *params), null, result, error))
    }

    private fun makeObjectRequest(resource: String, result: (JSONObject) -> Unit, error: (VolleyError) -> Unit, params: Bundle) {
        queue.add(JsonObjectRequest(GET, addParams("$apiUrl/$resource", params), null, result, error))
    }

    private fun makeArrayRequest(resource: String, result: (JSONArray) -> Unit, error: (VolleyError) -> Unit, vararg params: Parameter) {
        queue.add(JsonArrayRequest(GET, addParams("$apiUrl/$resource", *params), null, result, error))
    }

    private fun makeArrayRequest(resource: String, result: (JSONArray) -> Unit, error: (VolleyError) -> Unit, params: Bundle) {
        queue.add(JsonArrayRequest(GET, addParams("$apiUrl/$resource", params), null, result, error))
    }
}