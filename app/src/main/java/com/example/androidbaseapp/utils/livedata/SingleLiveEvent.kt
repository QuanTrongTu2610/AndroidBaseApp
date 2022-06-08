package com.example.androidbaseapp.utils.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.androidbaseapp.utils.Logger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This class used to abstract single event
 * */
open class SingleLiveEvent<T>(
    initValue: T? = null
) : MutableLiveData<T>(initValue) {

    private val pending: AtomicBoolean = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Logger.d("Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner) { value ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        }
    }

    override fun setValue(value: T?) {
        Logger.d("setValue $value")
        pending.set(true)
        super.setValue(value)
    }

    override fun postValue(value: T) {
        Logger.d("postValue $value")
        pending.set(true)
        super.postValue(value)
    }
}