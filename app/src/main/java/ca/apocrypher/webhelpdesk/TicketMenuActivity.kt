package ca.apocrypher.webhelpdesk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_ticket_menu.*

class TicketMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //Generated boilerplate
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_menu)

        // Attach a listener to mineButton
        mineButton.setOnClickListener {
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
        searchButton.setOnClickListener { startActivity(TicketSearchActivity::class.java) }
    }
}
