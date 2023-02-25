package ca.apocrypher.webhelpdesk

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import ca.apocrypher.webhelpdesk.databinding.ActivityTicketSearchBinding

class TicketSearchActivity : AppCompatActivity() {
	private lateinit var binding: ActivityTicketSearchBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		// Activity boilerplate
		super.onCreate(savedInstanceState)
		binding = ActivityTicketSearchBinding.inflate(layoutInflater)
		setContentView(binding.root)

		Api.getResources("StatusTypes",
				{
					val items = Array(it.size + 1) { i ->
						if (i == 0) "Status" else it.getJSONObject(i - 1).getString("statusTypeName")
					}
					val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items)
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
					binding.statusSpinner.adapter = adapter
				},
				{
					Api.reset()
					startActivity(LoginActivity::class.java)
				}
		)

		Api.getResources("Locations",
				{
					val items = Array(it.size + 1) { i ->
						if (i == 0) "Location" else it.getJSONObject(i - 1).getString("locationName")
					}
					val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items)
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
					binding.locationSpinner.adapter = adapter
				},
				{
					Api.reset()
					startActivity(LoginActivity::class.java)
				}
		)

		binding.searchButton.setOnClickListener {
			// Build qualifier
			val statusString = "(statustype.statusTypeName %3D \"${binding.statusSpinner.selectedItem}\")"
			val locationString = "(location.locationName %3D \"${binding.locationSpinner.selectedItem}\")"
			val s = binding.statusSpinner.selectedItemPosition
			val l = binding.locationSpinner.selectedItemPosition
			val qualifier = if (s != 0 && l != 0) { "($statusString and $locationString)" }
				else if (s != 0 && l == 0) { statusString }
				else if (s == 0 && l != 0) { locationString }
				else {
					return@setOnClickListener // Nothing selected
				}

			// Build intent
			val params = Bundle(2)
			params["qualifier"] = qualifier
			params["style"] = "details"

			val extras = Bundle(2)
			extras["RESOURCE"] = "Tickets"
			extras["PARAMS"] = params

			val intent = Intent(this, TicketListActivity::class.java)
			intent.putExtras(extras)

			// Start TicketListActivity with list of results from search
			startActivity(intent)
		}
	}
}
