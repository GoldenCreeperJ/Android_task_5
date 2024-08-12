package com.example.controlset.recycleChildClass

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

class RecycleChildClass(context: Context, attributeSet: AttributeSet?): RecyclerView(context,attributeSet) {
    private var eventSet = Pair(0F, 0F)
    private lateinit var deleteFunc: RecycleChildClass.(pos: Int) -> Unit
    private val screenWidth = context.resources.displayMetrics.widthPixels.toFloat()

    fun setDeleteFunc(func: RecycleChildClass.(Int) -> Unit) {
        deleteFunc = func
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (e!!.action == MotionEvent.ACTION_DOWN) {
            eventSet = eventSet.copy(e.x, e.y)
        } else if (e.action == MotionEvent.ACTION_UP) {
            if (((e.x - eventSet.first) / (e.y - eventSet.second)).absoluteValue > 3 || (e.x - eventSet.first).absoluteValue / screenWidth > 0.6) {
                val childView = findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    deleteFunc(getChildLayoutPosition(childView))
                    return true
                }
            }
        }
        return super.onTouchEvent(e)
    }
}