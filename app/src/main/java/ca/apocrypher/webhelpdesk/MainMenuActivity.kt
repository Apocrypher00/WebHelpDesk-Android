package ca.apocrypher.webhelpdesk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    override fun onBackPressed() {} //Do nothing so you don't accidentally go to Login

    override fun onCreate(savedInstanceState: Bundle?) {
        // Generated boilerplate
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Go to TicketMenuActivity when ticketsButton is clicked
        ticketsButton.setOnClickListener { startActivity(TicketMenuActivity::class.java) }
    }
}
