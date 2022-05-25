package com.example.chattyy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var ChatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: Message_Adapter
    private lateinit var messageList: ArrayList<message>
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom:String?=null
    var senderRoom:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name= intent.getStringExtra("name")
        val receiverUid= intent.getStringExtra("uid")

        val senderUid= FirebaseAuth.getInstance().currentUser?.uid
        mDbRef= FirebaseDatabase.getInstance().getReference()

         senderRoom= receiverUid+senderUid
        receiverRoom= senderUid+receiverUid

        supportActionBar?.title= name
        ChatRecyclerView = findViewById(R.id.chatRv)
        messageBox= findViewById(R.id.messageBox)
        sendButton= findViewById(R.id.sendButton)
        messageList= ArrayList()
        messageAdapter = Message_Adapter(this,messageList)

        ChatRecyclerView.layoutManager=LinearLayoutManager(this)
        ChatRecyclerView.adapter=messageAdapter
        //logic for adding data to recyclerview
        mDbRef.child("child").child(senderRoom!!).child("message")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        //adding msg to database
        sendButton.setOnClickListener{
            val message = messageBox.text.toString()
          val  messageObject =message(message,senderUid)
         mDbRef.child("Chats").child(senderRoom!!).child("message").push()
             .setValue(messageObject).addOnSuccessListener {

             }
            mDbRef.child("Chats").child(receiverRoom!!).child("message").push()
                .setValue(messageObject)

        }
        messageBox.setText("");
    }
}