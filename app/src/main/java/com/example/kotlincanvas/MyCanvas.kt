package com.example.kotlincanvas

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

//const are always declared on top of the class
private const val stroke_width = 8f

class MyCanvas(context:Context):View(context) {

    //1.canvas
    //2.bitmap
    //.3.path
    //Then we draw

    //background color
    private val bgColor = ResourcesCompat.getColor(resources,R.color.white_one, null)
    private lateinit var   canvas1:Canvas
    private lateinit var   bitmap1: Bitmap



    //frame
    private lateinit var frame: Rect

    //pen or touch color
    private val penColor = ResourcesCompat.getColor(resources,R.color.black_one, null)

    //Touch, means we have to start the drawing from where the user has touched
    private val touchTolerance = ViewConfiguration.get(context).scaledEdgeSlop

    private val paint = Paint().apply {

        //quality of the paint
        color = penColor
        isAntiAlias = true //smooth of the edges
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = stroke_width
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND

    }

    //path===
    private var path = Path()


    private var motionX = 0f
    private var motionY = 0f
    private var currentX = 0f
    private var currentY = 0f






    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //cal bitmap and canvas

        //=====For multiple sketch, we need to create a bitmap and reset using recycle. :: means protected
        if(::bitmap1.isInitialized)bitmap1.recycle()

        bitmap1 = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas1 = Canvas(bitmap1)
        canvas1.drawColor(bgColor)

        //frame
        var inset  = 15
        frame = Rect(inset,inset,w-inset,h-inset)


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap1, 0f,0f,null)

        canvas?.drawRect(frame,paint)

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

       // var dx = Math.abs(motionX - currentX)
        var dx = Math.abs(motionX - currentX)
        var dy = Math.abs(motionY - currentY)

        //IMPORTANT, dx and dy must be greater than touchTolerance
         //https://youtu.be/Npwj-WVJxSU?t=1710

        if(dx >= touchTolerance || dy >= touchTolerance){

            path.quadTo(currentX, currentY, (motionX + currentX)/2,(motionY + currentY)/2)


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
        path.moveTo(motionX, motionY) //in X and Y pixel

        //when we change position
        currentX = motionX
        currentY = motionY




    }

}