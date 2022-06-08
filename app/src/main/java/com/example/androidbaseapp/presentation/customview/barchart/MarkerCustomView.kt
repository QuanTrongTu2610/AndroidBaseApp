package com.example.androidbaseapp.presentation.customview.barchart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import com.example.androidbaseapp.R
import com.example.androidbaseapp.presentation.customview.DimensionHelper
import com.example.androidbaseapp.presentation.customview.ViewAnimationProvider
import kotlin.math.min


/**
 * Marker of column table
 * */
class MarkerCustomView : View {

    /*triangle*/
    private val paintTriangle = Paint(Paint.ANTI_ALIAS_FLAG)
    private val firsPoint = Point()
    private val secondPoint = Point()
    private val thirdPoint = Point()
    private val fourthPoint = Point()
    private val fifthPoint = Point()
    private val sixthPoint = Point()
    private val triangleHeight = DimensionHelper.convertDpToPixel(8f, context)
    private val triangleWidth = DimensionHelper.convertDpToPixel(12f, context)

    // default
    private var color: Int = 0

    /*Text*/
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var text: String = "None"
    private var textColor: Int = 0
    private var textSize: Float = 30f
    private var textWidth: Int = 0
    private var textHeight: Int = 0
    private val textPaddingTop = DimensionHelper.convertDpToPixel(4F, context)
    private val textPaddingBottom = DimensionHelper.convertDpToPixel(8f, context)
    private val textPaddingLeft = DimensionHelper.convertDpToPixel(4F, context)
    private val textPaddingRight = DimensionHelper.convertDpToPixel(4F, context)

    /*Rectangle*/
    private val rectanglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rectangleTopPos = 0F
    private var rectangleLeftPos = 0F
    private var rectangleRightPos = 0F
    private var rectangleBottomPos = 0F
    private var rectangleWidth = 0
    private var rectangleHeight = 0

    /*local variable*/
    private val duration = 700L
    private var fadeAnimation: Animation? = null


    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureTextView()

        rectangleWidth = (textWidth + textPaddingLeft + textPaddingRight).toInt()
        rectangleHeight = (textHeight + textPaddingTop + textPaddingBottom).toInt()

        /*after calculate*/
        val desireWidth = rectangleWidth
        val desireHeight = rectangleHeight + triangleHeight

        setMeasuredDimension(
            measureDimension(desireWidth, widthMeasureSpec),
            measureDimension(desireHeight.toInt(), heightMeasureSpec)
        )
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int

        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    private fun measureTextView() {
        val mBound = Rect()
        textPaint.textSize = textSize
        textPaint.getTextBounds(text, 0, text.length, mBound)
        textHeight = mBound.height()
        textWidth = mBound.width()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rectangleLeftPos = 0F
        rectangleRightPos = width.toFloat()
        rectangleTopPos = 0F
        rectangleBottomPos = rectangleHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTriangle(canvas)
        drawRectangle(canvas)
        drawText(canvas)
    }

    @SuppressLint("Recycle", "ResourceAsColor", "CustomViewStyleable")
    private fun init(context: Context?, attrs: AttributeSet? = null) {
        val typedArray = context?.obtainStyledAttributes(
            attrs,
            R.styleable.TriangleMessageCustomView,
            0,
            0
        ) ?: return
        try {
            text =
                typedArray.getText(R.styleable.TriangleMessageCustomView_labelText).toString()
            color =
                typedArray.getResourceId(R.styleable.TriangleMessageCustomView_color, 0)
            textColor =
                typedArray.getResourceId(R.styleable.TriangleMessageCustomView_textColor, 0)
            textSize =
                typedArray.getDimension(R.styleable.TriangleMessageCustomView_textSize, 0F)
        } catch (e: Exception) {

        } finally {
            typedArray.recycle()
        }
    }

    fun setText(value: String) {
        text = value
        requestLayout()
    }

    fun setBackGroundColor(value: Int) {
        color = value
        postInvalidate()
    }

    fun setTextColor(value: Int) {
        textColor = value
        postInvalidate()
    }

    fun setTextSize(value: Float) {
        textSize = DimensionHelper.convertDpToPixel(value, context)
        requestLayout()
    }

    /*------------------------------------------------*/
    private fun drawTriangle(canvas: Canvas?) {
        paintTriangle.style = Paint.Style.FILL
        paintTriangle.color = color
        firsPoint.x = width / 2
        firsPoint.y = rectangleHeight

        secondPoint.x = (firsPoint.x + triangleWidth / 2).toInt()
        secondPoint.y = firsPoint.y


        thirdPoint.x = (width / 2 + triangleWidth / 3).toInt()
        thirdPoint.y = (rectangleHeight + triangleHeight * (3 / 5)).toInt()

        fourthPoint.x = width / 2
        fourthPoint.y = (rectangleHeight + triangleHeight).toInt()

        fifthPoint.x = (width / 2 - triangleWidth / 3).toInt()
        fifthPoint.y = (rectangleHeight + triangleHeight * (3 / 5)).toInt()

        sixthPoint.x = (width / 2 - triangleWidth / 2).toInt()
        sixthPoint.y = rectangleHeight

        val path = Path()
        path.apply {
            moveTo(firsPoint.x.toFloat(), firsPoint.y.toFloat())
            lineTo(secondPoint.x.toFloat(), secondPoint.y.toFloat())
            lineTo(thirdPoint.x.toFloat(), thirdPoint.y.toFloat())
            cubicTo(
                thirdPoint.x.toFloat(), thirdPoint.y.toFloat(),
                fourthPoint.x.toFloat(), fourthPoint.y.toFloat(),
                fifthPoint.x.toFloat(), fifthPoint.y.toFloat()
            )
            lineTo(fifthPoint.x.toFloat(), fifthPoint.y.toFloat())
            lineTo(sixthPoint.x.toFloat(), sixthPoint.y.toFloat())
            lineTo(firsPoint.x.toFloat(), firsPoint.y.toFloat())
        }
        canvas?.drawPath(path, paintTriangle)
    }

    private fun drawRectangle(canvas: Canvas?) {
        rectanglePaint.color = color
        // draw rectangle
        canvas?.drawRoundRect(
            rectangleLeftPos,
            rectangleTopPos,
            rectangleRightPos,
            rectangleBottomPos,
            5F, 5F,
            rectanglePaint
        )
    }

    @SuppressLint("ResourceAsColor")
    private fun drawText(canvas: Canvas?) {
        textPaint.apply {
            color = textColor
            style = Paint.Style.FILL
            textSize = textSize
            textAlign = Paint.Align.CENTER
        }
        text.let {
            canvas?.drawText(
                it,
                (width / 2).toFloat(),
                rectangleHeight.toFloat() / 2.5F + textPaddingTop,
                textPaint
            )
        }
    }

    /*animation*/
    fun initFadeInAnimation(): MarkerCustomView {
        fadeAnimation = ViewAnimationProvider.fadeAnimation(
            duration = duration
        )
        return this
    }

    fun startFadeInAnimation() = fadeAnimation?.let { this.startAnimation(it) }

    fun clearFadeInAnimation() = this.clearAnimation()
}