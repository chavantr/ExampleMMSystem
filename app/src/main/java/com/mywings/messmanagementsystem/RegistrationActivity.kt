package com.mywings.messmanagementsystem

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.mywings.messmanagementsystem.process.OnRegistrationListener
import com.mywings.messmanagementsystem.process.ProgressDialogUtil
import com.mywings.messmanagementsystem.process.RegistrationAsync
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity(), OnRegistrationListener {


    private lateinit var progressDialogUtil: ProgressDialogUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnSignUp.setOnClickListener {
            if (txtUserName.text!!.isEmpty() && txtPassword.text!!.isEmpty()) {
                Snackbar.make(btnSignUp, "Enter mandatory fields", Snackbar.LENGTH_LONG).show()
            } else {
                initRegistration()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }

        progressDialogUtil = ProgressDialogUtil(this)

    }

    private fun initRegistration() {
        progressDialogUtil.show()
        val registrationAsync = RegistrationAsync()
        var jRequest = JSONObject()
        var jParams = JSONObject()
        jParams.put("Name", txtName)
        jParams.put("Username", txtUserName)
        jParams.put("Password", txtPassword)
        jParams.put("Number", txtNumber)
        jParams.put("LocalAddress", txtLocalAddress.text)
        jParams.put("UserType", "2")
        jRequest.put("request", jParams)
        registrationAsync.setOnRegistrationListener(this, jRequest)
    }

    override fun onRegistrationSuccess(result: String) {
        progressDialogUtil.hide()
        if (result.isNullOrBlank()) {
            Snackbar.make(btnSignUp, "Something went wrong,Please try again.", Snackbar.LENGTH_LONG).show()
        } else {

            var event = Snackbar.make(btnSignUp, "Registration completed.", Snackbar.LENGTH_INDEFINITE)
            event.setAction("Ok") {
                finish()
            }
            event.show()
        }
    }
}
