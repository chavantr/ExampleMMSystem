package com.mywings.messmanagementsystem.process

import android.os.AsyncTask

class SendOtpAsync : AsyncTask<String, Void, String?>() {

    private lateinit var onSendOptionListener: OnSendOptionListener

    override fun doInBackground(vararg param: String?): String? {
        return HttpConnectionUtil().requestGet(param[0])
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        onSendOptionListener.otpSent(result)
    }

    fun setSendOtpListener(onSendOptionListener: OnSendOptionListener, request: String) {
        this.onSendOptionListener = onSendOptionListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)
    }

}