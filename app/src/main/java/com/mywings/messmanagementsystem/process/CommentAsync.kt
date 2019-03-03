package com.mywings.messmanagementsystem.process

import android.os.AsyncTask

class CommentAsync : AsyncTask<String, Void, String>() {

    private lateinit var onRateListener: OnRateListener

    override fun doInBackground(vararg param: String?): String {

        return HttpConnectionUtil().requestGet(ConstantUtils.URL + ConstantUtils.MMS_RATE + param[0])

    }


    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        onRateListener.onFine(result)
    }

    fun setOnRateListener(onRateListener: OnRateListener, request: String) {
        this.onRateListener = onRateListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)
    }



}