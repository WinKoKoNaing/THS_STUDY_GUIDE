package com.techhousestudio.porlar.thsstudyguide.admin


import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.models.TimeTable
import kotlinx.android.synthetic.main.fragment_create_class.*

/**
 * A simple [Fragment] subclass.
 */
class CreateClassFragment : DialogFragment() {

    val db = FirebaseFirestore.getInstance()

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
        return inflater.inflate(R.layout.fragment_create_class, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        btnClassPost.setOnClickListener {


            val classId = db.collection("thsClasses").document().id


            if (TextUtils.isEmpty(etClassTitle.text) ||
                TextUtils.isEmpty(etDuration.text) ||
                TextUtils.isEmpty(etClassWeeks.text) ||
                TextUtils.isEmpty(etClassName.text) ||
                TextUtils.isEmpty(etClassProgress.text) ||
                TextUtils.isEmpty(etClassStartDate.text) ||
                TextUtils.isEmpty(etClassEndDate.text)
            ) {
                Snackbar.make(requireView(), "Fill All Box", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }


            val timeTable = TimeTable(
                classId,
                etClassName.text.toString(),
                etClassTitle.text.toString(),
                etClassStartDate.text.toString(),
                etClassEndDate.text.toString(),
                etDuration.text.toString(),
                spinner_language.selectedItem.toString(),
                etClassWeeks.text.toString(),
                "No available now",
                emptyMap(),
                etClassProgress.text.toString().toInt(),
                ""
            )


            db.collection("thsClasses").document(classId).set(timeTable).addOnCompleteListener {
                dismiss()
            }


        }


    }


}
