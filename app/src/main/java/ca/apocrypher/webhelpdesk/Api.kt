package ca.apocrypher.webhelpdesk

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.URLUtil
import androidx.preference.PreferenceManager
import com.android.volley.Request.Method.GET
import com.android.volley.Request.Method.POST
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
import androidx.core.content.edit

typealias Parameter = Pair<String, Any>

// This is a singleton class
// Only one Api object for the whole app
object Api {
    lateinit var apiUrl: String; private set
    lateinit var hostname: String; private set
    lateinit var accessToken: String; private set
    private lateinit var queue: RequestQueue
    private lateinit var cookieManager: CookieManager
    private lateinit var sharedPref: SharedPreferences

    fun initialize(context: Context) {
        // Create queue for sending api requests
        queue = Volley.newRequestQueue(context)

        // Create custom cookie manager for persistent cookies
        cookieManager = CookieManager(PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(cookieManager)

        // Get stored values to build api url
        // Never null because we supply a default
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        accessToken = sharedPref.getString("accessToken", "")!!
        hostname   = sharedPref.getString("hostname",   "")!!

        // Build apiUrl if values are available
        buildApiUrl(hostname)
    }

    // Build apiUrl from hostname, provided or stored
    // May be necessary if some implementations use a different format
    fun buildApiUrl(hostname: String) {
        // Use stored hostname if blank
        val host = if (hostname != "") { hostname } else { Api.hostname }
        // If hostname hasn't been stored, url is blank
        apiUrl = if (host != "") { "https://${ host }/api/v1/ra" } else { "" }
    }

    fun reset() {
        accessToken = ""
        sharedPref.edit { remove("accessToken") }
        // TODO: Create separate functions for hard/soft reset
        //sharedPref.edit().remove("hostname").apply()
        cookieManager.cookieStore.removeAll()
    }

    fun testUrl(url: String) {
        URLUtil.isValidUrl(url)
    }

    fun setAccessToken(key: String) {
        accessToken = key
        sharedPref.edit { putString("accessToken", accessToken) }
    }

    // TODO: Should this function also build apiUrl?
    fun setHostname(host: String) {
        hostname = host
        sharedPref.edit { putString("hostname", hostname) }
    }

    private fun addParams(url: String, vararg params: Parameter): String {
        var newUrl = "$url?"
        params.forEach { newUrl += "${it.first}=${it.second}&"}
        return newUrl.trimEnd('?', '&')
    }

    private fun addParams(url: String, params: Bundle): String {
        var newUrl = "$url?"
        params.keySet().forEach { newUrl += "${it}=${params[it]}&"}
        return newUrl.trimEnd('?', '&')
    }

    private fun getTokenBody(username: String, password: String): JSONObject {
        return JSONObject().put("username", username).put("password", password)
    }

    fun getToken(
        resource: String,
        result: (JSONObject) -> Unit,
        error: (VolleyError) -> Unit,
        username: String,
        password: String,
        hostname: String
    ) {
        buildApiUrl(hostname)
        makeTokenRequest(resource, result, error, username, password)
    }

    // Return the account-switch response; its "token" field is the technician bearer token.
    fun switchToTech(
        techId: Int,
        key: String,
        result: (JSONObject) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        val url = "${apiUrl.removeSuffix("/ra")}/whd/switch-account/switch-to-tech?techId=$techId"
        queue.add(object : JsonObjectRequest(POST, url, null, result, error) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Authorization" to "Bearer $key")
            }
        })
    }

    fun testToken(result: (JSONObject) -> Unit, error: (VolleyError) -> Unit) {
        getResource("Techs/currentTech", result, error)
    }

    private fun makeTokenRequest(resource: String, result: (JSONObject) -> Unit, error: (VolleyError) -> Unit, username: String, password: String) {
        queue.add(JsonObjectRequest(POST, "$apiUrl/$resource", getTokenBody(username, password), result, error))
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
        queue.add(object : JsonObjectRequest(GET, addParams("$apiUrl/$resource", *params), null, result, error) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Authorization" to "Bearer $accessToken")
            }
        })
    }

    private fun makeObjectRequest(resource: String, result: (JSONObject) -> Unit, error: (VolleyError) -> Unit, params: Bundle) {
        queue.add(object : JsonObjectRequest(GET, addParams("$apiUrl/$resource", params), null, result, error) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Authorization" to "Bearer $accessToken")
            }
        })
    }

    private fun makeArrayRequest(resource: String, result: (JSONArray) -> Unit, error: (VolleyError) -> Unit, vararg params: Parameter) {
        queue.add(object : JsonArrayRequest(GET, addParams("$apiUrl/$resource", *params), null, result, error) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Authorization" to "Bearer $accessToken")
            }
        })
    }

    private fun makeArrayRequest(resource: String, result: (JSONArray) -> Unit, error: (VolleyError) -> Unit, params: Bundle) {
        queue.add(object : JsonArrayRequest(GET, addParams("$apiUrl/$resource", params), null, result, error) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Authorization" to "Bearer $accessToken")
            }
        })
    }
}