package com.example.controlset.recycleChildView

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import androidx.core.view.MotionEventCompat
import kotlin.math.absoluteValue

class RecycleChildView(context: Context,attrs: AttributeSet?):LinearLayout(context,attrs) {
    private val screenWidth = context.resources.displayMetrics.widthPixels.toFloat()
    private var rightObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat("translationX", screenWidth),
        PropertyValuesHolder.ofFloat("alpha", 0f)
    ).apply {
        interpolator = AccelerateInterpolator()
        duration = 1000
    }
    private var leftObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat("translationX", -screenWidth),
        PropertyValuesHolder.ofFloat("alpha", 0f)
    ).apply {
        interpolator = AccelerateInterpolator()
        duration = 1000
    }
    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean = true

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if ((velocityX / velocityY).absoluteValue > 3 || (e2.x - e1.x).absoluteValue / screenWidth > 0.6) {
                    if (velocityX > 0) rightObjectAnimator.start()
                    else leftObjectAnimator.start()
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })

    fun setDeleteFunc(func: () -> Unit) {
        rightObjectAnimator.apply {
            removeAllListeners()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}
                override fun onAnimationCancel(p0: Animator) {}
                override fun onAnimationRepeat(p0: Animator) {}
                override fun onAnimationEnd(p0: Animator) {
                    func()
                }
            })
        }
        leftObjectAnimator.apply {
            removeAllListeners()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}
                override fun onAnimationCancel(p0: Animator) {}
                override fun onAnimationRepeat(p0: Animator) {}
                override fun onAnimationEnd(p0: Animator) {
                    func()
                }
            })
        }
    }

    @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event!!)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean =
        MotionEventCompat.isFromSource(ev!!, MotionEvent.ACTION_MOVE)
}