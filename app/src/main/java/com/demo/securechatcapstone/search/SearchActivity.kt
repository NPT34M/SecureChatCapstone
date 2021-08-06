package com.demo.securechatcapstone.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.securechatcapstone.R

class SearchActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        SearchFragment().also {
            SearchPresenter(it)
        }.let {
            supportFragmentManager.beginTransaction().replace(R.id.search_frame_layout,it).commit()
        }
    }
}