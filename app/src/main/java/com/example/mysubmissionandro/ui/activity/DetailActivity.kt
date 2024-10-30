package com.example.mysubmissionandro.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.mysubmissionandro.R
import com.example.mysubmissionandro.databinding.ActivityDetailBinding
import com.example.mysubmissionandro.data.local.Result
import com.example.mysubmissionandro.data.local.entity.EventEntity
import com.example.mysubmissionandro.data.remote.response.Event
import com.example.mysubmissionandro.viewmodel.EventViewModel
import com.example.mysubmissionandro.viewmodel.EventViewModelFactory
import com.example.mysubmissionandro.viewmodel.FavoriteEventViewModel
import com.example.mysubmissionandro.viewmodel.FavoriteEventViewModelFactory

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private var isFavorite: Boolean = false
    private lateinit var eventId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory: EventViewModelFactory = EventViewModelFactory.getInstance(applicationContext)
        val viewModel: EventViewModel by viewModels { factory }

        val favoriteFactory: FavoriteEventViewModelFactory = FavoriteEventViewModelFactory.getInstance(applicationContext)
        val favoriteViewModel: FavoriteEventViewModel by viewModels { favoriteFactory }

        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventId = intent.getStringExtra(EXTRA_EVENT_ID) ?: ""
        if (eventId.isNotEmpty()) {
            viewModel.getEventsById(eventId)
        }

        viewModel.detailEvents.observe(this) { detail ->
            when (detail) {
                is Result.Loading -> { binding.progressBarDetail.visibility = View.VISIBLE }
                is Result.Success -> {
                    binding.progressBarDetail.visibility = View.GONE
                    showDetailEvent(detail.data, favoriteViewModel)

                }
                is Result.Error -> {
                    binding.progressBarDetail.visibility = View.VISIBLE
                    Toast.makeText(this, detail.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        favoriteViewModel.getFavoriteEventById(eventId).observe(this) { favoriteEvent ->
            isFavorite = favoriteEvent != null
            updateFabIcon(isFavorite)
        }
    }

    private fun showDetailEvent(event: Event, favoriteViewModel: FavoriteEventViewModel) {
        binding.tvDetailEventName.text = event.name
        binding.tvDetailOwnerName.text = event.ownerName
        binding.tvBeginTime.text = event.beginTime
        binding.tvEndTime.text = event.endTime

        val remainingQuota = event.quota - event.registrants
        binding.tvRemainingQuota.text = remainingQuota.toString()

        binding.tvDetailDescription.text = HtmlCompat.fromHtml(
            event.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        Glide.with(this@DetailActivity)
            .load(event.mediaCover)
            .into(binding.ivDetailImg)

        binding.btnRegistration.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
        }

        binding.fabFavorite.setOnClickListener {
            val eventEntity = EventEntity(
                id = eventId,
                name = event.name,
                mediaCover = event.mediaCover
            )
            if (isFavorite) {
                favoriteViewModel.removeToFavorite(eventEntity)
                Toast.makeText(this, "Event removed from favorites", Toast.LENGTH_SHORT).show()
            } else {
                favoriteViewModel.addToFavorite(eventEntity)
                Toast.makeText(this, "Event added to favorites", Toast.LENGTH_SHORT).show()
            }
            isFavorite = !isFavorite
            updateFabIcon(isFavorite)
        }
    }

    private fun updateFabIcon(isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.favorite else R.drawable.favorite_border
        binding.fabFavorite.setImageResource(iconRes)
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
