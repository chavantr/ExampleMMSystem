package com.mywings.messmanagementsystem

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.mywings.messmanagementsystem.process.ProgressDialogUtil
import com.mywings.messmanagementsystem.routes.Constants
import com.mywings.messmanagementsystem.routes.DirectionsJSONParser
import com.mywings.messmanagementsystem.routes.JsonUtil
import kotlinx.android.synthetic.main.activity_result_mess_management.*
import kotlinx.android.synthetic.main.layout_mess_row.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.HashMap

class ResultMessManagementActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private lateinit var messAdapter: MessAdapter

    private var mMap: GoogleMap? = null
    private val SHOW_ICON_IN_MAP = 49
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var latLng: LatLng = LatLng(18.490625, 73.843489)
    private var locationManager: LocationManager? = null
    private lateinit var cPosition: Marker
    private lateinit var messes: List<Mess>

    private lateinit var jsonUtil: JsonUtil

    private lateinit var nsource: String

    private lateinit var ndest: String

    private lateinit var progressDialogUtil: ProgressDialogUtil

    private var destlat: Double = 0.0

    private var destlng: Double = 0.0

    private var srctlat: Double = 0.0

    private var srclng: Double = 0.0

    private val strPopUpData = arrayOf("Info", "Route")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_mess_management)

        var frame = activity_place_map as SupportMapFragment

        frame.getMapAsync(this)

        jsonUtil = JsonUtil()

        progressDialogUtil = ProgressDialogUtil(this)

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
        try {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val enabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (enabled) {
                var location = LocationUtil.getBestLastKnownLocation(this)

                latLng = LatLng(location.latitude, location.longitude)
            }
        } catch (e: Exception) {
        }

        mMap!!.uiSettings.isMyLocationButtonEnabled = false
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

        srctlat = latLng.latitude
        srclng = latLng.longitude

        mMap!!.addMarker(MarkerOptions().position(latLng))

        val cameraPos = CameraPosition.Builder().tilt(60f).target(latLng).zoom(20f).build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos), 1000, null)

        mMap!!.setInfoWindowAdapter(infoWindowAdapter)

        mMap!!.setOnInfoWindowClickListener(infoClick)


        if (messes.isNotEmpty())
            for (i in messes.indices) {
                val mess = messes[i]
                val ltLg = LatLng(mess.latitude.toDouble(), mess.longitude.toDouble())
                var marker = MarkerOptions().position(ltLg).icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
                mMap!!.addMarker(marker)
                    .snippet =
                    "${mess.name}#${mess.localArea}#${mess.rating}#${mess.foodType}#${mess.messType}#${mess.id}#${mess.latitude}#${mess.longitude}"
            }
    }


    private fun questionPaperSelectConfirmation(context: Context, strPopUpData: Array<String>, it: Marker) {
        val builder = AlertDialog.Builder(
            context
        )
        builder.setTitle("Select").setIcon(
            context.resources.getDrawable(R.mipmap.ic_launcher)
        )
        builder.setSingleChoiceItems(strPopUpData, 0) { dialog, which ->
            val language = strPopUpData[which]
            when (language) {
                "Info" -> {
                    info(it)
                }
                "Route" -> {
                    showL(it)
                }
            }
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


    private val infoWindowAdapter = object : GoogleMap.InfoWindowAdapter {
        override fun getInfoContents(marker: Marker?): View? {
            var view: View? = null
            var values: List<String>
            try {
                if (marker!!.tag != 1) {
                    view = layoutInflater.inflate(R.layout.layout_mess_row, null)
                    values = marker!!.snippet.toString().split("#")
                    view!!.lblName.text = values[0]
                    view!!.lblLocalarea.text = values[1]
                    view!!.lblRating.text = "Rating : ${values[2]}"
                    view!!.lblFoodType.text = "Food Type : " + generateFoodType(values[3])
                    view!!.lblMessType.text = "Mess Type : " + generateMessType(values[4])
                    UserHolder.getInstance().id = values[5].toInt()
                    view!!.lblLocalarea.visibility = View.VISIBLE
                    view!!.lblRating.visibility = View.VISIBLE
                    view!!.lblFoodType.visibility = View.VISIBLE
                    view!!.lblMessType.visibility = View.VISIBLE
                    nsource = "Your location"
                    ndest = values[0]
                }
            } catch (e: Exception) {
                view!!.lblName.text = "Your location"
            }

            return view;
        }

        override fun getInfoWindow(marker: Marker?): View? {
            return null
        }

    }

// IMP

    private var key = "&key=AIzaSyClCN7T0VPX7MIoOJEMA3W9JLXhV_S7yx4"

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
        val strOrigin = ("origin=" + origin.latitude + ","
                + origin.longitude)
        val strDest = "destination=" + dest.latitude + "," + dest.longitude
        val sensor = "sensor=false"

        val parameters = "$strOrigin&$strDest&$sensor$key"
        val output = "json"
        return ("https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters)
    }

    private inner class DownloadTask : AsyncTask<String, Void, String>() {

        // Downloading data in non-ui thread
        override fun doInBackground(vararg url: String): String {

            // For storing data from web service
            var data = ""

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0])
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())

            }

            return data
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            val parserTask = ParserTask(mMap!!)

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result)

        }
    }

    /** A method to download json data from url  */
    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection.connect()

            // Reading data from url
            iStream = urlConnection.inputStream

            /* val br = BufferedReader(
                 InputStreamReader(
                     iStream!!
                 )
             )*/
            data = jsonUtil.convertStreamToString(iStream)
            // br.close()
        } catch (e: Exception) {

        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    /** A class to parse the Google Places in JSON format  */
    private inner class ParserTask(internal var map: GoogleMap?) :
        AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

        // Parsing the data in non-ui thread
        override fun doInBackground(
            vararg jsonData: String
        ): List<List<HashMap<String, String>>>? {

            val jObject: JSONObject
            var jArray: JSONArray
            var routes: List<List<HashMap<String, String>>>? = null

            try {
                jObject = JSONObject(jsonData[0])
                val parser = DirectionsJSONParser()
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return routes
        }

        // Executes in UI thread, after the parsing process
        override fun onPostExecute(result: List<List<HashMap<String, String>>>) {

            //progressDialogUtil.show()

            var points: java.util.ArrayList<LatLng>? = null

            var lineOptions: PolylineOptions? = null

            // MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (i in result.indices) {
                points = java.util.ArrayList()
                lineOptions = PolylineOptions()
                // Fetching i-th route
                val path = result[i]
                // Fetching all the points in i-th route
                for (j in path.indices) {
                    // lineOptions = new PolylineOptions();
                    val point = path[j]
                    val lat = java.lang.Double.parseDouble(point[Constants.LAT]!!)
                    val lng = java.lang.Double.parseDouble(point[Constants.LNG]!!)
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(9f)
                lineOptions.color(Color.RED)
            }
            // Drawing polyline in the Google Map for the i-th route
            map!!.clear()

            if (null != lineOptions) {
                map!!.addPolyline(lineOptions)
                setStartPosition(srctlat, srclng)
                setDestPosition(destlat, destlng)
                if (map != null) {
                    fixZoom(lineOptions.points)
                }

                progressDialogUtil.hide()

            } else {
                Toast.makeText(
                    this@ResultMessManagementActivity,
                    "Enable to draw routes, Please try again",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    /**
     * @param lat
     * @param lng
     */
    private fun setStartPosition(lat: Double, lng: Double) {
        var startmark = mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title(nsource)
                .snippet("")
        )
        startmark.tag = 1
    }

    /**
     * @param lat
     * @param lng
     */
    private fun setDestPosition(lat: Double, lng: Double) {
        var destmark = mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title(ndest)
                .snippet("")
        )

        destmark.tag = 1
    }


    private fun fixZoom(points: List<LatLng>) {
        val bc = LatLngBounds.Builder()
        for (item in points) {
            bc.include(item)
        }
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 90))
    }

// IMP

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

            //showL(it)

            questionPaperSelectConfirmation(this@ResultMessManagementActivity, strPopUpData, it)


        } catch (e: Exception) {
        }
    }

    private fun info(it: Marker) {
        val intent = Intent(this@ResultMessManagementActivity, MessDetailsActivity::class.java)
        intent.putExtra("id", it.snippet)
        startActivity(intent)
    }

    private fun showL(it: Marker) {
        progressDialogUtil.show()

        val value = it.snippet.toString().split("#")[6]
        val value0 = it.snippet.toString().split("#")[7]

        val source = LatLng(srctlat, srclng)

        destlat = value.toDouble()

        destlng = value0.toDouble()

        val dest = LatLng(destlat, destlng)


        val nav = DownloadTask()

        nav.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getDirectionsUrl(source, dest))
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
