package com.zain.rumahquranonline.ui.DoaDoa

import DoaAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentDoaBinding
import com.zain.rumahquranonline.databinding.FragmentHomeBinding


class Doa : Fragment() {
    private var _binding: FragmentDoaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DoaViewModel
    private lateinit var adapter: DoaAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentDoaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DoaViewModel::class.java)
        viewModel.getDoa()

        adapter = DoaAdapter(listOf()) { doaItem ->
            // Handle item click, navigate to detail fragment or activity
        }
        binding.rvDoa.adapter = adapter
        binding.rvDoa.layoutManager = LinearLayoutManager(context)

        viewModel.doaList.observe(viewLifecycleOwner) { doaList ->
            adapter = DoaAdapter(doaList) { doaItem ->
                // Handle item click, navigate to detail fragment or activity
                val bundle = bundleOf("doaItem" to doaItem)
                findNavController().navigate(R.id.action_doa_to_detailDoa, bundle)
            }
            binding.rvDoa.adapter = adapter
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}