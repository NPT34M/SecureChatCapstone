package com.demo.securechatcapstone.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.adapter.SearchAdapter
import com.demo.securechatcapstone.model.User
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment() : Fragment(R.layout.fragment_search), SearchContract.View {

    @Inject
    override lateinit var presenter: SearchContract.Presenter

    private val adapter = SearchAdapter {
        presenter.addToContact(it)
    }

    override fun showUser(listUser: List<User>) {
        adapter?.submitList(listUser)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                presenter.getCurrentUserFriend(getSearchText())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.getCurrentUserFriend(getSearchText())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        recyclerViewSearch.adapter = adapter
    }

    override fun addSuccess() {
        Toast.makeText(requireContext(), "Add Friend Success!", Toast.LENGTH_LONG).show()
    }

    override fun addFail() {
        Toast.makeText(requireContext(), "Add Friend Fail!", Toast.LENGTH_LONG).show()
    }

    override fun getSearchText() :String{
        return edtSearch.text.toString()
    }
}