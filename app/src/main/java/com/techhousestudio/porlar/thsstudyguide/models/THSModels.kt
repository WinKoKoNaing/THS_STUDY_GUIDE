package com.techhousestudio.porlar.thsstudyguide.models

import com.google.firebase.Timestamp

data class User(
    var userId: String = "",
    var userName: String = "",
    var userImageUri: String = "",
//    var created_at: Long,
    var isAdmin: Boolean = false,
    var isMember: Boolean = false,
    var classIdList: List<String> = emptyList()
)

data class TimeTable(
    var classId: String = "",
    var className: String = "",
    var classTitle: String = "",
    var classStartTime: String = "",
    var classEndTime: String = "",
    var classDuration: String = "",
    var classLanguage: String = "",
    var classWeeks: String = "",
    var classInfo:String = "",
    var userIdList: Map<String, Boolean>? = emptyMap(),
    var classProgress: Int = 0,
    var classNoteCaution: String = ""
)

data class Post(
    var postId: String? = null,
    var classId: String? = null,
    var className: String? = null,
    var classTitle: String? = null,
    var classLanguage: String? = null,
    var postTopic: String? = null,
    var postStartTime: String? = null,
    var postEndTime: String? = null,
    var postLectureDate: Timestamp? = null,
    var postNote: String? = null,
    var userList: Map<String, Boolean> = emptyMap()

)

data class Course(
    var courseId: String = "",
    var courseName: String = "",
    var courseImageUri: String = "",
    var courseDetail: String = "",
    var courseViewCount: Long = 0L
)
