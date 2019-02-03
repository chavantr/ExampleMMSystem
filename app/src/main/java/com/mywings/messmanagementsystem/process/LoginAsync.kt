package com.mywings.messmanagementsystem.process

import android.os.AsyncTask
import com.mywings.messmanagementsystem.model.User
import org.json.JSONObject

class LoginAsync : AsyncTask<JSONObject, Void, User>() {

    private val httpConnectionUtil = HttpConnectionUtil()
    private lateinit var onLoginListener: OnLoginListener

    override fun doInBackground(vararg param: JSONObject?): User? {
        var response = httpConnectionUtil.requestPost(ConstantUtils.URL + ConstantUtils.LOGIN, param[0])
        if (response.isNullOrBlank()) return null else {
            val jsonObject = JSONObject(response)
            var user = User()
            user.id = jsonObject.getInt("Id")
            user.localaddress = jsonObject.getString("LocalAddress")
            user.name = jsonObject.getString("Name")
            user.username = jsonObject.getString("Username")
            user.password = jsonObject.getString("Password")
            user.number = jsonObject.getString("Number")
            user.usertype = jsonObject.getString("UserType")
            return user;
        }
    }

    override fun onPostExecute(result: User?) {
        super.onPostExecute(result)
        onLoginListener.onLoginSuccess(result!!)
    }

    fun setOnLoginListener(onLoginListener: OnLoginListener, request: JSONObject) {
        this.onLoginListener = onLoginListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)
    }
}