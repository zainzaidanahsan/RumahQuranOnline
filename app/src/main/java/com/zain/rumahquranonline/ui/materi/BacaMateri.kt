package com.zain.rumahquranonline.ui.materi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.data.const
import com.zain.rumahquranonline.databinding.FragmentBacaMateriBinding
import com.zain.rumahquranonline.databinding.FragmentListPdfBinding


class BacaMateri : Fragment() {

    private var _binding : FragmentBacaMateriBinding?= null
    private val binding get() = _binding!!
    var bookId = ""
    companion object{
        const val TAG = "PDF_VIEW_TAG"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBacaMateriBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookId = arguments?.getString("bookId")!!
        loadBookDetails()

    }

    private fun loadBookDetails() {
        Log.d(TAG, "loadBookDetails: Get Pdf Url from db")
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pdfUrl = snapshot.child("url").value
                    Log.d(TAG, "onDataChange: Pdf Url $pdfUrl")
                    loadBookFromUrl("$pdfUrl")
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun loadBookFromUrl(pdfUrl:String) {
        Log.d(TAG, "loadBookFromUrl: get pdf url from db firebase")
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
//        reference.metadata.addOnSuccessListener {metadata->
//            Log.d(TAG, "loadPdfSize: Size Bytes ${metadata.sizeBytes}")
//        }.addOnFailureListener { e ->
//            Log.e(TAG, "loadPdfSize: Failed to get metadata", e)
//        }
        reference.getBytes(const.MAX_BYTES_PDF)
            .addOnSuccessListener {
                binding.pdfView.fromBytes(it)
                    .swipeHorizontal(false)
                    .onError {
                        Log.d(TAG, "loadBookFromUrl: Error karena ${it.message}")

                    }
                    .onPageError { page, t ->
                        Log.d(TAG, "loadBookFromUrl: Error ${t.message}")
                    }
                    .load()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener{e->
                Log.d(TAG, "loadBookFromUrl: Gagal memuat pdf karena ${e.message}")
                binding.progressBar.visibility = View.GONE
            }
    }

}