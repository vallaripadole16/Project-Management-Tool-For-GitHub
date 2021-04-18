package com.example.projectmanagementtool.models

import android.os.Parcel
import android.os.Parcelable

class Project(
    var joinId: String = "",
    var projectId: String = "",
    val name: String = "",
    val description:String ="",
    val image: String = "",
    val createdBy: String = "",
    val tech: String = "",
    val githubRepoUrl: String = "",
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
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(joinId)
        parcel.writeString(projectId)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeString(createdBy)
        parcel.writeString(tech)
        parcel.writeString(githubRepoUrl)
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