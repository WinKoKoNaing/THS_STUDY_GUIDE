package com.techhousestudio.porlar.thsstudyguide.lectures


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedList
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.adapters.LectureDetialViewHolder
import com.techhousestudio.porlar.thsstudyguide.databinding.FragmentLectureDetailBinding
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerViewpagerDetailRowBinding
import com.techhousestudio.porlar.thsstudyguide.models.Post
import com.techhousestudio.porlar.thsstudyguide.models.User
import kotlinx.android.synthetic.main.fragment_lecture_detail_filter.*

class LectureDetailFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser
    lateinit var binding: FragmentLectureDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_lecture_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findByPostId(arguments?.getString("post_id").toString())
        if (user != null)
            checkMember()
    }

    private fun findByPostId(postId: String) {
        db.collection("ths_lectures").document(postId)
            .get().addOnSuccessListener {

                val post = it.toObject(Post::class.java)
                if (post != null)
                    binding.post = post
            }
    }

    private fun checkMember() {
        db.collection("users").document(user!!.uid)
            .get().addOnSuccessListener {

                val currentUser = it.toObject(User::class.java)
                if (currentUser != null)
                    binding.user = currentUser
            }
    }

}
