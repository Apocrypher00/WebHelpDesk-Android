package ca.apocrypher.webhelpdesk

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import android.widget.ExpandableListView.ExpandableListContextMenuInfo
import androidx.appcompat.app.AppCompatActivity
import ca.apocrypher.webhelpdesk.databinding.ActivityTicketListBinding
//import kotlinx.android.synthetic.main.activity_ticket_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Todo: Fix memory leak
private lateinit var binding: ActivityTicketListBinding

class TicketListActivity : AppCompatActivity() {
	private lateinit var tickets: JSONArray
	private val groups: LinkedHashMap<String, Array<String>> = LinkedHashMap()

	override fun onCreate(savedInstanceState: Bundle?) {
		//Generated boilerplate
		super.onCreate(savedInstanceState)
		binding = ActivityTicketListBinding.inflate(layoutInflater)
		val view = binding.root
		//setContentView(R.layout.activity_ticket_list)
		setContentView(view)

		//Populate page
		getTickets(intent.extras!!)
	}

	private fun getTickets(extras: Bundle) {
		Api.getResources(extras["RESOURCE"] as String,
			{
				tickets = it
				formatTickets()
				showTickets()
			},
			{
				Api.reset(this)
				startActivity(LoginActivity::class.java)
			},
			extras["PARAMS"] as Bundle)
	}

	private fun formatTickets() {
		// Iterate through tickets
		for (ticket in tickets) {
			// Generate group header
			val id: Int = ticket.getInt("id")
			val locationName: String = try { ticket.getJSONObject("location").getString("locationName") } catch (e: JSONException) { "" }
			val room: String = ticket.getString("room")
			val roomString: String = if (room != "null") "($room)" else ""
			val clientReporter: JSONObject? = try { ticket.getJSONObject("clientReporter") } catch (e: JSONException) { null }
			val fullName: String = if (clientReporter != null)
				"${clientReporter.getString("firstName")}  ${clientReporter.getString("lastName")}" else ""
			val header = "$id - $locationName $roomString - $fullName"

			// Generate children
			val notes: JSONArray = ticket.getJSONArray("notes")
			val items: Array<String> = Array(notes.size + 1) { "" }
			items[0] = "${ticket.getString("subject")}: ${ticket.getString("detail")}"
			for ((i, note) in notes.withIndex()) {
				items[i + 1] = "${note.getString("prettyUpdatedString")}:<br>${note.getString("mobileNoteText")}"
			}

			// Map header to children
			groups[header] = items
		}
	}

	private fun showTickets() {
		val expandableListAdapter = ExpandableListAdapter(this, groups.keys, groups)
		binding.expandableListView.setAdapter(expandableListAdapter)
		registerForContextMenu(binding.expandableListView)
	}

	override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo)
		val info = menuInfo as ExpandableListContextMenuInfo
		val type = ExpandableListView.getPackedPositionType(info.packedPosition)

		// Only create a context menu for group items
		if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) { menu.add("Open Ticket") }
	}

	override fun onContextItemSelected(menuItem: MenuItem): Boolean {
		val info = menuItem.menuInfo as ExpandableListContextMenuInfo
		val groupPos: Int = ExpandableListView.getPackedPositionGroup(info.packedPosition)

		// Pull values from the array we built when we created the list
		ErrorBox(this, "Nice", (tickets[groupPos] as JSONObject).getInt("id").toString())

		return true
	}
}
