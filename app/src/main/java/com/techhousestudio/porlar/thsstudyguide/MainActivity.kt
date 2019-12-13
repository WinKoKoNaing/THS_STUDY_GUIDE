package com.techhousestudio.porlar.thsstudyguide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.techhousestudio.porlar.thsstudyguide.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)



        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


        binding.mainViewModel = mainViewModel


        binding.lifecycleOwner = this

    }


}
