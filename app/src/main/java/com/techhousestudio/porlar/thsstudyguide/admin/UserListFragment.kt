package com.techhousestudio.porlar.thsstudyguide.admin


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.adapters.UserListViewHolder
import com.techhousestudio.porlar.thsstudyguide.helpers.MarginItemDecoration
import com.techhousestudio.porlar.thsstudyguide.models.User
import kotlinx.android.synthetic.main.fragment_user_list.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class UserListFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    lateinit var query: Query
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_user_list.layoutManager = LinearLayoutManager(requireContext())
        query = db.collection("users")
        queryByLanguage(query)
    }

    private fun queryByLanguage(query: Query) {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<User>()
            .setLifecycleOwner(this)
            .setQuery(query, config, User::class.java)
            .build()


        val mAdapter = object : FirestorePagingAdapter<User, UserListViewHolder>(options) {


            @RequiresApi(Build.VERSION_CODES.N)
            override fun onBindViewHolder(
                viewHolder: UserListViewHolder,
                position: Int,
                user: User
            ) {

                viewHolder.bind(user)
            }

            override fun onError(e: Exception) {
                super.onError(e)
                Timber.e(e.message.toString())
            }

            override fun onLoadingStateChanged(state: LoadingState) = when (state) {
                LoadingState.LOADING_INITIAL -> {
                    swipeRefreshLayout.isRefreshing = true
                }

                LoadingState.LOADING_MORE -> {
                    swipeRefreshLayout.isRefreshing = true
                }

                LoadingState.LOADED -> {
                    swipeRefreshLayout.isRefreshing = false
                }

                LoadingState.ERROR -> {
                    Toast.makeText(
                        context,
                        "Error Occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                    swipeRefreshLayout.isRefreshing = false
                }

                LoadingState.FINISHED -> {
                    swipeRefreshLayout.isRefreshing = false
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): UserListViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.recycler_user_list_row,
                    parent,
                    false
                )
                return UserListViewHolder(view)
            }
        }


        swipeRefreshLayout.setOnRefreshListener {
            mAdapter.refresh()
        }


        recycler_user_list.adapter = mAdapter
        recycler_user_list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        recycler_user_list.addItemDecoration(MarginItemDecoration(16))

    }

}
