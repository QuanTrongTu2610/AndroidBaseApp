package com.example.androidbaseapp.presentation.customview.barchart

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.setPadding
import com.example.androidbaseapp.R
import com.example.androidbaseapp.presentation.customview.ColorHelper
import com.example.androidbaseapp.presentation.customview.DimensionHelper
import com.example.androidbaseapp.presentation.customview.ITabBarClickedHandler
import com.example.androidbaseapp.presentation.customview.PropertiesAnimationProvider
import com.example.androidbaseapp.common.Logger
import com.example.androidbaseapp.presentation.customview.DimensionHelper.pixelsToSp

@SuppressLint("CustomViewStyleable")
class TabBarCustom : ConstraintLayout, View.OnClickListener {

    private val data = mutableListOf<TabItemModel>()
    private val labelIds = mutableListOf<Int>()
    private var underLineBackGroundColor: Int = 0
    private var underLineForeGroundColor: Int = 0
    private var selectedTextColor: Int = 0
    private var unSelectedTextColor: Int = 0
    private var textSize: Float = 0F
    private var marginBetween: Float = 0F
    private var paddingTextLabel: Float = 0F
    private var callback: ITabBarClickedHandler? = null
    private var currentSelectedLabel: TextView? = null
    private lateinit var backGroundUnderLine: View
    private lateinit var foreGroundUnderLine: View


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

