package com.hastverket.gamebook

class RoomLink(link: String, description: String) {
    var mRoomLink: String? = null
    var mRoomDescription: String? = null

    init {
        mRoomLink = link
        mRoomDescription = description
    }
}