package com.mystery0.hitokoto.classes

import android.os.Parcel
import android.os.Parcelable

data class Hitokoto(val id: Long,
					val hitokoto: String,
					val type: String,
					val from: String,
					val creator: String,
					val created_at: String):Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readLong(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeLong(id)
		parcel.writeString(hitokoto)
		parcel.writeString(type)
		parcel.writeString(from)
		parcel.writeString(creator)
		parcel.writeString(created_at)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Hitokoto> {
		override fun createFromParcel(parcel: Parcel): Hitokoto {
			return Hitokoto(parcel)
		}

		override fun newArray(size: Int): Array<Hitokoto?> {
			return arrayOfNulls(size)
		}
	}
}