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
import com.mystery0.hitokoto.utils.WidgetUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
		Logs.i("onUpdate: $stringBuilder")
		updateAppWidget(context)
	}

	override fun onDeleted(context: Context, appWidgetIds: IntArray) {
		Logs.i("onDeleted: ")
		for (appWidgetId in appWidgetIds) {
			HitokotoWidgetConfigureActivity.deleteTitlePref(context, appWidgetId)
		}
	}

	override fun onEnabled(context: Context) {
		Logs.i("onEnabled: ")
	}

	override fun onDisabled(context: Context) {
		Logs.i("onDisabled: ")
	}

	override fun onReceive(context: Context, intent: Intent) {
		Logs.i("onReceive: ${intent.action}")
		if (intent.action == "android.appwidget.action.APPWIDGET_UPDATE")
			if (intent.getStringExtra("temp") == "tt")
				updateAppWidget(context)
	}

	companion object {
		private val list = ArrayList<Int>()

		internal fun updateAppWidget(context: Context) {
			Observable.create<Hitokoto> { observer ->
				WidgetUtil.getHitokoto {
					Logs.i("updateAppWidget: $it")
					if (it != null)
						observer.onNext(it)
					observer.onComplete()
				}
			}
					.subscribeOn(Schedulers.newThread())
					.unsubscribeOn(Schedulers.newThread())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(object : Observer<Hitokoto> {
						private var hitokoto: Hitokoto? = null
						override fun onComplete() {
							val views = RemoteViews(context.packageName, R.layout.hitokoto_widget)
							if (hitokoto != null) {
								val appWidgetManager = AppWidgetManager.getInstance(context)
								val widgetText = "${hitokoto!!.hitokoto}\n——${hitokoto!!.from}"
								views.setTextViewText(R.id.appwidget_text, widgetText)
								list.forEach {
									appWidgetManager.updateAppWidget(it, views)
								}
							}
							val intent = Intent("android.appwidget.action.APPWIDGET_UPDATE")
							intent.putExtra("temp", "tt")
							views.setOnClickPendingIntent(R.id.appwidget_text, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
						}

						override fun onSubscribe(d: Disposable) {
						}

						override fun onNext(t: Hitokoto) {
							hitokoto = t
						}

						override fun onError(e: Throwable) {
							e.printStackTrace()
						}
					})
		}
	}
}

