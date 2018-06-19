package com.mystery0.hitokoto.activity

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText

import com.mystery0.hitokoto.R
import com.mystery0.hitokoto.widget.HitokotoWidget

/**
 * The configuration screen for the [HitokotoWidget] AppWidget.
 */
class HitokotoWidgetConfigureActivity : Activity() {
	private var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
	private lateinit var mAppWidgetText: EditText
	private var mOnClickListener: View.OnClickListener = View.OnClickListener {
		val context = this@HitokotoWidgetConfigureActivity

		val widgetText = mAppWidgetText.text.toString()
		saveTitlePref(context, mAppWidgetId, widgetText)

//		HitokotoWidget.updateAppWidget(context)

		val resultValue = Intent()
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
		setResult(Activity.RESULT_OK, resultValue)
		finish()
	}

	public override fun onCreate(icicle: Bundle?) {
		super.onCreate(icicle)

		setResult(Activity.RESULT_CANCELED)

		setContentView(R.layout.hitokoto_widget_configure)
		mAppWidgetText = findViewById<View>(R.id.appwidget_text) as EditText
		findViewById<View>(R.id.add_button).setOnClickListener(mOnClickListener)

		val intent = intent
		val extras = intent.extras
		if (extras != null) {
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
		}

		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish()
			return
		}

		mAppWidgetText.setText(loadTitlePref(this@HitokotoWidgetConfigureActivity, mAppWidgetId))
	}

	companion object {

		private const val PREFS_NAME = "com.mystery0.hitokoto.service.HitokotoWidget"
		private const val PREF_PREFIX_KEY = "appwidget_"

		internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
			val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
			prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
			prefs.apply()
		}

		internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
			val prefs = context.getSharedPreferences(PREFS_NAME, 0)
			val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
			return titleValue ?: context.getString(R.string.appwidget_text)
		}

		internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
			val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
			prefs.remove(PREF_PREFIX_KEY + appWidgetId)
			prefs.apply()
		}
	}
}

