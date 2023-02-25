package ca.apocrypher.webhelpdesk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.apocrypher.webhelpdesk.databinding.ActivityTicketMenuBinding

class TicketMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Activity boilerplate
        super.onCreate(savedInstanceState)
        binding = ActivityTicketMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Attach a listener to mineButton
        binding.mineButton.setOnClickListener {
            //Build intent
            val params = Bundle(2)
            params["list"] = "mine"
            params["style"] = "details"

            val extras = Bundle(2)
            extras["RESOURCE"] = "Tickets"
            extras["PARAMS"] = params

            val intent = Intent(this, TicketListActivity::class.java)
            intent.putExtras(extras)

            // Start TicketListActivity with list of user's tickets
            startActivity(intent)
        }

        // Go to TicketSearchActivity when searchButton is clicked
        binding.searchButton.setOnClickListener { startActivity(TicketSearchActivity::class.java) }

        // Attach listener to addButton
        binding.addButton.setOnClickListener { ErrorBox.notImplemented(this) }
    }
}
