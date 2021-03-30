package com.example.kotlincanvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import java.security.AccessController.getContext

private const val stroke_width = 6f

class MyCanvas(context:Context):View(context) {

    //1.canvas
    //2.bitmap
    //.3.path
    //Then we draw

    //background color
    private val bgColor = ResourcesCompat.getColor(resources,R.color.white, null)
    private lateinit var   canvas1:Canvas
    private lateinit var   bitmap1:Bitmap

    //pen or touch color
    private val penColor = ResourcesCompat.getColor(resources,R.color.black, null)

    //Touch, means we have to touch the drawing from where the user has touched
    private val touchTolerance = ViewConfiguration.get(context).scaledEdgeSlop

    private val paint = Paint().apply {
        color = penColor
        isAntiAlias = true //smooth of the edges
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = stroke_width
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND

    }

    private val path = Path()
    private val motionX = 0f
    private val motionY = 0f
    private var currentX = 0f
    private var currentY = 0f






    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //=====For multiple sketch, we need to create a bitmap and reset using recycle. :: means protected
        if(::bitmap1.isInitialized)bitmap1.recycle()

        bitmap1 = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)

        canvas1 = Canvas(bitmap1)
        canvas1.drawColor(bgColor)


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap1, 0f,0f,null)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

        //check the motion
        motionX = event!!.x
        motionY = event!!.y

        when(event.action){
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        //make it reachable
        return true

    }



    private fun touchUp() {

        //we need to reset only
        path.reset()

    }

    private fun touchMove() {

        //most important function is this

        val dx = Math.abs(motionX - currentX)
        val dy = Math.abs(motionY - currentY)

        //IMPORTANT, dx and dy must be greater than touchTolerance
         //https://youtu.be/Npwj-WVJxSU?t=1710

        if(dx >= touchTolerance || dy >= touchTolerance){
            path.quadTo(currentX,currentY, (motionX + currentX)/2,(motionY + currentY)/2)


            //make again motion x and y
            currentX = motionX
            currentY = motionY

            canvas1.drawPath(path,paint)

        }

        //once all done, we invalidate
        invalidate()

    }

    private fun touchStart() {
        path.reset() //we reset it first
        path.moveTo(motionX,motionY) //in X and Y pixel
        //when we changed position
        currentX = motionX
        currentY = motionY




    }

}