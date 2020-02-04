package com.techhousestudio.porlar.thsstudyguide.timetable


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
import com.techhousestudio.porlar.thsstudyguide.adapters.TimeTableViewHolder
import com.techhousestudio.porlar.thsstudyguide.databinding.FragmentTimeTableBinding
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerTimeTableRowBinding
import com.techhousestudio.porlar.thsstudyguide.helpers.MarginItemDecoration
import com.techhousestudio.porlar.thsstudyguide.models.TimeTable
import kotlinx.android.synthetic.main.fragment_time_table.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class TimeTableFragment : Fragment() {

    var queryName: String? = null
    lateinit var query: Query
    lateinit var binding: FragmentTimeTableBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_time_table, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Course Time Table"

        rvCourseList.layoutManager = LinearLayoutManager(this.requireContext())

        rvCourseList.addItemDecoration(MarginItemDecoration(16))
        query = FirebaseFirestore.getInstance()
            .collection("thsClasses")
        queryByLanguage(query)

        chip_group.setOnCheckedChangeListener { _, checkedId ->
            queryName = when (checkedId) {
                R.id.chipAndroid -> "Android"
                R.id.chipJava -> "Java"
                R.id.chipPhp -> "Php"
                R.id.chipNodeJs -> "NodeJs"
                R.id.chipIot -> "Iot"
                R.id.chipComputerBasic -> "ComputerBasic"
                else -> null
            }
            Timber.i( queryName.toString())
            query = if (queryName == null) {
                FirebaseFirestore.getInstance()
                    .collection("thsClasses")
            } else {
                FirebaseFirestore.getInstance()
                    .collection("thsClasses").whereEqualTo("classLanguage", queryName)
            }
            queryByLanguage(query)
        }


    }

    private fun queryByLanguage(query: Query) {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<TimeTable>()
            .setLifecycleOwner(this)
            .setQuery(query, config, TimeTable::class.java)
            .build()


        val mAdapter = object : FirestorePagingAdapter<TimeTable, TimeTableViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableViewHolder {
                val binding: RecyclerTimeTableRowBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.recycler_time_table_row,
                    parent,
                    false
                )
                return TimeTableViewHolder(binding)
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onBindViewHolder(
                viewHolder: TimeTableViewHolder,
                position: Int,
                timeTable: TimeTable
            ) {
                // Bind to ViewHolder
                viewHolder.bind(timeTable)
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

        rvCourseList.adapter = mAdapter

    }

}
