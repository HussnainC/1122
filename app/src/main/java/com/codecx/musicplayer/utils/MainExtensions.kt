package com.codecx.musicplayer.utils

import android.app.Activity
import android.content.Intent
import android.view.View

inline fun <reified T> Activity.startNewActivity(activity: Class<*>, isFinish: Boolean) {
    startActivity(Intent(this, activity))
    if (isFinish) {
        finish()
    }
}
fun View.beGone() {
    this.visibility = View.GONE
}
fun View.beVisible() {
    this.visibility = View.VISIBLE
}
fun View.beInvisible() {
    this.visibility = View.INVISIBLE
}
inline fun <reified T> Activity.startNewActivity(
    activity: T,
    extras: (Intent) -> Unit,
    isFinish: Boolean
) {
    startActivity(Intent(this, activity!!::class.java).also {
        extras(it)
    })
    if (isFinish) {
        finish()
    }
}