package com.example.e2ee_mvp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e2ee_mvp.R
import com.example.e2ee_mvp.model.User
import com.squareup.picasso.Picasso

class SearchAdapter(val onclick: (String) -> Unit) :
    ListAdapter<User, SearchAdapter.SearchViewHolder>(object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.equals(newItem)
        }

    }) {
    class SearchViewHolder(view: View, onclick: (String) -> Unit) : RecyclerView.ViewHolder(view) {
private var user:User?=null
        private val textView:TextView
        private val imageView:ImageView
        init {
            textView = view.findViewById(R.id.tvSearchUsername)
            imageView = view.findViewById(R.id.imgSearchUser)
        }
        fun bindData(user:User){
            this.user = user
            textView.text = user.username
            Picasso.get().load(user.profileImage).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_row,parent,false)
        return SearchViewHolder(view,onclick)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }
}