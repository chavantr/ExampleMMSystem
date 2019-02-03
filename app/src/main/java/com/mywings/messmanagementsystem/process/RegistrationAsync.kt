package com.mywings.messmanagementsystem.process

import android.os.AsyncTask
import org.json.JSONObject

class RegistrationAsync : AsyncTask<JSONObject, Void, String>() {

    private lateinit var onRegistrationListener: OnRegistrationListener
    private val httpConnectionUtil = HttpConnectionUtil()

    override fun doInBackground(vararg param: JSONObject?): String {
        return httpConnectionUtil.requestPost(ConstantUtils.URL + ConstantUtils.REGISTRATION, param[0])
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        onRegistrationListener.onRegistrationSuccess(result!!)
    }

    fun setOnRegistrationListener(onRegistrationListener: OnRegistrationListener, request: JSONObject) {
        this.onRegistrationListener = onRegistrationListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)
    }
}