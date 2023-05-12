package com.dursunpekcan.todoapp.util

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.showKeyboard() {
    val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

    var view = this.currentFocus
    if (view == null) view = View(this)

    imm.showSoftInput(view, 0)

}


