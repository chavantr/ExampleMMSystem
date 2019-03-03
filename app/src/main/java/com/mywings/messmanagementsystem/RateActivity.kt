package com.mywings.messmanagementsystem

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.mywings.messmanagementsystem.model.UserHolder
import com.mywings.messmanagementsystem.process.CommentAsync
import com.mywings.messmanagementsystem.process.OnRateListener
import com.mywings.messmanagementsystem.process.ProgressDialogUtil
import kotlinx.android.synthetic.main.activity_rate.*

class RateActivity : AppCompatActivity(), OnRateListener {

    private lateinit var progressDialogUtil: ProgressDialogUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        progressDialogUtil = ProgressDialogUtil(this)

        btnRate.setOnClickListener {

            if (txtComment.text!!.isNotEmpty()) {
                init()
            }

        }


    }

    private fun init() {
        progressDialogUtil.show()
        val commentAsync = CommentAsync()
        commentAsync.setOnRateListener(
            this,
            "?id=${UserHolder.getInstance().id}&rate=${rate.rating}&${txtComment.text}"
        )
    }

    override fun onFine(result: String?) {
        progressDialogUtil.hide()
        if (result.isNullOrBlank()) {
            Snackbar.make(btnRate, "Something went wrong, Please try again.", Snackbar.LENGTH_LONG).show()
        } else {
            var event = Snackbar.make(btnRate, "Commented", Snackbar.LENGTH_INDEFINITE)
            event.setAction("Ok") {
                finish()
            }
            event.show()
        }
    }
}
