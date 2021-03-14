package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var mRight = 0F

    private var downloadStatus : ButtonState = ButtonState.Clicked  // The active selection.
    private val cornerRadius = 10.0f


    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 45.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
    }


    init {
        isClickable = true
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = Color.BLUE

        var bounds = canvas?.clipBounds

        var mRectangle = RectF()
        var mOval = RectF()


        val arcDiameter = cornerRadius * 2
        val arcRectSize = measuredHeight.toFloat() - paddingBottom.toFloat() - arcDiameter



        if (bounds != null) {

            mRectangle = RectF(bounds.left.toFloat(),
                bounds.top.toFloat(), 0F, bounds.bottom.toFloat())

            mOval = RectF(paddingEnd + arcDiameter ,
                paddingTop + arcDiameter, 150F, bounds.bottom.toFloat()
            )

/*
            canvas?.drawArc(paddingEnd + arcDiameter ,
                paddingTop + arcDiameter,
                arcRectSize,
                arcRectSize,
                0f,
                mRight/2,
                true,
                arcPaint)*/

            canvas?.drawRect(mRectangle, paint)

        }

        when (downloadStatus) {
            ButtonState.Clicked -> {
                mRight = 0f
                textPaint.color = Color.WHITE
            }
            ButtonState.Loading -> {
                textPaint.color = Color.LTGRAY

                if (bounds != null) {
                    mRight += 10
                    mRectangle = RectF(bounds.left.toFloat(),
                        bounds.top.toFloat(), mRight, bounds.bottom.toFloat())

                    canvas?.drawRect(mRectangle, paint)

                    if (mRight < bounds.right.toFloat()) {
                        invalidate()
                    } else {
                        downloadStatus = downloadStatus.next()
                    }
                }

              /*  canvas?.drawArc(paddingEnd + arcDiameter ,
                    paddingTop + arcDiameter,
                    arcRectSize,
                    arcRectSize,
                    0f,
                    mRight/2,
                    true,
                    arcPaint)*/

                canvas?.drawArc( mOval, 0f,
                    mRight/2,
                    true,arcPaint)

            }
            ButtonState.Completed -> {
                textPaint.color = Color.GREEN

            }
        }

        canvas?.drawText(downloadStatus.getLabel(), (width/2).toFloat(), (height/2).toFloat(), textPaint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()

        downloadStatus = downloadStatus.next()
        invalidate()
        return true
    }


}
