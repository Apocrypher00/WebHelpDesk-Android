package ca.apocrypher.webhelpdesk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.apocrypher.webhelpdesk.databinding.ActivityLoginBinding
import com.android.volley.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Activity boilerplate
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Api Wrapper
        Api.initialize(applicationContext)

        // Skip Login and go to MainMenu if accessToken has already been generated
        // TODO: Can we make the login not appear in this case?
        if (Api.accessToken != "") {
            Api.testToken(
                { startActivity(MainMenuActivity::class.java) }, // Pass
                { Api.reset() }                                  // Fail
            )
        }

        // Allow hostname to be provided, or disallow if already provided and display it
        if (Api.hostname == "") {
            binding.hostnameEditText.isEnabled = true
            binding.hostnameButton.isEnabled = false
        } else {
            binding.hostnameEditText.isEnabled = false
            binding.hostnameEditText.setText(Api.hostname)
            binding.hostnameButton.isEnabled = true
        }

        // Attach listener to hostnameButton
        // Allows the user to unlock hostname for editing
        binding.hostnameButton.setOnClickListener {
            if (!binding.hostnameEditText.isEnabled) {
                binding.hostnameEditText.isEnabled = true
                binding.hostnameButton.isEnabled = false
            }
        }

        // Attach listener to loginButton
        binding.loginButton.setOnClickListener {
            // Retrieve contents of text boxes
            val hostname: String = binding.hostnameEditText.text.toString()
            val username: String = binding.usernameEditText.text.toString()
            val password: String = binding.passwordEditText.text.toString()

            // Stop if the provided hostname is blank
            if (binding.hostnameEditText.isEnabled && hostname == "") {
                binding.hostnameEditText.error = "Hostname cannot be blank!"
                return@setOnClickListener
            }

            // Stop if username is blank
            if (username == "") {
                binding.usernameEditText.error = "Username cannot be blank!"
                return@setOnClickListener
            }

            // Stop if password is blank
            if (password == "") {
                binding.passwordEditText.error = "Password cannot be blank!"
                return@setOnClickListener
            }

            // Check if the provided credentials are valid
            Api.getToken("Token",
                { token ->
                    Api.setAccessToken(token.getString("accessToken"))
                    // If we reached this point, save the hostname
                    if (hostname != "") { Api.setHostname(hostname) }
                    startActivity(MainMenuActivity::class.java)
                },
                { error ->
                    when (error) {
                        is TimeoutError -> {
                            ErrorBox(
                                this,
                                "TimeoutError",
                                "Dev Error: Please report this!"
                            )
                        }
                        is NoConnectionError -> {
                            ErrorBox(
                                this,
                                "Failed to Connect",
                                "Is the domain correct? ${error.localizedMessage}"
                            )
                        }
                        is AuthFailureError -> {
                            ErrorBox(
                                this,
                                "Failed to Authenticate",
                                "Incorrect Username or Password"
                            )
                        }
                        is ServerError -> {
                            ErrorBox(
                                this,
                                "ServerError",
                                "Dev Error: Please report this!"
                            )
                        }
                        is NetworkError -> {
                            ErrorBox(
                                this,
                                "NetworkError",
                                "Dev Error: Please report this!"
                            )
                        }
                        is ParseError -> {
                            ErrorBox(
                                this,
                                "ParseError",
                                "Dev Error: Please report this!"
                            )
                        }
                    }
                },
                username, password, hostname
            )
        }
    }
}