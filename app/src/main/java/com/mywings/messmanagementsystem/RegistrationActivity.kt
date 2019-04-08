package com.mywings.messmanagementsystem


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.mywings.messmanagementsystem.process.*
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONObject
import kotlin.random.Random


class RegistrationActivity : AppCompatActivity(), OnRegistrationListener, OnSendOptionListener {

    private lateinit var progressDialogUtil: ProgressDialogUtil
    private lateinit var input: String
    private lateinit var number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnSignUp.setOnClickListener {
            if (txtUserName.text!!.isEmpty() && txtPassword.text!!.isEmpty()) {
                Snackbar.make(btnSignUp, "Enter mandatory fields", Snackbar.LENGTH_LONG).show()
            } else {
                //initRegistration()
                initotp()
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
        jParams.put("Name", txtName.text)
        jParams.put("Username", txtUserName.text)
        jParams.put("Password", txtPassword.text)
        jParams.put("Number", txtNumber.text)
        jParams.put("LocalAddress", txtLocalAddress.text)
        jParams.put("UserType", "1")
        jRequest.put("request", jParams)
        registrationAsync.setOnRegistrationListener(this, jRequest)
    }

    private fun initotp() {
        progressDialogUtil.show()
        number = getRandomNumberString()
        input =
            "https://api.textlocal.in/send/?apiKey=wnl6P220GB4-NLTbWCaPwzfFPHRoSBz16bgyFjAsie&sender=TXTLCL&numbers=${txtNumber.text}&message=${number}"
        val sendOtp = SendOtpAsync()
        sendOtp.setSendOtpListener(this, input)
    }

    private fun getRandomNumberString(): String {
        val rnd = Random(100000)
        val number = rnd.nextInt(999999)
        return String.format("%06d", number)
    }

    override fun otpSent(result: String?) {
        progressDialogUtil.hide()
        if (result!!.contains("error")) {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show()
        } else {
            show()
        }
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

    private fun show() {
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.layout_prompt, null)
        val alertDialogBuilder = AlertDialog.Builder(
            this
        )
        alertDialogBuilder.setView(promptsView)
        val userInput = promptsView
            .findViewById(R.id.editTextDialogUserInput) as EditText
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id ->
                if (userInput.text.toString() == number) {
                    initRegistration()
                } else {
                    Toast.makeText(this, "Enter valid otp", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, id -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
