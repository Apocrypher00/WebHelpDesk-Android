package ca.apocrypher.webhelpdesk

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Generated boilerplate
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Api Wrapper
        Api.initialize(applicationContext)

        // Skip Login and go to Tickets if a session key has already been entered, verified, and stored
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sessionKey: String = sharedPref.getString("sessionKey", "")!!
        if (sessionKey != "") {
            Api.setSessionKey(sessionKey)
            Api.testSession(
                { startActivity(MainMenuActivity::class.java) },
                { Api.reset(this) }
            )
        }

        // Attach listener to loginButton
        loginButton.setOnClickListener {
            // Retrieve contents of text boxes
            val username: String = usernameEditText.text.toString()
            val password: String = passwordEditText.text.toString()

            // Check if the content of editText is a valid apiKey
            checkInput(sharedPref, username, password)
        }
    }

    private fun checkInput(sharedPref: SharedPreferences, username: String, password: String) {
        if (username == "" || password == "") return

        Api.getSession("Session",
            { session ->
                val sessionKey = session.getString("sessionKey")
                Api.setSessionKey(sessionKey)
                sharedPref.edit().putString("sessionKey", sessionKey).apply()
                startActivity(MainMenuActivity::class.java)
            },
            {
                // Username or password false, display and an error
                ErrorBox(this, "Failed to Authenticate", "Incorrect username or password")
            },
            username,
            password
        )
    }
}