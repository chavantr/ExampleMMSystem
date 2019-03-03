package com.mywings.messmanagementsystem

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import com.mywings.messmanagementsystem.model.Mess
import com.mywings.messmanagementsystem.model.UserHolder
import com.mywings.messmanagementsystem.process.GetMessAsync
import com.mywings.messmanagementsystem.process.OnMessListener
import com.mywings.messmanagementsystem.process.ProgressDialogUtil
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*
import org.json.JSONObject

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMessListener {

    private lateinit var progressDialogUtil: ProgressDialogUtil

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        skDistance.incrementProgressBy(100)
        skPrice.incrementProgressBy(10)

        skDistance.setOnSeekBarChangeListener(seekLe)

        skPrice.setOnSeekBarChangeListener(seekLp)

        btnSearch.setOnClickListener {
            initGetMess()
        }

        progressDialogUtil = ProgressDialogUtil(this)

    }

    val seekLe = object : SeekBar.OnSeekBarChangeListener {

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }

        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

            lblL.text = "Distance $progress (meter)"


        }

    }

    val seekLp = object : SeekBar.OnSeekBarChangeListener {

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }

        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

            lblPrie.text = "Price $progress (rupees)"


        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
                startActivity(intent)
                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initGetMess() {
        progressDialogUtil.show()
        val getMessAsync = GetMessAsync()
        var jRequst = JSONObject()
        var jParam = JSONObject()
        jParam.put("Name", txtLocalName.text)
        jParam.put("Distance", skDistance.progress)
        jParam.put("FoodType", getFoodType())
        jParam.put("MessType", getMessType())
        jParam.put("Price", skPrice.progress)
        jRequst.put("request", jParam)
        getMessAsync.setOnMessListener(this, jRequst)

    }

    private fun getFoodType(): String {
        return if (chkVeg.isChecked && chkNonVeg.isChecked) "3"
        else if (chkMix.isChecked) "3"
        else if (chkMix.isChecked && !chkNonVeg.isChecked && !chkMix.isChecked) "1"
        else if (!chkMix.isChecked && chkNonVeg.isChecked && chkMix.isChecked) "2"
        else ""
    }

    private fun getMessType(): String {
        return if (chkHomeDelivery.isChecked && chkMessOnly.isChecked) "3"
        else if (!chkHomeDelivery.isChecked && !chkMessOnly.isChecked) "3"
        else if (chkHomeDelivery.isChecked && !chkMessOnly.isChecked) "1"
        else if (!chkHomeDelivery.isChecked && chkMessOnly.isChecked) "2"
        else ""
    }

    override fun onMessSuccess(result: List<Mess>) {
        progressDialogUtil.hide()
        if (result.isNotEmpty()) {
            UserHolder.getInstance().messes = result
            val intent = Intent(this@DashboardActivity, ResultMessManagementActivity::class.java)
            startActivity(intent)
        } else {
            Snackbar.make(btnSignUp, "Something went wrong,Please try again.", Snackbar.LENGTH_LONG).show()
        }

    }
}
