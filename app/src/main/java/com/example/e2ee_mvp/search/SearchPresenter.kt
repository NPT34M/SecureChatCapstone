package com.example.e2ee_mvp.search

class SearchPresenter(val view: SearchContract.View) : SearchContract.Presenter {
    init {
        view.presenter = this
    }
    override fun addToContact() {
    }

    override fun start() {
        return
    }
}