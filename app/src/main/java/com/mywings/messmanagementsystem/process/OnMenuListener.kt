package com.mywings.messmanagementsystem.process

import com.mywings.messmanagementsystem.model.Menu

interface OnMenuListener {
    fun onMenuSuccess(result: Menu?)
}