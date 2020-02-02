package com.techhousestudio.porlar.thsstudyguide.ourths


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.techhousestudio.porlar.thsstudyguide.helpers.DepthPageTransformer
import com.techhousestudio.porlar.thsstudyguide.R
import com.techhousestudio.porlar.thsstudyguide.adapters.ViewPagerAdapter
import com.techhousestudio.porlar.thsstudyguide.databinding.FragmentThsBinding
import kotlinx.android.synthetic.main.fragment_ths.*

/**
 * A simple [Fragment] subclass.
 */
class THSFragment : Fragment() {

    lateinit var binding: FragmentThsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ths, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // enable menu
        setHasOptionsMenu(true)
        // setup viewPager with tabLayout
        setupViewPager(viewPager)
        val pageTitle = arrayListOf("Courses", "Projects", "Setting")
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = pageTitle[position]
        }.attach()
        // Animation
        viewPager.setPageTransformer(DepthPageTransformer())
    }

    private fun setupViewPager(viewPager: ViewPager2) {
        val adapter =
            ViewPagerAdapter(this.requireActivity())
        adapter.addFrag(CourseFragment())
        adapter.addFrag(ProjectFragment())
        adapter.addFrag(SettingFragment())
        viewPager.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ths_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController())
                || super.onOptionsItemSelected(item)
    }
}