    private fun onGetAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(
                    it,
                    R.styleable.TabBarCustom,
                    0,
                    0
                )
            try {
                typedArray.apply {
                    selectedTextColor =
                        typedArray.getResourceId(R.styleable.TabBarCustom_selectedTextLabelColor, 0)
                    unSelectedTextColor =
                        typedArray.getResourceId(
                            R.styleable.TabBarCustom_unselectedTextLabelColor,
                            0
                        )
                    underLineBackGroundColor =
                        typedArray.getResourceId(
                            R.styleable.TabBarCustom_underLineBackGroundColor,
                            0
                        )
                    underLineForeGroundColor =
                        typedArray.getResourceId(
                            R.styleable.TabBarCustom_underLineForeGroundColor,
                            0
                        )
                    textSize =
                        typedArray.getDimension(R.styleable.TabBarCustom_textTabSize, 0F)
                    marginBetween =
                        typedArray.getDimension(R.styleable.TabBarCustom_customMarginBetween, 0F)
                    paddingTextLabel =
                        typedArray.getDimension(R.styleable.TabBarCustom_customPaddingTextLabel, 0F)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                typedArray.recycle()
            }
        }
    }

    fun setData(data: List<TabItemModel>) {
        Logger.d("setDataTabOptions: $data")
        clearData()
        this.data.addAll(data)
        post {
            initView()
            initAction()
        }
    }

    fun clearData() {
        this.data.clear()
        labelIds.forEach { removeView(findViewById(it)) }
    }

    private fun initView() {
        onAddViewsToLayout()
        onConstraintToLayout()
    }

    private fun initAction() {
        labelIds.forEach { findViewById<TextView>(it).setOnClickListener(this) }
    }

    /*setter*/
    fun setCallBack(iTabBarClickedHandler: ITabBarClickedHandler) {
        this.callback = iTabBarClickedHandler
    }

    /*getter*/
    fun getLabels(): List<TextView> {
        val labels = mutableListOf<TextView>()
        labelIds.forEach { labels.add(findViewById(it)) }
        return labels
    }

    /*labels should be as before adding foreground underline*/
    private fun onAddViewsToLayout() {
        addBackGroundUnderLine()
        addLabels()
        addForeGroundUnderLine()
    }

    private fun onConstraintToLayout() {
        constraintBackGroundUnderLine()
        constraintForeGroundUnderLine()
        constraintTextLabel()
    }

    private fun addForeGroundUnderLine() {
        findViewById<TextView>(labelIds[0]).let {
            it.post {
                foreGroundUnderLine = View(context)
                foreGroundUnderLine.apply {
                    layoutParams = LayoutParams(
                        it.width,
                        DimensionHelper.convertDpToPixel(1F, context).toInt()
                    )
                    setBackgroundColor(
                        ColorHelper.getColorById(context, underLineForeGroundColor)
                    )
                    id = View.generateViewId()
                }
                addView(foreGroundUnderLine)
            }
        }

    }

    private fun addBackGroundUnderLine() {
        backGroundUnderLine = View(context)
        backGroundUnderLine.apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                DimensionHelper.convertDpToPixel(1F, context).toInt()
            )
            setBackgroundColor(
                ColorHelper.getColorById(context, underLineBackGroundColor)
            )
            id = View.generateViewId()
        }

        addView(backGroundUnderLine)
    }

    private fun addLabels() {
        for (index in data.indices) {
            val label = TextView(context)
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            label.apply {
                label.setPadding(paddingTextLabel.toInt())
                layoutParams = params
                id = View.generateViewId()
                text = data[index].name
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                textSize = pixelsToSp(this@TabBarCustom.textSize, context)
                setTextColor(
                    ColorHelper.getColorById(
                        context,
                        if (index == 0) {
                            currentSelectedLabel = label
                            selectedTextColor
                        } else {
                            unSelectedTextColor
                        }
                    )
                )
            }

            addView(label)
            labelIds.add(label.id)
        }
    }

    private fun constraintBackGroundUnderLine() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this@TabBarCustom)
        constraintSet.apply {
            connect(
                backGroundUnderLine.id,
                ConstraintSet.RIGHT,
                this@TabBarCustom.id,
                ConstraintSet.RIGHT,
                marginBetween.toInt()
            )
            connect(
                backGroundUnderLine.id,
                ConstraintSet.LEFT,
                this@TabBarCustom.id,
                ConstraintSet.LEFT,
                marginBetween.toInt()
            )
            connect(
                backGroundUnderLine.id,
                ConstraintSet.BOTTOM,
                this@TabBarCustom.id,
                ConstraintSet.BOTTOM
            )
            setVerticalBias(backGroundUnderLine.id, 1F)
            applyTo(this@TabBarCustom)
        }
    }

    private fun constraintForeGroundUnderLine() {
        findViewById<TextView>(labelIds[0]).let {
            it.post {
                val constraintSet = ConstraintSet()
                constraintSet.apply {
                    clone(this@TabBarCustom)
                    connect(
                        foreGroundUnderLine.id,
                        ConstraintSet.LEFT,
                        this@TabBarCustom.id,
                        ConstraintSet.LEFT,
                        marginBetween.toInt()
                    )
                    connect(
                        foreGroundUnderLine.id,
                        ConstraintSet.BOTTOM,
                        backGroundUnderLine.id,
                        ConstraintSet.BOTTOM
                    )
                    connect(
                        foreGroundUnderLine.id,
                        ConstraintSet.TOP,
                        backGroundUnderLine.id,
                        ConstraintSet.TOP
                    )
                    setVerticalBias(backGroundUnderLine.id, 1F)
                    applyTo(this@TabBarCustom)
                }
            }
        }
    }

    private fun constraintTextLabel() {
        for (index in data.indices) {
            val constrainSet = ConstraintSet()
            constrainSet.apply {
                clone(this@TabBarCustom)
                if (index == 0) {
                    connect(
                        labelIds[index],
                        ConstraintSet.LEFT,
                        this@TabBarCustom.id,
                        ConstraintSet.LEFT,
                        marginBetween.toInt()
                    )
                    connect(
                        labelIds[index],
                        ConstraintSet.RIGHT,
                        labelIds[index + 1],
                        ConstraintSet.LEFT,
                        marginBetween.toInt()
                    )
                } else if (index == data.size - 1) {
                    connect(
                        labelIds[index],
                        ConstraintSet.LEFT,
                        labelIds[index - 1],
                        ConstraintSet.RIGHT,
                        marginBetween.toInt()
                    )
                } else {
                    connect(
                        labelIds[index],
                        ConstraintSet.LEFT,
                        labelIds[index - 1],
                        ConstraintSet.RIGHT
                    )
                    connect(
                        labelIds[index],
                        ConstraintSet.RIGHT,
                        labelIds[index + 1],
                        ConstraintSet.LEFT
                    )
                }
                connect(
                    labelIds[index],
                    ConstraintSet.TOP,
                    this@TabBarCustom.id,
                    ConstraintSet.TOP
                )
                connect(
                    labelIds[index],
                    ConstraintSet.BOTTOM,
                    backGroundUnderLine.id,
                    ConstraintSet.TOP
                )
                applyTo(this@TabBarCustom)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            in labelIds -> {
                currentSelectedLabel?.setTextColor(
                    ColorHelper.getColorById(
                        context,
                        unSelectedTextColor
                    )
                )
                (view as? TextView)?.setTextColor(
                    ColorHelper.getColorById(
                        context,
                        selectedTextColor
                    )
                )
                callback?.onLabelClick(labelIds.indexOf(view?.id), data[labelIds.indexOf(view?.id)].name)
                currentSelectedLabel = view as? TextView
                PropertiesAnimationProvider.moveHorizontalAnimation(
                    foreGroundUnderLine,
                    view as TextView,
                    1000L,
                    marginBetween
                ).start()
                PropertiesAnimationProvider.getViewChangeWidthAnimation(
                    foreGroundUnderLine,
                    foreGroundUnderLine.width,
                    view.width,
                    1000L
                ).start()
            }
        }
    }
}

data class TabItemModel(
    val name: String
)