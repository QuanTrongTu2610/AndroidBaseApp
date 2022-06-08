package com.example.androidbaseapp.domain

interface SingleUseCaseWithParameter<P, R> {
    suspend fun execute(parameter: P? = null): R
}