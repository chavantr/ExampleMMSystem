package com.mywings.messmanagementsystem.process

import android.os.AsyncTask
import org.json.JSONObject

class ForgotPasswordAsync : AsyncTask<JSONObject, Void, String>() {


    private val httpConnectionUtil = HttpConnectionUtil()

    private lateinit var onUpdatePasswordListener: OnUpdatePasswordListener

    override fun doInBackground(vararg param: JSONObject?): String {
        return httpConnectionUtil.requestPost(ConstantUtils.URL + ConstantUtils.MMS_FORGOT_PASSWORD, param[0])
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        onUpdatePasswordListener.onUpdateSuccess(result)
    }

    fun setOnUpdateListener(onUpdatePasswordListener: OnUpdatePasswordListener, request: JSONObject) {
        this.onUpdatePasswordListener = onUpdatePasswordListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)

    }


}