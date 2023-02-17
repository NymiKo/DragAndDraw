package com.easyprog.android.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.os.bundleOf

class BoxDrawingView(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {

    companion object {
        private const val KEY_BOXEN = "boxen"
        private const val KEY_STATE = "state"
    }

    private var currentBox: Box? = null
    private var boxen = mutableListOf<Box>()
    private val boxPaint = Paint().apply {
        color = 0x22ff0000
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        var action = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                currentBox = Box(current).also {
                    boxen.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentBox(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentBox(current)
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }

        return true
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        //Заполнение фона
        canvas.drawPaint(backgroundPaint)

        boxen.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return bundleOf().apply {
            putParcelableArrayList(KEY_BOXEN, ArrayList<Parcelable>(boxen))
            putParcelable(KEY_STATE, super.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                boxen = state.getParcelableArrayList(KEY_BOXEN, Box::class.java)?.toMutableList() ?: mutableListOf()
                super.onRestoreInstanceState(state.getParcelable(KEY_STATE, Parcelable::class.java))
            } else {
                boxen = state.getParcelableArrayList<Box>(KEY_BOXEN)?.toMutableList() ?: mutableListOf()
                super.onRestoreInstanceState(state.getParcelable(KEY_STATE))
            }
        }
        Log.e("BOXEN", boxen.toString())
    }
}