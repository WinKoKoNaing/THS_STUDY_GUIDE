package com.techhousestudio.porlar.thsstudyguide.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerDailyLectureRowBinding
import com.techhousestudio.porlar.thsstudyguide.helpers.Converters
import com.techhousestudio.porlar.thsstudyguide.models.Post
import java.text.SimpleDateFormat
import java.util.*

class AdminPostViewHolder(private val binding: RecyclerDailyLectureRowBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val db = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    fun bind(post: Post) {


        binding.lectureCardView.setOnLongClickListener { v ->
            val alertDialog = MaterialAlertDialogBuilder(v.context,R.style.AlertDialogTheme)
            alertDialog.setTitle("Warning ...")
            alertDialog.setMessage("Are you sure to delete?")
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton("Yes") { dialog, _ ->
                db.collection("ths_lectures").document(post.postId.toString()).delete()
                    .addOnCompleteListener {
                        dialog.dismiss()
                        Toast.makeText(v.context, "Delete Success", Toast.LENGTH_LONG).show()
                    }
            }
            alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
            return@setOnLongClickListener true
        }
        binding.lectureCardView.setOnClickListener {
            val b = bundleOf(
                "post_id" to post.postId
            )

            it.findNavController()
                .navigate(R.id.action_adminPanelFragment_to_updateLectureFragment, b)

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