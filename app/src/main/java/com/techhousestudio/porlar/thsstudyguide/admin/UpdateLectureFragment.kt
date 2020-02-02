package com.techhousestudio.porlar.thsstudyguide.admin


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.models.Post
import kotlinx.android.synthetic.main.fragment_create_lecture.*
import kotlinx.android.synthetic.main.fragment_update_lecture.*
import kotlinx.android.synthetic.main.fragment_update_lecture.btnEndTime
import kotlinx.android.synthetic.main.fragment_update_lecture.btnStartTime
import kotlinx.android.synthetic.main.fragment_update_lecture.etLectureNote
import kotlinx.android.synthetic.main.fragment_update_lecture.etTopic
import kotlinx.android.synthetic.main.fragment_update_lecture.tvClassInfo
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class UpdateLectureFragment : DialogFragment(), SetOnTimeClickListener {

    lateinit var post: Post
    lateinit var postId: String
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_lecture, container, false)
    }

    override fun onStart() {
        super.onStart()
        val d: Dialog? = dialog
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            d.window?.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = arguments?.getString("post_id").toString()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        val docRef =
            db.collection("ths_lectures").whereEqualTo("postId", postId)
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    post = document.toObject(Post::class.java)

                    tvClassInfo.text =
                        "${post.className}\n ${post.classLanguage} \n ${post.classTitle}"
                    etTopic.setText(post.postTopic)
                    btnStartTime.text = post.postStartTime
                    btnEndTime.text = post.postEndTime
                    etLectureNote.setText(post.postNote)
                }

            }

            .addOnFailureListener {
                Timber.d("get failed with ")
            }
        btnDone.setOnClickListener {
            val post = Post(
                postId,
                post.classId,
                post.className,
                post.classTitle,
                post.classLanguage,
                etTopic.text.toString(),
                btnStartTime.text.toString(),
                btnEndTime.text.toString(),
                Timestamp.now(),
                etLectureNote.text.toString()
            )

            db.collection("ths_lectures").document(postId).set(post).addOnCompleteListener {
                Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_LONG).show()
                dismiss()
            }
        }

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
