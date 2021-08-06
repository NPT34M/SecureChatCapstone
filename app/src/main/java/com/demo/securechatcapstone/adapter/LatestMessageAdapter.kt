package com.demo.securechatcapstone.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.securechatcapstone.R
import com.demo.securechatcapstone.model.LatestMessageModel
import com.demo.securechatcapstone.model.User
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class LatestMessageAdapter(val onclick: (User) -> Unit) :
    ListAdapter<LatestMessageModel, LatestMessageAdapter.LatestMessageViewHolder>(object :
        DiffUtil.ItemCallback<LatestMessageModel>() {
        override fun areItemsTheSame(
            oldItem: LatestMessageModel,
            newItem: LatestMessageModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LatestMessageModel,
            newItem: LatestMessageModel
        ): Boolean {
            return oldItem.equals(newItem)
        }

        override fun getChangePayload(
            oldItem: LatestMessageModel,
            newItem: LatestMessageModel
        ): Any? {
            return Bundle().apply {
                putBoolean("message", oldItem.text != newItem.text)
//                putBoolean("timestamp", oldItem.timestamp != newItem.timestamp)
            }
        }
    }) {
    class LatestMessageViewHolder(view: View, onclick: (User) -> Unit) :
        RecyclerView.ViewHolder(view) {
        var latesMessage: LatestMessageModel? = null
        private var user: User? = null
        private val userTextView: TextView
        private val messageTextView: TextView
        private val imageView: ImageView
        private val latestTime: TextView

        init {
            userTextView = view.findViewById(R.id.tvUsernameLatestMessage)
            messageTextView = view.findViewById(R.id.tvLatestMessage)
            imageView = view.findViewById(R.id.imgLatestMessage)
            latestTime = view.findViewById(R.id.tvLatestDateTime)
            view.setOnClickListener {
                user?.let { onclick.invoke(it) }
            }
        }

        fun convertLongToDate(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("yyyy/MM/dd HH:mm")
            return format.format(date)
        }

        fun bindData(latesMessage: LatestMessageModel) {
            this.user = latesMessage.user
            this.latesMessage = latesMessage
            userTextView.text = latesMessage.user?.username
            if (latesMessage.image) {
                messageTextView.text = "[image]"
            } else {
                messageTextView.text = if(latesMessage.text.length>17) latesMessage.text.substring(0,16)+"..." else latesMessage.text
            }
            latestTime.text = convertLongToDate(latesMessage.timestamp * 1000)
            Picasso.get().load(latesMessage.user?.profileImage).into(imageView)
        }

        fun bindMessageData(latesMessage: LatestMessageModel) {
            if (latesMessage.image) {
                messageTextView.text = "[image]"
            } else {
                messageTextView.text = if(latesMessage.text.length>17) latesMessage.text.substring(0,16)+"..." else latesMessage.text
            }
            latestTime.text = convertLongToDate(latesMessage.timestamp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestMessageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.latest_mess_row, parent, false)
        return LatestMessageViewHolder(view, onclick)
    }

    override fun onBindViewHolder(holder: LatestMessageViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    override fun onBindViewHolder(
        holder: LatestMessageViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val bundle = payloads.first() as Bundle
            bundle.getBoolean("message", false).takeIf { it }?.let {
                holder.bindMessageData(getItem(position))
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

}