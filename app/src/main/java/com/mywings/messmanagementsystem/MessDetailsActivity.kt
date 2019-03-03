package com.mywings.messmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.mywings.messmanagementsystem.model.UserHolder
import com.mywings.messmanagementsystem.process.ListSuAsync
import com.mywings.messmanagementsystem.process.OnListListener
import com.mywings.messmanagementsystem.process.ProgressDialogUtil
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.content_mess_details_activty.*


class MessDetailsActivity : AppCompatActivity(), OnListListener {

    private lateinit var progressDialogUtil: ProgressDialogUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mess_details_activty)
        setSupportActionBar(toolbar)
        progressDialogUtil = ProgressDialogUtil(this)
        var values: List<String>
        try {
            values = intent.extras.getString("id").split("#")
            lblName.text = "Name : " + values[0]
            lblLocalarea.text = "Address : " + values[1]
            lblRating.text = "Rating : ${values[2]}"
            lblFoodType.text = "Food Type : " + generateFoodType(values[3])
            lblMessType.text = "Mess Type : " + generateMessType(values[4])

            btnRateScreen.setOnClickListener {
                val intent = Intent(this@MessDetailsActivity, RateActivity::class.java)
                startActivity(intent)
            }

            btnSubscribeEvent.setOnClickListener {
                init()
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

    private fun init() {
        progressDialogUtil.show()
        val listSuAsync = ListSuAsync()
        listSuAsync.setOnLikeListener(
            this,
            "?uid=${UserHolder.getInstance().user.id}&mid=${UserHolder.getInstance().id}"
        )
    }

    override fun onList(result: String) {
        progressDialogUtil.hide()
        if (result.isNullOrBlank()) {
            Snackbar.make(btnSubscribeEvent, "Something went wrong, Please try again.", Snackbar.LENGTH_LONG).show()
        } else {
            var event = Snackbar.make(btnSubscribeEvent, "Perfect", Snackbar.LENGTH_INDEFINITE)
            event.setAction("Ok") {
                finish()
            }
            event.show()
        }
    }
}
