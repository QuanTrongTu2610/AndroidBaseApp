package com.example.androidbaseapp.common.views

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * bindView delegation
 * */
inline fun <reified T : ViewBinding> Activity.viewBinding() =
    ActivityViewBindingDelegate(T::class.java)

class ActivityViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>
) : ReadOnlyProperty<Activity, T> {

    companion object {
        private const val INFLATE_METHOD = "inflate"
    }

    private var binding: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return if (binding == null) {
            val inflateMethod = bindingClass.getMethod(INFLATE_METHOD, LayoutInflater::class.java)
            val invokeLayout = inflateMethod.invoke(null, thisRef.layoutInflater) as T
            thisRef.setContentView(invokeLayout.root)
            invokeLayout.also { this.binding = it }
        } else binding!!
    }
}