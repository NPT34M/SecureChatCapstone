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

const val VIEW_TYPE_CHAT_LOG = 1
const val VIEW_TYPE_DAILY = 2

class LatestMessageAdapter(
    val onclicktype1: (User) -> Unit,
    val onclicktype2: (User) -> Unit,
    val onlongclick: (User) -> Unit
) :
    ListAdapter<LatestMessageModel, LatestMessageAdapter.Latest>(object :
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

    open class Latest(view: View) : RecyclerView.ViewHolder(view)

    class LatestMessageChatViewHolder(
        view: View,
        onclicktype1: (User) -> Unit,
        onlongclick: (User) -> Unit
    ) :
        Latest(view) {
        var latesMessage: LatestMessageModel? = null
        private var user: User? = null
        private val userTextView: TextView
        private val messageTextView: TextView
        private val imageView: ImageView
        private val latestTime: TextView

        init {
            userTextView = view.findViewById(R.id.tvUsernameLatestMessageDaily)
            messageTextView = view.findViewById(R.id.tvLatestMessageDaily)
            imageView = view.findViewById(R.id.imgLatestMessage)
            latestTime = view.findViewById(R.id.tvLatestDateTimeDaily)
            view.setOnClickListener {
                user?.let { onclicktype1.invoke(it) }
            }
            view.setOnLongClickListener {
                user?.let { onlongclick.invoke(it) }
                true
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
                messageTextView.text =
                    if (latesMessage.text.length > 17) latesMessage.text.substring(
                        0,
                        16
                    ) + "..." else latesMessage.text
            }
            latestTime.text = convertLongToDate(latesMessage.timestamp * 1000)
            Picasso.get().load(latesMessage.user?.profileImage).into(imageView)
        }


        fun bindMessageData(latesMessage: LatestMessageModel) {
            if (latesMessage.image) {
                messageTextView.text = "[image]"
            } else {
                messageTextView.text =
                    if (latesMessage.text.length > 17) latesMessage.text.substring(
                        0,
                        16
                    ) + "..." else latesMessage.text
            }
            latestTime.text = convertLongToDate(latesMessage.timestamp)
        }
    }

    class LatestMessageDailyViewHolder(
        view: View,
        onclicktype2: (User) -> Unit
    ) :
        Latest(view) {
        var latesMessage: LatestMessageModel? = null
        private var user: User? = null
        private val userTextView: TextView
        private val messageTextView: TextView
        private val imageView: ImageView
        private val latestTime: TextView

        init {
            userTextView = view.findViewById(R.id.tvUsernameLatestMessageDaily)
            messageTextView = view.findViewById(R.id.tvLatestMessageDaily)
            imageView = view.findViewById(R.id.imgLatestMessage)
            latestTime = view.findViewById(R.id.tvLatestDateTimeDaily)
            view.setOnClickListener {
                user?.let { onclicktype2.invoke(it) }
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
                messageTextView.text =
                    if (latesMessage.text.length > 17) latesMessage.text.substring(
                        0,
                        16
                    ) + "..." else latesMessage.text
            }
            latestTime.text = convertLongToDate(latesMessage.timestamp * 1000)
            Picasso.get().load(latesMessage.user?.profileImage).into(imageView)
        }


        fun bindMessageData(latesMessage: LatestMessageModel) {
            if (latesMessage.image) {
                messageTextView.text = "[image]"
            } else {
                messageTextView.text =
                    if (latesMessage.text.length > 17) latesMessage.text.substring(
                        0,
                        16
                    ) + "..." else latesMessage.text
            }
            latestTime.text = convertLongToDate(latesMessage.timestamp)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).daily == true) {
            return VIEW_TYPE_DAILY
        } else {
            return VIEW_TYPE_CHAT_LOG
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Latest {
        val view: View
        if (viewType == VIEW_TYPE_DAILY) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.latest_message_daily_row, parent, false)
            return LatestMessageDailyViewHolder(view, onclicktype2)
        } else {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.latest_mess_row, parent, false)
            return LatestMessageChatViewHolder(view, onclicktype1, onlongclick)
        }
    }

    override fun onBindViewHolder(holder: Latest, position: Int) {
        val item = getItem(position)
        if (holder is LatestMessageDailyViewHolder) {
            holder.bindData(item)
        }
        if (holder is LatestMessageChatViewHolder) {
            holder.bindData(item)
        }
    }

    override fun onBindViewHolder(
        holder: Latest,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val bundle = payloads.first() as Bundle
            bundle.getBoolean("message", false).takeIf { it }?.let {
                if (holder is LatestMessageChatViewHolder) {
                    holder.bindMessageData(getItem(position))
                }
                if (holder is LatestMessageDailyViewHolder) {
                    holder.bindMessageData(getItem(position))
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

}