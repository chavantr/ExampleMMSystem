package com.mywings.messmanagementsystem

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.mywings.messmanagementsystem.process.ForgotPasswordAsync
import com.mywings.messmanagementsystem.process.OnUpdatePasswordListener
import com.mywings.messmanagementsystem.process.ProgressDialogUtil
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity(), OnUpdatePasswordListener {

    private lateinit var progressDialogUtil: ProgressDialogUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        progressDialogUtil = ProgressDialogUtil(this)

        btnSignUp.setOnClickListener {
            if (validate()) {
                init()
            } else {
                Toast.makeText(this@ForgotPasswordActivity, "Enter fields", Toast.LENGTH_LONG).show()
            }
        }

        btnCancel.setOnClickListener { finish() }


    }

    override fun onUpdateSuccess(result: String?) {
        progressDialogUtil.hide()
        if (result!!.isNotEmpty() && result.contains("1")) {
            var snack = Snackbar.make(btnCancel, "Updated", Snackbar.LENGTH_INDEFINITE)
            snack.setAction("OK") { finish() }
            snack.show()
        } else {
            var snack = Snackbar.make(btnCancel, "Error occurred", Snackbar.LENGTH_INDEFINITE)
            snack.show()
        }
    }

    private fun init() {
        progressDialogUtil.show()
        val forgotPasswordAsync = ForgotPasswordAsync()
        var request = JSONObject()

        var param = JSONObject()

        param.put("Username", txtUserName.text)
        param.put("Password", txtPassword.text)

        request.put("request", param)

        forgotPasswordAsync.setOnUpdateListener(this, request)
    }

    private fun validate(): Boolean =
        txtUserName.text!!.isNotEmpty() && txtPassword.text!!.isNotEmpty() && txtConfirmPassword.text!!.isNotEmpty()
}
