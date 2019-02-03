package com.mywings.messmanagementsystem.process

import android.os.AsyncTask
import com.mywings.messmanagementsystem.model.Mess
import org.json.JSONArray
import org.json.JSONObject

class GetMessAsync : AsyncTask<JSONObject, Void, List<Mess>>() {


    private lateinit var onMessListener: OnMessListener

    private val httpConnectionUtil = HttpConnectionUtil()


    override fun doInBackground(vararg param: JSONObject?): List<Mess>? {
        var messes = ArrayList<Mess>()
        val response = httpConnectionUtil.requestPost(ConstantUtils.URL + ConstantUtils.GET_MESS, param[0])
        return if (response.isNullOrBlank()) null else {
            val jmesses = JSONArray(response)
            for (i in 0..(jmesses.length() - 1)) {
                val jnode = jmesses.getJSONObject(i)
                val mess = Mess()
                mess.id = jnode.getInt("Id")
                mess.name = jnode.getString("Name")
                mess.localArea = jnode.getString("LocalArea")
                mess.latitude = jnode.getString("Latitide")
                mess.longitude = jnode.getString("Longitude")
                mess.messType = jnode.getString("MessType")
                mess.foodType = jnode.getString("FoodType")
                mess.rating = jnode.getString("Rating")
                mess.periodType = jnode.getString("PeriodType")
                mess.comment = jnode.getString("Comment")
                messes.add(mess)
            }
            messes
        }
    }

    override fun onPostExecute(result: List<Mess>?) {
        super.onPostExecute(result)
        onMessListener.onMessSuccess(result!!)
    }

    fun setOnMessListener(onMessListener: OnMessListener, request: JSONObject) {
        this.onMessListener = onMessListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)
    }
}