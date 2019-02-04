package com.mywings.messmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mess_details_activty.*
import kotlinx.android.synthetic.main.content_mess_details_activty.*

class MessDetailsActivty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mess_details_activty)
        setSupportActionBar(toolbar)
        var values: List<String>
        try {
            values = intent.extras.getString("id").split("#")
            lblName.text = values[0]
            lblLocalarea.text = values[1]
            lblRating.text = "Rating : ${values[2]}"
            lblFoodType.text = "Food Type : " + generateFoodType(values[3])
            lblMessType.text = "Mess Type : " + generateMessType(values[4])
            btnRate.setOnClickListener {
                val intent = Intent(this@MessDetailsActivty, RateActivity::class.java)
                startActivity(intent)
            }
            btnSubscribe.setOnClickListener {

            }
        } catch (e: Exception) {

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
}
