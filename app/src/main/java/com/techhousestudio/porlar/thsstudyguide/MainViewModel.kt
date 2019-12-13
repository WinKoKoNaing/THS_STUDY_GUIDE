package com.techhousestudio.porlar.thsstudyguide

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var count = 0
    var _count = MutableLiveData<Int>()

}