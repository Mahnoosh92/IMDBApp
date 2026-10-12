package com.example.firebase.crashlytics

interface CrashReportingRepository {
    fun setCustomKey(key: String, value: String)
    fun setCustomKey(key: String, value: Boolean)
    fun setCustomKey(key: String, value: Int)
    fun setUserId(userId: String)
    fun log(message: String)
    fun recordException(throwable: Throwable)
}
