package com.mywings.messmanagementsystem.process

import android.os.AsyncTask
import org.json.JSONObject

class UpdateAsync : AsyncTask<JSONObject, Void, String>() {

    private lateinit var onUpdateListener: OnUpdateListener

    override fun doInBackground(vararg param: JSONObject?): String {
        return HttpConnectionUtil().requestPost(ConstantUtils.URL + ConstantUtils.UPDATE_PROFILE, param[0])
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        onUpdateListener.onUpdateSuccess(result)
    }

    fun setOnUpdateListener(onUpdateListener: OnUpdateListener, request: JSONObject) {
        this.onUpdateListener = onUpdateListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)
    }

}