package ca.apocrypher.webhelpdesk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.apocrypher.webhelpdesk.databinding.ActivityTicketBinding
//import kotlinx.android.synthetic.main.activity_ticket.*

class TicketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //Generated boilerplate
        super.onCreate(savedInstanceState)
        val binding = ActivityTicketBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_ticket)
        setContentView(view)
    }
}