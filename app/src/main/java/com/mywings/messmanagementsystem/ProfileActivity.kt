package com.mywings.messmanagementsystem

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.mywings.messmanagementsystem.model.UserHolder
import com.mywings.messmanagementsystem.process.OnUpdateListener
import com.mywings.messmanagementsystem.process.ProgressDialogUtil
import com.mywings.messmanagementsystem.process.UpdateAsync
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject

class ProfileActivity : AppCompatActivity(), OnUpdateListener {


    private val user = UserHolder.getInstance().user

    private lateinit var progressDialogUtil: ProgressDialogUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        progressDialogUtil = ProgressDialogUtil(this)
        txtName.setText(user.name)
        txtUserName.setText(user.username)
        txtPassword.setText(user.password)
        txtConfirmPassword.setText(user.password)
        txtNumber.setText(user.number)
        txtLocalAddress.setText(user.localaddress)

        btnSignUp.setOnClickListener {
            init()
        }

        btnCancel.setOnClickListener {
            finish()
        }

    }

    private fun init() {
        progressDialogUtil.show()
        val updateAsync = UpdateAsync()
        var jRequest = JSONObject()
        var jParams = JSONObject()
        jParams.put("Name", txtName.text)
        jParams.put("Username", txtUserName.text)
        jParams.put("Password", txtPassword.text)
        jParams.put("Number", txtNumber.text)
        jParams.put("LocalAddress", txtLocalAddress.text)
        jParams.put("UserType", "1")
        jParams.put("Id", UserHolder.getInstance().user.id)
        jRequest.put("request", jParams)
        updateAsync.setOnUpdateListener(this, jRequest)

    }

    override fun onUpdateSuccess(result: String?) {
        progressDialogUtil.hide()
        if (result.isNullOrBlank()) {
            Snackbar.make(btnSignUp, "Something went wrong,Please try again.", Snackbar.LENGTH_LONG).show()
        } else {
            var event = Snackbar.make(btnSignUp, "Updated.", Snackbar.LENGTH_INDEFINITE)
            event.setAction("Ok") {
                finish()
            }
            event.show()
        }

    }
}
