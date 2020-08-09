package com.vietnamese.maskregion.utils


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.GoogleMap
import com.vietnamese.maskregion.R
import com.yomemo.pentagon.model.InfoWindowData


class CustomInfoWindow(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = (context as Activity).layoutInflater
            .inflate(R.layout.map_custom_infowindow, null)

        val textTitle = view.findViewById<TextView>(R.id.textTitle)
        val textMessage = view.findViewById<TextView>(R.id.textMessage)

        val textType = view.findViewById<TextView>(R.id.textType)
        val imageType = view.findViewById<ImageView>(R.id.image_type)


        val infoWindowData = marker.tag as InfoWindowData?

        textTitle.setText(infoWindowData?.title)
        textMessage.setText(infoWindowData?.message)
        textType.setText(infoWindowData?.type)

        infoWindowData?.imageUrl?.let {
            Log.d("","Image:"+ it)
            Glide.with(context).load(it)
                .apply(RequestOptions.centerInsideTransform())
                .into(imageType)
        }
        return view
    }
}
