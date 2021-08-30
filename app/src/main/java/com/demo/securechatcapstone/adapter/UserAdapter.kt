package com.demo.securechatcapstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.model.User
import com.squareup.picasso.Picasso

class UserAdapter(
    val onClick: (User) -> Unit,
    val onImageClick: (User) -> Unit,
    val onlongclick: (User) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var data: List<User> = mutableListOf()

    class UserViewHolder(
        view: View,
        onClick: (User) -> Unit,
        onImageClick: (User) -> Unit,
        onlongclick: (User) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private var user: User? = null
        private val textView: TextView
        private val imageView: ImageView

        init {
            textView = view.findViewById(R.id.tvSearchUsername)
            imageView = view.findViewById(R.id.imgSearchUser)
            imageView.setOnClickListener {
                user?.let { onImageClick.invoke(it) }
            }
            view.setOnClickListener {
                user?.let { onClick.invoke(it) }
            }
            view.setOnLongClickListener {
                user?.let { onlongclick.invoke(it) }
                true
            }
        }

        fun bindData(user: User) {
            this.user = user
            textView.text = user.username
            Picasso.get().load(user.profileImage).into(imageView)
        }
    }

    fun setData(listUsers: List<User>) {
        data = listUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_user_row, parent, false)
        return UserViewHolder(view, onClick, onImageClick, onlongclick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}