package com.mywings.messmanagementsystem

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mywings.messmanagementsystem.model.Menu
import com.mywings.messmanagementsystem.process.GetMenuAsync
import com.mywings.messmanagementsystem.process.OnMenuListener
import com.mywings.messmanagementsystem.process.ProgressDialogUtil
import kotlinx.android.synthetic.main.activity_todays_menu.*
import java.util.*

class TodaysMenuActivity : AppCompatActivity(), OnMenuListener {

    private lateinit var progressDialogUtil: ProgressDialogUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todays_menu)
        progressDialogUtil = ProgressDialogUtil(this)
        init()
        btnFinish.setOnClickListener { finish() }
    }

    private fun getDay(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    private fun init() {
        progressDialogUtil.show()
        val getMenuAsync = GetMenuAsync()
        getMenuAsync.setOnMenuListener(this, "?day=${getDay()}")
    }

    override fun onMenuSuccess(result: Menu?) {
        progressDialogUtil.hide()
        if (null != result) {
            lblItem1.text = "Menu : " + result.item1
            lblItem2.text = "Menu : " + result.item2
            lblItem3.text = "Menu : " + result.item3
            lblItem4.text = "Menu : " + result.item4
            lblItem5.text = "Menu : " + result.item5
        }

    }
}
