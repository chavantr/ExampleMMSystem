package com.mywings.messmanagementsystem

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mywings.messmanagementsystem.model.User
import com.mywings.messmanagementsystem.model.UserHolder
import com.mywings.messmanagementsystem.process.LoginAsync
import com.mywings.messmanagementsystem.process.OnLoginListener
import com.mywings.messmanagementsystem.process.ProgressDialogUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), OnLoginListener {


    private lateinit var progressDialogUtil: ProgressDialogUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSignUp.setOnClickListener {
            val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            if (txtUserName.text!!.isNotEmpty() && txtPassword.text!!.isNotEmpty()) {
                initLogin()
            } else {
                Toast.makeText(this@MainActivity, "Enter username and password", Toast.LENGTH_LONG).show()
            }
        }
        btnForgotPassword.setOnClickListener {
            val intent = Intent(this@MainActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        progressDialogUtil = ProgressDialogUtil(this)
    }

    private fun initLogin() {
        progressDialogUtil.show()
        val loginAsync = LoginAsync()
        var jRequest = JSONObject()
        var jparams = JSONObject()
        jparams.put("Username", txtUserName.text)
        jparams.put("Password", txtPassword.text)
        jRequest.put("login", jparams)
        loginAsync.setOnLoginListener(this, jRequest)
    }

    override fun onLoginSuccess(user: User) {
        progressDialogUtil.hide()
        if (null != user) {
            UserHolder.getInstance().user = user
            val intent = Intent(this@MainActivity, DashboardActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this@MainActivity, "Something went wrong, try again", Toast.LENGTH_LONG).show()
        }
    }
}
