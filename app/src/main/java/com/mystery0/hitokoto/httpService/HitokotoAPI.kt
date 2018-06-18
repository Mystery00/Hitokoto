package com.mystery0.hitokoto.httpService

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface HitokotoAPI {
	@GET("/")
	fun get(@Query("c") c: String?): Observable<ResponseBody>
}