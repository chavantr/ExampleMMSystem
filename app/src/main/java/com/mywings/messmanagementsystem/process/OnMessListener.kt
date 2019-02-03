package com.mywings.messmanagementsystem.process

import com.mywings.messmanagementsystem.model.Mess

interface OnMessListener {
    fun onMessSuccess(result: List<Mess>)
}