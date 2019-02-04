package com.mywings.messmanagementsystem

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mywings.messmanagementsystem.binder.MessAdapter
import com.mywings.messmanagementsystem.model.Mess
import com.mywings.messmanagementsystem.model.UserHolder
import kotlinx.android.synthetic.main.activity_result_mess_management.*
import kotlinx.android.synthetic.main.layout_mess_row.view.*

class ResultMessManagementActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private lateinit var messAdapter: MessAdapter

    private var mMap: GoogleMap? = null
    private val SHOW_ICON_IN_MAP = 49
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var latLng: LatLng = LatLng(18.515665, 73.924090)
    private var locationManager: LocationManager? = null
    private lateinit var cPosition: Marker
    private lateinit var messes: List<Mess>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_mess_management)

        var frame = activity_place_map as SupportMapFragment

        frame.getMapAsync(this)

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap?) {


        this.messes = UserHolder.getInstance().messes

        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupMap()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                SHOW_ICON_IN_MAP
            )
        }

    }


    private fun setupMap() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val enabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (enabled) {
            var location = LocationUtil.getBestLastKnownLocation(this)

            latLng = LatLng(location.latitude, location.longitude)
        }

        mMap!!.uiSettings.isMyLocationButtonEnabled = false
        // mMap!!.uiSettings.isMyLocationButtonEnabled = true

        mGoogleApiClient = GoogleApiClient.Builder(this!!)

            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()



        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval((10 * 1000).toLong())
            .setFastestInterval((1 * 1000).toLong())
        mGoogleApiClient!!.connect()

        val strokeColor = ContextCompat.getColor(this, R.color.map_circle_stroke)
        val shadeColor = ContextCompat.getColor(this, R.color.map_circle_shade)
        val latLng = this.latLng
        mMap!!.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(5.0)
                .fillColor(shadeColor)
                .strokeColor(strokeColor)
                .strokeWidth(2f)
        )

        mMap!!.addMarker(MarkerOptions().position(latLng))

        val cameraPos = CameraPosition.Builder().tilt(60f).target(latLng).zoom(20f).build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos), 1000, null)

        mMap!!.setInfoWindowAdapter(infoWindowAdapter)

        mMap!!.setOnInfoWindowClickListener(infoClick)


        if (null != messes && messes.isNotEmpty())
            for (i in messes.indices) {
                val mess = messes.get(i)
                val ltLg = LatLng(mess.latitude.toDouble(), mess.longitude.toDouble())
                mMap!!.addMarker(MarkerOptions().position(ltLg)).snippet =
                    "${mess.name}#${mess.localArea}#${mess.rating}#${mess.foodType}#${mess.messType}"
            }
    }

    private val infoWindowAdapter = object : GoogleMap.InfoWindowAdapter {
        override fun getInfoContents(marker: Marker?): View? {
            var view: View? = null
            var values: List<String>
            try {
                view = layoutInflater.inflate(R.layout.layout_mess_row, null)
                values = marker!!.snippet.toString().split("#")
                view!!.lblName.text = values[0]
                view!!.lblLocalarea.text = values[1]
                view!!.lblRating.text = "Rating : ${values[2]}"
                view!!.lblFoodType.text = "Food Type : " + generateFoodType(values[3])
                view!!.lblMessType.text = "Mess Type : " + generateMessType(values[4])
                view!!.lblLocalarea.visibility = View.VISIBLE
                view!!.lblRating.visibility = View.VISIBLE
                view!!.lblFoodType.visibility = View.VISIBLE
                view!!.lblMessType.visibility = View.VISIBLE
            } catch (e: Exception) {
                view!!.lblName.text = "Your location"
            }
            return view;
        }

        override fun getInfoWindow(marker: Marker?): View? {
            return null
        }

    }

    private fun generateMessType(id: String): String {

        return when (id) {
            "3", "4" -> "Mix"
            "1" -> "Veg"
            "2" -> "Non Veg"
            else -> "Mix"
        }

    }

    private fun generateFoodType(id: String): String {
        return when (id) {
            "3", "4" -> "Mix"
            "1" -> "Home Delivery"
            "2" -> "Mess Only"
            else -> "Mix"
        }

    }

    private val infoClick = GoogleMap.OnInfoWindowClickListener {
        try {
            //val value = it.snippet.toString().split("#")[3]
            val intent = Intent(this@ResultMessManagementActivity, MessDetailsActivty::class.java)
            intent.putExtra("id", it.snippet)
            startActivity(intent)
        } catch (e: Exception) {
        }
    }


    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            SHOW_ICON_IN_MAP ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    setupMap()
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onLocationChanged(p0: Location?) {

    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }
}
