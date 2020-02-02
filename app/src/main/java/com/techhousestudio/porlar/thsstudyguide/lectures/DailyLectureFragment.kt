package com.techhousestudio.porlar.thsstudyguide.lectures


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.helpers.ZoomOutPageTransformer
import com.techhousestudio.porlar.thsstudyguide.adapters.ViewPagerAdapter
import com.techhousestudio.porlar.thsstudyguide.databinding.FragmentDailyBinding
import kotlinx.android.synthetic.main.fragment_daily.*

class DailyLectureFragment : Fragment() {

    lateinit var binding: FragmentDailyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_daily, container, false
        )


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager(viewPager)
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        val pageTitle = arrayListOf("Android", "Php", "NodeJs", "Java","IOT","Computer Basic")
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = pageTitle[position]

        }.attach()
    }


    private fun setupViewPager(viewPager: ViewPager2) {
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.addFrag(DailyLectureContentFragment().also { lecture ->
            val b = Bundle()
            b.putString("language", "Android")
            lecture.arguments = b
        })
        adapter.addFrag(DailyLectureContentFragment().also { lecture ->
            val b = Bundle()
            b.putString("language", "Php")
            lecture.arguments = b
        })
        adapter.addFrag(DailyLectureContentFragment().also { lecture ->
            val b = Bundle()
            b.putString("language", "NodeJs")
            lecture.arguments = b
        })
        adapter.addFrag(DailyLectureContentFragment().also { lecture ->
            val b = Bundle()
            b.putString("language", "Java")
            lecture.arguments = b
        })
        adapter.addFrag(DailyLectureContentFragment().also { lecture ->
            val b = Bundle()
            b.putString("language", "Iot")
            lecture.arguments = b
        })
        adapter.addFrag(DailyLectureContentFragment().also { lecture ->
            val b = Bundle()
            b.putString("language", "ComputerBasic")
            lecture.arguments = b
        })
        viewPager.adapter = adapter


    }


}
