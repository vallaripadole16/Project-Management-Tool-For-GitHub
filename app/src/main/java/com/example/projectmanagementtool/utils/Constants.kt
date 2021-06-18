package com.example.projectmanagementtool.utils

object Constants {

    const val JOIN_ID = "joinId"
    const val LOG_LIST = "logList"
    const val DOCUMENT_ID = "documentId"
    const val YES = "yes"
    const val NO = "no"
    const val ID = "id"
    const val MEMBERS = "members"
    const val PROJECT = "project"
    const val USER = "user"
    const val USER_ID = "userId"

    const val PROJECTS = "projects"
    const val USERS: String = "users"
    val COMMANDS_ARRAY = ArrayList<String>(
        arrayListOf(
            "None",
            "MERGE",
            "COMMIT",
            "PUSH",
            "PULL",
            "NEW BRANCH"
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