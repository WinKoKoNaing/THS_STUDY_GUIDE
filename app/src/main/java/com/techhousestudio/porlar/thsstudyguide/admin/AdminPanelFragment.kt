package com.techhousestudio.porlar.thsstudyguide.admin


import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.adapters.AdminPostViewHolder
import com.techhousestudio.porlar.thsstudyguide.adapters.DailyLectureViewHolder
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerDailyLectureRowBinding
import com.techhousestudio.porlar.thsstudyguide.helpers.MarginItemDecoration
import com.techhousestudio.porlar.thsstudyguide.models.Post
import kotlinx.android.synthetic.main.fragment_admin_panel.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class AdminPanelFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    lateinit var query: Query
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        search_post.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(input: String?): Boolean {
                query = db.collection("ths_lectures")
                    .whereGreaterThanOrEqualTo("postTopic", input.toString())
                queryByLanguage(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })




        rv_post_list.layoutManager = LinearLayoutManager(this.requireContext())
        query = db.collection("ths_lectures")
        queryByLanguage(query)

//        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.START, ItemTouchHelper.END) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return true
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//            }
//
//        }
//        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rv_post_list)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.admin_panel_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        item.onNavDestinationSelected(findNavController())
                || super.onOptionsItemSelected(item)

    private fun queryByLanguage(query: Query) {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<Post>()
            .setLifecycleOwner(this)
            .setQuery(query, config, Post::class.java)
            .build()


        val mAdapter = object : FirestorePagingAdapter<Post, AdminPostViewHolder>(options) {


            @RequiresApi(Build.VERSION_CODES.N)
            override fun onBindViewHolder(
                viewHolder: AdminPostViewHolder,
                position: Int,
                post: Post
            ) {

                viewHolder.bind(post)
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
            ): AdminPostViewHolder {
                val binding = DataBindingUtil.inflate<RecyclerDailyLectureRowBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.recycler_daily_lecture_row,
                    parent,
                    false
                )
                return AdminPostViewHolder(binding)
            }
        }


        swipeRefreshLayout.setOnRefreshListener {
            mAdapter.refresh()
        }


        rv_post_list.adapter = mAdapter
        rv_post_list.addItemDecoration(MarginItemDecoration(16))

    }
}
