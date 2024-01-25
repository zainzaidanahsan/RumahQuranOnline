package com.zain.rumahquranonline.ui.setoranhafalan

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.adapter.adapteradmin.ChatAdapter
import com.zain.rumahquranonline.data.Message
import com.zain.rumahquranonline.databinding.FragmentChatBinding
import com.zain.rumahquranonline.databinding.FragmentCreateScheduleBinding
import java.util.Date


class Chat : Fragment() {
    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog
    private lateinit var adapter: ChatAdapter
    private var jadwalId: String? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
        db = Firebase.database

        jadwalId = arguments?.getString("jadwalId")

//        if (jadwalId != null) {
//            setupChatRoom(jadwalId!!)
//        } else {
//            Toast.makeText(context, "ID Jadwal tidak ditemukan.", Toast.LENGTH_SHORT).show()
//        }
        setupChatRoom()
        setupRecyclerView()
    }


    private fun setupChatRoom() {
        binding.sendButton.setOnClickListener {
            fetchUsernameAndSendMessage()
        }
    }
    private fun sendMessage(username: String) {
        val userPhotoUrl = auth.currentUser?.photoUrl?.toString() ?: ""
        val messageText = binding.messageEditText.text.toString()
        if (messageText.isNotEmpty()) {
            val message = Message(messageText, username, userPhotoUrl, Date().time)
            val messagesRef = database.child("chatRooms").child(jadwalId ?: "").child("messages")
            messagesRef.push().setValue(message)
            binding.messageEditText.setText("")
        }
    }

    private fun fetchUsernameAndSendMessage() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.child("username").value as? String ?: "Anonymous"
                    sendMessage(username)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to load username: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager

        val messagesRef = database.child("chatRooms").child(jadwalId ?: "").child("messages")
        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messagesRef, Message::class.java)
            .build()

        adapter = ChatAdapter(options, auth.currentUser?.displayName)
        binding.messageRecyclerView.adapter = adapter
    }
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}