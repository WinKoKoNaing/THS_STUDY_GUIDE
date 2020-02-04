package com.techhousestudio.porlar.thsstudyguide.helpers

import android.webkit.WebView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class BindingAdapters {
    companion object {
        @BindingAdapter("app:setDateTimeFormat")
        @JvmStatic
        fun setDateTimeFormat(view: TextView, timestamp: Timestamp?) {
            if (timestamp != null)
                view.text = SimpleDateFormat(
                    "EEE, dd MMM yyyy HH:mm",
                    Locale.ENGLISH
                ).format(timestamp.toDate())
        }

        @BindingAdapter("app:loadDataFromString","app:checkMember")
        @JvmStatic
        fun loadDataFromString(view: WebView, data: String?,isMember: Boolean) {
            if (data != null) {
                if (isMember){
                    if (data.isEmpty()) {
                        view.loadData("Notes not available now", "text/html", "UTF-8")
                    } else
                        view.loadData(data, "text/html", "UTF-8")

                }else{
                    view.loadData("Need to be THS member ...", "text/html", "UTF-8")
                }

            }
        }

    }
}

