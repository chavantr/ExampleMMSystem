package com.mywings.messmanagementsystem.process

import android.os.AsyncTask
import com.mywings.messmanagementsystem.model.Menu
import org.json.JSONObject

class GetMenuAsync : AsyncTask<String?, Void, Menu?>() {
    private lateinit var onMenuListener: OnMenuListener
    override fun doInBackground(vararg param: String?): Menu? {
        val response = HttpConnectionUtil().requestGet(ConstantUtils.URL + ConstantUtils.GET_MENU + param[0])
        return run {
            var menu = Menu()
            val jMenu = JSONObject(response)
            menu.id = jMenu.getInt("Id")
            menu.day = jMenu.getInt("Day")
            menu.item1 = jMenu.getString("Item1")
            menu.item2 = jMenu.getString("Item2")
            menu.item3 = jMenu.getString("Item3")
            menu.item4 = jMenu.getString("Item4")
            menu.item5 = jMenu.getString("Item5")
            menu
        }
    }

    override fun onPostExecute(result: Menu?) {
        super.onPostExecute(result)
        onMenuListener.onMenuSuccess(result)
    }

    fun setOnMenuListener(onMenuListener: OnMenuListener, request: String?) {
        this.onMenuListener = onMenuListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)
    }
}