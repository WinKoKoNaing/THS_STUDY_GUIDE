package com.techhousestudio.porlar.thsstudyguide.timetable


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techhousestudio.porlar.thsstudyguide.R

/**
 * A simple [Fragment] subclass.
 */
class TimeTableDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_table_detail, container, false)
    }


}
