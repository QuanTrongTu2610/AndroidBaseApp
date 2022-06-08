package com.example.androidbaseapp.domain

interface SingleUseCase<out T> {
    suspend fun execute(): T
}