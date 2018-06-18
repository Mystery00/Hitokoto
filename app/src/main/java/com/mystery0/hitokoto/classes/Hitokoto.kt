package com.mystery0.hitokoto.classes

data class Hitokoto(val id: Long,
					val hitokoto: String,
					val type: String,
					val from: String,
					val creator: String,
					val created_at: String)