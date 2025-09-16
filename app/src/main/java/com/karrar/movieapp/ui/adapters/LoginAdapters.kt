package com.karrar.movieapp.ui.adapters

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.EditText
import androidx.databinding.BindingAdapter

@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("endDrawableClick")
fun EditText.setEndDrawableClick(onClick: (() -> Unit)?) {
    if (onClick == null) return
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            val drawableEnd = 2
            if (compoundDrawables[drawableEnd] != null &&
                event.rawX >= (right - compoundDrawables[drawableEnd].bounds.width())
            ) {
                onClick()
                return@setOnTouchListener true
            }
        }
        false
    }
}
