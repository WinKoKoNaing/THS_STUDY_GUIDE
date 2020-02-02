package com.techhousestudio.porlar.thsstudyguide.admin


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.helpers.Converters
import com.techhousestudio.porlar.thsstudyguide.models.Post
import com.techhousestudio.porlar.thsstudyguide.models.TimeTable
import kotlinx.android.synthetic.main.fragment_create_lecture.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CreateLectureFragment : DialogFragment(), AdapterView.OnItemSelectedListener,
    SetOnTimeClickListener {
    private lateinit var timeTable: TimeTable
    private val db = FirebaseFirestore.getInstance()
    private var spinnerName = arrayListOf<String>()
    private var timeTableList = arrayListOf<TimeTable>()

    override fun onStart() {
        super.onStart()
        val d: Dialog? = dialog
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            d.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_lecture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val docRef = db.collection("thsClasses")
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val timeTable = document.toObject(TimeTable::class.java)
                    timeTableList.add(timeTable)
                    spinnerName.add(timeTable.classLanguage + " / " + timeTable.className)
                    Timber.d("${document.id} => ${document.data}")
                }

                ArrayAdapter(
                    this.requireContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerName
                ).also { arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    ths_spinner.adapter = arrayAdapter
                }
            }

            .addOnFailureListener {
                Timber.d("get failed with ")
            }


        ths_spinner.onItemSelectedListener = this


        btnStartTime.setOnClickListener {
            val timePickerFragment =
                TimePickerFragment()
            val b = Bundle()
            b.putBoolean("is_start", true)
            timePickerFragment.arguments = b
            timePickerFragment.setOnTimeClickListener = this
            timePickerFragment.show(childFragmentManager, "start_time_picker")
        }
        btnEndTime.setOnClickListener {
            val timePickerFragment =
                TimePickerFragment()
            val b = Bundle()
            b.putBoolean("is_start", false)
            timePickerFragment.arguments = b
            timePickerFragment.setOnTimeClickListener = this
            timePickerFragment.show(childFragmentManager, "end_time_picker")
        }
        btnPost.setOnClickListener {
            val id = db.collection("ths_lectures").document().id
            val post = Post(
                id,
                timeTable.classId,
                timeTable.className,
                timeTable.classTitle,
                timeTable.classLanguage,
                etTopic.text.toString(),
                btnStartTime.text.toString(),
                btnEndTime.text.toString(),
                Timestamp.now(),
                etLectureNote.text.toString()
            )

            db.collection("ths_lectures").document(id).set(post).addOnCompleteListener {
                Toast.makeText(requireContext(), "Post Successful", Toast.LENGTH_LONG).show()
                dismiss()
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        timeTable = timeTableList[position]

        tvClassInfo.text =
            "${timeTableList[position].classTitle}\n${timeTableList[position].className}\n${timeTableList[position].classWeeks}"
    }

    override fun onTimeClick(time: String, isStart: Boolean?) {
        val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val date12Format = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        if (isStart != null)
            if (isStart) btnStartTime.text =
                date12Format.format(sdf.parse(time)) else btnEndTime.text =
                date12Format.format(sdf.parse(time))

    }


}
