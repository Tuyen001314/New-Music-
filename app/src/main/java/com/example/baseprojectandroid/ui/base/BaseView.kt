package com.example.baseprojectandroid.ui.base

interface BaseView {
    fun getContentViewId(): Int

    fun initializeViews() {}

    fun registerListeners() {}

    fun initializeData() {}

    fun registerObservers() {}
}