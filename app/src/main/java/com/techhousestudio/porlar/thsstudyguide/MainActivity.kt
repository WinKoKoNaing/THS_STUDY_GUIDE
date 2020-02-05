package com.techhousestudio.porlar.thsstudyguide

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.techhousestudio.porlar.thsstudyguide.databinding.ActivityMainBinding
import com.techhousestudio.porlar.thsstudyguide.helpers.setupWithNavController
import com.techhousestudio.porlar.thsstudyguide.lectures.UpdateFragment
import com.techhousestudio.porlar.thsstudyguide.models.MessageBox
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)



        setSupportActionBar(toolbar)
        toolbar_text.text = toolbar.title
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }


        db.collection("message_box")
            .document("ths")
            .get().addOnSuccessListener {
                val messageBox = it.toObject(MessageBox::class.java)
                val versionCode =
                    BuildConfig.VERSION_CODE
                val versionName =
                    BuildConfig.VERSION_NAME
                if (messageBox!!.hasUpdate && messageBox.currentVersion != versionName) {
                    Timber.i("Show Dialog")
                    val updateFragment = UpdateFragment()
                    var b = bundleOf(
                        "version" to messageBox.currentVersion
                    )
                    updateFragment.arguments = b
                    updateFragment.isCancelable = false
                    updateFragment.show(supportFragmentManager, "update")
                }
            }



        currentNavController?.observe(this, Observer {
            it.addOnDestinationChangedListener { _, destination, _ ->
                //                title = when (destination.id) {
//                    R.id.dailyFragment -> "Daily Lectures"
//                    R.id.THSFragment -> "Tech House Studio"
//                    R.id.timeTableFragment -> "Course Timetable"
//                    else -> "Error"
//                }
                toolbar_text.text = destination.label
                if (destination.id == R.id.dailyFragment || destination.id == R.id.timeTableFragment ||
                    destination.id == R.id.THSFragment
                ) {
                    toolbar_text.visibility = View.VISIBLE

                    bottom_navigation.visibility = View.VISIBLE
                    Timber.i("Visible " + destination.id)
                } else {
                    Timber.i("Gone")
                    toolbar.title = destination.label
                    toolbar_text.visibility = View.GONE
                    bottom_navigation.visibility = View.GONE
                }
            }
        })

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with app_setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val navGraphIds = listOf(
            R.navigation.daily_lecture,
            R.navigation.time_table,
            R.navigation.our_ths
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )


//         Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}
