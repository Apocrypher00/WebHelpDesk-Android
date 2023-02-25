package ca.apocrypher.webhelpdesk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.apocrypher.webhelpdesk.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onBackPressed() {} //Do nothing so you don't accidentally go to Login

    override fun onCreate(savedInstanceState: Bundle?) {
        // Activity boilerplate
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Go to TicketMenuActivity when ticketsButton is clicked
        binding.ticketsButton.setOnClickListener { startActivity(TicketMenuActivity::class.java) }
    }
}
