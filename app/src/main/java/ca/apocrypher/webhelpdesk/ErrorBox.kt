package ca.apocrypher.webhelpdesk

import android.app.AlertDialog
import android.content.Context

class ErrorBox(context: Context, title: String , msg: String) {
	init {
		val dlgAlert: AlertDialog.Builder = AlertDialog.Builder(context)
		dlgAlert.setTitle(title)
		dlgAlert.setMessage(msg)
		dlgAlert.setPositiveButton("OK", null)
		dlgAlert.setCancelable(true)
		dlgAlert.create().show()
	}

	companion object {
		fun notImplemented(context: Context) {
			ErrorBox(context, "Work In Progress", "Feature not yet available.")
		}
	}
}