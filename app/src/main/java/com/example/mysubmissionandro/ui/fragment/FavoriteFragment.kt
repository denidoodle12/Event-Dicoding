package com.example.mysubmissionandro.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmissionandro.adapter.FavoriteEventAdapter
import com.example.mysubmissionandro.databinding.FragmentFavoriteBinding
import com.example.mysubmissionandro.ui.activity.DetailActivity
import com.example.mysubmissionandro.viewmodel.FavoriteEventViewModel
import com.example.mysubmissionandro.viewmodel.FavoriteEventViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteAdapter: FavoriteEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()

        val favoriteFactory: FavoriteEventViewModelFactory =
            FavoriteEventViewModelFactory.getInstance(requireContext())
        val favoriteViewModel: FavoriteEventViewModel by viewModels { favoriteFactory }

        favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteEvents ->
            favoriteAdapter.submitList(favoriteEvents)
        }

        favoriteViewModel.getAllFavoriteEvent().observe(viewLifecycleOwner) { favEvents ->
            if (favEvents != null && favEvents.isNotEmpty()) {
                favoriteAdapter.submitList(favEvents)
                showEmptyView(false)
            } else {
                showEmptyView(true)
            }
        }
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteEventAdapter { selectedEvent ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, selectedEvent.id)
            startActivity(intent)
        }

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
    }

    private fun showEmptyView(isEmpty: Boolean) {
        if (isEmpty) {
            binding.progressBarFavorite.visibility = View.GONE
            binding.tvEmptyFavorite.visibility = View.VISIBLE
        } else {
            binding.progressBarFavorite.visibility = View.GONE
            binding.tvEmptyFavorite.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
