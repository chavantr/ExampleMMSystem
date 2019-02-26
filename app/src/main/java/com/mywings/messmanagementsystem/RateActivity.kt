package com.mywings.messmanagementsystem

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_mess_details_activty.*

class RateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        btnRate.setOnClickListener {
            var snack = Snackbar.make(btnRate, "Rated successfully", Snackbar.LENGTH_INDEFINITE)
            snack.setAction("OK") { finish() }
            snack.show()
        }


        btnSubscribe.setOnClickListener {
            var snack = Snackbar.make(btnRate, "Request completed", Snackbar.LENGTH_INDEFINITE)
            snack.setAction("OK") { finish() }
            snack.show()
        }

    }
}
