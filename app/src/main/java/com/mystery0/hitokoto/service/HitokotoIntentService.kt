package com.mystery0.hitokoto.service

import android.app.IntentService
import android.content.Intent
import com.mystery0.hitokoto.classes.Hitokoto
import com.mystery0.hitokoto.constant.IntentConstant
import com.mystery0.hitokoto.utils.WidgetUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vip.mystery0.logs.Logs

class HitokotoIntentService : IntentService("HitokotoIntentService") {

	override fun onCreate() {
		super.onCreate()
		Logs.i("onCreate: ")
	}

	override fun onHandleIntent(intent: Intent?) {
		Logs.i("onHandleIntent: ")
		Observable.create<Hitokoto> { observer ->
			WidgetUtil.getHitokoto {
				if (it != null)
					observer.onNext(it)
				observer.onComplete()
			}
		}
				.subscribeOn(Schedulers.newThread())
				.unsubscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(object : Observer<Hitokoto> {
					private lateinit var hitokoto: Hitokoto
					override fun onComplete() {
						val widgetIntent = Intent("android.appwidget.action.APPWIDGET_UPDATE")
						widgetIntent.putExtra(IntentConstant.intent_hitokoto, hitokoto)
						sendBroadcast(widgetIntent)
						stopSelf()
					}

					override fun onSubscribe(d: Disposable) {
					}

					override fun onNext(t: Hitokoto) {
						hitokoto = t
					}

					override fun onError(e: Throwable) {
						stopSelf()
					}
				})
	}
}
