package com.mywings.messmanagementsystem.binder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mywings.messmanagementsystem.R
import com.mywings.messmanagementsystem.model.Mess
import kotlinx.android.synthetic.main.layout_mess_row.view.*

class MessAdapter(var mess: ArrayList<Mess>?) : RecyclerView.Adapter<MessAdapter.MessViewHolder>() {


    private var messes: ArrayList<Mess>? = mess

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): MessViewHolder {
        return MessViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_mess_row, parent, false))
    }

    override fun getItemCount(): Int = messes!!.size

    override fun onBindViewHolder(viewHolder: MessViewHolder, postion: Int) {

        viewHolder.lblName.text = messes!![postion].name

        viewHolder.lblLocalarea.text = messes!![postion].localArea

        viewHolder.lblFoodType.text = messes!![postion].foodType

        viewHolder.lblMessType.text = messes!![postion].messType

        viewHolder.lblRating.text = messes!![postion].rating

    }


    inner class MessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val lblName = itemView.lblName!!

        val lblLocalarea = itemView.lblLocalarea!!

        val lblFoodType = itemView.lblFoodType!!

        val lblMessType = itemView.lblMessType!!

        val lblRating = itemView.lblRating!!

    }

}