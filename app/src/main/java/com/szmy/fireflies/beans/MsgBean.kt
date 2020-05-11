package com.szmy.fireflies.beans

data class MsgBean(
    val userId: Int,
    val msgId: Int,
    val content: String,
    val type: Int,
    val commentedCount: Int,
    val commentCount: Int,
    val transferredCount: Int,
    val transferCount: Int,
    val favourCount: Int,
    val time: String
)