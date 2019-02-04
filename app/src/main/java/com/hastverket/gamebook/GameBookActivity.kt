package com.hastverket.gamebook

import java.io.IOException
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class GameBookActivity : Activity() {

    private var mGameBook: GameBook? = null

    private var mTextView: TextView? = null

    private var mGestureDetector: GestureDetector? = null

    private var mCurrentRoom: Room? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            mGameBook = GameBook(resources.assets.open("test_book_1.xml"))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mGestureDetector = GestureDetector(TapGestureDetector())

        setContentView(R.layout.activity_main)

        mTextView = findViewById<View>(R.id.textview) as TextView
        mTextView!!.movementMethod = ScrollingMovementMethod()
        mTextView!!.setOnTouchListener { v, event -> mGestureDetector!!.onTouchEvent(event) }

        var firstRoom: String? = "1"

        if (savedInstanceState != null) {
            firstRoom = savedInstanceState.getString(STATE_ROOM)
        }

        printRoomInfo(firstRoom)
    }

    public override fun onSaveInstanceState(savedInstantState: Bundle) {
        //Save current room the user is in
        savedInstantState.putString(STATE_ROOM, mCurrentRoom!!.mId)

        super.onSaveInstanceState(savedInstantState)
    }

    private fun printRoomInfo(id: String?) {
        mCurrentRoom = mGameBook!!.getRoomWithId(id!!)

        mTextView!!.text = mCurrentRoom!!.mDescription
    }

    override fun onCreateDialog(id: Int): Dialog {
        val nbrOfLinks = mCurrentRoom!!.mRoomLinks!!.size
        val roomLinks = arrayOfNulls<String>(nbrOfLinks)

        for (i in 0 until nbrOfLinks) {
            roomLinks[i] = mCurrentRoom!!.mRoomLinks!![i].mRoomDescription
        }

        val alert: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose wisely")
        builder.setItems(roomLinks) { dialog, which ->
            val newRoom = mCurrentRoom!!.mRoomLinks!![which].mRoomLink
            printRoomInfo(newRoom)
        }
        alert = builder.create()
        return alert
    }

    internal inner class TapGestureDetector : SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            showDialog(Integer.parseInt(mCurrentRoom!!.mId))

            return true
        }
    }

    companion object {

        private val STATE_ROOM = "playerRoom"

        internal val DIALOG_SELECT_ROOM = 0

        protected val TAG = "GameBookActivity"
    }
}