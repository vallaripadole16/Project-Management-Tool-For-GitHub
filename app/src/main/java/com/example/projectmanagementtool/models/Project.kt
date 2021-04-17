package com.example.projectmanagementtool.models

import android.os.Parcel
import android.os.Parcelable

class Project(
    var joinId: String = "",
    var classId: String = "",
    val name: String = "",
    val image: String = "",
    val createdBy: String = "",
    val tech: String = "",
    val githubRepo: String = "",
    val createdAt: String = "",
    val members: ArrayList<String> = ArrayList(),
    var logList :ArrayList<String> = ArrayList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(classId)
        parcel.writeString(joinId)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(createdBy)
        parcel.writeString(tech)
        parcel.writeString(githubRepo)
        parcel.writeString(createdAt)
        parcel.writeStringList(members)
        parcel.writeStringList(logList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }
    }
}