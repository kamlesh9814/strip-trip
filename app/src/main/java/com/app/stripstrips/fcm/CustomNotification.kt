package com.app.stripstrips.fcm

data class CustomNotification(
    val notificationId: String,
    val userId: String,
    val actionId: String,
    val actionById: String,
    val title: String,
    val description: String,
    val notificationType: String,
    val seen: String,
    val created: String,
    val disable: String,
    )

