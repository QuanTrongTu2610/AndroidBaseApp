/*
 * Copyright (c) 2022 All Rights Reserved, Ingenico SA.
 */
package com.example.androidbaseapp.utils

import android.util.Log
import com.example.androidbaseapp.BuildConfig


/**
 * This class util used to set logcat for source code when we want to trace log.
 * It help us trace log easy and full of information include (class name, function name, position line of code, etc..)
 * It can turn on/off logcat.
 *
 */
internal object Logger {

    private val isDebug: Boolean
        get() = BuildConfig.DEBUG

    var testingModeEnable: Boolean = false

    private val shouldLog: Boolean
        get() = isDebug && !testingModeEnable

    private fun createLog(log: String, methodName: String, lineNumber: Int): String {
        val buffer = StringBuffer()
        buffer.append(" [")
        buffer.append(methodName)
        buffer.append(":")
        buffer.append(lineNumber)
        buffer.append("] ")
        buffer.append(log)

        return buffer.toString()
    }

    private fun getLogAddress(stackTraces: Array<StackTraceElement>): Triple<String, String, Int> {
        return stackTraces.getOrNull(1)?.let { element ->
            Triple(element.fileName, element.methodName, element.lineNumber)
        } ?: Triple("Not found", "Not found", -1)
    }

    fun e(message: String) {
        if (!shouldLog) {
            println(message)
            return
        }

        // Throwable instance must be created before any methods
        val (className, methodName, lineNumber) = getLogAddress(Throwable().stackTrace)
        Log.e(className, createLog(message, methodName, lineNumber))
    }

    fun e(error: Throwable) {
        if (!shouldLog) {
            println(error)
            return
        }

        // Throwable instance must be created before any methods
        val (className, methodName, lineNumber) = getLogAddress(Throwable().stackTrace)
        Log.e(className, createLog(error.toString(), methodName, lineNumber))
    }

    fun i(message: String) {
        if (!shouldLog) {
            println(message)
            return
        }

        // Throwable instance must be created before any methods
        val (className, methodName, lineNumber) = getLogAddress(Throwable().stackTrace)
        Log.i(className, createLog(message, methodName, lineNumber))
    }

    fun v(message: String) {
        if (!shouldLog) {
            println(message)
            return
        }

        // Throwable instance must be created before any methods
        val (className, methodName, lineNumber) = getLogAddress(Throwable().stackTrace)
        Log.v(className, createLog(message, methodName, lineNumber))
    }

    fun w(message: String) {
        if (!shouldLog) {
            println(message)
            return
        }

        // Throwable instance must be created before any methods
        val (className, methodName, lineNumber) = getLogAddress(Throwable().stackTrace)
        Log.w(className, createLog(message, methodName, lineNumber))
    }

    fun d(obj: Any?) {
        if (!shouldLog) {
            println(obj.toString())
            return
        }

        // Throwable instance must be created before any methods
        val (className, methodName, lineNumber) = getLogAddress(Throwable().stackTrace)
        Log.d(className, createLog(obj.toString(), methodName, lineNumber))
    }

    fun d(tag: String, obj: Any?) {
        if (!shouldLog) {
            println(obj.toString())
            return
        }

        // Throwable instance must be created before any methods
        val (className, methodName, lineNumber) = getLogAddress(Throwable().stackTrace)
        getLogAddress(Throwable().stackTrace)
        Log.d(tag, "[$className]${createLog(obj.toString(), methodName, lineNumber)}")
    }
}