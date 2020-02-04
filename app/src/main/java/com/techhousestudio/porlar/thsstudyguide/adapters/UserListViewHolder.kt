package com.techhousestudio.porlar.thsstudyguide.adapters

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techhousestudio.porlar.thsstudyguide.models.User
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_user_list_row.view.*

class UserListViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    val db = FirebaseFirestore.getInstance()

    fun bind(user: User) {
        containerView.tvUserName.text = user.userName
        containerView.switch_member.isChecked = user.isMember
        containerView.switch_member.setOnCheckedChangeListener { _, isChecked ->
            user.isMember = isChecked
            db.collection("users").document(user.userId).set(user).addOnCompleteListener {
                Toast.makeText(containerView.context, "Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
}