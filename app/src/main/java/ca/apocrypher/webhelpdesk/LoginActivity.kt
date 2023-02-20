package ca.apocrypher.webhelpdesk

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import ca.apocrypher.webhelpdesk.databinding.ActivityLoginBinding
//import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Generated boilerplate
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_login)
        setContentView(view)

        // Initialize Api Wrapper
        Api.initialize(applicationContext)

        // Skip Login and go to Main Menu if a session key has already been entered, verified, and stored
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
        binding.loginButton.setOnClickListener {
            // Retrieve contents of text boxes
            val username: String = binding.usernameEditText.text.toString()
            val password: String = binding.passwordEditText.text.toString()

            // Check if the if the provided credentials are valid
            checkCredentials(sharedPref, username, password)
        }
    }

    private fun checkCredentials(sharedPref: SharedPreferences, username: String, password: String) {
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