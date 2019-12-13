package com.techhousestudio.porlar.thsstudyguide

import android.view.View
import android.widget.Toast

class MyHandler {
    fun onClickCount(view:View){
        Toast.makeText(view.context,"Count Click",Toast.LENGTH_LONG).show()
    }
}