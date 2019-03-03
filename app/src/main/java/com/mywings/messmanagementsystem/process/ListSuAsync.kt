package com.mywings.messmanagementsystem.process

import android.os.AsyncTask

class ListSuAsync : AsyncTask<String, Void, String>() {

    private lateinit var onListListener: OnListListener

    override fun doInBackground(vararg param: String?): String {

        return HttpConnectionUtil().requestGet(ConstantUtils.URL + ConstantUtils.MMS_LIKE + param[0])

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        onListListener.onList(result!!)
    }

    fun setOnLikeListener(onListListener: OnListListener, request: String) {
        this.onListListener = onListListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)
    }


}