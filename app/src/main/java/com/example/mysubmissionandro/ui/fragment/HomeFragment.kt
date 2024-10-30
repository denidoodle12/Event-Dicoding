package com.example.mysubmissionandro.ui.fragment

import android.content.Intent
import com.example.mysubmissionandro.data.local.Result
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmissionandro.adapter.EventAdapter
import com.example.mysubmissionandro.databinding.FragmentHomeBinding
import com.example.mysubmissionandro.ui.activity.DetailActivity
import com.example.mysubmissionandro.viewmodel.EventViewModel
import com.example.mysubmissionandro.viewmodel.EventViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingAdapter: EventAdapter
    private lateinit var finishedAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory: EventViewModelFactory = EventViewModelFactory.getInstance(requireActivity())
        val viewModel: EventViewModel by viewModels { factory }

        upcomingAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
            startActivity(intent)
        }

        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            hasFixedSize()
            adapter = upcomingAdapter
        }

        viewModel.getUpcomingEvents()
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { upcoming ->
            when (upcoming) {
                is Result.Loading -> { binding.progressBarHome.visibility = View.VISIBLE }
                is Result.Success -> {
                    binding.progressBarHome.visibility = View.GONE
                    upcomingAdapter.submitList(upcoming.data.take(5))
                }
                is Result.Error -> {
                    binding.progressBarHome.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), upcoming.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        finishedAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
            startActivity(intent)
        }

        binding.rvFinished.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            adapter = finishedAdapter
        }

        viewModel.getFinishedEvents()
        viewModel.finishedEvents.observe(viewLifecycleOwner) { finished ->
            when (finished) {
                is Result.Loading -> { binding.progressBarHome.visibility = View.VISIBLE }
                is Result.Success -> {
                    binding.progressBarHome.visibility = View.GONE
                    finishedAdapter.submitList(finished.data.take(5))
                }
                is Result.Error -> {
                    binding.progressBarHome.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), finished.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}