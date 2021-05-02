package com.example.projectmanagementtool.models

import android.os.Parcel
import android.os.Parcelable

class Log(
    val name: String = "",
    val description: String = "",
    val createdBy: String = "",
    val createdAt: String = "",
    val image: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(createdBy)
        parcel.writeString(createdAt)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Log> {
        override fun createFromParcel(parcel: Parcel): Log {
            return Log(parcel)
        }

        override fun newArray(size: Int): Array<Log?> {
            return arrayOfNulls(size)
        }
    }
}