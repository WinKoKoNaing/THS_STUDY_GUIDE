package com.techhousestudio.porlar.thsstudyguide.ourths


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
import com.techhousestudio.porlar.thsstudyguide.adapters.CourseViewHolder
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerCourseRowBinding
import com.techhousestudio.porlar.thsstudyguide.helpers.MarginItemDecoration
import com.techhousestudio.porlar.thsstudyguide.models.Course
import kotlinx.android.synthetic.main.fragment_course.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class CourseFragment : Fragment() {
    private lateinit var query: Query

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_courses.layoutManager = LinearLayoutManager(requireContext())


        rv_courses.addItemDecoration(MarginItemDecoration(16))
        query = FirebaseFirestore.getInstance()
            .collection("courses")
        queryByLanguage(query)
    }

    private fun queryByLanguage(query: Query) {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<Course>()
            .setLifecycleOwner(this)
            .setQuery(query, config, Course::class.java)
            .build()


        val mAdapter = object : FirestorePagingAdapter<Course, CourseViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
                val binding: RecyclerCourseRowBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.recycler_course_row,
                    parent,
                    false
                )
                return CourseViewHolder(binding)
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onBindViewHolder(
                viewHolder: CourseViewHolder,
                position: Int,
                course: Course
            ) {
                // Bind to ViewHolder
                viewHolder.bind(course)
            }

            override fun onError(e: Exception) {
                super.onError(e)
               Timber.e(e.message.toString())
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                when (state) {
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
            }
        }


        swipeRefreshLayout.setOnRefreshListener {
            mAdapter.refresh()
        }

        rv_courses.adapter = mAdapter

    }

}
