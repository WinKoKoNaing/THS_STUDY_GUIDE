package com.techhousestudio.porlar.thsstudyguide.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerDailyLectureRowBinding
import com.techhousestudio.porlar.thsstudyguide.helpers.Converters
import com.techhousestudio.porlar.thsstudyguide.models.Post
import com.techhousestudio.porlar.thsstudyguide.models.User
import java.text.SimpleDateFormat
import java.util.*

class DailyLectureViewHolder(private val binding: RecyclerDailyLectureRowBinding) :
    RecyclerView.ViewHolder(binding.root) {



    @SuppressLint("SetTextI18n")
    fun bind(post: Post) {

        binding.btnLectureDate.setOnClickListener {

            val bundle = Bundle()
            bundle.putLong("lecture_date", post.postLectureDate?.toDate()!!.time)
            it.findNavController().navigate(R.id.action_global_lectureDetailFilterFragment, bundle)
        }

        binding.lectureCardView.setOnClickListener {
            val b = Bundle()
            b.putString("post_id", post.postId)
            it.findNavController()
                .navigate(R.id.action_global_lectureDetailFragment, b)
        }

        binding.tvClassTopic.text = post.postTopic
        binding.tvClassTitle.text = post.classTitle
        binding.tvClassTopic.isSelected = true
        binding.tvWeeks.text =
            SimpleDateFormat(
                "EEE, MMM d, ''yy",
                Locale.ENGLISH
            ).format(post.postLectureDate!!.toDate())
        binding.tvDuration.text =
            "${Converters.fromTimeString(post.postStartTime, post.postEndTime)} / ${post.className}"
        binding.btnLectureDate.text =
            SimpleDateFormat(
                "dd",
                Locale.ENGLISH
            ).format(post.postLectureDate!!.toDate())
        binding.tvStartTime.text = post.postStartTime
        binding.tvEndTime.text = post.postEndTime
    }
}