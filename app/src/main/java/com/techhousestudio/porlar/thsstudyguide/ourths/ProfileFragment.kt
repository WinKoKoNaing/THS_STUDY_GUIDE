package com.techhousestudio.porlar.thsstudyguide.ourths


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.FacebookBuilder
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.models.User
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BottomSheetDialogFragment() {
    private val db = FirebaseFirestore.getInstance()

    private val FB_SIGN_IN = 100
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            btnLogOut.text = getString(R.string.com_facebook_loginview_log_out_button)
            Glide.with(this)
                .load(currentUser.providerData[0]?.photoUrl.toString())
                .transform(CircleCrop())
                .into(ivUserImageUri)
            tvDisplayName.text = currentUser.displayName
        } else {
            tvDisplayName.visibility = View.GONE
            ivUserImageUri.visibility = View.GONE
            btnLogOut.text = getString(R.string.login_with_fb)
        }



        checkMember()
        btnAdmin.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_createLectureFragment)
        }

        btnLogOut.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {

                AuthUI.getInstance().signOut(this.requireContext()).addOnCompleteListener {
                    tvDisplayName.visibility = View.GONE
                    btnLogOut.text = getString(R.string.login_with_fb)
                    Snackbar.make(view.rootView, "Logout Completed!", Snackbar.LENGTH_LONG).show()
                    dismiss()
                }
            } else {

                val providers: List<IdpConfig> = listOf(
                    FacebookBuilder().build()
                )
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                    FB_SIGN_IN
                )

            }
        }

    }

    private fun checkMember() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .get().addOnSuccessListener {

                    val currentUser = it.toObject(User::class.java)
                    if (currentUser != null) {
                        if (currentUser.isAdmin) {
                            btnAdmin.visibility = View.VISIBLE
                        }


                    }
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FB_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in


                val user = FirebaseAuth.getInstance().currentUser

                tvDisplayName.visibility = View.VISIBLE
                ivUserImageUri.visibility = View.VISIBLE
                Glide.with(this)
                    .load(user?.providerData?.get(0)?.photoUrl.toString())
                    .transform(CircleCrop())
                    .into(ivUserImageUri)
                tvDisplayName.text = user?.displayName

                btnLogOut.text = getString(R.string.com_facebook_loginview_log_out_button)

                val savedUser = User(
                    user!!.uid,
                    user.displayName.toString(),
                    user.providerData[0]?.photoUrl.toString()
                )
                db.collection("users").document(user.uid).set(savedUser).addOnSuccessListener {
//                    Toast.makeText(requireContext(), "Successful Login", Toast.LENGTH_LONG).show()
                }

            } else {
                Timber.i(response.toString())
                Toast.makeText(context, response?.error.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }


}
