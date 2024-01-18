package com.zain.rumahquranonline.adapter.adapteradmin

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.FirebaseOptions
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.data.Message
import com.zain.rumahquranonline.databinding.ChatItemBinding

class ChatAdapter (options: FirebaseRecyclerOptions<Message>,
                   private val currentUserName: String?)
    : FirebaseRecyclerAdapter<Message, ChatAdapter.MessageViewHolder>(options){


    inner class MessageViewHolder(private val binding: ChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.tvMessage.text = item.text
            setTextColor(item.name, binding.tvMessage)
            binding.tvMessenger.text = item.name
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .circleCrop()
                .into(binding.ivMessenger)
            if (item.timestamp != null) {
                binding.tvTimestamp.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chat_item, parent, false)
        val binding = ChatItemBinding.bind(view)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
       holder.bind(model)
    }

    private fun setTextColor(userName: String?, textView: TextView) {
        if (currentUserName == userName && userName != null) {
            textView.setBackgroundResource(R.drawable.rounded_message_blue)
        } else {
            textView.setBackgroundResource(R.drawable.rounded_message_yellow)
        }
    }
}