package com.example.androidbaseapp.presentation.customview.barchart

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.androidbaseapp.presentation.customview.PropertiesAnimationProvider


@SuppressLint("CustomViewStyleable")
class ColumnCustomView(
    context: Context?,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val path = Path()
    private var rectRadius = 0F
    private var rectangleWidth = 0
    private var rectangleHeight = 0
    private var rectangleTopPos = 0F
    private var rectangleLeftPos = 0F
    private var rectangleRightPos = 0F
    private var rectangleBottomPos = 0F
    private val firsPoint = Point()
    private val secondPoint = Point()
    private val thirdPoint = Point()
    private val fourthPoint = Point()
    private val firstQuadPoint = Point()
    private val secondQuadPoint = Point()
    private var backGroundColor: Int = 0
    private var selectedBackGroundColor: Int = 0
    private val changeHeightDuration = 1000L
    private val changeColorDuration = 1000L
    private var changeHeightAnim: Animator? = null
    private var changeColorAnim: Animator? = null
    private val rectanglePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rectangleWidth = width
        rectangleHeight = height

        rectangleLeftPos = 0F
        rectangleRightPos = width.toFloat()
        rectangleTopPos = (0 + width / 5).toFloat()
        rectangleBottomPos = rectangleHeight.toFloat()
        rectRadius = (rectangleWidth / 5).toFloat()
    }

    /*setter*/
    fun setColumnBackGroundColor(color: Int) {
        backGroundColor = color
        postInvalidate()
    }

    fun setSelectedColumnBackGroundColor(color: Int) {
        selectedBackGroundColor = color
    }

    override fun setBackgroundColor(color: Int) {
        setColumnBackGroundColor(color)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rectanglePaint.color = backGroundColor
        rectanglePaint.style = Paint.Style.FILL
        drawRect(canvas)
        drawBoundRect(canvas)
    }

    private fun drawBoundRect(canvas: Canvas?) {
        firsPoint.x = 0
        firsPoint.y = rectRadius.toInt()
        secondPoint.x = rectRadius.toInt()
        secondPoint.y = 0
        thirdPoint.x = (rectangleWidth - rectRadius).toInt()
        thirdPoint.y = 0
        fourthPoint.x = rectangleWidth
        fourthPoint.y = (0 + rectRadius).toInt()
        firstQuadPoint.x = 0
        firstQuadPoint.y = 0
        secondQuadPoint.x = rectangleWidth
        secondQuadPoint.y = 0
        path.moveTo(firsPoint.x.toFloat(), firsPoint.y.toFloat())
        path.quadTo(
            firstQuadPoint.x.toFloat(),
            firstQuadPoint.y.toFloat(),
            secondPoint.x.toFloat(),
            secondPoint.y.toFloat()
        )
        path.lineTo(thirdPoint.x.toFloat(), thirdPoint.y.toFloat())
        path.quadTo(
            secondQuadPoint.x.toFloat(),
            secondQuadPoint.y.toFloat(),
            fourthPoint.x.toFloat(),
            fourthPoint.y.toFloat()
        )
        path.lineTo(firsPoint.x.toFloat(), firsPoint.y.toFloat())
        canvas?.drawPath(path, rectanglePaint)
    }

    private fun drawRect(canvas: Canvas?) {
        canvas?.drawRect(
            RectF(
                rectangleLeftPos,
                rectangleTopPos,
                rectangleRightPos,
                rectangleBottomPos
            ),
            rectanglePaint
        )
    }

    /*helper*/
    fun initChangeHeightAnimation(destinationHeight: Int): ColumnCustomView {
        post {
            changeHeightAnim = PropertiesAnimationProvider.getViewChangeHeightAnimation(
                this,
                height,
                destinationHeight,
                changeHeightDuration
            )
        }
        return this
    }

    fun initChangeBackgroundColorAnimation(): ColumnCustomView {
        changeColorAnim = PropertiesAnimationProvider.changeBackGroundColorAnimation(
            this,
            backGroundColor,
            selectedBackGroundColor,
            changeColorDuration
        )
        return this
    }

    fun startChangeHeightAnimation(): ColumnCustomView {
        post {
            changeHeightAnim?.start()
        }
        return this
    }

    fun stopChangeHeightAnimation(): ColumnCustomView {
        changeHeightAnim?.cancel()
        return this
    }

    fun startChangeColorAnimation(): ColumnCustomView {
        changeColorAnim?.start()
        return this
    }

    fun stopChangeColorAnimation(): ColumnCustomView {
        changeColorAnim?.cancel()
        return this
    }
}