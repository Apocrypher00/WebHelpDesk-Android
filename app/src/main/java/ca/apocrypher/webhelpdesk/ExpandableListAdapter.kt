package ca.apocrypher.webhelpdesk

import android.content.Context
import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

// Todo: Comment everything
class ExpandableListAdapter(private val context: Context?, private val titles: MutableSet<String>, private val details: HashMap<String, Array<String>>) : BaseExpandableListAdapter() {
	override fun getChild(listPosition: Int, expandedListPosition: Int): Any? { return details[titles.elementAt(listPosition)]?.get(expandedListPosition) }
	override fun getChildId(listPosition: Int, expandedListPosition: Int): Long { return expandedListPosition.toLong() }
	override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
		var cv = convertView
		val expandedListText = getChild(listPosition, expandedListPosition) as String?
		if (cv == null) {
			val layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
			cv = layoutInflater.inflate(R.layout.list_item, null)
		}
		val expandedListTextView = cv?.findViewById<TextView?>(R.id.expandedListItem)
		expandedListTextView?.text = Html.fromHtml(expandedListText, Html.FROM_HTML_MODE_COMPACT)
		return cv
	}
	override fun getChildrenCount(listPosition: Int): Int { return details[titles.elementAt(listPosition)]?.size!! }

	override fun getGroup(listPosition: Int): Any { return titles.elementAt(listPosition) }
	override fun getGroupId(listPosition: Int): Long { return listPosition.toLong() }
	override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
		var cv = convertView
		val listTitle = getGroup(listPosition) as String?
		if (cv == null) {
			val layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
			cv = layoutInflater.inflate(R.layout.list_group, null)
		}
		val listTitleTextView = cv?.findViewById<TextView?>(R.id.listTitle)
		listTitleTextView?.setTypeface(null, Typeface.BOLD)
		listTitleTextView?.text = listTitle
		return cv
	}
	override fun getGroupCount(): Int { return titles.size }

	override fun hasStableIds(): Boolean {return false}
	override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {return true}
}