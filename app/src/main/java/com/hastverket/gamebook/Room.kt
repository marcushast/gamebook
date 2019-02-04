package com.hastverket.gamebook

import java.util.LinkedList

class Room {
    var mId: String? = null
    var mDescription: String? = null
    var mRoomLinks: LinkedList<RoomLink>? = null

    constructor(id: String, descr: String, roomLinks: LinkedList<RoomLink>) {
        mId = id
        mDescription = descr
        mRoomLinks = roomLinks
    }

    constructor() {

    }

    fun setId(id: String) {
        mId = id
    }

    fun setDescription(desc: String) {
        mDescription = desc
    }

    fun setRoomLinks(roomLinks: LinkedList<RoomLink>) {
        mRoomLinks = roomLinks
    }
}