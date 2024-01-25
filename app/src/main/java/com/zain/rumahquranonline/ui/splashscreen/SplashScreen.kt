package com.zain.rumahquranonline.ui.splashscreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zain.rumahquranonline.R

class SplashScreen : Fragment() {
    private val splashDuration: Long = 3000
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        Handler(Looper.getMainLooper()).postDelayed({
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null){
                checkUserRole(firebaseUser.uid) { isAdmin ->
                    if (isAdmin) {
                        findNavController().navigate(R.id.action_splashScreen_to_homeAdmin)
                    } else {
                        findNavController().navigate(R.id.action_splashScreen_to_home2)
                    }
                }
            }else{
                navigateToNextFragment()
            }
        }, splashDuration)
    }
    private fun navigateToNextFragment() {
        findNavController().navigate(R.id.action_splashScreen_to_pilihRole)
    }

    private fun checkUserRole(uid: String, callback: (Boolean) -> Unit) {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userType = dataSnapshot.child("userType").value as String?
                    callback(userType == "admin")
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle potential errors here
                callback(false)
            }
        })
    }
}