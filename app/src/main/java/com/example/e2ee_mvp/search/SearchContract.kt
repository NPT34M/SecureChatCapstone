package com.example.e2ee_mvp.search

import com.example.e2ee_mvp.BasePresenter
import com.example.e2ee_mvp.BaseView
import com.example.e2ee_mvp.model.User

interface SearchContract {
    interface View : BaseView<Presenter> {
        fun showUser(listUser: List<User>)
        fun addSuccess()
        fun addFail()
    }

    interface Presenter : BasePresenter {
        fun addToContact()
    }
}