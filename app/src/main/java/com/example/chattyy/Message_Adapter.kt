package com.example.chattyy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class Message_Adapter(val context: Context,val MessageList:ArrayList<message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieved, parent, false)
            return ReceiveViewHolder(view)

        } else {
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = MessageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            //do the stuff for sent ViewHolder

            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        } else {
            //do the stuff for receive ViewHolder
            val viewHolder = holder as ReceiveViewHolder
            holder.ReceiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = MessageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.SenderId)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }


    override fun getItemCount(): Int {
      return  MessageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.txtSent_msg)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ReceiveMessage = itemView.findViewById<TextView>(R.id.txtRec_msg)
    }


}