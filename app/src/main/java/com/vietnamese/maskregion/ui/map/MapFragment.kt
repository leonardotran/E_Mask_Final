package com.vietnamese.maskregion.ui.map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.vietnamese.maskregion.MainActivity
import com.vietnamese.maskregion.R
import com.vietnamese.maskregion.utils.CustomInfoWindow
import com.yomemo.pentagon.model.InfoWindowData


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener {


    private lateinit var main: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        main = activity as MainActivity
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    lateinit var mMap: GoogleMap
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        mMap.setOnInfoWindowClickListener(this)
        mMap.uiSettings.setMyLocationButtonEnabled(true)

        val mission1 = LatLng(37.3134948, -121.9335041)
        val mission2 = LatLng(37.0366135, -121.5737036)
        val mission3 = LatLng(37.3408825, -121.9224587)
        val mission4 = LatLng(37.3463779, -121.8113664)
        val mission5 = LatLng(37.3688462, -122.0823926)
        createMarker(
            mission1,
            "Santa Clara Valley Medical Center",
            "Santa Clara Valley Medical Center is a 731-bed tertiary referral hospital in Santa Clara County, California. SCVMC is owned and operated by the county as both a research hospital and teaching hospital.",
            "Medical Center",
            "https://aecom.com/wp-content/uploads/2018/01/sobrato-pavilion-aecom.jpg",
            mMap
        )
        createMarker(
            mission2,
            "St. Louise Regional Hospital",
            "St. Louise Regional Hospital is the community hospital for Santa Clara & San Benito counties.",
            "General hospital",
            "https://slrh.sccgov.org/PublishingImages/topreason_slrh.jpg",
            mMap
        )
        createMarker(
            mission3,
            "Roots Community Health Center",
            "Open to the community on Wednesdays from 10 am – 3pm. Registration preferred, but not mandatory.  Call 408-490-4710 or register at",
            "Community Health Center",
            "https://media-exp1.licdn.com/dms/image/C560BAQHaHCzmLBKNKA/company-logo_200_200/0?e=2159024400&v=beta&t=EJhGZ3yUmSqVJ11OMCPIpQ1pNwCUK5KBjcYo069Wi4E",
            mMap
        )
        createMarker(
            mission4,
            "Santa Clara County Fairgrounds and Mount Pleasant High School",
            "Community testing is currently available at the Santa Clara County Fairgrounds (344 Tully Road) and at Mount Pleasant High School (1750 S White Road) to anyone who meets the testing criteria, which includes individuals with symptoms, and asymptomatic workers in specific fields. Both sites will operate from 1pm to 7pm, Monday through Friday and 9am to 1pm on Saturday. Visit their screening tool to determine if you are eligible for these services. Testing at the Fairgrounds is a free, drive-through service.",
            "High School",
            "https://3.files.edl.io/9e50/19/02/11/233305-54b2b686-ef01-49af-915e-89de2d1d3fe7.jpg",
            mMap
        )
        createMarker(
            mission5,
            "El Camino Healthcare District Testing",
            "El Camino Health’s Mountain View hospital (2500 Grant Rd) offers COVID-19 testing funded by the El Camino Healthcare District for those who live, work or go to school within the District (including most of Mountain View, Los Altos and Los Altos Hills, a large portion of Sunnyvale, and small sections of Cupertino, Santa Clara and Palo Alto)",
            "Healthcare Center",
            "https://www.mv-voice.com/news/photos/2020/july/29/24877_col.jpg",
            mMap
        )

        val customInfoWindow = CustomInfoWindow(main)
        googleMap.setInfoWindowAdapter(customInfoWindow)

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mission1, 11f))
    }

    lateinit var marker1: Marker
    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker.equals(marker1)) {
            marker.showInfoWindow()
        }
        return false
    }

    fun createMarker(
        position: LatLng,
        title: String,
        message: String,
        type: String,
        url: String,
        googleMap: GoogleMap
    ) {
        val markerOptions = MarkerOptions()
        markerOptions.position(position)
            .title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        marker1 = mMap.addMarker(markerOptions)
        val info = InfoWindowData()
        info.title = title
        info.message = message
        info.type = type
        info.imageUrl = url
        marker1.setTag(info)
    }

    override fun onInfoWindowClick(marker: Marker) {
        val url =
            "https://www.google.com/maps/search/?api=1&query=" + marker.position.latitude + "," + marker.position.longitude
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)

//        findNavController().navigate(R.id.action_mapFragment_to_pointsAwardedFragment)
    }

}
