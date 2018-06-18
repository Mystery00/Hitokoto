package com.mystery0.hitokoto.utils

import com.google.gson.Gson
import com.mystery0.hitokoto.classes.Hitokoto
import com.mystery0.hitokoto.httpService.HitokotoAPI
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.InputStreamReader

object WidgetUtil {
	private const val api = "https://v1.hitokoto.cn/"
	private val retrofit = Retrofit.Builder()
			.baseUrl(api)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.build()

	fun getHitokoto(listener: (Hitokoto?) -> Unit) {
		retrofit.create(HitokotoAPI::class.java)
				.get(null)
				.observeOn(Schedulers.newThread())
				.unsubscribeOn(Schedulers.newThread())
				.map { Gson().fromJson(InputStreamReader(it.byteStream()), Hitokoto::class.java) }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(object : Observer<Hitokoto> {
					private var hitokoto: Hitokoto? = null
					override fun onComplete() {
						listener.invoke(hitokoto)
					}

					override fun onSubscribe(d: Disposable) {
					}

					override fun onNext(t: Hitokoto) {
						hitokoto = t
					}

					override fun onError(e: Throwable) {
						e.printStackTrace()
						listener.invoke(null)
					}
				})
	}
}