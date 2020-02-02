package com.techhousestudio.porlar.thsstudyguide.lectures


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.adapters.DailyLectureViewHolder
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerDailyLectureRowBinding
import com.techhousestudio.porlar.thsstudyguide.helpers.MarginItemDecoration
import com.techhousestudio.porlar.thsstudyguide.models.Post
import kotlinx.android.synthetic.main.fragment_daily_lecture_contrainer.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class DailyLectureContentFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    var queryName: String? = null
    lateinit var query: Query
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_lecture_contrainer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queryName = arguments?.getString("language")
        rv_daily_lecture.layoutManager = LinearLayoutManager(this.requireContext())

        query = db.collection("ths_lectures").whereEqualTo("classLanguage", queryName)
        queryByLanguage(query)

    }

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


        val mAdapter = object : FirestorePagingAdapter<Post, DailyLectureViewHolder>(options) {


            @RequiresApi(Build.VERSION_CODES.N)
            override fun onBindViewHolder(
                viewHolder: DailyLectureViewHolder,
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
                    daily_swipeRefreshLayout.isRefreshing = true
                }

                LoadingState.LOADING_MORE -> {
                    daily_swipeRefreshLayout.isRefreshing = true
                }

                LoadingState.LOADED -> {
                    daily_swipeRefreshLayout.isRefreshing = false
                }

                LoadingState.ERROR -> {
                    Toast.makeText(
                        context,
                        "Error Occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                    daily_swipeRefreshLayout.isRefreshing = false
                }

                LoadingState.FINISHED -> {
                    daily_swipeRefreshLayout.isRefreshing = false
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): DailyLectureViewHolder {
                val binding = DataBindingUtil.inflate<RecyclerDailyLectureRowBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.recycler_daily_lecture_row,
                    parent,
                    false
                )
                return DailyLectureViewHolder(binding)
            }
        }


        daily_swipeRefreshLayout.setOnRefreshListener {
            mAdapter.refresh()
        }

        rv_daily_lecture.adapter = mAdapter
        rv_daily_lecture.addItemDecoration(MarginItemDecoration(16))

    }

}
