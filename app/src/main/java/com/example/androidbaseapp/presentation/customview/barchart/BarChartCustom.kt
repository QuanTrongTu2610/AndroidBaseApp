package com.example.androidbaseapp.presentation.customview.barchart

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import com.example.androidbaseapp.R
import com.example.androidbaseapp.presentation.customview.ColorHelper
import com.example.androidbaseapp.presentation.customview.DimensionHelper
import com.example.androidbaseapp.presentation.customview.FormatHelper
import com.example.androidbaseapp.presentation.customview.IColumnClickedHandler
import com.example.androidbaseapp.utils.Logger
import java.lang.Exception

@SuppressLint("ResourceType", "CustomViewStyleable")
class BarChartCustom : ConstraintLayout, View.OnClickListener {

    /*configured variables*/
    private val data = mutableListOf<ColumnModel>()
    private var columnColor: Int = 0
    private var labelColor: Int = 0
    private var markerColor: Int = 0
    private var markerTextColor: Int = 0
    private var selectedColumnColor: Int = 0
    private var marginBetweenColumn: Float = 0F
    private lateinit var minGuideLine: Guideline
    private lateinit var maxGuideline: Guideline
    private val listColumnViewId = mutableListOf<Int>()
    private val listMarkerViewId = mutableListOf<Int>()
    private val listLabelViewId = mutableListOf<Int>()
    private var currentTabPos = 0
    private var onColumnClickCallBack: IColumnClickedHandler? = null
    private var defaultClickedColumnPos: Int? = null

    /*declare variables*/
    @Volatile
    private var currentClickedColumn: ColumnCustomView? = null
    private var currentShowedMarker: MarkerCustomView? = null

