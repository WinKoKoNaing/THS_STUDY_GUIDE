package com.techhousestudio.porlar.thsstudyguide.adapters

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerTimeTableRowBinding
import com.techhousestudio.porlar.thsstudyguide.helpers.Converters
import com.techhousestudio.porlar.thsstudyguide.models.TimeTable

class TimeTableViewHolder(private val binding: RecyclerTimeTableRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    fun bind(classTimetable: TimeTable) {
        binding.btnLectureDate.setOnClickListener {
            val b = bundleOf(
                "class_id" to classTimetable.classId
            )
            it.findNavController().navigate(R.id.action_global_timeTableInfoFragment,b)
        }
        binding.tvClassTitle.text = classTimetable.classTitle
        binding.tvDuration.text = "${classTimetable.classDuration}/ ${classTimetable.className}"
        binding.tvClassProgress.text = classTimetable.classProgress.toString() + "% Finished"
        binding.tvWeeks.text = classTimetable.classWeeks
        binding.tvStartTime.text = classTimetable.classStartTime
        binding.tvEndTime.text = classTimetable.classEndTime
    }
}