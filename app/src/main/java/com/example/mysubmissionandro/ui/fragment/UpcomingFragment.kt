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
import com.example.mysubmissionandro.databinding.FragmentUpcomingBinding
import com.example.mysubmissionandro.ui.activity.DetailActivity
import com.example.mysubmissionandro.viewmodel.EventViewModel
import com.example.mysubmissionandro.viewmodel.EventViewModelFactory

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory: EventViewModelFactory = EventViewModelFactory.getInstance(requireActivity())
        val viewModel: EventViewModel by viewModels { factory }

        viewModel.getUpcomingEvents()
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> { binding.progressBarUpcoming.visibility = View.VISIBLE }
                is Result.Success -> {
                    binding.progressBarUpcoming.visibility = View.GONE
                    upcomingAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBarUpcoming.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        upcomingAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
            startActivity(intent)
        }
        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = upcomingAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}