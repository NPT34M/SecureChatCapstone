package com.example.e2ee_mvp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e2ee_mvp.R
import com.example.e2ee_mvp.model.User
import com.squareup.picasso.Picasso

class UserAdapter(val onClick: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var data: List<User> = mutableListOf()

    class UserViewHolder(view: View, onClick: (User) -> Unit) : RecyclerView.ViewHolder(view) {
        private var user: User? = null
        private val textView: TextView
        private val imageView: ImageView

        init {
            textView = view.findViewById(R.id.tvSearchUsername)
            imageView = view.findViewById(R.id.imgSearchUser)
//            imageView.setOnClickListener {
//                user?.let { onClick.invoke(it) }
//            }
            view.setOnClickListener {
                user?.let { onClick.invoke(it) }
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
        return UserViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        holder.textView.text = data[position].username
//        Picasso.get().load(data[position].profileImage).into(holder.imageView)
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}