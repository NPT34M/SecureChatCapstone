package com.example.e2ee_mvp.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e2ee_mvp.R
import com.example.e2ee_mvp.model.ChatMessage
import com.example.e2ee_mvp.model.User
import com.squareup.picasso.Picasso

const val VIEW_TYPE_FROM = 1
const val VIEW_TYPE_TO = 2

class ChatAdapter(val curentUserUid: String, val anotherUser: User?) :
    ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(
        object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.equals(newItem)
            }
        }
    ) {

    open class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ChatFromViewHolder(view: View) : ChatViewHolder(view) {
        var chatMessage: ChatMessage? = null
        private val textView: TextView
        private val imageViewSend: ImageView

        init {
            textView = view.findViewById(R.id.tvFromRow)
            imageViewSend = view.findViewById(R.id.imgSendFromUser)
        }

        fun bindData(chatMessage: ChatMessage) {
            this.chatMessage = chatMessage
            if (chatMessage.image) {
                textView.visibility = View.GONE
                imageViewSend.visibility = View.VISIBLE
                val re = chatMessage.text.replace("\n", "")
                decodeImage(re, imageViewSend)
            } else {
                imageViewSend.visibility = View.GONE
                textView.visibility = View.VISIBLE
                textView.text = chatMessage.text
            }
        }

        fun decodeImage(text: String, img: ImageView) {
            val imageBytes = Base64.decode(text, Base64.DEFAULT)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            img.setImageBitmap(bitmap)
        }
    }

    class ChatToViewHolder(view: View) : ChatViewHolder(view) {
        var chatMessage: ChatMessage? = null
        private val textView: TextView
        private val imageView: ImageView
        private val imageViewSend: ImageView

        init {
            textView = view.findViewById(R.id.tvToRow)
            imageView = view.findViewById(R.id.imgToRow)
            imageViewSend = view.findViewById(R.id.imgSendToUser)
        }

        fun bindData(chatMessage: ChatMessage, user: User) {
            this.chatMessage = chatMessage
            if (chatMessage.image) {
                textView.visibility = View.GONE
                imageViewSend.visibility = View.VISIBLE
                val re = chatMessage.text.replace("\n", "")
                decodeImage(re, imageViewSend)
            } else {
                textView.visibility = View.VISIBLE
                imageViewSend.visibility = View.GONE
                textView.text = chatMessage.text
            }
            Picasso.get().load(user.profileImage).into(imageView)
        }

        fun decodeImage(text: String, img: ImageView) {
            val imageBytes = Base64.decode(text, Base64.DEFAULT)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            img.setImageBitmap(bitmap)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).fromId == curentUserUid) {
            return VIEW_TYPE_FROM
        } else {
            return VIEW_TYPE_TO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        var view: View
        if (viewType == VIEW_TYPE_FROM) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_from_row, parent, false)
            return ChatFromViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.chat_to_row, parent, false)
            return ChatToViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is ChatFromViewHolder) {
            holder.bindData(item)
        }
        if (holder is ChatToViewHolder) {
            anotherUser?.let { holder.bindData(item, it) }
        }
    }
}