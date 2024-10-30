package com.example.mysubmissionandro.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mysubmissionandro.adapter.EventAdapter
import com.example.mysubmissionandro.data.local.Result
import com.example.mysubmissionandro.databinding.FragmentFinishedBinding
import com.example.mysubmissionandro.ui.activity.DetailActivity
import com.example.mysubmissionandro.viewmodel.EventViewModel
import com.example.mysubmissionandro.viewmodel.EventViewModelFactory

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var finishedAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory: EventViewModelFactory = EventViewModelFactory.getInstance(requireActivity())
        val viewModel: EventViewModel by viewModels { factory }

        viewModel.getFinishedEvents()

        binding.searchViewFav.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchFavEvents(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.getFinishedEvents()
                }
                return false
            }
        })

        viewModel.finishedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> { binding.progressFinished.visibility = View.VISIBLE }
                is Result.Success -> {
                    binding.progressFinished.visibility = View.GONE
                    finishedAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressFinished.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        finishedAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
            startActivity(intent)
        }
        binding.rvFinished.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            hasFixedSize()
            adapter = finishedAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}