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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
        val firebaseUser = auth.currentUser
        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)


        binding.sendButton.setOnClickListener {
            val userName = firebaseUser?.displayName ?: "Anonymous" // Default name if not available
            val userPhotoUrl = firebaseUser?.photoUrl?.toString() ?: "" // Empty string if not available
            val friendlyMessage = Message(
                binding.messageEditText.text.toString(),
                userName,
                userPhotoUrl,
                Date().time
            )
            messagesRef.push().setValue(friendlyMessage) { error, _ ->
                if (error != null) {
                    Toast.makeText(requireContext(), getString(R.string.send_error) + error.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.send_success), Toast.LENGTH_SHORT).show()
                }
            }
            binding.messageEditText.setText("")
        }
        val manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messagesRef, Message::class.java)
            .build()
        adapter = ChatAdapter(options, firebaseUser?.displayName)
        binding.messageRecyclerView.adapter = adapter
    }

    companion object {
        const val MESSAGES_CHILD = "messages"
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