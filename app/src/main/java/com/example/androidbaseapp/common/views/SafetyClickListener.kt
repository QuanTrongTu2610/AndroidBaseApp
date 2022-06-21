package com.example.androidbaseapp.common.views

import android.view.View

/**
 * It's prevent the click event is delivered to fast by setting timeout between 2 respective click
 *
 * @property delayTimeBetweenClick: Define the time between the delivery of two click events.
 */
class SafetyClickListener(
    private val delayTimeBetweenClick: Long = DEFAULT_DELAY_BETWEEN_CLICK
) : View.OnClickListener {
    companion object {
        private const val DEFAULT_DELAY_BETWEEN_CLICK = 500L

        // The reason it is set as global due to we want to handle time between more than 2 clickable view
        private var lastClickTime = 0L
    }

    private val listeners = mutableMapOf<Int, View.OnClickListener>()

    override fun onClick(view: View?) {
        val currentTimeMillis = System.currentTimeMillis()
        if (view != null && currentTimeMillis - lastClickTime >= delayTimeBetweenClick) {
            lastClickTime = currentTimeMillis
            listeners[view.id]?.onClick(view)
        }
    }

    fun addViewSafetyClickListener(view: View, listener: View.OnClickListener) {
        if (listeners.containsKey(view.id)) return
        listeners[view.id] = listener
        view.setOnClickListener(this)
    }

    fun removeViewSafetyClickListener(view: View) {
        listeners[view.id]?.apply { listeners.remove(view.id) }
    }

    fun clearAllSafetyClickListeners() = listeners.clear()
}