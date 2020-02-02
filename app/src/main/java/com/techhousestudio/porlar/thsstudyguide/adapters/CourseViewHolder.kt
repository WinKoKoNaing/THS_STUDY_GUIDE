package com.techhousestudio.porlar.thsstudyguide.adapters

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerCourseRowBinding
import com.techhousestudio.porlar.thsstudyguide.models.Course

class CourseViewHolder(val binding: RecyclerCourseRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(course: Course) {
        binding.btnCourseDetail.setOnClickListener {
            val b = bundleOf(
                "course_id" to course.courseId
            )
            it.findNavController().navigate(R.id.action_global_courseDetailFragment, b)
        }
        binding.tvSubject.text = course.courseName
        binding.tvViewCount.text = getViewCount(course.courseViewCount)
        Glide.with(itemView.context).load(course.courseImageUri).into(binding.ivSubjectImage)
    }

    private fun getViewCount(courseViewCount: Long): CharSequence? =
        if (courseViewCount > 1) "$courseViewCount Viewers" else "$courseViewCount Viewer"
}