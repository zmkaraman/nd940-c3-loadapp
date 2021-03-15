package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val arcDiameter = 40f

    private var buttonFirstBGColor = 0
    private var buttonSecondBGColor = 0
    private var buttonTextColor = 0
    private var arcPaintColor = 0

    private var progress: Float = 0f

    private var valueAnimator = ValueAnimator()


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Clicked) { p, old, new ->

        valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                progress = animatedValue as Float
                invalidate()
            }
            duration = 3000
            when (new) {
                ButtonState.Loading -> {
                    textPaint.color = Color.GREEN
                }
                ButtonState.Completed -> {
                    textPaint.color = arcPaintColor
                }
                ButtonState.Clicked -> {
                    textPaint.color = buttonTextColor
                }
            }
            start()
        }
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 45.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = ContextCompat.getColor(context, R.color.white)
    }

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.white)
    }

    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonFirstBGColor = getColor(R.styleable.LoadingButton_downloadBGColor, 0)
            buttonSecondBGColor = getColor(R.styleable.LoadingButton_downloadingBGColor, 0)
            buttonTextColor = getColor(R.styleable.LoadingButton_downloadTextColor, 0)
            arcPaintColor = getColor(R.styleable.LoadingButton_arcPaintColor, 0)
        }


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        arcPaint.color = arcPaintColor
        paint.color = buttonSecondBGColor

        var bounds = canvas?.clipBounds

        var mRectangle = RectF()
        if (bounds != null) {

            mRectangle = RectF(
                bounds.left.toFloat(),
                bounds.top.toFloat(), 0F, bounds.bottom.toFloat()
            )

            canvas?.drawRect(mRectangle, paint)
        }


        when (buttonState) {
            ButtonState.Clicked -> {

            }
            ButtonState.Loading -> {
                if (bounds != null) {
                    mRectangle = RectF(
                        bounds.left.toFloat(),
                        bounds.top.toFloat(),
                        progress * bounds.right.toFloat(),
                        bounds.bottom.toFloat()
                    )
                    canvas?.drawRect(mRectangle, paint)
                }

                canvas?.drawText(
                    buttonState.getLabel(),
                    (width / 2).toFloat(),
                    (height / 2).toFloat(),
                    textPaint
                )

                var progressVal = progress * 360f
                canvas?.drawArc(
                    measuredWidth - 4 * arcDiameter,
                    arcDiameter,
                    measuredWidth - 2 * arcDiameter,
                    measuredHeight - arcDiameter,
                    0f,
                    progressVal,
                    true,
                    arcPaint
                )

                if ( progress == 1f) {
                    buttonState =  buttonState.next() //finish
                    //invalidate()
                }

            }
            ButtonState.Completed -> {

            }
        }


        canvas?.drawText(
            buttonState.getLabel(),
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            textPaint
        )


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
        return true
    }

    fun startAnimation(): Boolean {
        buttonState = buttonState.next()
        invalidate()
        return true
    }

}
