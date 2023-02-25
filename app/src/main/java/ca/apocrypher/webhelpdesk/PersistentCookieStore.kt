package ca.apocrypher.webhelpdesk

import android.content.Context
import java.net.CookieManager
import java.net.CookieStore
import java.net.HttpCookie
import java.net.URI

class PersistentCookieStore(val context: Context) : CookieStore {
    private val store: CookieStore = CookieManager().cookieStore
    private val cookieSharedPreferences = context.getSharedPreferences("cookieStore", Context.MODE_PRIVATE)

    init {
        cookieSharedPreferences.all.entries.map {
            store.add(URI(it.key), HttpCookie.parse(it.value as String?)[0])
        }
    }

    override fun add(uri: URI?, cookie: HttpCookie?) {
        store.add(uri, cookie)
        cookieSharedPreferences.edit().putString(uri.toString(), cookie.toString()).apply()
    }

    override fun getCookies(): MutableList<HttpCookie> { return store.cookies }

    override fun getURIs(): MutableList<URI> { return store.urIs }

    override operator fun get(uri: URI?): List<HttpCookie> { return store.get(uri) }

    override fun remove(uri: URI?, cookie: HttpCookie?): Boolean {
        cookieSharedPreferences.edit().remove(uri.toString()).apply()
        return store.remove(uri, cookie)
    }

    override fun removeAll(): Boolean {
        cookieSharedPreferences.edit().clear().apply()
        return store.removeAll()
    }
}