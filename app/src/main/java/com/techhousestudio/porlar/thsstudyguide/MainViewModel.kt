package com.techhousestudio.porlar.thsstudyguide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var count: Int = 0

    var _countNumber = MutableLiveData<Int>()

    fun addCountOne() {
        _countNumber.value = ++count

    }


}