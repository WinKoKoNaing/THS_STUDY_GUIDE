package com.techhousestudio.porlar.thsstudyguide.lectures


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.databinding.FragmentMessageBinding
import com.techhousestudio.porlar.thsstudyguide.models.MessageBox

class MessageFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    lateinit var binding: FragmentMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("message_box")
            .document("ths")
            .get().addOnSuccessListener {
                val messageBox = it.toObject(MessageBox::class.java)
                binding.messageBox = messageBox
            }
    }


}
