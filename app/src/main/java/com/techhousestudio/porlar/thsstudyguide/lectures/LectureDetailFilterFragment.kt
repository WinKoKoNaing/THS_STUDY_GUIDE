package com.techhousestudio.porlar.thsstudyguide.lectures


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.adapters.LectureDetialViewHolder
import com.techhousestudio.porlar.thsstudyguide.databinding.RecyclerViewpagerDetailRowBinding
import com.techhousestudio.porlar.thsstudyguide.models.Post
import com.techhousestudio.porlar.thsstudyguide.models.User
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_lecture_detail_filter.*
import java.util.*


class LectureDetailFilterFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    lateinit var query: Query

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lecture_detail_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as AppCompatActivity).supportActionBar?.title = "Filter lecture notes by Day"

        // Get Argument
        val lectureDate = arguments?.getLong("lecture_date")
        // Setup DateTime
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -3)
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 3)
        // setUp Date
        val calendar = Calendar.getInstance()
        calendar.time = Date(lectureDate!!)
//        calendar.set(Calendar.YEAR,29)
//        calendar.set(Calendar.HOUR, 0)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 0)


        val horizontalCalendar = HorizontalCalendar.Builder(requireActivity(), R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(5)
            .defaultSelectedDate(calendar)
            .build()



        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {

                // Query Data
                query = db.collection("ths_lectures")
                    .whereGreaterThanOrEqualTo("postLectureDate", getStartDate(date!!))
                    .whereLessThanOrEqualTo("postLectureDate", getEndDate(date))
                checkMember()

            }

        }

        // Query Data
        query = db.collection("ths_lectures")
            .whereGreaterThanOrEqualTo("postLectureDate", getStartDate(calendar))
            .whereLessThanOrEqualTo("postLectureDate", getEndDate(calendar))
        checkMember()

    }


    private fun queryByDate(query: Query) {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<Post>()
            .setLifecycleOwner(this)
            .setQuery(query, config, Post::class.java)
            .build()


        val mAdapter = object : FirestorePagingAdapter<Post, LectureDetialViewHolder>(options) {


            @RequiresApi(Build.VERSION_CODES.N)
            override fun onBindViewHolder(
                viewHolder: LectureDetialViewHolder,
                position: Int,
                post: Post
            ) {
                viewHolder.bind(post)
            }

            override fun onLoadingStateChanged(state: LoadingState) = when (state) {
                LoadingState.LOADING_INITIAL -> {
//                    daily_swipeRefreshLayout.isRefreshing = true
                }

                LoadingState.LOADING_MORE -> {
//                    daily_swipeRefreshLayout.isRefreshing = true
                }

                LoadingState.LOADED -> {
//                    daily_swipeRefreshLayout.isRefreshing = false
                }

                LoadingState.ERROR -> {
                    Toast.makeText(
                        context,
                        "Error Occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
//                    daily_swipeRefreshLayout.isRefreshing = false
                }

                LoadingState.FINISHED -> {
//                    daily_swipeRefreshLayout.isRefreshing = false
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): LectureDetialViewHolder {
                val binding = DataBindingUtil.inflate<RecyclerViewpagerDetailRowBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.recycler_viewpager_detail_row,
                    parent,
                    false
                )
                return LectureDetialViewHolder(binding)
            }
        }


//        daily_swipeRefreshLayout.setOnRefreshListener {
//            mAdapter.refresh()
//        }

        viewPagerDetailLecture.adapter = mAdapter

    }


    private fun checkMember() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .get().addOnSuccessListener {

                    val currentUser = it.toObject(User::class.java)
                    if (currentUser != null) {
                        if (currentUser.isMember)
                            queryByDate(query)
                        else {
                            Snackbar.make(
                                requireView(),
                                "Sorry, Need to be THS member to access filter",
                                Snackbar.LENGTH_LONG
                            ).setAction(
                                "Understand"
                            ) {
                                Toast.makeText(requireContext(), "Thanks", Toast.LENGTH_LONG).show()
                            }.show()

                        }
                    }
                }
        } else {
            Snackbar.make(
                requireView(),
                "Sorry Please login, Need to be THS member to access filter",
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    private fun getStartDate(calendar: Calendar): Timestamp {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        return Timestamp(calendar.time)
    }

    private fun getEndDate(calendar: Calendar): Timestamp {
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        return Timestamp(calendar.time)
    }
}
