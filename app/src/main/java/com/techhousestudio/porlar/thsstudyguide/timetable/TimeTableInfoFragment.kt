package com.techhousestudio.porlar.thsstudyguide.timetable


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.databinding.FragmentTimeTableInfoBinding
import com.techhousestudio.porlar.thsstudyguide.models.TimeTable

/**
 * A simple [Fragment] subclass.
 */
class TimeTableInfoFragment : DialogFragment() {

    private val db = FirebaseFirestore.getInstance()
    lateinit var binding: FragmentTimeTableInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_time_table_info, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val d: Dialog? = dialog
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            d.window?.setLayout(width, height)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db.collection("thsClasses").document(arguments!!.getString("class_id").toString())
            .get().addOnSuccessListener {
                val timeTable = it.toObject(TimeTable::class.java)
                binding.timeTable = timeTable
            }
    }


}
