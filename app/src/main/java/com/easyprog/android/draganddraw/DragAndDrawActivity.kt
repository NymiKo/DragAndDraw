package com.easyprog.android.draganddraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DragAndDrawActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_and_draw)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, DragAndDrawFragment())
                .commit()
        }
    }
}