    /*overload constructor*/
    constructor(context: Context) : super(context) {
        initBasic()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initBasic(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initBasic(attrs)
    }

    private fun initBasic(attrs: AttributeSet? = null) {
        onGetAttrs(attrs)
    }

    private fun initView() {
        onAddViewsToLayout()
        onConstraintToLayout()
        initAnimation()
    }

    private fun initAction() {
        handleColumnAction()
    }

    private fun initDefaultSelectedColumn() {
        defaultClickedColumnPos?.let {
            val clickedView  = findViewById<View>(listColumnViewId[it])
            beginChangeColorColumnAnimation(clickedView as? ColumnCustomView)
            beginMarkerAnimation(clickedView as? ColumnCustomView)
            onColumnClickCallBack?.onColumnClick(
                currentTabPos,
                listColumnViewId.indexOf(clickedView?.id)
            )
        }
    }

    private fun onGetAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(
                    it,
                    R.styleable.BarChartCustom,
                    0,
                    0
                )
            try {
                columnColor =
                    typedArray.getResourceId(R.styleable.BarChartCustom_defaultColumnColor, 0)
                labelColor =
                    typedArray.getResourceId(R.styleable.BarChartCustom_defaultLabelColor, 0)
                markerColor =
                    typedArray.getResourceId(R.styleable.BarChartCustom_defaultMarkerColor, 0)
                markerTextColor =
                    typedArray.getResourceId(R.styleable.BarChartCustom_defaultMarkerTextColor, 0)
                selectedColumnColor =
                    typedArray.getResourceId(R.styleable.BarChartCustom_selectedColumnColor, 0)
                marginBetweenColumn =
                    typedArray.getDimension(R.styleable.BarChartCustom_marginBetweenColumn, 0F)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                typedArray.recycle()
            }
        }
    }

    /*setter*/
    fun setColumnClickCallBack(iColumnClickedHandler: IColumnClickedHandler) {
        onColumnClickCallBack = iColumnClickedHandler
    }

    fun setColumnColor(color: Int) {
        columnColor = color
    }

    fun setMarkerColor(color: Int) {
        markerColor = color
    }

    fun setLabelColor(color: Int) {
        labelColor = color
    }

    fun setMarkerTextColor(color: Int) {
        markerColor = color
    }

    fun setSelectedColumnColor(color: Int) {
        selectedColumnColor = color
    }

    fun setColumnSpaceMargin(value: Float) {
        marginBetweenColumn = DimensionHelper.convertDpToPixel(value, context)
    }

    fun clickedDefaultColumn(columnPos: Int) {
        defaultClickedColumnPos = columnPos
    }

    fun setData(data: List<ColumnModel>) {
        Logger.d("setColumnData: $data")
        clearData()
        this.data.addAll(data)
        post {
            initView()
            initAction()
            initDefaultSelectedColumn()
        }
    }

    private fun clearData() {
        this.data.clear()
        this.listColumnViewId.forEach { removeView(findViewById(it)) }
        this.listMarkerViewId.forEach { removeView(findViewById(it)) }
        this.listLabelViewId.forEach { removeView(findViewById(it)) }
    }

    /* calculate and draw*/
    private fun onAddViewsToLayout() {
        addGuideLines()
        addColumns()
        addLabels()
        addMarkers()
    }

    private fun onConstraintToLayout() {
        constraintColumns()
        constraintLabels()
        constraintMarkers()
    }

    private fun addGuideLines() {
        addMaxGuideLine()
        addMinGuideLine()
    }

    private fun addMaxGuideLine() {
        maxGuideline = Guideline(context)
        maxGuideline.id = Guideline.generateViewId()
        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layoutParams.orientation = LayoutParams.HORIZONTAL
        layoutParams.guidePercent = 0.22F
        maxGuideline.layoutParams = layoutParams
        addView(maxGuideline)
    }

    private fun addMinGuideLine() {
        minGuideLine = Guideline(context)
        minGuideLine.id = Guideline.generateViewId()
        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layoutParams.orientation = LayoutParams.HORIZONTAL
        layoutParams.guidePercent = 0.75F
        minGuideLine.layoutParams = layoutParams
        addView(minGuideLine)
    }

    private fun addColumns() {
        for (index in data.indices) {
            val column =
                ColumnCustomView(
                    context
                )
            column.apply {
                id = View.generateViewId()
                setColumnBackGroundColor(
                    ColorHelper.getColorById(
                        context, columnColor
                    )
                )
                setSelectedColumnBackGroundColor(
                    ColorHelper.getColorById(
                        context,
                        selectedColumnColor
                    )
                )
                layoutParams = LayoutParams(
                    0,
                    10
                )
            }
            addView(column)
            listColumnViewId.add(column.id)
        }
    }

    private fun addLabels() {
        for (index in data.indices) {
            val label = TextView(context)
            label.apply {
                id = TextView.generateViewId()
                text = data[index].label
                gravity = Gravity.CENTER_HORIZONTAL
                textSize = 8f
                setTextColor(ColorHelper.getColorById(context, labelColor))
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                ellipsize = TextUtils.TruncateAt.END
            }
            addView(label)
            listLabelViewId.add(label.id)
        }
    }

    private fun addMarkers() {
        for (element in data) {
            val marker =
                MarkerCustomView(
                    context
                )
            marker.apply {
                id = View.generateViewId()
                setText("0.0")
                setTextSize(8F)
                setBackGroundColor(ColorHelper.getColorById(context, markerColor))
                setTextColor(ColorHelper.getColorById(context, markerTextColor))
                layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                visibility = View.INVISIBLE
            }
            addView(marker)
            listMarkerViewId.add(marker.id)
        }
    }

    private fun constraintColumns() {
        for (index in data.indices) {
            val constrainSet = ConstraintSet()
            constrainSet.apply {
                clone(this@BarChartCustom)
                when (index) {
                    0 -> {
                        connect(
                            listColumnViewId[index],
                            ConstraintSet.LEFT,
                            this@BarChartCustom.id,
                            ConstraintSet.LEFT,
                            marginBetweenColumn.toInt()
                        )
                        connect(
                            listColumnViewId[index],
                            ConstraintSet.RIGHT,
                            listColumnViewId[index + 1],
                            ConstraintSet.LEFT,
                            (marginBetweenColumn / 2).toInt()
                        )
                    }
                    data.size - 1 -> {
                        connect(
                            listColumnViewId[index],
                            ConstraintSet.LEFT,
                            listColumnViewId[index - 1],
                            ConstraintSet.RIGHT,
                            (marginBetweenColumn / 2).toInt()
                        )
                        connect(
                            listColumnViewId[index],
                            ConstraintSet.RIGHT,
                            this@BarChartCustom.id,
                            ConstraintSet.RIGHT,
                            marginBetweenColumn.toInt()
                        )
                    }
                    else -> {
                        connect(
                            listColumnViewId[index],
                            ConstraintSet.LEFT,
                            listColumnViewId[index - 1],
                            ConstraintSet.RIGHT,
                            (marginBetweenColumn / 2).toInt()
                        )
                        connect(
                            listColumnViewId[index],
                            ConstraintSet.RIGHT,
                            listColumnViewId[index + 1],
                            ConstraintSet.LEFT,
                            (marginBetweenColumn / 2).toInt()
                        )
                    }
                }
                connect(
                    listColumnViewId[index],
                    ConstraintSet.TOP,
                    maxGuideline.id,
                    ConstraintSet.BOTTOM
                )
                connect(
                    listColumnViewId[index],
                    ConstraintSet.BOTTOM,
                    minGuideLine.id,
                    ConstraintSet.TOP
                )
                setVerticalBias(listColumnViewId[index], 1F)
                applyTo(this@BarChartCustom)

            }
        }
    }

    private fun constraintLabels() {
        for (index in data.indices) {
            val constrainSet = ConstraintSet()
            constrainSet.apply {
                clone(this@BarChartCustom)
                connect(
                    listLabelViewId[index],
                    ConstraintSet.TOP,
                    listColumnViewId[index],
                    ConstraintSet.BOTTOM
                )
                connect(
                    listLabelViewId[index],
                    ConstraintSet.RIGHT,
                    listColumnViewId[index],
                    ConstraintSet.RIGHT
                )
                connect(
                    listLabelViewId[index],
                    ConstraintSet.LEFT,
                    listColumnViewId[index],
                    ConstraintSet.LEFT
                )
                connect(
                    listLabelViewId[index],
                    ConstraintSet.BOTTOM,
                    this@BarChartCustom.id,
                    ConstraintSet.BOTTOM
                )
                applyTo(this@BarChartCustom)
            }
        }
    }

    private fun constraintMarkers() {
        for (index in data.indices) {
            val constrainSet = ConstraintSet()
            constrainSet.apply {
                clone(this@BarChartCustom)
                connect(
                    listMarkerViewId[index],
                    ConstraintSet.BOTTOM,
                    listColumnViewId[index],
                    ConstraintSet.TOP
                )
                connect(
                    listMarkerViewId[index],
                    ConstraintSet.RIGHT,
                    listColumnViewId[index],
                    ConstraintSet.RIGHT
                )
                connect(
                    listMarkerViewId[index],
                    ConstraintSet.LEFT,
                    listColumnViewId[index],
                    ConstraintSet.LEFT
                )
                connect(
                    listMarkerViewId[index],
                    ConstraintSet.TOP,
                    this@BarChartCustom.id,
                    ConstraintSet.TOP
                )
                setVerticalBias(listMarkerViewId[index], 1F)
                applyTo(this@BarChartCustom)
            }
        }
    }

    /*action*/
    private fun initAnimation() {
        initColumnAnimation(0)
        initMarkerAnimation()
    }

    private fun initColumnAnimation(pos: Int) {
        listColumnViewId.forEachIndexed { index, id ->
            findViewById<ColumnCustomView>(id)
                .initChangeHeightAnimation(
                    calculateColumnHeight(
                        pos,
                        data[index].values[pos].value,
                        height
                    )
                )
                .initChangeBackgroundColorAnimation()
        }
    }

    private fun initMarkerAnimation() {
        listMarkerViewId.forEachIndexed { _, id ->
            findViewById<MarkerCustomView>(id)
                .initFadeInAnimation()
        }
    }

    private fun handleColumnAction() {
        listColumnViewId.forEachIndexed { _, id ->
            findViewById<ColumnCustomView>(id)
                .startChangeHeightAnimation()
                .setOnClickListener(this)
        }
    }

    /*change column when select item of tab bar*/
    fun updateColumn(tabItemPos: Int) {
        currentTabPos = tabItemPos
        currentShowedMarker?.apply {
            visibility = View.INVISIBLE
            currentShowedMarker = null
        }
        currentClickedColumn?.apply {
            stopChangeColorAnimation()
            setColumnBackGroundColor(
                ColorHelper.getColorById(
                    context,
                    R.color.cl_cs_ternary_blue
                )
            )
        }
        for (index in 0 until data.size) {
            val column = findViewById<ColumnCustomView>(listColumnViewId[index])
            clearColumnAnimation(column)
            initColumnAnimation(tabItemPos)
            column.startChangeHeightAnimation()
        }
    }

    private fun clearColumnAnimation(view: ColumnCustomView) {
        view.apply {
            stopChangeColorAnimation()
            stopChangeHeightAnimation()
        }
    }

    /* override method*/
    override fun onClick(clickedView: View?) {
        when (clickedView?.id) {
            in listColumnViewId -> {
                beginChangeColorColumnAnimation(clickedView as? ColumnCustomView)
                beginMarkerAnimation(clickedView as? ColumnCustomView)
                onColumnClickCallBack?.onColumnClick(
                    currentTabPos,
                    listColumnViewId.indexOf(clickedView?.id)
                )
            }
        }
    }

    /* helper function to calculate bias*/
    private fun calculateColumnHeight(pos: Int, value: Float, mParentHeight: Int): Int {
        // find max value
        var sum = 0F
        data.forEach { if (sum < it.values[pos].value) sum = it.values[pos].value }
        val paddingTopPercent =
            (1 - (minGuideLine.layoutParams as LayoutParams).guidePercent)
        val paddingBottomPercent =
            (maxGuideline.layoutParams as LayoutParams).guidePercent
        val totalHeight = mParentHeight * (1 - paddingBottomPercent - paddingTopPercent)
        return ((totalHeight / sum) * value).toInt()
    }

    private fun beginChangeColorColumnAnimation(columnView: ColumnCustomView?) {
        currentClickedColumn?.apply {
            stopChangeColorAnimation()
            setColumnBackGroundColor(
                ColorHelper.getColorById(
                    context,
                    R.color.cl_cs_ternary_blue
                )
            )
        }
        currentClickedColumn = columnView
        columnView?.apply {
            startChangeColorAnimation()
        }
    }

    private fun beginMarkerAnimation(columnView: ColumnCustomView?) {
        currentShowedMarker?.apply {
            clearFadeInAnimation()
            visibility = View.INVISIBLE
        }
        currentShowedMarker = null
        val marker = findViewById<MarkerCustomView>(
            listMarkerViewId[listColumnViewId.indexOf(columnView?.id)]
        )
        currentShowedMarker = marker
        marker.apply {
            visibility = View.VISIBLE
            setText(FormatHelper.convertStringToMoneyFormat(data[listColumnViewId.indexOf(columnView?.id)].values[currentTabPos].value))
            startFadeInAnimation()
        }

    }
}

// the values will be change due to tab position
data class ColumnModel(
    val id: Int,
    val label: String,
    val values: MutableList<DetailColumnModelStatusValue>
)

data class DetailColumnModelStatusValue(
    val tabOption: String,
    val value: Float
)

