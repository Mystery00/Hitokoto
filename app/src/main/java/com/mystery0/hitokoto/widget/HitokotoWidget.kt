package com.mystery0.hitokoto.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import com.mystery0.hitokoto.R
import com.mystery0.hitokoto.activity.HitokotoWidgetConfigureActivity
import com.mystery0.hitokoto.classes.Hitokoto
import com.mystery0.hitokoto.constant.IntentConstant
import com.mystery0.hitokoto.service.HitokotoIntentService
import vip.mystery0.logs.Logs

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [HitokotoWidgetConfigureActivity]
 */
class HitokotoWidget : AppWidgetProvider() {

	override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
		val stringBuilder = StringBuilder()
		appWidgetIds.forEach {
			stringBuilder.append(it).append(" ")
			list.add(it)
		}
		context.startService(Intent(context, HitokotoIntentService::class.java))
		Logs.i("onUpdate: $stringBuilder")
	}

	override fun onDeleted(context: Context, appWidgetIds: IntArray) {
		val stringBuilder = StringBuilder()
		appWidgetIds.forEach {
			stringBuilder.append(it).append(" ")
			list.remove(it)
		}
		Logs.i("onDeleted: ")
	}

	override fun onEnabled(context: Context) {
		Logs.i("onEnabled: ")
	}

	override fun onDisabled(context: Context) {
		Logs.i("onDisabled: ")
	}

	override fun onReceive(context: Context, intent: Intent) {
		Logs.i("onReceive: ${intent.action}")
		if (intent.action == "android.appwidget.action.APPWIDGET_UPDATE") {
			val hitokoto = intent.getParcelableExtra<Hitokoto>(IntentConstant.intent_hitokoto)
			if (hitokoto != null) {
				Logs.i("onReceive: $hitokoto")
				updateAppWidget(context, hitokoto)
			}
		}
	}

	companion object {
		private val list = ArrayList<Int>()

		internal fun updateAppWidget(context: Context, hitokoto: Hitokoto) {
			val views = RemoteViews(context.packageName, R.layout.hitokoto_widget)
			val appWidgetManager = AppWidgetManager.getInstance(context)
			val widgetText = "${hitokoto.hitokoto}\n——${hitokoto.from}"
			views.setTextViewText(R.id.appwidget_text, widgetText)
			list.forEach {
				appWidgetManager.updateAppWidget(it, views)
			}
			val intent = Intent(context, HitokotoIntentService::class.java)
			views.setOnClickPendingIntent(R.id.appwidget_text, PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
		}
	}
}

