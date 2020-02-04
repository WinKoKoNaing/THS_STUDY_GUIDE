package com.techhousestudio.porlar.thsstudyguide.ourths


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore

import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.models.Course
import kotlinx.android.synthetic.main.fragment_course_detail.*

/**
 * A simple [Fragment] subclass.
 */
class CourseDetailFragment : Fragment() {
    val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db.collection("courses").document(arguments!!.getString("course_id").toString())
            .get().addOnSuccessListener {
                val course = it.toObject(Course::class.java)
                tvCourseTitle.text = course!!.courseName
                webView_course_detail.loadData(course.courseDetail, "text/html", "UTF-8")
            }
    }

}
