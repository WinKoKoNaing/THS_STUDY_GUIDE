package com.techhousestudio.porlar.thsstudyguide.lectures


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.techhousestudio.porlar.thsstudyguide.R
import kotlinx.android.synthetic.main.fragment_update.*

/**
 * A simple [Fragment] subclass.
 */
class UpdateFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false)
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnUpdate.text =
            "Please Update ${arguments!!.getString("version")} Version And Download From GitHub Latest Released"
        btnUpdate.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/WinKoKoNaing/TechHouseStudio/releases")
            )
            startActivity(intent)
        }

    }


}
