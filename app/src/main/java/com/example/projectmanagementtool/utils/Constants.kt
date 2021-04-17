package com.example.projectmanagementtool.utils

object Constants {

    const val USER_PREFERENCE_NAME = "user_preference"
    const val CURRENT_USER = "current_user"
    const val JOIN_ID = "joinId"
    const val LECTURE_LIST = "lectureList"
    const val CLASS_ROOM = "classroom"
    const val END_TIME = "endTime"
    const val START_TIME = "startTime"
    const val DOCUMENT_ID = "documentId"
    const val YES = "yes"
    const val NO = "no"
    const val ID = "id"
    const val CLASS_ID = "classId"
    const val CREATED_BY = "createdBy"
    const val COLLEGE_NAME_CLASSROOM = "collegeName"
    const val STUDY_YEAR = "yearOfStudy"
    const val CREATED_AT = "createdAt"
    const val MEMBERS = "members"
    const val CLASSROOMS = "classrooms"

    const val PROJECTS = "projects"
    const val USERS: String = "users"
    const val NAME = "name"
    const val IMAGE = "image"
    const val EMAIL = "email"
    const val MOBILE = "mobile"
    const val COLLEGE_NAME = "college"
    val YEAR_OF_STUDY = arrayOf("1st year", "2nd year", "3rd year", "4th year")
    val STUDY_SEMESTER =
        arrayOf("Sem I", "Sem II", "Sem III", "Sem IV", "Sem V", "Sem VI", "Sem VII", "Sem VIII")
    const val NOT_STARTED_FLAG = "Not Started yet"
    const val STARTED_FLAG = "Started"
    const val FINISHED_FLAG = "Finished"
    const val CANCELED_FLAG = "Canceled"
    val WEEK_DAYS_ARRAY_LIST = ArrayList<String>(
        arrayListOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
        )
    )
    val CURRENT_DAYS_ARRAY_LIST = ArrayList<String>(
        arrayListOf(
            "Saturday",
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday"
        )
    )

    // date format converter
    fun formatConverter(time: String): String {
        val timeSplit = time.split(":")
        return when {
            timeSplit[0].toInt() > 12 -> {
                val hour = timeSplit[0].toInt() - 12
                "${hour}:${timeSplit[1]} PM"
            }
            timeSplit[0].toInt() == 12 -> {
                "$time PM"
            }
            else -> {
                "$time AM"
            }
        }
    }
}