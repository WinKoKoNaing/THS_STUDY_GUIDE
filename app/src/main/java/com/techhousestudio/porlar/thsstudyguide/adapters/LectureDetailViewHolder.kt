package com.techhousestudio.porlar.thsstudyguide.adapters

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerViewpagerDetailRowBinding
import com.techhousestudio.porlar.thsstudyguide.helpers.Converters
import com.techhousestudio.porlar.thsstudyguide.models.Post
import com.techhousestudio.porlar.thsstudyguide.models.User
import java.text.SimpleDateFormat

class LectureDetialViewHolder(private val binding: RecyclerViewpagerDetailRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    fun bind(post: Post) {
        binding.tvClassTopic.text = post.postTopic
        binding.tvClassTitle.text = post.classTitle
        binding.tvClassTopic.isSelected = true
        binding.tvDuration.text =
            "${Converters.fromTimeString(post.postStartTime, post.postEndTime)} / ${post.className}"
//        binding.tvWeeks.text = post.classWeeks
        binding.tvTime.text = "${post.postStartTime} - ${post.postEndTime}"
        binding.webViewLectureNote.loadData(post.postNote, "text/html", "UTF-8")
    }

}